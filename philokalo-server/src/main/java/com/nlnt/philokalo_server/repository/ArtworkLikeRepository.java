package com.nlnt.philokalo_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.ArtworkLike;
import com.nlnt.philokalo_server.model.ArtworkLikePK;

public interface ArtworkLikeRepository extends JpaRepository<ArtworkLike, ArtworkLikePK> {

    List<ArtworkLike> findByArtworkLikePKArtworkIdOrderByCreatedAtDesc(String artworkId);

    List<ArtworkLike> findByArtworkLikePKUserIdOrderByCreatedAtDesc(String userId);

    boolean existsByArtworkLikePKArtworkIdAndArtworkLikePKUserId(String artworkId, String userId);

    void deleteByArtworkLikePKArtworkIdAndArtworkLikePKUserId(String artworkId, String userId);

    long countByArtworkLikePKArtworkId(String artworkId);
}
