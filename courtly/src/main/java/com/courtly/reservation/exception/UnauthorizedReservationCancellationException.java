package com.courtly.reservation.exception;

public class UnauthorizedReservationCancellationException extends RuntimeException {
    public UnauthorizedReservationCancellationException(String message) {
        super(message);
    }
    
}
