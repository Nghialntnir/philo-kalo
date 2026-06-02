package com.nlnt.philokalo_server.security;

import com.nimbusds.jose.JOSEException;
import com.nlnt.philokalo_server.dto.request.IntrospectRequest;
import com.nlnt.philokalo_server.service.AuthenticationService;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author nghia Verify token mỗi khi request
 */
@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${app.jwt.signer-key}")
    private String signerKey;

    @Value("${app.jwt.algorithm}")
    private String algorithm;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) {
        try { // Kiểm tra token còn valid ko
            var response = authenticationService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());
            if (!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), algorithm);
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }

}
