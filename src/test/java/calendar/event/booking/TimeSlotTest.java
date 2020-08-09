package calendar.event.booking;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeSlotTest {
    @Nested
    class Constructor {
        @Test
        void successfulWhenStartTimeIsLessThanEndTime() {
            assertDoesNotThrow(() -> new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        }

        @Test
        void unsuccessfulWhenStartTimeIsEqualToEndTime() {
            assertThrows(InvalidTimeSlotException.class, () -> new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:00")));
        }

        @Test
        void unsuccessfulWhenStartTimeIsMoreThanEndTime() {
            assertThrows(InvalidTimeSlotException.class, () -> new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("10:00")));
        }

        @Test
        void unsuccessfulWhenStartTimeIsNull() {
            assertThrows(InvalidTimeSlotException.class, () -> new TimeSlot(null, LocalTime.parse("10:00")));
        }

        @Test
        void unsuccessfulWhenEndTimeIsNull() {
            assertThrows(InvalidTimeSlotException.class, () -> new TimeSlot(LocalTime.parse("10:00"), null));
        }
    }

    @Nested
    class CommonTimeSlot {
        @Test
        void returnsNullWhenSlotsDontCoincideAtAll() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));

            TimeSlot common = timeSlot.common(anotherTimeSlot);

            assertThat(common, is(nullValue()));
        }

        @Test
        void returnsSmallerTimeSlotWhenOneTimeSlotIsSubsetOfAnotherTimeSlot() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("12:00"));
            TimeSlot expectedCommon = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            TimeSlot common = timeSlot.common(anotherTimeSlot);

            assertThat(common, is(expectedCommon));
        }
    }

    @Nested
    class SubsetTimeSlot {
        @Test
        void returnsFalseWhenSlotsDontCoincideAtAll() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));

            boolean isSubset = timeSlot.isSubset(anotherTimeSlot);

            assertThat(isSubset, is(false));
        }

        @Test
        void returnsTrueWhenTimeSlotIsASubset() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("12:00"));

            boolean isSubset = timeSlot.isSubset(anotherTimeSlot);

            assertThat(isSubset, is(true));
        }

        @Test
        void returnsTrueWhenTimeSlotsAreEqual() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            boolean isSubset = timeSlot.isSubset(anotherTimeSlot);

            assertThat(isSubset, is(true));
        }
    }

    @Nested
    class FinishesBeforeStartingOfTimeSlot {
        @Test
        void returnsTrueWhenTimeSlotFinishesBeforeStartingOfAnotherTimeSlot() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));

            boolean isSubset = timeSlot.finishesBeforeStartingOf(anotherTimeSlot);

            assertThat(isSubset, is(true));
        }

        @Test
        void returnsFalseWhenTimeSlotDoesNotFinishBeforeStartingOfAnotherTimeSlot() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            boolean isSubset = timeSlot.finishesBeforeStartingOf(anotherTimeSlot);

            assertThat(isSubset, is(false));
        }
    }
}
