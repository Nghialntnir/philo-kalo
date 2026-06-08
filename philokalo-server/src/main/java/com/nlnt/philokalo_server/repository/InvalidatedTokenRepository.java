package com.nlnt.philokalo_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.InvalidatedToken;

/**
 *
 * @author nghia
 */
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
