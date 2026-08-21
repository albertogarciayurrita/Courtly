package com.courtly.availability.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.availability.dto.AvailabilitySlotResponse;
import com.courtly.availability.service.AvailabilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Tag(
        name = "Availability",
        description = "Operations for checking court availability"
)
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/facilities/{facilityId}/courts/{courtId}/availability")
public class AvailabilityController {
    
    private AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Operation(
        summary = "Get court availability",
        description = "Returns the available and unavailable time slots for a court on the specified date."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Court availability retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or missing date"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Facility or court not found"
            )
    })
    @GetMapping
    public ResponseEntity<List<AvailabilitySlotResponse>> getCourtAvailability(@PathVariable Long facilityId,
                @PathVariable Long courtId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<AvailabilitySlotResponse> response = availabilityService.getCourtAvailability(facilityId, courtId, date);

        return ResponseEntity.ok(response);
    }
    
}
