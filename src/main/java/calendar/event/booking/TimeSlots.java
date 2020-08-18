package calendar.event.booking;

import java.util.Collections;
import java.util.List;

// TimeSlots represents a list of time slots or a collection
// of time slots
public class TimeSlots {
    private final List<TimeSlot> timeSlots;

    public TimeSlots(List<TimeSlot> timeSlots) throws InvalidTimeSlotsException {
        if (timeSlots == null) {
            throw new InvalidTimeSlotsException("time slots is null");
        }
        this.timeSlots = Collections.unmodifiableList(timeSlots);
    }

    public TimeSlots gaps(TimeSlot boundary) {
        return null;
    }
}
