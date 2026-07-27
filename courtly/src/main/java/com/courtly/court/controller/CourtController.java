package com.courtly.court.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/facilities/{facilityId}/courts")
public class CourtController {
    
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    public ResponseEntity<CourtResponse> createCourt(@PathVariable Long facilityId, @Valid @RequestBody CourtRequest request) {

        CourtResponse courtResponse = courtService.createCourt(facilityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(courtResponse);
    }

    @GetMapping
    public ResponseEntity<List<CourtResponse>> findAllByFacility(@PathVariable Long facilityId) {

        List<CourtResponse> response = courtService.findAllByFacility_Id(facilityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<CourtResponse> findById(@PathVariable Long facilityId, @PathVariable Long courtId) {

        CourtResponse response = courtService.findById(facilityId, courtId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<CourtResponse> updateCourt(@PathVariable Long facilityId, @PathVariable Long courtId, @Valid @RequestBody CourtRequest request) {

        CourtResponse response = courtService.updateCourt(facilityId, courtId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courtId}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long facilityId, @PathVariable Long courtId) {

        courtService.deleteCourt(facilityId, courtId);
        return ResponseEntity.noContent().build();
    }
}
