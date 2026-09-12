package com.nlnt.philokalo_server.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.ArtworkRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkCommentResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkImageResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkResponse;
import com.nlnt.philokalo_server.dto.response.CategorySimpleResponse;
import com.nlnt.philokalo_server.dto.response.UserSimpleResponse;
import com.nlnt.philokalo_server.model.Artwork;
import com.nlnt.philokalo_server.model.ArtworkComment;
import com.nlnt.philokalo_server.model.Category;

@Mapper(
        componentModel = "spring",
        config = GlobalMapperConfig.class,
        uses = {ArtworkImageMapper.class, ArtworkCommentMapper.class})
public interface ArtworkMapper {

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    @Mapping(target = "tagSet", ignore = true)
    @Mapping(target = "artworkCommentSet", ignore = true)
    @Mapping(target = "artworkLikeSet", ignore = true)
    @Mapping(target = "artworkImageSet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Artwork toArtwork(ArtworkRequest request);

    @Mapping(target = "artist", expression = "java(mapArtist(artwork))")
    @Mapping(target = "categories", expression = "java(mapCategories(artwork))")
    @Mapping(target = "images", expression = "java(mapImages(artwork))")
    @Mapping(target = "comments", expression = "java(mapComments(artwork))")
    ArtworkResponse toArtworkResponse(Artwork artwork);

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "categorySet", ignore = true)
    @Mapping(target = "tagSet", ignore = true)
    @Mapping(target = "artworkCommentSet", ignore = true)
    @Mapping(target = "artworkLikeSet", ignore = true)
    @Mapping(target = "artworkImageSet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateArtwork(@MappingTarget Artwork artwork, ArtworkRequest request);

    default UserSimpleResponse mapArtist(Artwork artwork) {
        if (artwork.getArtist() == null) {
            return null;
        }
        return UserSimpleResponse.builder()
                .id(artwork.getArtist().getId())
                .username(artwork.getArtist().getUsername())
                .fullName(artwork.getArtist().getFullName())
                .avatarUrl(artwork.getArtist().getAvatarUrl())
                .build();
    }

    default Set<CategorySimpleResponse> mapCategories(Artwork artwork) {
        if (artwork.getCategorySet() == null) {
            return new HashSet<>();
        }
        return artwork.getCategorySet().stream()
                .map(category -> CategorySimpleResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .slug(category.getSlug())
                        .build())
                .collect(Collectors.toSet());
    }

    default List<ArtworkImageResponse> mapImages(Artwork artwork) {
        if (artwork.getArtworkImageSet() == null) {
            return List.of();
        }
        return artwork.getArtworkImageSet().stream()
                .sorted(Comparator.comparing(
                        image -> image.getSortOrder() == null ? Short.MAX_VALUE : image.getSortOrder()))
                .map(image -> ArtworkImageResponse.builder()
                        .id(image.getId())
                        .isPrimary(image.getIsPrimary())
                        .originalUrl(image.getOriginalUrl())
                        .fullUrl(image.getFullUrl())
                        .mediumUrl(image.getMediumUrl())
                        .thumbUrl(image.getThumbUrl())
                        .blurHash(image.getBlurHash())
                        .width(image.getWidth())
                        .height(image.getHeight())
                        .fileSizeKb(image.getFileSizeKb())
                        .format(image.getFormat())
                        .sortOrder(image.getSortOrder())
                        .createdAt(image.getCreatedAt() == null ? null : image.getCreatedAt().toInstant())
                        .build())
                .toList();
    }

    default Set<ArtworkCommentResponse> mapComments(Artwork artwork) {
        return mapComments(artwork.getArtworkCommentSet());
    }

    Set<ArtworkCommentResponse> mapComments(Set<ArtworkComment> comments);
}
