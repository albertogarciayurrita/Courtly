package com.courtly.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest (
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 6 and 100 characters")
    String password
){
}
