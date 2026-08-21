package com.courtly.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record BookingRequest (

    @Schema(
        description = "Identifier of the court to reserve",
        example = "20"
    )
    @NotNull(message = "Court ID cannot be null")
    Long courtId,

    @Schema(
        description = "Date of the reservation",
        example = "2026-08-25"
    )
    @NotNull(message = "Reservation date cannot be null")
    @FutureOrPresent(message = "Reservation date cannot be in the past")
    LocalDate reservationDate,

    @Schema(
        description = "Start time of the booking slot",
        example = "17:00:00"
    )
    @NotNull(message = "Start time cannot be null")
    LocalTime startTime

) {
}
