package com.courtly.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.courtly.court.entity.Court;
import com.courtly.court.repository.CourtRepository;
import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.dto.BookingResponse;
import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.entity.ReservationStatus;
import com.courtly.reservation.exception.InvalidReservationSlotException;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.slot.dto.BookingSlot;
import com.courtly.slot.service.BookingSlotService;
import com.courtly.user.dto.UserRegistrationRequest;
import com.courtly.user.entity.User;
import com.courtly.user.repository.UserRepository;

import jakarta.transaction.Transactional;

public class ReservationService {
    
    CourtRepository courtRepository;
    UserRepository userRepository;
    BookingSlotService bookingSlotService;
    ReservationRepository reservationRepository;

    public ReservationService(CourtRepository courtRepository, UserRepository userRepository, BookingSlotService bookingSlotService, ReservationRepository reservationRepository) {
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
        this.bookingSlotService = bookingSlotService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public BookingResponse createBooking(String aunthenticatedEmail, BookingRequest request){

        User user = userRepository.findByEmail(aunthenticatedEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Court court = courtRepository.findById(request.courtId())
            .orElseThrow(() -> new IllegalArgumentException("Court not found"));

        if(!court.isActive()){
            throw new IllegalArgumentException("Court with ID " + court.getId() + " is not active");
        }

        validateReservationDateTime(request.reservationDate(), request.startTime());

        BookingSlot selectedSlot = bookingSlotService.generateBookingSlots(
            court.getFacility().getOpeningTime(), court.getFacility().getClosingTime())
            .stream()
            .filter(slot -> slot.startTime().equals(request.startTime())).findFirst().orElseThrow(() -> new InvalidReservationSlotException("The selectd start time does not match a valid booking slot"));

        boolean alreadyReserved = reservationRepository.existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
            court.getId(),  request.reservationDate(), request.startTime(), ReservationStatus.CONFIRMED);

        if(alreadyReserved){
            throw new InvalidReservationSlotException("The selected time slot is already reserved");
        }

        Reservation reservation = new Reservation(user, court, request.reservationDate(), selectedSlot.startTime(), selectedSlot.endTime());
        Reservation savedReservation = reservationRepository.save(reservation);
        
        return new BookingResponse(savedReservation.getId(), savedReservation.getUser().getId(), savedReservation.getCourt().getId(), 
        savedReservation.getCourt().getName(), savedReservation.getReservationDate(), savedReservation.getStartTime(), 
        savedReservation.getEndTime(), savedReservation.getStatus().name(), savedReservation.getCreatedAt());
    }

    private void validateReservationDateTime(LocalDate reservationDate, LocalTime startTime) {
        
        LocalDateTime requestDateTime = LocalDateTime.of(reservationDate, startTime);

        if(requestDateTime.isBefore(LocalDateTime.now())){
            throw new InvalidReservationSlotException("Reservation date and time must be in the future");
        }
    }
}
