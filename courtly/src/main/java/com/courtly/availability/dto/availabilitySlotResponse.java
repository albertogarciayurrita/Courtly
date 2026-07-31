package com.courtly.availability.dto;

import java.time.LocalTime;

public record AvailabilitySlotResponse(
    LocalTime startTime,
    LocalTime endtime,
    boolean available
) {
}  
