package com.courtly.court.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.court.dto.CourtRequest;
import com.courtly.court.dto.CourtResponse;
import com.courtly.court.entity.Court;
import com.courtly.court.exception.CourtNotFoundException;
import com.courtly.court.repository.CourtRepository;
import com.courtly.facility.entity.Facility;
import com.courtly.facility.service.FacilityService;

@Service
public class CourtService {
    
    private final CourtRepository courtRepository;
    private final FacilityService facilityService;

    public CourtService(CourtRepository courtRepository, FacilityService facilityService) {
        this.courtRepository = courtRepository;
        this.facilityService = facilityService;
    }

    @Transactional
    public CourtResponse createCourt(Long facilityId, CourtRequest request){
        Facility facility = facilityService.getFacilityById(facilityId);

        Court court = new Court(
            request.name(),
            request.description(),
            request.creditCost(),
            request.active(),
            facility
        );

        Court savedCourt = courtRepository.save(court);

        return toResponse(savedCourt);
    }

    @Transactional(readOnly = true)
    public List<CourtResponse> findAllByFacility_Id(Long facilityId){
        List<Court> courts = courtRepository.findAllByFacility_Id(facilityId);

        return courts.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CourtResponse findById(Long facilityId, Long courtId){

        Court court = getCourtByIdAndFacilityId(courtId, facilityId);

        return toResponse(court);
    }

    @Transactional
    public CourtResponse updateCourt(Long facilityId, Long courtId, CourtRequest request){

        Court court = getCourtByIdAndFacilityId(courtId, facilityId);

        court.setName(request.name());
        court.setDescription(request.description());
        court.setCreditCost(request.creditCost());
        court.setActive(request.active());

        Court updatedCourt = courtRepository.save(court);

        return toResponse(updatedCourt);
    }

    @Transactional
    public void deleteCourt(Long facilityId, Long courtId){

        Court court = getCourtByIdAndFacilityId(courtId, facilityId);

        courtRepository.delete(court);
    }

    private Court getCourtByIdAndFacilityId(Long courtId, Long facilityId){

        return courtRepository.findByIdAndFacility_Id(courtId, facilityId).orElseThrow(() -> new CourtNotFoundException(
            "Court with ID " + courtId
                    + " was not found in facility with ID "
                    + facilityId + "."
        ));

    }

    private CourtResponse toResponse(Court court){

        return new CourtResponse(
            court.getId(),
            court.getName(),
            court.getDescription(),
            court.getCreditCost(),
            court.isActive(),
            court.getFacility().getId()
        );
    }
}
