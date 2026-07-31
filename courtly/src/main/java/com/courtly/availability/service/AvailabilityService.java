package com.courtly.availability.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.availability.dto.AvailabilitySlotResponse;
import com.courtly.availability.exception.InvalidAvailabilityDateException;
import com.courtly.court.entity.Court;
import com.courtly.court.exception.CourtNotFoundException;
import com.courtly.court.repository.CourtRepository;
import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.entity.ReservationStatus;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.slot.dto.BookingSlot;
import com.courtly.slot.service.BookingSlotService;

@Service
public class AvailabilityService {
    
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;
    private final BookingSlotService bookingSlotService;

    public AvailabilityService(CourtRepository courtRepository, ReservationRepository reservationRepository, 
        BookingSlotService bookingSlotService){

            this.courtRepository = courtRepository;
            this.reservationRepository = reservationRepository;
            this.bookingSlotService = bookingSlotService;
        }

    @Transactional(readOnly = true)
    public List<AvailabilitySlotResponse> getCourtAvailability(Long facilityId, Long courtId, LocalDate date) {

        validateDate(date);

        Court court = courtRepository
            .findByIdAndFacility_Id(courtId, facilityId)
            .orElseThrow(() -> new CourtNotFoundException(
                "Court with ID " + courtId + " was not found in facility with ID " + facilityId + "."));

        List<BookingSlot> bookingSlots =
            bookingSlotService.generateBookingSlots(
                court.getFacility().getOpeningTime(),
                court.getFacility().getClosingTime());
            

        List<Reservation> reservations = reservationRepository.findAllByCourt_IdAndReservationDate(courtId, date);
        
        return bookingSlots.stream()
            .map(slot -> new AvailabilitySlotResponse(
                slot.startTime(),
                slot.endTime(),
                court.isActive() && !isBooked(slot, reservations)
            ))
            .toList();

    }

    private boolean isBooked(
            BookingSlot slot,
            List<Reservation> reservations) {

        return reservations.stream()
            .filter(reservation ->
                reservation.getStatus() == ReservationStatus.CONFIRMED
            )
            .anyMatch(reservation ->
                reservation.getStartTime().equals(slot.startTime())
                    && reservation.getEndTime().equals(slot.endTime())
            );
    }

    private void validateDate(LocalDate date) {

        if (date == null) {
            throw new InvalidAvailabilityDateException(
                "Availability date is required."
            );
        }

        if (date.isBefore(LocalDate.now())) {
            throw new InvalidAvailabilityDateException(
                "Availability cannot be checked for a past date."
            );
        }
    }
}
