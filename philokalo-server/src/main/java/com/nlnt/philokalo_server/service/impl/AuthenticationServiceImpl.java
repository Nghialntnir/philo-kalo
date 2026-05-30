package com.nlnt.philokalo_server.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nlnt.philokalo_server.dto.request.AuthenticationRequest;
import com.nlnt.philokalo_server.dto.request.IntrospectRequest;
import com.nlnt.philokalo_server.dto.response.AuthenticationResponse;
import com.nlnt.philokalo_server.dto.response.IntrospectResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.AuthenticationService;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author nghia
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @NonFinal
    @Value("${app.jwt.signer-key}")
    String SIGNER_KEY;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jWTClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("philokalo.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("userId", user.getId())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jWTClaimsSet.toJSONObject());
        JWSObject jWSObject = new JWSObject(header, payload);
        try {
            jWSObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jWSObject.serialize();
        } catch (JOSEException ex) {
            log.error("Uncategorized exception: ", ex);
            throw new RuntimeException(ex);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner("");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}
