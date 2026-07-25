package com.courtly.facility.dto;

public record FacilityResponse(
        Long id,
        String name,
        String address,
        String description,
        String openingTime,
        String closingTime
) {
}
