package com.nlnt.philokalo_server.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ArtworkCommentRequest {

    @NotBlank(message = "ARTWORK_COMMENT_CONTENT_REQUIRED")
    @Size(max = 65535, message = "ARTWORK_COMMENT_CONTENT_INVALID")
    String content;

    String parentId;
}
