package com.nlnt.philokalo_server.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.InvalidatedToken;

/**
 *
 * @author nghia
 */
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    List<InvalidatedToken> findByExpiryTimeBefore(Date time);
    void deleteByExpiryTimeBefore(Date time);
}
