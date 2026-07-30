package com.courtly.availability.dto;

import java.time.LocalTime;

public record availabilitySlotResponse(
    LocalTime startTime,
    LocalTime endtime,
    boolean available
) {
}  
