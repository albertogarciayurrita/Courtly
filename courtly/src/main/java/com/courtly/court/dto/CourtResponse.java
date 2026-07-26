package com.courtly.court.dto;

import java.math.BigDecimal;

public record CourtResponse(

    Long id,
    String name,
    String description,
    BigDecimal creditsCost,
    boolean active,
    Long facilityId
) {
}
