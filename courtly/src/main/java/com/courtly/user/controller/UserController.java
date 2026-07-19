package com.courtly.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.courtly.user.dto.UserRegistrationRequest;
import com.courtly.user.dto.UserRegistrationResponse;
import com.courtly.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        
        UserRegistrationResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
