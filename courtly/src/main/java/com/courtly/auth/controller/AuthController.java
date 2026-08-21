package com.courtly.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.auth.dto.LoginRequest;
import com.courtly.auth.dto.LoginResponse;
import com.courtly.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Tag(
        name = "Authentication",
        description = "Operations for user authentication and account access"
)
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "Login user",
        description = "Authenticates a user with email and password and returns a JWT token."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Login successful"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Invalid credentials")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    
}
