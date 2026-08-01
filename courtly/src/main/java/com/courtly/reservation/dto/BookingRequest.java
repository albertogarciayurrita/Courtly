package com.courtly.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record BookingRequest (
    
    @NotNull(message = "Court ID cannot be null")
    Long courtId,

    @NotNull(message = "Reservation date cannot be null")
    @FutureOrPresent(message = "Reservation date cannot be in the past")
    LocalDate reservationDate,

    @NotNull(message = "Start time cannot be null")
    LocalTime startTime
) {
}
