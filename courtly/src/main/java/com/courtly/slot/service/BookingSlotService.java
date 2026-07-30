package com.courtly.slot.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.courtly.slot.exception.InvalidBookingSlotException;

import com.courtly.slot.dto.BookingSlot;

@Service
public class BookingSlotService {

    private static final int SLOT_DURATION_MINUTES = 60;

    public List<BookingSlot> generateBookingSlots(LocalTime openingTime, LocalTime closingTime) {
        validateFacilitySchedule(openingTime, closingTime);

        List<BookingSlot> slots = new ArrayList<>();

        LocalTime currentStartTime = openingTime;
        LocalTime currentEndTime = openingTime.plusMinutes(SLOT_DURATION_MINUTES);

        while (!currentEndTime.isAfter(closingTime)) {
            slots.add(new BookingSlot(currentStartTime, currentEndTime));

            currentStartTime = currentEndTime;
            currentEndTime = currentStartTime.plusMinutes(SLOT_DURATION_MINUTES);
        }

        return slots;
    }


    private void validateFacilitySchedule(LocalTime openingTime, LocalTime closingTime){
        if (openingTime == null || closingTime == null) {
            throw new InvalidBookingSlotException(
                "Facility opening time and closing time are required."
            );
        }

        if (!openingTime.isBefore(closingTime)) {
            throw new InvalidBookingSlotException(
                "Facility opening time must be before closing time."
            );
        }

        long availableMinutes = Duration.between(openingTime, closingTime).toMinutes();

        if(availableMinutes < SLOT_DURATION_MINUTES) {
            throw new InvalidBookingSlotException(
                "Facility opening time and closing time must be at least 1 hour apart."
            );
        }
    }
}
