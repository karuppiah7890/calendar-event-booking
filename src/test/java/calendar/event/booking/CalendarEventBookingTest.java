package calendar.event.booking;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CalendarEventBookingTest {
    @Test
    void returnsOneFreeTimeSlotForMeetingWhenThereIsOne() throws InvalidTimeSlotException, InvalidTimeSlotsException {
        TimeSlot firstBusySlot = new TimeSlot(LocalTime.parse("09:00"), LocalTime.parse("11:00"));
        TimeSlot secondBusySlot = new TimeSlot(LocalTime.parse("12:00"), LocalTime.parse("14:00"));
        TimeSlot thirdBusySlot = new TimeSlot(LocalTime.parse("14:30"), LocalTime.parse("17:00"));
        TimeSlots busySlotsOfOnePerson = new TimeSlots(List.of(firstBusySlot, secondBusySlot, thirdBusySlot));
        TimeSlot fourthBusySlot = new TimeSlot(LocalTime.parse("09:00"), LocalTime.parse("11:00"));
        TimeSlot fifthBusySlot = new TimeSlot(LocalTime.parse("11:45"), LocalTime.parse("15:00"));
        TimeSlot sixthBusySlot = new TimeSlot(LocalTime.parse("15:30"), LocalTime.parse("17:00"));
        TimeSlots busySlotsOfAnotherPerson = new TimeSlots(List.of(fourthBusySlot, fifthBusySlot, sixthBusySlot));
        List<TimeSlots> busySlotsOfPeople = List.of(busySlotsOfOnePerson, busySlotsOfAnotherPerson);
        Duration meetingDuration = Duration.ofMinutes(25);
        TimeSlot workingHours = new TimeSlot(LocalTime.parse("09:00"), LocalTime.parse("17:00"));
        CalendarEventBooking calendarEventBooking = new CalendarEventBooking(busySlotsOfPeople, meetingDuration,
                workingHours);
        TimeSlot freeSlot = new TimeSlot(LocalTime.parse("11:00"), LocalTime.parse("11:45"));
        TimeSlots expectedFreeTimeSlots = new TimeSlots(List.of(freeSlot));

        TimeSlots freeTimeSlotsForMeeting = calendarEventBooking.freeTimeSlotsForMeeting();

        assertThat(freeTimeSlotsForMeeting, is(expectedFreeTimeSlots));
    }
}