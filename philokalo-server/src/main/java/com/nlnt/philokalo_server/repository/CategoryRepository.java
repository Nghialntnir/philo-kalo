package com.nlnt.philokalo_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nlnt.philokalo_server.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
