package com.nlnt.philokalo_server.service;

import com.nlnt.philokalo_server.dto.request.AuthenticationRequest;

/**
 *
 * @author nghia
 */
public interface AuthenticationService {

    boolean authenticate(AuthenticationRequest request);
}
