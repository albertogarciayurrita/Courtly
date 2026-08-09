package com.courtly.reservation.exception;

public class ReservationAlreadyCancelledExcetion extends RuntimeException {
    public ReservationAlreadyCancelledExcetion(String message) {
        super(message);
    }
    
}
