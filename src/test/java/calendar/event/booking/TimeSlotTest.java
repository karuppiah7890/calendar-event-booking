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
            TimeSlot anotherCommon = anotherTimeSlot.common(timeSlot);

            assertThat(common, is(nullValue()));
            assertThat(anotherCommon, is(nullValue()));
        }

        @Test
        void returnsSmallerTimeSlotWhenOneTimeSlotIsSubsetOfAnotherTimeSlot() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("12:00"));
            TimeSlot expectedCommon = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            TimeSlot common = timeSlot.common(anotherTimeSlot);
            TimeSlot anotherCommon = anotherTimeSlot.common(timeSlot);

            assertThat(common, is(expectedCommon));
            assertThat(anotherCommon, is(expectedCommon));
        }

        @Test
        void returnsCommonTimeSlotWhenTimeSlotsCoincide() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:30"));
            TimeSlot expectedCommon = new TimeSlot(LocalTime.parse("10:30"), LocalTime.parse("11:00"));

            TimeSlot common = timeSlot.common(anotherTimeSlot);
            TimeSlot anotherCommon = anotherTimeSlot.common(timeSlot);

            assertThat(common, is(expectedCommon));
            assertThat(anotherCommon, is(expectedCommon));
        }

        @Test
        void returnsNullWhenPassedSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            TimeSlot common = timeSlot.common(null);

            assertThat(common, is(nullValue()));
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

        @Test
        void returnsFalseWhenPassedTimeSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            boolean isSubset = timeSlot.isSubset(null);

            assertThat(isSubset, is(false));
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

        @Test
        void returnsFalseWhenPassedTimeSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));

            boolean isSubset = timeSlot.finishesBeforeStartingOf(null);

            assertThat(isSubset, is(false));
        }
    }

    @Nested
    class Gap {
        @Test
        void returnsGapWhenThereIsOneGap() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:30"), LocalTime.parse("12:00"));
            TimeSlot expectedGap = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:30"));

            TimeSlot gap = timeSlot.gap(anotherTimeSlot);
            TimeSlot anotherGap = anotherTimeSlot.gap(timeSlot);

            assertThat(gap, is(expectedGap));
            assertThat(anotherGap, is(expectedGap));
        }

        @Test
        void returnsNullWhenThereIsNoGap() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));

            TimeSlot gap = timeSlot.gap(anotherTimeSlot);
            TimeSlot anotherGap = anotherTimeSlot.gap(timeSlot);

            assertThat(gap, is(nullValue()));
            assertThat(anotherGap, is(nullValue()));
        }

        @Test
        void returnsNullWhenTheTimeSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            TimeSlot gap = timeSlot.gap(null);

            assertThat(gap, is(nullValue()));
        }
    }

    @Nested
    class StartTimeGap {
        @Test
        void returnsGapWhenThereIsOne() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlot expectedStartTimeGap = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00"));

            TimeSlot startTimeGap = timeSlot.startTimeGap(anotherTimeSlot);
            TimeSlot anotherStartTimeGap = anotherTimeSlot.startTimeGap(timeSlot);

            assertThat(startTimeGap, is(expectedStartTimeGap));
            assertThat(anotherStartTimeGap, is(expectedStartTimeGap));
        }

        @Test
        void returnsNullWhenThereIsNoGap() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("12:00"));

            TimeSlot startTimeGap = timeSlot.startTimeGap(anotherTimeSlot);
            TimeSlot anotherStartTimeGap = anotherTimeSlot.startTimeGap(timeSlot);

            assertThat(startTimeGap, is(nullValue()));
            assertThat(anotherStartTimeGap, is(nullValue()));
        }

        @Test
        void returnsNullWhenTheTimeSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlot startTimeGap = timeSlot.startTimeGap(null);

            assertThat(startTimeGap, is(nullValue()));
        }
    }

    @Nested
    class EndTimeGap {
        @Test
        void returnsGapWhenThereIsOne() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("12:00"));
            TimeSlot expectedEndTimeGap = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00"));

            TimeSlot endTimeGap = timeSlot.endTimeGap(anotherTimeSlot);
            TimeSlot anotherEndTimeGap = anotherTimeSlot.endTimeGap(timeSlot);

            assertThat(endTimeGap, is(expectedEndTimeGap));
            assertThat(anotherEndTimeGap, is(expectedEndTimeGap));
        }

        @Test
        void returnsNullWhenThereIsNoGap() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));
            TimeSlot anotherTimeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("13:00"));

            TimeSlot endTimeGap = timeSlot.endTimeGap(anotherTimeSlot);
            TimeSlot anotherEndTimeGap = anotherTimeSlot.endTimeGap(timeSlot);

            assertThat(endTimeGap, is(nullValue()));
            assertThat(anotherEndTimeGap, is(nullValue()));
        }

        @Test
        void returnsNullWhenTheTimeSlotIsNull() throws InvalidTimeSlotException {
            TimeSlot timeSlot = new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("13:00"));

            TimeSlot startTimeGap = timeSlot.endTimeGap(null);

            assertThat(startTimeGap, is(nullValue()));
        }
    }
}
