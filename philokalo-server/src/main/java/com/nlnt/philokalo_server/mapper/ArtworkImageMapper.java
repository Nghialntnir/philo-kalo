package com.nlnt.philokalo_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.ArtworkImageRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkImageResponse;
import com.nlnt.philokalo_server.model.ArtworkImage;

@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface ArtworkImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artwork", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ArtworkImage toArtworkImage(ArtworkImageRequest request);

    ArtworkImageResponse toArtworkImageResponse(ArtworkImage image);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artwork", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateArtworkImage(@MappingTarget ArtworkImage image, ArtworkImageRequest request);
}
