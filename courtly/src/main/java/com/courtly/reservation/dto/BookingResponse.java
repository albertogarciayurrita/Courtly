package com.courtly.reservation.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record BookingResponse(

    Long id,
    Long userId,
    Long courtId,
    String courtName,
    LocalDate reservationDate,
    LocalTime startTime,
    LocalTime endTime,
    String status,
    Instant createdAt
) {
}
