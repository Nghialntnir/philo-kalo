package com.nlnt.philokalo_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import com.nlnt.philokalo_server.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Category> findByName(String name);
    Optional<Category> findBySlug(String slug);
    List<Category> findByParentIdOrderByNameAsc(String parentId);
    List<Category> findByParentIsNullOrderByNameAsc();
}
