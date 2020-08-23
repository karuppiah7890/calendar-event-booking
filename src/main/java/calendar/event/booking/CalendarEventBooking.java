package calendar.event.booking;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventBooking {
    List<TimeSlots> busySlotsOfPeople;
    Duration meetingDuration;
    TimeSlot workingHours;

    public CalendarEventBooking(List<TimeSlots> busySlotsOfPeople, Duration meetingDuration, TimeSlot workingHours) {
        this.busySlotsOfPeople = busySlotsOfPeople;
        this.meetingDuration = meetingDuration;
        this.workingHours = workingHours;
    }

    public TimeSlots freeTimeSlotsForMeeting() throws InvalidTimeSlotException, InvalidTimeSlotsException {
        List<TimeSlots> freeSlotsOfPeople = new ArrayList<>();
        for (TimeSlots busySlotsOfPerson : busySlotsOfPeople) {
            TimeSlots freeSlotsOfPerson = busySlotsOfPerson.gaps(workingHours);
            freeSlotsOfPeople.add(freeSlotsOfPerson);
        }

        TimeSlots commonFreeSlots = new TimeSlots(List.of(workingHours));

        for (TimeSlots freeSlotsOfPerson : freeSlotsOfPeople) {
            commonFreeSlots = freeSlotsOfPerson.common(commonFreeSlots);
        }

        return commonFreeSlots;
    }
}
