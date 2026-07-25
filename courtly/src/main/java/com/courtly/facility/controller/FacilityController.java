package com.courtly.facility.controller;

import org.springframework.web.bind.annotation.RestController;

import com.courtly.facility.service.FacilityService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("/facility")
public class FacilityController {
    
    private FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping
    public ResponseEntity<FacilityResponse> createFacility(@Valid @RequestBody FacilityRequest request) {
        
        FacilityResponse response = facilityService.createFacility(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<FacilityResponse>> findAll() {
        List<FacilityResponse> response = facilityService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    public ResponseEntity<FacilityResponse> findById(@PathVariable Long id) {
        FacilityResponse response = facilityService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/id")
    public ResponseEntity<FacilityResponse> updateFacility(@PathVariable Long id, FacilityRequest request) {
        
        FacilityResponse response = facilityService.updateFacility(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/id")
    public ResponseEntity<FacilityResponse> deleteFacility(@PathVariable Long id) {
        
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }
}
