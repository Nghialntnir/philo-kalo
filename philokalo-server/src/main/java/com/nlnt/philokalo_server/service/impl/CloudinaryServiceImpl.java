package com.nlnt.philokalo_server.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nlnt.philokalo_server.dto.response.CloudinaryUploadResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nghia
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    @Value("${app.cloudinary.size}")
    private long maxSize;

    @Value("${app.cloudinary.folder.avatar}")
    private String avatarFolder;

    @Value("${app.cloudinary.folder.gallery}")
    private String galleryFolder;

    private final Cloudinary cloudinary;

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    @Override
    public String uploadAvatar(MultipartFile file, String userId) {
        validateFile(file);
        try {
            Map result = cloudinary
                    .uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder",
                                    avatarFolder,
                                    "public_id",
                                    "avatar_" + userId,
                                    "overwrite",
                                    true,
                                    "resource_type",
                                    "image",
                                    "width",
                                    200,
                                    "height",
                                    200,
                                    "crop",
                                    "fill",
                                    "gravity",
                                    "face",
                                    "quality",
                                    "auto",
                                    "fetch_format",
                                    "auto"));
            return (String) result.get("secure_url");
        } catch (IOException ex) {
            log.error("Upload avatar failed, userId={}", userId, ex);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    @Override
    public CloudinaryUploadResponse uploadPhoto(MultipartFile request, String userId) {
        validateFile(request);
        try {
            Map result = cloudinary
                    .uploader()
                    .upload(
                            request.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", galleryFolder + "/" + userId,
                                    "resource_type", "image",
                                    "quality", "auto",
                                    "fetch_format", "auto"));
            return mapToResponse(result);
        } catch (IOException ex) {
            log.error("Upload photo failed, userId={}", userId, ex);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            if (!"ok".equals(result.get("result"))) {
                log.warn("Delete unexpected result: publicId={}, result={}", publicId, result.get("result"));
            }
        } catch (IOException ex) {
            log.error("Delete file failed, publicId={}", publicId, ex);
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new AppException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
        if (file.getSize() > maxSize) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
    }

    private CloudinaryUploadResponse mapToResponse(Map result) {
        return CloudinaryUploadResponse.builder()
                .publicId((String) result.get("public_id"))
                .secureUrl((String) result.get("secure_url"))
                .format((String) result.get("format"))
                .width((Integer) result.get("width"))
                .height((Integer) result.get("height"))
                .bytes(((Number) result.get("bytes")).longValue())
                .build();
    }
}
