package com.nlnt.philokalo_server.repository;

import com.nlnt.philokalo_server.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nghia
 */
public interface PermissionRepository extends JpaRepository<Permission, String> {

    boolean existsByName(String name);
}
