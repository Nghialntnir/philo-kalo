package com.nlnt.philokalo_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nlnt.philokalo_server.model.ArtworkImage;

public interface ArtworkImageRepository extends JpaRepository<ArtworkImage, String> {
    List<ArtworkImage> findByArtworkId(String artworkId);
}
