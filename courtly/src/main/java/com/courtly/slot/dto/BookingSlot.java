package com.courtly.slot.dto;

import java.time.LocalTime;

public record BookingSlot(
    LocalTime startTime,
    LocalTime endTime
) {
    public BookingSlot {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}
