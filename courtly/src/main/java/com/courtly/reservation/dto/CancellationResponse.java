package com.courtly.reservation.dto;

import com.courtly.reservation.entity.ReservationStatus;

public record CancellationResponse(
    Long reservationId, 
    ReservationStatus status
){
}