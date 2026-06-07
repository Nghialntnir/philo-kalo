package com.nlnt.philokalo_server.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author nghia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryUploadResponse {

    private String publicId;
    private String secureUrl;
    private String format;
    private Integer width;
    private Integer height;
    private Long bytes;
}
