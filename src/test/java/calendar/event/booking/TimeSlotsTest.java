package calendar.event.booking;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

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
}