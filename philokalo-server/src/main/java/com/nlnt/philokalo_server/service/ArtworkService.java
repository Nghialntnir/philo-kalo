package com.nlnt.philokalo_server.service;

import java.util.List;

import com.nlnt.philokalo_server.dto.request.ArtworkRequest;
import com.nlnt.philokalo_server.dto.request.ArtworkUploadRequest;
import com.nlnt.philokalo_server.dto.response.ArtworkResponse;
import com.nlnt.philokalo_server.dto.response.ArtworkUploadResponse;

public interface ArtworkService {
    ArtworkUploadResponse createUploadAuthorization(ArtworkUploadRequest request);
    ArtworkResponse createArtwork(ArtworkRequest request);
    ArtworkResponse getArtwork(String artworkId);
    List<ArtworkResponse> getMyArtworks();
    ArtworkResponse updateArtwork(String artworkId, ArtworkRequest request);
    void deleteArtwork(String artworkId);
}
