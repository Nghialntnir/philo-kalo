package com.nlnt.philokalo_server.repository;

import com.nlnt.philokalo_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nghia
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsById(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
