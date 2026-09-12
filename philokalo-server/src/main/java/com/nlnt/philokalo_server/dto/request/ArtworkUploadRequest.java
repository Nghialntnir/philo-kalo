package com.nlnt.philokalo_server.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ArtworkUploadRequest {
    @Min(value = 1, message = "ARTWORK_IMAGE_COUNT_INVALID")
    @Max(value = 20, message = "ARTWORK_IMAGE_COUNT_INVALID")
    private int count;
}
