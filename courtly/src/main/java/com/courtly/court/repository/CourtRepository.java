package com.courtly.court.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.court.entity.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {

    List<Court> findAllByFacility_Id(Long facilityId);
    Optional<Court> findByIdAndFacility_Id(Long courtId, Long facilityId);
}
