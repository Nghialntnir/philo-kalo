package com.nlnt.philokalo_server.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ArtworkUploadResponse {
    List<ArtworkUploadUrlResponse> uploads;
}
