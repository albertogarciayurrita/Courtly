package com.courtly.auth.dto;

import com.courtly.user.entity.Role;

public record LoginResponse(
    
    String token,
    String tokenType,
    Long userId,
    String username,
    String email,
    int credits,
    Role role
) {
}