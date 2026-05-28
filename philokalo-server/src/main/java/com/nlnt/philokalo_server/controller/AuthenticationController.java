package com.nlnt.philokalo_server.controller;

import com.nlnt.philokalo_server.dto.request.AuthenticationRequest;
import com.nlnt.philokalo_server.dto.response.ApiResponse;
import com.nlnt.philokalo_server.dto.response.AuthenticationResponse;
import com.nlnt.philokalo_server.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nghia
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ApiResponse<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder().isAuthenticated(result).build()).build();
    }

}
