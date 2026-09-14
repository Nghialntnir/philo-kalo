package com.nlnt.philokalo_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    Optional<Tag> findByName(String name);

    Optional<Tag> findBySlug(String slug);
}
