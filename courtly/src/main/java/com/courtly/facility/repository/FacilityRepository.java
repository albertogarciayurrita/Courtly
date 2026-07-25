package com.courtly.facility.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.facility.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

}