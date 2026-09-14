package com.nlnt.philokalo_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.ArtworkComment;

public interface ArtworkCommentRepository extends JpaRepository<ArtworkComment, String> {

    List<ArtworkComment> findByArtworkIdAndParentIsNullOrderByCreatedAtAsc(String artworkId);

    List<ArtworkComment> findByArtworkIdOrderByCreatedAtAsc(String artworkId);

    List<ArtworkComment> findByUserIdOrderByCreatedAtDesc(String userId);

    long countByArtworkIdAndIsHiddenFalse(String artworkId);
}
