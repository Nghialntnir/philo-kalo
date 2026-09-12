package com.nlnt.philokalo_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.ArtworkLikeRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkLikeResponse;
import com.nlnt.philokalo_server.model.ArtworkLike;
import com.nlnt.philokalo_server.model.ArtworkLikePK;

@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface ArtworkLikeMapper {

    @Mapping(target = "artworkLikePK", expression = "java(toPrimaryKey(request, userId))")
    @Mapping(target = "artwork", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ArtworkLike toArtworkLike(ArtworkLikeRequest request, String userId);

    @Mapping(source = "artworkLikePK.userId", target = "userId")
    @Mapping(source = "artworkLikePK.artworkId", target = "artworkId")
    ArtworkLikeResponse toArtworkLikeResponse(ArtworkLike like);

    default ArtworkLikePK toPrimaryKey(ArtworkLikeRequest request, String userId) {
        return ArtworkLikePK.builder().userId(userId).artworkId(request.getArtworkId()).build();
    }
}
