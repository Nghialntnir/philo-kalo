package com.nlnt.philokalo_server.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

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
public class ArtworkRequest {

    @NotBlank(message = "ARTWORK_TITLE_REQUIRED")
    @Size(max = 255, message = "ARTWORK_TITLE_INVALID")
    String title;

    @Size(max = 65535, message = "ARTWORK_DESCRIPTION_INVALID")
    String description;

    @Size(max = 100, message = "ARTWORK_MEDIUM_INVALID")
    String medium;

    Date yearCreated;
    @NotBlank(message = "ARTWORK_STATUS_REQUIRED")
    @Size(max = 20, message = "ARTWORK_STATUS_INVALID")
    String status;
    Boolean isForSale;

    @DecimalMin(value = "0.0", inclusive = true, message = "ARTWORK_PRICE_INVALID")
    BigDecimal price;

    @Size(min = 3, max = 3, message = "ARTWORK_CURRENCY_INVALID")
    String currency;

    Set<String> categoryIds;
    Set<String> tagIds;
    @Valid
    List<ArtworkImageRequest> images;
}
