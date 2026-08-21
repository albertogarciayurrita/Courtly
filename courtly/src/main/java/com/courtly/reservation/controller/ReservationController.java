package com.courtly.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.dto.BookingResponse;
import com.courtly.reservation.dto.CancellationResponse;
import com.courtly.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(
        name = "Reservations",
        description = "Operations for creating and cancelling court reservations")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/reservations")
public class ReservationController {
    
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(
        summary = "Create a reservation",
        description = "Creates a reservation for the authenticated user in the requested court and time slot.")

    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Reservation created successfully"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid reservation request or time slot"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Authentication is required"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "User or court not found"
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Court unavailable, insufficient credits, or time slot already reserved")})

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(Authentication authentication, @Valid @RequestBody BookingRequest request){

        BookingResponse response = reservationService.createBooking(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Cancel a reservation",
        description = "Cancels an existing reservation. The reservation owner or an administrator can perform this operation.")
        @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User is not allowed to cancel this reservation"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reservation not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Reservation is already cancelled"
            )
    })
    
    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<CancellationResponse> cancelReservation(Authentication authentication, @PathVariable Long reservationId){
        CancellationResponse response = reservationService.cancelReservartion(authentication.getName(), reservationId); //authentication.getName() return the main id of the user (in my case the email)
        return ResponseEntity.ok(response);
    }
}
