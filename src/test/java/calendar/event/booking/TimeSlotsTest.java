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

            assertDoesNotThrow(() -> new TimeSlots(List.of(timeSlot, anotherTimeSlot)));
        }
    }

    @Nested
    class Gaps {
        @Test
        void returnsBoundaryWhenThereAreNoTimeSlots() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            ArrayList<TimeSlot> timeSlotsList = new ArrayList<>();
            TimeSlots timeSlots = new TimeSlots(timeSlotsList);
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(boundary));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }

        @Test
        void returnsNullWhenThereAreNoGaps() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(timeSlot, anotherTimeSlot));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(nullValue()));
        }

        @Test
        void returnsOneGapWhenThereIsOneGapInBetween() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(timeSlot, anotherTimeSlot));
            TimeSlot gap = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(gap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }

        @Test
        void returnsTwoGapsWhenThereAreTwoGapsWithOneAtTheEnd() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(timeSlot, anotherTimeSlot));
            TimeSlot firstGap = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlot secondGap = new TimeSlot(LocalTime.parse("13:00"), LocalTime.parse("14:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(firstGap, secondGap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("14:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }

        @Test
        void returnsGapsSortedByStartTimeWhenTheInputTimeSlotsAreUnordered() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("10:30"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("12:30"));
            TimeSlot yetAnotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:45"));
            TimeSlots timeSlots = new TimeSlots(List.of(timeSlot, anotherTimeSlot, yetAnotherTimeSlot));
            TimeSlot firstGap = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));
            TimeSlot secondGap = new TimeSlot(LocalTime.parse("11:45"), LocalTime.parse("12:00"));
            TimeSlot thirdGap = new TimeSlot(LocalTime.parse("12:30"), LocalTime.parse("13:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(firstGap, secondGap, thirdGap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }
    }
}