package com.courtly.court.dto;

public record CourtResponse(

    Long id,
    String name,
    String description,
    int creditsCost,
    boolean active,
    Long facilityId
) {
}
