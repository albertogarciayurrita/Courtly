package com.courtly.user.dto;

public record UserRegistrationRequest (
    String username,
    String email,
    String password
){
}
