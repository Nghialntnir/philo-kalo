package com.nlnt.philokalo_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.Role;

/**
 *
 * @author nghia
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
