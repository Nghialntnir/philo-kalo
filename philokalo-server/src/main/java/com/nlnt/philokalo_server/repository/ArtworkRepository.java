package com.nlnt.philokalo_server.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.Artwork;

public interface ArtworkRepository extends JpaRepository<Artwork, String> {
    List<Artwork> findByArtistIdOrderByCreatedAtDesc(String artistId);
    Optional<Artwork> findByIdAndArtistId(String id, String artistId);
    List<Artwork> findByStatusOrderByCreatedAtDesc(String status);
    List<Artwork> findByIsForSaleTrueOrderByCreatedAtDesc();
    List<Artwork> findByArtistIdAndStatusOrderByCreatedAtDesc(String artistId, String status);
    List<Artwork> findByCategorySetId(String categoryId);
}
