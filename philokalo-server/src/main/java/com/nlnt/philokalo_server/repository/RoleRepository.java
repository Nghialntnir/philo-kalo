package com.nlnt.philokalo_server.repository;

import com.nlnt.philokalo_server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nghia
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByName(String name);

}
