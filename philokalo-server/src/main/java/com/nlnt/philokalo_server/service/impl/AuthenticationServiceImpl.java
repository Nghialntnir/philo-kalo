package com.nlnt.philokalo_server.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
import com.nlnt.philokalo_server.dto.request.LogoutRequest;
import com.nlnt.philokalo_server.dto.request.RefreshRequest;
import com.nlnt.philokalo_server.dto.response.AuthenticationResponse;
import com.nlnt.philokalo_server.dto.response.IntrospectResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.model.InvalidatedToken;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.InvalidatedTokenRepository;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

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

    @NonFinal
    @Value("${app.jwt.valid-duration}")
    long VALID_DURATION;

    @NonFinal
    @Value("${app.jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException {
        var signJWT = verifyToken(request.getToken(), true);
        var jti = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException ex) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

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

    // Create and sign a JWT token
    // JWT include 3 parses Header (include Althgorithm), Payload (issue at, expTime, claims), Signature
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jWTClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("philokalo.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
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

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jti = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException ex) {
            log.info("Token already expired");
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getUserRoleSet())) {
            user.getUserRoleSet().forEach(userRole -> {
                // Thêm role name vào scope
                stringJoiner.add("ROLE_" + userRole.getRole().getName());
                // Thêm permissions của role đó vào scope
                if (!CollectionUtils.isEmpty(userRole.getRole().getRolePermissionSet())) {
                    userRole.getRole()
                            .getRolePermissionSet()
                            .forEach(rolePermission -> stringJoiner.add(
                                    rolePermission.getPermission().getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
