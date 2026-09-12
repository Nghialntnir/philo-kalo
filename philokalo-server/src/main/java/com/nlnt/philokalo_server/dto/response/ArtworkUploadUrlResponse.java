package com.nlnt.philokalo_server.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ArtworkUploadUrlResponse {
    String uploadUrl;
    String publicId;
    String apiKey;
    long timestamp;
    String signature;
    Instant expiresAt;
}
