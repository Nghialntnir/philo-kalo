package com.nlnt.philokalo_server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nlnt.philokalo_server.dto.request.ArtworkImageRequest;
import com.nlnt.philokalo_server.dto.request.ArtworkRequest;
import com.nlnt.philokalo_server.dto.request.ArtworkUploadRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkUploadResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkUploadUrlResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.mapper.ArtworkMapper;
import com.nlnt.philokalo_server.model.Artwork;
import com.nlnt.philokalo_server.model.ArtworkImage;
import com.nlnt.philokalo_server.model.Category;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.ArtworkImageRepository;
import com.nlnt.philokalo_server.repository.ArtworkRepository;
import com.nlnt.philokalo_server.repository.CategoryRepository;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.ArtworkService;
import com.nlnt.philokalo_server.service.CloudinaryCleanupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private static final Set<String> SALE_STATUSES = Set.of("published", "auction");
    private static final String DEFAULT_CURRENCY = "VND";
    private static final int MONEY_SCALE = 2;

    private final ArtworkRepository artworkRepository;
    private final ArtworkImageRepository artworkImageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ArtworkMapper artworkMapper;
    private final Cloudinary cloudinary;
    private final CloudinaryCleanupService cloudinaryCleanupService;

    @Value("${app.cloudinary.cloud-name}")
    private String cloudName;

    @Value("${app.cloudinary.api-key}")
    private String apiKey;

    @Value("${app.cloudinary.api-secret}")
    private String apiSecret;

    @Value("${app.cloudinary.folder.gallery}")
    private String galleryFolder;

    @Override
    public ArtworkUploadResponse createUploadAuthorization(ArtworkUploadRequest request) {
        User user = currentUser();
        long timestamp = Instant.now().getEpochSecond();
        Instant expiresAt = Instant.ofEpochSecond(timestamp + 900);
        List<ArtworkUploadUrlResponse> uploads = new ArrayList<>();

        for (int i = 0; i < request.getCount(); i++) {
            String publicId = galleryFolder + "/" + user.getId() + "/" + UUID.randomUUID();
            Map<String, Object> params = ObjectUtils.asMap(
                    "folder", galleryFolder + "/" + user.getId(),
                    "public_id", publicId.substring(publicId.lastIndexOf('/') + 1),
                    "timestamp", timestamp);
            String signature;
            try {
                signature = cloudinary.apiSignRequest(params, apiSecret);
            } catch (RuntimeException ex) {
                throw new AppException(ErrorCode.ARTWORK_UPLOAD_SIGN_FAILED);
            }
            uploads.add(ArtworkUploadUrlResponse.builder()
                    .uploadUrl("https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload")
                    .publicId(publicId)
                    .apiKey(apiKey)
                    .timestamp(timestamp)
                    .signature(signature)
                    .expiresAt(expiresAt)
                    .build());
        }
        return ArtworkUploadResponse.builder().uploads(uploads).build();
    }

    @Override
    @Transactional
    public ArtworkResponse createArtwork(ArtworkRequest request) {
        User user = currentUser();
        Artwork artwork = artworkMapper.toArtwork(request);
        applyBusinessRules(artwork);
        artwork.setArtist(user);
        artwork.setViewCount(0);
        artwork.setLikeCount(0);
        if (request.getCategoryIds() != null) {
            artwork.setCategorySet(resolveCategories(request.getCategoryIds()));
        }
        artwork.setArtworkImageSet(toImages(request.getImages(), artwork));
        Artwork saved = artworkRepository.save(artwork);
        return artworkMapper.toArtworkResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtworkResponse getArtwork(String artworkId) {
        return artworkRepository.findById(artworkId)
                .map(artworkMapper::toArtworkResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ARTWORK_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtworkResponse> getMyArtworks() {
        return artworkRepository.findByArtistIdOrderByCreatedAtDesc(currentUser().getId()).stream()
                .map(artworkMapper::toArtworkResponse)
                .toList();
    }

    @Override
    @Transactional
    public ArtworkResponse updateArtwork(String artworkId, ArtworkRequest request) {
        Artwork artwork = ownedArtwork(artworkId);
        artworkMapper.updateArtwork(artwork, request);
        applyBusinessRules(artwork);
        artwork.setCategorySet(resolveCategories(request.getCategoryIds()));

        if (request.getImages() != null) {
            synchronizeImages(artwork, request.getImages());
        }
        return artworkMapper.toArtworkResponse(artworkRepository.save(artwork));
    }

    @Override
    @Transactional
    public void deleteArtwork(String artworkId) {
        artworkRepository.delete(ownedArtwork(artworkId));
    }

    private void applyBusinessRules(Artwork artwork) {
        String status = artwork.getStatus() == null ? "draft" : artwork.getStatus().trim().toLowerCase();
        boolean forSale = Boolean.TRUE.equals(artwork.getIsForSale());
        if (forSale != SALE_STATUSES.contains(status)) {
            throw new AppException(ErrorCode.ARTWORK_STATUS_INVALID_FOR_SALE);
        }
        BigDecimal price = artwork.getPrice() == null ? BigDecimal.ZERO : artwork.getPrice();
        if (price.signum() < 0 || (forSale && price.signum() <= 0)) {
            throw new AppException(ErrorCode.ARTWORK_PRICE_REQUIRED);
        }
        if (!forSale && price.signum() > 0) {
            throw new AppException(ErrorCode.ARTWORK_PRICE_INVALID_FOR_SALE);
        }
        artwork.setStatus(status);
        artwork.setIsForSale(forSale);
        artwork.setPrice(price.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        artwork.setCurrency(
                artwork.getCurrency() == null || artwork.getCurrency().isBlank()
                        ? DEFAULT_CURRENCY
                        : artwork.getCurrency().trim().toUpperCase());
    }

    private Set<Category> resolveCategories(Set<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.ARTWORK_CATEGORY_NOT_FOUND)))
                .collect(Collectors.toSet());
    }

    private Set<ArtworkImage> toImages(List<ArtworkImageRequest> requests, Artwork artwork) {
        if (requests == null || requests.isEmpty()) {
            throw new AppException(ErrorCode.ARTWORK_IMAGES_REQUIRED);
        }
        Set<ArtworkImage> images = new HashSet<>();
        boolean hasPrimary = requests.stream().anyMatch(request -> Boolean.TRUE.equals(request.getIsPrimary()));
        for (int i = 0; i < requests.size(); i++) {
            ArtworkImageRequest request = requests.get(i);
            ArtworkImage image = toImage(request, artwork, i, !hasPrimary && i == 0);
            images.add(image);
        }
        return images;
    }

    private void synchronizeImages(Artwork artwork, List<ArtworkImageRequest> requests) {
        if (requests.isEmpty()) {
            throw new AppException(ErrorCode.ARTWORK_IMAGES_REQUIRED);
        }
        Map<String, ArtworkImage> existing = artworkImageRepository.findByArtworkId(artwork.getId()).stream()
                .collect(Collectors.toMap(ArtworkImage::getId, image -> image));
        Set<String> retainedIds = requests.stream()
                .map(ArtworkImageRequest::getId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());
        existing.values().stream()
                .filter(image -> !retainedIds.contains(image.getId()))
                .forEach(image -> {
                    artworkImageRepository.delete(image);
                    cloudinaryCleanupService.deleteAsync(image.getOriginalUrl());
                });

        boolean hasPrimary = requests.stream().anyMatch(request -> Boolean.TRUE.equals(request.getIsPrimary()));
        Set<ArtworkImage> images = new HashSet<>();
        for (int i = 0; i < requests.size(); i++) {
            ArtworkImageRequest request = requests.get(i);
            ArtworkImage image = request.getId() == null ? null : existing.get(request.getId());
            if (request.getId() != null && image == null) {
                throw new AppException(ErrorCode.ARTWORK_IMAGE_NOT_FOUND);
            }
            if (image == null) {
                image = new ArtworkImage();
                image.setId(UUID.randomUUID().toString());
                image.setArtwork(artwork);
            }
            copyImage(request, image, i, !hasPrimary && i == 0);
            images.add(image);
        }
        artwork.setArtworkImageSet(images);
    }

    private ArtworkImage toImage(ArtworkImageRequest request, Artwork artwork, int index, boolean fallbackPrimary) {
        ArtworkImage image = new ArtworkImage();
        image.setId(UUID.randomUUID().toString());
        image.setArtwork(artwork);
        copyImage(request, image, index, fallbackPrimary);
        return image;
    }

    private void copyImage(ArtworkImageRequest request, ArtworkImage image, int index, boolean fallbackPrimary) {
        image.setIsPrimary(Boolean.TRUE.equals(request.getIsPrimary()) || fallbackPrimary);
        image.setOriginalUrl(request.getOriginalUrl());
        image.setFullUrl(request.getFullUrl());
        image.setMediumUrl(request.getMediumUrl());
        image.setThumbUrl(request.getThumbUrl());
        image.setBlurHash(request.getBlurHash());
        image.setWidth(request.getWidth());
        image.setHeight(request.getHeight());
        image.setFileSizeKb(request.getFileSizeKb());
        image.setFormat(request.getFormat());
        image.setSortOrder((short) index);
    }

    private Artwork ownedArtwork(String artworkId) {
        return artworkRepository.findByIdAndArtistId(artworkId, currentUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTWORK_NOT_FOUND));
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
