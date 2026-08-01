package com.courtly.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    
    List<Reservation> findAllByCourt_IdAndReservationDate(Long courtId, LocalDate reservatiDate);
    boolean existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
        Long courtId, LocalDate reservationDate, LocalTime startTime, ReservationStatus status);
}   
