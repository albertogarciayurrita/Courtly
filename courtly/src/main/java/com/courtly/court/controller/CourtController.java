package com.courtly.court.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.court.dto.CourtRequest;
import com.courtly.court.dto.CourtResponse;
import com.courtly.court.service.CourtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(
        name = "Courts",
        description = "Operations for managing courts within a sports facility"
)
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/facilities/{facilityId}/courts")
public class CourtController {
    
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @Operation(
        summary = "Create a court",
        description = "Creates a new court inside the specified facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Court created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid court data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @PostMapping
    public ResponseEntity<CourtResponse> createCourt(@PathVariable Long facilityId, @Valid @RequestBody CourtRequest request) {

        CourtResponse courtResponse = courtService.createCourt(facilityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(courtResponse);
    }

    @Operation(
        summary = "List courts by facility",
        description = "Returns all courts belonging to the specified facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping
    public ResponseEntity<List<CourtResponse>> findAllByFacility(@PathVariable Long facilityId) {

        List<CourtResponse> response = courtService.findAllByFacility_Id(facilityId);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get court by ID",
        description = "Returns a court belonging to the specified facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Court retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility or court not found")
    })
    @GetMapping("/{courtId}")
    public ResponseEntity<CourtResponse> findById(@PathVariable Long facilityId, @PathVariable Long courtId) {

        CourtResponse response = courtService.findById(facilityId, courtId);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update a court",
        description = "Updates an existing court within the specified facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Court updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid court data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility or court not found")
    })
    @PutMapping("/{courtId}")
    public ResponseEntity<CourtResponse> updateCourt(@PathVariable Long facilityId, @PathVariable Long courtId, @Valid @RequestBody CourtRequest request) {

        CourtResponse response = courtService.updateCourt(facilityId, courtId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a court",
        description = "Deletes a court from the specified facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Court deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility or court not found")
    })
    @DeleteMapping("/{courtId}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long facilityId, @PathVariable Long courtId) {

        courtService.deleteCourt(facilityId, courtId);
        return ResponseEntity.noContent().build();
    }
}
