package com.nlnt.philokalo_server.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.ArtworkCommentRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkCommentResponse;
import com.nlnt.philokalo_server.dto.response.UserSimpleResponse;
import com.nlnt.philokalo_server.model.ArtworkComment;

@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface ArtworkCommentMapper {

    @Mapping(target = "artwork", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "artworkCommentSet", ignore = true)
    @Mapping(target = "isHidden", ignore = true)
    ArtworkComment toArtworkComment(ArtworkCommentRequest request);

    @Mapping(target = "user", expression = "java(mapUser(comment))")
    @Mapping(target = "parentId", expression = "java(mapParentId(comment))")
    @Mapping(target = "replies", expression = "java(mapReplies(comment))")
    ArtworkCommentResponse toArtworkCommentResponse(ArtworkComment comment);

    @Mapping(target = "artwork", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "artworkCommentSet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isHidden", ignore = true)
    void updateArtworkComment(@MappingTarget ArtworkComment comment, ArtworkCommentRequest request);

    default UserSimpleResponse mapUser(ArtworkComment comment) {
        if (comment.getUser() == null) {
            return null;
        }
        return UserSimpleResponse.builder()
                .id(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .fullName(comment.getUser().getFullName())
                .avatarUrl(comment.getUser().getAvatarUrl())
                .build();
    }

    default String mapParentId(ArtworkComment comment) {
        return comment.getParent() == null ? null : comment.getParent().getId();
    }

    default Set<ArtworkCommentResponse> mapReplies(ArtworkComment comment) {
        if (comment.getArtworkCommentSet() == null) {
            return new HashSet<>();
        }
        return comment.getArtworkCommentSet().stream()
                .map(this::toArtworkCommentResponse)
                .collect(Collectors.toSet());
    }
}
