package com.courtly.slot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.courtly.slot.dto.BookingSlot;
import com.courtly.slot.exception.InvalidBookingSlotException;

public class BookingSlotServiceTest {
    
    private BookingSlotService bookingSlotService;

    @BeforeEach
    void setUp() {
        bookingSlotService = new BookingSlotService();
    }

    @Test
    void shouldGenerateFixedBookingSlots() {
    LocalTime openingTime = LocalTime.of(9, 0);
    LocalTime closingTime = LocalTime.of(13, 0);

    List<BookingSlot> slots = bookingSlotService.generateBookingSlots(openingTime, closingTime);
    
    assertEquals(4, slots.size());

    assertEquals(new BookingSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)), slots.get(0));
    assertEquals(new BookingSlot(LocalTime.of(12, 0), LocalTime.of(13, 0)), slots.get(3));
    } 


    @Test
    void shouldNotGenerateSlotOutsideFacilityHours() {
        LocalTime openingTime = LocalTime.of(9, 0);
        LocalTime closingTime = LocalTime.of(12, 30);

        List<BookingSlot> slots = bookingSlotService.generateBookingSlots(openingTime, closingTime);

        assertEquals(3, slots.size());
        assertEquals(new BookingSlot(LocalTime.of(11, 0), LocalTime.of(12, 0)), slots.get(2));
    }

    @Test
    void shouldAllowSlotsStartingAtNonExactHour() {
        LocalTime openingTime = LocalTime.of(9, 30);
        LocalTime closingTime = LocalTime.of(12, 30);

        List<BookingSlot> slots = bookingSlotService.generateBookingSlots(openingTime, closingTime);

        assertEquals(3, slots.size());
        assertEquals(new BookingSlot(LocalTime.of(9, 30), LocalTime.of(10, 30)), slots.get(0));
    }

    @Test
    void shouldRejectScheduleShorterThanOneSlot() {
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(10, 45);

        assertThrows(InvalidBookingSlotException.class, () -> bookingSlotService.generateBookingSlots(openingTime, closingTime));
    }

    @Test
    void shouldRejectOpeningTimeAfterClosingTime() {
        LocalTime openingTime = LocalTime.of(18, 0);
        LocalTime closingTime = LocalTime.of(11, 0);

        assertThrows(InvalidBookingSlotException.class, () -> bookingSlotService.generateBookingSlots(openingTime, closingTime));
    }

    @Test
    void shouldRejectNullOpeningOrClosingTime() {
        assertThrows(InvalidBookingSlotException.class, () -> bookingSlotService.generateBookingSlots(null, LocalTime.of(12, 0)));
        assertThrows(InvalidBookingSlotException.class, () -> bookingSlotService.generateBookingSlots(LocalTime.of(9, 0), null));
    }

    @Test
    void shouldGenerateOneSlotWhenScheduleHasExactlyOneHour() {
        List<BookingSlot> slots = bookingSlotService.generateBookingSlots(LocalTime.of(10, 0), LocalTime.of(11, 0));

        assertEquals(1, slots.size());
        assertEquals(new BookingSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)), slots.get(0)); 
    }
}
