package com.nlnt.philokalo_server.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nlnt.philokalo_server.dto.request.ArtworkRequest;
import com.nlnt.philokalo_server.dto.request.ArtworkUploadRequest;
import com.nlnt.philokalo_server.dto.response.ApiResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkUploadResponse;
import com.nlnt.philokalo_server.service.ArtworkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/artworks")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    @PostMapping("/upload-authorization")
    ApiResponse<ArtworkUploadResponse> createUploadAuthorization(
            @RequestBody @Valid ArtworkUploadRequest request) {
        return ApiResponse.<ArtworkUploadResponse>builder()
                .result(artworkService.createUploadAuthorization(request))
                .build();
    }

    @PostMapping
    ApiResponse<ArtworkResponse> createArtwork(@RequestBody @Valid ArtworkRequest request) {
        return ApiResponse.<ArtworkResponse>builder()
                .result(artworkService.createArtwork(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ArtworkResponse>> getMyArtworks() {
        return ApiResponse.<List<ArtworkResponse>>builder()
                .result(artworkService.getMyArtworks())
                .build();
    }

    @GetMapping("/{artworkId}")
    ApiResponse<ArtworkResponse> getArtwork(@PathVariable String artworkId) {
        return ApiResponse.<ArtworkResponse>builder()
                .result(artworkService.getArtwork(artworkId))
                .build();
    }

    @PatchMapping("/{artworkId}")
    ApiResponse<ArtworkResponse> updateArtwork(
            @PathVariable String artworkId, @RequestBody @Valid ArtworkRequest request) {
        return ApiResponse.<ArtworkResponse>builder()
                .result(artworkService.updateArtwork(artworkId, request))
                .build();
    }

    @DeleteMapping("/{artworkId}")
    ApiResponse<Void> deleteArtwork(@PathVariable String artworkId) {
        artworkService.deleteArtwork(artworkId);
        return ApiResponse.<Void>builder().message("Artwork deleted successfully").build();
    }
}
