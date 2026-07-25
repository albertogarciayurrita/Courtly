package com.courtly.facility.service;

import java.util.List;
import java.util.Optional;

import com.courtly.facility.dto.FacilityRequest;
import com.courtly.facility.dto.FacilityResponse;
import com.courtly.facility.entity.Facility;
import com.courtly.facility.exception.FacilityNotFoundException;
import com.courtly.facility.repository.FacilityRepository;

import org.springframework.transaction.annotation.Transactional;

public class FacilityService {
    
    private final FacilityRepository facilityRepository;
    
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Transactional
    public FacilityResponse createFacility(FacilityRequest request) {
        validateSchedule(request);

        Facility  facility = new Facility(
            request.name(),
            request.address(),
            request.description(),
            request.openingTime(),
            request.closingTime()
        );

        Facility savedFacility = facilityRepository.save(facility);

        return toResponse(savedFacility);
    }

    @Transactional(readOnly = true)
    public List<FacilityResponse> findAll() {
        List<Facility> facilities = facilityRepository.findAll();

        return facilities.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FacilityResponse findById(Long id) {
        Facility facility = getFacilityById(id);

        return toResponse(facility);
    }

    public Facility getFacilityById(Long id) {
        Optional<Facility> facility = facilityRepository.findById(id);

        if (facility.isEmpty()) {
            throw new FacilityNotFoundException("Facility with ID " + id + " not found.");
        }

        return facility.get();
    }

    private void validateSchedule(FacilityRequest request) {
        if (!request.openingTime().isBefore(request.closingTime())) {
            throw new IllegalArgumentException("Opening time must be before closing time.");
        }
    }

    private FacilityResponse toResponse(Facility facility) {
        return new FacilityResponse(
            facility.getId(),
            facility.getName(),
            facility.getAddress(),
            facility.getDescription(),
            facility.getOpeningTime().toString(),
            facility.getClosingTime().toString()
        );
    }

    @Transactional
    public FacilityResponse updateFacility(Long id, FacilityRequest request) {
        
        validateSchedule(request);

        Facility facility = getFacilityById(id);

        facility.setName(request.name());
        facility.setAddress(request.address());
        facility.setDescription(request.description());
        facility.setOpeningTime(request.openingTime());
        facility.setClosingTime(request.closingTime());

        Facility updatedFacility = facilityRepository.save(facility);

        return toResponse(updatedFacility);
    }

    @Transactional
    public void deleteFacility(Long id) {
        Facility facility = getFacilityById(id);
        facilityRepository.delete(facility);
    }
}
