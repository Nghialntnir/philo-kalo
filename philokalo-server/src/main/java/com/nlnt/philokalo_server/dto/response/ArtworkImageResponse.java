package com.nlnt.philokalo_server.dto.response;

import java.time.Instant;

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
public class ArtworkImageResponse {
    String id;
    Boolean isPrimary;
    String originalUrl;
    String fullUrl;
    String mediumUrl;
    String thumbUrl;
    String blurHash;
    Integer width;
    Integer height;
    Integer fileSizeKb;
    String format;
    Short sortOrder;
    Instant createdAt;
}
