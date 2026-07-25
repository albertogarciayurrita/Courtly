package com.courtly.facility.exception;

public class FacilityNotFoundException extends RuntimeException{
    
    public FacilityNotFoundException(String message) {
        super(message);
    }
}
