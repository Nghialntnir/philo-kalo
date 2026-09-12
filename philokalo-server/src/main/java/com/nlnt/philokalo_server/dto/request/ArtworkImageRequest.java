package com.nlnt.philokalo_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtworkImageRequest {

    Boolean isPrimary;
    @NotBlank(message = "ARTWORK_IMAGE_ORIGINAL_URL_REQUIRED")
    String originalUrl;
    String fullUrl;
    String mediumUrl;
    @NotBlank(message = "ARTWORK_IMAGE_THUMB_URL_REQUIRED")
    String thumbUrl;
    @Size(max = 100, message = "ARTWORK_IMAGE_BLUR_HASH_INVALID")
    String blurHash;
    @PositiveOrZero(message = "ARTWORK_IMAGE_WIDTH_INVALID")
    Integer width;
    @PositiveOrZero(message = "ARTWORK_IMAGE_HEIGHT_INVALID")
    Integer height;
    @PositiveOrZero(message = "ARTWORK_IMAGE_FILE_SIZE_INVALID")
    Integer fileSizeKb;
    @Size(max = 10, message = "ARTWORK_IMAGE_FORMAT_INVALID")
    String format;
    @PositiveOrZero(message = "ARTWORK_IMAGE_SORT_ORDER_INVALID")
    Short sortOrder;
}
