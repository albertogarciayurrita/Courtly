package com.courtly.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.courtly.user.dto.UserRegistrationRequest;
import com.courtly.user.dto.UserRegistrationResponse;
import com.courtly.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@Tag(
        name = "Authentication",
        description = "Operations for user authentication and account access"
)
@RequestMapping("/auth")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Register user",
        description = "Creates a new user account."
)
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "User registered successfully"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid registration data"
        )})
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        
        UserRegistrationResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
