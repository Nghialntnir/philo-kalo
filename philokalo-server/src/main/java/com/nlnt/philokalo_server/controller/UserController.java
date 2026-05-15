package com.nlnt.philokalo_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nghia
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "Hello world!";
    }

    @PostMapping("/sign-up")
    public String signup() {
        return "Sign up";
    }
}
