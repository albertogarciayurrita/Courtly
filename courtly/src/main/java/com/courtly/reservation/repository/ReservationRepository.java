package com.courtly.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    
    List<Reservation> findAllByCourt_IdAndReservationDate(Long courtId, LocalDate reservatiDate);

}
