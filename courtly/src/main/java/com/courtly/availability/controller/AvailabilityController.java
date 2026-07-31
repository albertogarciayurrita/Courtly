package com.courtly.availability.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.courtly.availability.dto.AvailabilitySlotResponse;
import com.courtly.availability.service.AvailabilityService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/facilities/{facilityId}/courts/{courtId}/availability")
public class AvailabilityController {
    
    private AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }


    @GetMapping
    public ResponseEntity<List<AvailabilitySlotResponse>> getCourtAvailability(@PathVariable Long facilityId,
                @PathVariable Long courtId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<AvailabilitySlotResponse> response = availabilityService.getCourtAvailability(facilityId, courtId, date);

        return ResponseEntity.ok(response);
    }
    
}
