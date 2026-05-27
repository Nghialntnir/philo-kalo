package com.nlnt.philokalo_server.controller;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.response.ApiResponse;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nghia
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    ApiResponse<String> login() {
        ApiResponse<String> api = new ApiResponse<>();
        api.setResult("Hello world");
        return api;
    }

    @PostMapping("/sign-up")
    ApiResponse<User> signup(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
}
