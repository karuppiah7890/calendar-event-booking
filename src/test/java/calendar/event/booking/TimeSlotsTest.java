package calendar.event.booking;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeSlotsTest {
    @Nested
    class Constructor {
        @Test
        void unsuccessfulWhenTimeSlotsIsNull() {
            assertThrows(InvalidTimeSlotsException.class, () -> new TimeSlots(null));
        }

        @Test
        void successfulWhenTimeSlotsIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            ArrayList<TimeSlot> timeSlots = new ArrayList<>();
            timeSlots.add(timeSlot);
            timeSlots.add(anotherTimeSlot);

            assertDoesNotThrow(() -> new TimeSlots(timeSlots));
        }
    }

    @Nested
    class Gaps {
        @Test
        void returnsNullWhenThereAreNoGaps() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("13:00"));
            ArrayList<TimeSlot> timeSlotsList = new ArrayList<>();
            timeSlotsList.add(timeSlot);
            timeSlotsList.add(anotherTimeSlot);
            TimeSlots timeSlots = new TimeSlots(timeSlotsList);
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(nullValue()));
        }

        @Test
        void returnsOneGapWhenThereIsOneGap() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(timeSlot, anotherTimeSlot));
            TimeSlot gap = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(gap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }
    }
}