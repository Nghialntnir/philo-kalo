package com.nlnt.philokalo_server.repository;

import com.nlnt.philokalo_server.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nghia
 */
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

}
