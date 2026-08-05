package com.courtly.reservation.respository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import javax.swing.text.html.parser.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.court.entity.Court;
import com.courtly.court.repository.CourtRepository;
import com.courtly.facility.entity.Facility;
import com.courtly.facility.repository.FacilityRepository;
import com.courtly.reservation.entity.Reservation;
import com.courtly.reservation.entity.ReservationStatus;
import com.courtly.reservation.repository.ReservationRepository;
import com.courtly.user.entity.User;
import com.courtly.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationRepositoryTest {
    
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindReservation(){
        User user = new User( "reservation-user", "reservation-user@courtly.com", "encoded-password-placeholder");
        User savedUser = userRepository.saveAndFlush(user);

        Facility facility = new Facility( "Courtly Sports Center", "Gran Via 1, Madrid", "Sports facility used for reservation tests",
            LocalTime.of(9, 0), LocalTime.of(21, 0));

        Facility savedFacility = facilityRepository.saveAndFlush(facility);

        Court court = new Court("Padel Court 1", "Indoor padel court", 10, true, 
            savedFacility);

        Court savedCourt = courtRepository.saveAndFlush(court);

        Reservation reservation = new Reservation(savedUser, savedCourt, LocalDate.of(2026, 8, 10), LocalTime.of(10, 0), LocalTime.of(11, 0));

        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);

        Long reservationId = savedReservation.getId();

        entityManager.clear();
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);

        assertThat(reservationId).isNotNull();
        assertThat(foundReservation).isPresent();

        Reservation retrievedReservation = foundReservation.orElseThrow();

        assertThat(retrievedReservation.getId()).isEqualTo(reservationId);
        assertThat(retrievedReservation.getUser().getId())
                .isEqualTo(savedUser.getId());
        assertThat(retrievedReservation.getCourt().getId())
                .isEqualTo(savedCourt.getId());
        assertThat(retrievedReservation.getReservationDate())
                .isEqualTo(LocalDate.of(2026, 8, 10));
        assertThat(retrievedReservation.getStartTime())
                .isEqualTo(LocalTime.of(10, 0));
        assertThat(retrievedReservation.getEndTime())
                .isEqualTo(LocalTime.of(11, 0));
        assertThat(retrievedReservation.getStatus())
                .isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(retrievedReservation.getCreatedAt()).isNotNull();
    }
}
