package com.nlnt.philokalo_server.dto.response;

import java.time.Instant;
import java.util.Set;

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
public class ArtworkCommentResponse {
    String id;
    String content;
    Boolean isHidden;
    Instant createdAt;
    UserSimpleResponse user;
    String parentId;
    Set<ArtworkCommentResponse> replies;
}
