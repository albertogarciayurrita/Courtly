package com.courtly.shared.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden //a want to avoid to appear in swagger documentation, as this is a temporary controller for testing purposes only
@RestController
@RequestMapping("/test")
public class SecurityTestController {
    
    // Temporary endpoints for manually verifying role-based authorization.
    // Remove when real USER and ADMIN endpoints are available.

    @GetMapping("/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("USER endpoint accessed successfully!");
    }
    
    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("ADMIN endpoint accessed successfully!");
    }
}
