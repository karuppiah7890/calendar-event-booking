package calendar.event.booking;

import java.util.ArrayList;

// TimeSlots represents a list of time slots or a collection
// of time slots
public class TimeSlots {
    private final ArrayList<TimeSlot> timeSlots;

    public TimeSlots(ArrayList<TimeSlot> timeSlots) throws InvalidTimeSlotsException {
        if (timeSlots == null) {
            throw new InvalidTimeSlotsException("time slots is null");
        }
        this.timeSlots = timeSlots;
    }
}
