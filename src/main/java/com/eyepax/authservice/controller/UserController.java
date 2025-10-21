package com.eyepax.authservice.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // Endpoint for any authenticated user
    @GetMapping("/api/v1/me")
    public String getProfile(@AuthenticationPrincipal Jwt jwt) {
        // Return some info from the JWT claims
        String email = jwt.getClaimAsString("email"); // Cognito usually includes email
        String username = jwt.getClaimAsString("cognito:username"); // optional username
        return "Hello, " + email + " (username: " + username + ")";
    }

    // Endpoint for admin users only
    @GetMapping("/api/v1/admin/test")
    public String adminTest() {
        return "Hello Admin! You have access to this endpoint.";
    }

    // Optional: simple health check
    @GetMapping("/healthz")
    public String healthCheck() {
        return "Health is OK";
    }
}
