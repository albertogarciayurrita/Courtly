package com.courtly.shared.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


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
