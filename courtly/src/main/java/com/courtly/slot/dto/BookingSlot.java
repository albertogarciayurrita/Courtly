package com.courtly.slot.dto;

import java.time.LocalTime;

import com.courtly.slot.exception.InvalidBookingSlotException;

public record BookingSlot(
    LocalTime startTime,
    LocalTime endTime
) {
    public BookingSlot {
        if (startTime == null || endTime == null) {
            throw new InvalidBookingSlotException("Start time and end time cannot be null");
        }
        if (!startTime.isBefore(endTime)) {
            throw new InvalidBookingSlotException("Start time must be before end time");
        }
    }
}
