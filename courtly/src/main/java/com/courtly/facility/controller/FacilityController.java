package com.courtly.facility.controller;

import org.springframework.web.bind.annotation.RestController;

import com.courtly.facility.service.FacilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.courtly.facility.dto.FacilityRequest;
import com.courtly.facility.dto.FacilityResponse;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@Tag(
        name = "Facilities",
        description = "Operations for managing sports facilities"
)
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/facilities")
public class FacilityController {
    
    private FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @Operation(
        summary = "Create a facility",
        description = "Creates a new sports facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Facility created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid facility data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    @PostMapping
    public ResponseEntity<FacilityResponse> createFacility(@Valid @RequestBody FacilityRequest request) {
        
        FacilityResponse response = facilityService.createFacility(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(
        summary = "List facilities",
        description = "Returns all sports facilities."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facilities retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    @GetMapping
    public ResponseEntity<List<FacilityResponse>> findAll() {
        List<FacilityResponse> response = facilityService.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get facility by ID",
        description = "Returns a facility identified by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacilityResponse> findById(@PathVariable Long id) {
        FacilityResponse response = facilityService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update a facility",
        description = "Updates an existing sports facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid facility data"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FacilityResponse> updateFacility(@PathVariable Long id, @Valid @RequestBody FacilityRequest request) {
        
        FacilityResponse response = facilityService.updateFacility(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a facility",
        description = "Deletes an existing sports facility."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Facility deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<FacilityResponse> deleteFacility(@PathVariable Long id) {
        
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }
}
