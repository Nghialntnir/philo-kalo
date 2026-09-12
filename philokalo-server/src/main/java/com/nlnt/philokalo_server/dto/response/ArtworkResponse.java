package com.nlnt.philokalo_server.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.List;

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
public class ArtworkResponse {
    String id;
    String title;
    String description;
    String medium;
    Date yearCreated;
    String status;
    Boolean isForSale;
    BigDecimal price;
    String currency;
    Integer viewCount;
    Integer likeCount;
    Instant createdAt;
    Instant updatedAt;
    UserSimpleResponse artist;
    Set<CategorySimpleResponse> categories;
    List<ArtworkImageResponse> images;
    Set<ArtworkCommentResponse> comments;
}
