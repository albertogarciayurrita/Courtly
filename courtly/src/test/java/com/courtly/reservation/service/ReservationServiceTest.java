package com.courtly.reservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courtly.court.entity.Court;
import com.courtly.court.repository.CourtRepository;
import com.courtly.credit.entity.CreditTransaction;
import com.courtly.credit.entity.CreditTransactionType;
import com.courtly.credit.exception.InsufficientCreditsException;
import com.courtly.credit.repository.CreditTransactionRepository;
import com.courtly.facility.entity.Facility;
import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.slot.dto.BookingSlot;
import com.courtly.slot.service.BookingSlotService;
import com.courtly.user.entity.User;
import com.courtly.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    
    @Mock
    private CourtRepository courtRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingSlotService bookingSlotService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CreditTransactionRepository creditTransactionRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp(){
        reservationService = new ReservationService(courtRepository, userRepository, bookingSlotService, reservationRepository, creditTransactionRepository);
    }

    @Test
    void shouldCreateReservationDeductCreditsAndSaveCreditTransaction(){
        String email = "user@test.com";
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        User user = new User("testuser", email, "encoded-password");
        user.setCredits(60);

        Facility facility = org.mockito.Mockito.mock(Facility.class);
        Court court = org.mockito.Mockito.mock(Court.class);

        BookingRequest request = new BookingRequest(
            1L,
            reservationDate,
            startTime
        );

        BookingSlot bookingSlot = new BookingSlot(
                startTime,
                endTime
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(courtRepository.findById(1L))
                .thenReturn(Optional.of(court));

        when(court.isActive())
                .thenReturn(true);

        when(court.getId())
                .thenReturn(1L);

        when(court.getName())
                .thenReturn("Court 1");

        when(court.getCreditCost())
                .thenReturn(10);

        when(court.getFacility())
                .thenReturn(facility);

        when(facility.getOpeningTime())
                .thenReturn(LocalTime.of(8, 0));

        when(facility.getClosingTime())
                .thenReturn(LocalTime.of(22, 0));

        when(bookingSlotService.generateBookingSlots(
                LocalTime.of(8, 0),
                LocalTime.of(22, 0)
        )).thenReturn(List.of(bookingSlot));

        when(reservationRepository
                .existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
                        any(),
                        any(),
                        any(),
                        any()
                ))
                .thenReturn(false);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.createBooking(email, request);

        assertEquals(50, user.getCredits());

        verify(reservationRepository)
                .save(any(Reservation.class));

        ArgumentCaptor<CreditTransaction> transactionCaptor =
                ArgumentCaptor.forClass(CreditTransaction.class);

        verify(creditTransactionRepository)
                .save(transactionCaptor.capture());

        CreditTransaction savedTransaction =
                transactionCaptor.getValue();

        assertEquals(user, savedTransaction.getUser());
        assertEquals(-10, savedTransaction.getAmount());
        assertEquals(
                CreditTransactionType.RESERVATION,
                savedTransaction.getType()
        );
    }

    @Test
    void shouldNotCreateReservationWhenUserHasInsufficientCredits() {
        String email = "user@test.com";
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        User user = new User(
                "testuser",
                email,
                "encoded-password"
        );
        user.setCredits(5);

        Facility facility = org.mockito.Mockito.mock(Facility.class);
        Court court = org.mockito.Mockito.mock(Court.class);

        BookingRequest request = new BookingRequest(
                1L,
                reservationDate,
                startTime
        );

        BookingSlot bookingSlot = new BookingSlot(
                startTime,
                endTime
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(courtRepository.findById(1L))
                .thenReturn(Optional.of(court));

        when(court.isActive())
                .thenReturn(true);

        when(court.getId())
                .thenReturn(1L);

        when(court.getCreditCost())
                .thenReturn(10);

        when(court.getFacility())
                .thenReturn(facility);

        when(facility.getOpeningTime())
                .thenReturn(LocalTime.of(8, 0));

        when(facility.getClosingTime())
                .thenReturn(LocalTime.of(22, 0));

        when(bookingSlotService.generateBookingSlots(
                LocalTime.of(8, 0),
                LocalTime.of(22, 0)
        )).thenReturn(List.of(bookingSlot));

        when(reservationRepository
                .existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
                        any(),
                        any(),
                        any(),
                        any()
                ))
                .thenReturn(false);

        InsufficientCreditsException exception = assertThrows(
                InsufficientCreditsException.class,
                () -> reservationService.createBooking(email, request)
        );

        assertEquals(
                "Insufficient credits. Available: 5, required: 10",
                exception.getMessage()
        );

        assertEquals(5, user.getCredits());

        verify(reservationRepository, never())
                .save(any(Reservation.class));

        verify(creditTransactionRepository, never())
                .save(any(CreditTransaction.class));
    }
}
