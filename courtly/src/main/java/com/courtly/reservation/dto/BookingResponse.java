package com.courtly.reservation.dto;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record BookingResponse(

    Long id,
    Long userId,
    Long courtId,
    String courtName,
    LocalDate reservationDate,
    String startTime,
    String endTime,
    String status,
    Instant createdAt
) {
}
