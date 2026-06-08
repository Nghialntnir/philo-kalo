package com.nlnt.philokalo_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.Permission;

/**
 *
 * @author nghia
 */
public interface PermissionRepository extends JpaRepository<Permission, String> {

    boolean existsByName(String name);

    Optional<Permission> findByName(String name);
}
