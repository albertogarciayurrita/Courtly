package com.courtly.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.courtly.court.entity.Court;
import com.courtly.court.repository.CourtRepository;
import com.courtly.credit.entity.CreditTransaction;
import com.courtly.credit.repository.CreditTransactionRepository;
import com.courtly.facility.entity.Facility;
import com.courtly.facility.repository.FacilityRepository;
import com.courtly.reservation.dto.BookingRequest;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.user.entity.User;
import com.courtly.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationTransactionIntegrationTest {
     @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private CreditTransactionRepository creditTransactionRepository;


        private Long testUserId;
        private Long testCourtId;
        private Long testFacilityId;

        @AfterEach
                void cleanUp() {
                if (testCourtId != null) {
                        courtRepository.deleteById(testCourtId);
                }

                if (testFacilityId != null) {
                        facilityRepository.deleteById(testFacilityId);
                }

                if (testUserId != null) {
                        userRepository.deleteById(testUserId);
                }
}
    @Test
    void shouldRollbackReservationAndCreditDeductionWhenCreditTransactionFails() {
        String email = "rollback-user@courtly.com";

        User user = new User(
                "rollback-user",
                email,
                "encoded-password"
        );
        user.setCredits(60);

        User savedUser = userRepository.saveAndFlush(user);
        testUserId = savedUser.getId();

        Facility facility = new Facility(
                "Rollback Sports Center",
                "Test address",
                "Facility for transaction rollback test",
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        );

        Facility savedFacility = facilityRepository.saveAndFlush(facility);
        testFacilityId = savedFacility.getId();

        Court court = new Court(
                "Rollback Court",
                "Court for transaction rollback test",
                10,
                true,
                savedFacility
        );

        Court savedCourt = courtRepository.saveAndFlush(court);
        testCourtId = savedCourt.getId();

        LocalDate reservationDate = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);

        BookingRequest request = new BookingRequest(
                savedCourt.getId(),
                reservationDate,
                startTime
        );

        when(creditTransactionRepository.save(
                any(CreditTransaction.class)
        )).thenThrow(
                new RuntimeException(
                        "Simulated credit transaction failure"
                )
        );

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservationService.createBooking(email, request)
        );

        assertThat(exception.getMessage())
                .isEqualTo(
                        "Simulated credit transaction failure"
                );

        entityManager.clear();

        User userAfterFailure = userRepository
                .findByEmail(email)
                .orElseThrow();

        boolean reservationExists =
                reservationRepository
                        .existsByCourt_IdAndReservationDateAndStartTimeAndStatus(
                                savedCourt.getId(),
                                reservationDate,
                                startTime,
                                com.courtly.reservation.entity.ReservationStatus.CONFIRMED
                        );

        assertThat(userAfterFailure.getCredits())
                .isEqualTo(60);

        assertThat(reservationExists)
                .isFalse();
    }
}
