package com.courtly.court.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.court.entity.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {

    List<Court> findAllByFacility_Id(Long facilityId);
}
