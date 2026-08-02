package com.courtly.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.dto.BookingResponse;
import com.courtly.reservation.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(Authentication authentication, @Valid @RequestBody BookingRequest request){

        BookingResponse response = reservationService.createBooking(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
