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

        @Test
        void returnsGapsWhenTheInputTimeSlotsAreOutsideBoundaryToo() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot firstSlot = new TimeSlot(LocalTime.parse("10:20"), LocalTime.parse("10:30"));
            TimeSlot secondSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("12:30"));
            TimeSlot thirdSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:45"));
            TimeSlot fourthSlot = new TimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:20"));
            TimeSlot fifthSlot = new TimeSlot(LocalTime.parse("08:00"), LocalTime.parse("08:30"));
            TimeSlot sixthSlot = new TimeSlot(LocalTime.parse("14:00"), LocalTime.parse("15:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(firstSlot, secondSlot, thirdSlot,
                    fourthSlot, fifthSlot, sixthSlot));
            TimeSlot firstGap = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));
            TimeSlot secondGap = new TimeSlot(LocalTime.parse("11:45"), LocalTime.parse("12:00"));
            TimeSlot thirdGap = new TimeSlot(LocalTime.parse("12:30"), LocalTime.parse("13:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(firstGap, secondGap, thirdGap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }

        @Test
        void returnsGapsWhenTheInputTimeSlotsAreOverlapping() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot firstSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("10:30"));
            TimeSlot secondSlot = new TimeSlot(LocalTime.parse("10:15"), LocalTime.parse("10:45"));
            TimeSlot thirdSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:45"));
            TimeSlot fourthSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("12:20"));
            TimeSlot fifthSlot = new TimeSlot(LocalTime.parse("12:10"), LocalTime.parse("12:30"));
            TimeSlots timeSlots = new TimeSlots(List.of(firstSlot, secondSlot, thirdSlot,
                    fourthSlot, fifthSlot));
            TimeSlot firstGap = new TimeSlot(LocalTime.parse("10:45"), LocalTime.parse("11:00"));
            TimeSlot secondGap = new TimeSlot(LocalTime.parse("11:45"), LocalTime.parse("12:00"));
            TimeSlot thirdGap = new TimeSlot(LocalTime.parse("12:30"), LocalTime.parse("13:00"));
            TimeSlots expectedGaps = new TimeSlots(List.of(firstGap, secondGap, thirdGap));
            TimeSlot boundary = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlots gaps = timeSlots.gaps(boundary);

            assertThat(gaps, is(expectedGaps));
        }
    }

    @Nested
    class Common {
        @Test
        void returnsCommonTimeSlots() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot firstTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot secondTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(firstTimeSlot, secondTimeSlot));
            TimeSlot thirdTimeSlot = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:30"));
            TimeSlot fourthTimeSlot = new TimeSlot(LocalTime.parse("12:30"), LocalTime.parse("13:30"));
            TimeSlots anotherTimeSlots = new TimeSlots(List.of(thirdTimeSlot, fourthTimeSlot));
            TimeSlot firstExpectedSlot = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));
            TimeSlot secondExpectedSlot = new TimeSlot(LocalTime.parse("12:30"), LocalTime.parse("13:00"));
            TimeSlots expectedCommon = new TimeSlots(List.of(firstExpectedSlot, secondExpectedSlot));

            TimeSlots common = timeSlots.common(anotherTimeSlots);
            TimeSlots anotherCommon = anotherTimeSlots.common(timeSlots);

            assertThat(common, is(expectedCommon));
            assertThat(anotherCommon, is(expectedCommon));
        }

        @Test
        void returnsNullTimeSlotWhenThereIsNone() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot firstTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot secondTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(firstTimeSlot, secondTimeSlot));
            TimeSlot thirdTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:30"));
            TimeSlot fourthTimeSlot = new TimeSlot(LocalTime.parse("11:30"), LocalTime.parse("12:00"));
            TimeSlots anotherTimeSlots = new TimeSlots(List.of(thirdTimeSlot, fourthTimeSlot));

            TimeSlots common = timeSlots.common(anotherTimeSlots);
            TimeSlots anotherCommon = anotherTimeSlots.common(timeSlots);

            assertThat(common, is(nullValue()));
            assertThat(anotherCommon, is(nullValue()));
        }

        @Test
        void returnsNullTimeSlotIsNull() throws InvalidTimeSlotException, InvalidTimeSlotsException {
            TimeSlot firstTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot secondTimeSlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));
            TimeSlots timeSlots = new TimeSlots(List.of(firstTimeSlot, secondTimeSlot));

            TimeSlots common = timeSlots.common(null);

            assertThat(common, is(nullValue()));
        }
    }
}