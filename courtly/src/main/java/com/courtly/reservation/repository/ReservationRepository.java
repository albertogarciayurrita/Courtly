package com.courtly.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    
}
