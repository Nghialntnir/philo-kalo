package com.nlnt.philokalo_server.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author nghia
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    List<T> content;
    int currentPage;
    int pageSize;
    int totalPages;
    long totalElements;
}
