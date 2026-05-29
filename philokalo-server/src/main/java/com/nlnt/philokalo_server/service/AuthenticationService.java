package com.nlnt.philokalo_server.service;

import com.nimbusds.jose.JOSEException;
import com.nlnt.philokalo_server.dto.request.AuthenticationRequest;
import com.nlnt.philokalo_server.dto.request.IntrospectRequest;
import com.nlnt.philokalo_server.dto.response.AuthenticationResponse;
import com.nlnt.philokalo_server.dto.response.IntrospectResponse;
import java.text.ParseException;

/**
 *
 * @author nghia
 */
public interface AuthenticationService {

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
