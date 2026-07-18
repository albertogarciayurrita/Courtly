package com.courtly.user.dto;

import java.time.Instant;

import com.courtly.user.entity.Role;

public record UserRegistrationResponse (

    Long id,
    String username,
    String email,
    int credits,
    Role role,
    Instant createdAt
) {
}
