package com.courtly.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.courtly.court.entity.Court;
import com.courtly.court.exception.CourtNotFoundException;
import com.courtly.court.repository.CourtRepository;
import com.courtly.credit.entity.CreditTransaction;
import com.courtly.credit.entity.CreditTransactionType;
import com.courtly.credit.repository.CreditTransactionRepository;
import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.dto.BookingResponse;
import com.courtly.reservation.dto.CancellationResponse;
import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.entity.ReservationStatus;
import com.courtly.reservation.exception.InactiveCourtException;
import com.courtly.reservation.exception.InvalidReservationSlotException;
import com.courtly.reservation.exception.ReservationAlreadyCancelledException;
import com.courtly.reservation.exception.ReservationConflictException;
import com.courtly.reservation.exception.ReservationNotFoundException;
import com.courtly.reservation.exception.UnauthorizedReservationCancellationException;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.slot.dto.BookingSlot;
import com.courtly.slot.service.BookingSlotService;
import com.courtly.user.entity.Role;
import com.courtly.user.entity.User;
import com.courtly.user.exception.UserNotFoundException;
import com.courtly.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
    
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final BookingSlotService bookingSlotService;
    private final ReservationRepository reservationRepository;
    private final CreditTransactionRepository creditTransactionRepository;

    public ReservationService(CourtRepository courtRepository, UserRepository userRepository, BookingSlotService bookingSlotService, ReservationRepository reservationRepository, CreditTransactionRepository creditTransactionRepository) {
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
        this.bookingSlotService = bookingSlotService;
        this.reservationRepository = reservationRepository;
        this.creditTransactionRepository = creditTransactionRepository;
    }

    @Transactional
    public BookingResponse createBooking(String aunthenticatedEmail, BookingRequest request){

        User user = userRepository.findByEmail(aunthenticatedEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Court court = courtRepository.findById(request.courtId())
            .orElseThrow(() -> new CourtNotFoundException("Court not found"));

        if(!court.isActive()){
            throw new InactiveCourtException("Court with ID " + court.getId() + " is not active");
        }

        validateReservationDateTime(request.reservationDate(), request.startTime());

        BookingSlot selectedSlot = bookingSlotService.generateBookingSlots(
            court.getFacility().getOpeningTime(), court.getFacility().getClosingTime())
            .stream()
            .filter(slot -> slot.startTime().equals(request.startTime())).findFirst().orElseThrow(() -> new InvalidReservationSlotException("The selectd start time does not match a valid booking slot"));

        boolean alreadyReserved = reservationRepository.existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
            court.getId(),  request.reservationDate(), request.startTime(), ReservationStatus.CONFIRMED);

        if(alreadyReserved){
            throw new ReservationConflictException("The selected time slot is already reserved");
        }

        Reservation reservation = new Reservation(user, court, request.reservationDate(), selectedSlot.startTime(), selectedSlot.endTime());
        int creditCost = court.getCreditCost();
        user.deductCredits(creditCost);
        Reservation savedReservation = reservationRepository.save(reservation);
        CreditTransaction creditTransaction = new CreditTransaction(user, -creditCost, CreditTransactionType.RESERVATION);

        CreditTransaction savedCreditTransaction = creditTransactionRepository.save(creditTransaction);
        
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


    @Transactional
    public CancellationResponse cancelReservartion(String authentictedEmail, Long reservationId){

        User authenticatedUser = userRepository.findByEmail(authentictedEmail)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        boolean isOwner = reservation.getUser().getId().equals(authenticatedUser.getId());
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;

        if(!isOwner && !isAdmin) {
            throw new UnauthorizedReservationCancellationException("User is not allowed to cancel this reservation");
        }

        if(reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        User reservationOwner = reservation.getUser();

        int refundCredits = reservation.getCourt().getCreditCost();

        reservationOwner.addCredits(refundCredits);

        CreditTransaction refundTransaction = new CreditTransaction(reservationOwner, refundCredits, CreditTransactionType.REFUND);

        creditTransactionRepository.save(refundTransaction);

        return new CancellationResponse(reservation.getId(), reservation.getStatus());
    }
}
