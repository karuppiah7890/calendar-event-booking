package calendar.event.booking;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlots timeSlots1 = (TimeSlots) o;
        return timeSlots.equals(timeSlots1.timeSlots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeSlots);
    }

    public TimeSlots gaps(TimeSlot boundary) throws InvalidTimeSlotException, InvalidTimeSlotsException {
        List<TimeSlot> slotGaps = new ArrayList<>();
        LocalTime gapStart = boundary.getStart();

        for (TimeSlot timeSlot : timeSlots) {
            LocalTime gapEnd = timeSlot.getStart();

            if (!gapStart.equals(gapEnd)) {
                slotGaps.add(new TimeSlot(gapStart, gapEnd));
            }

            gapStart = timeSlot.getEnd();
        }

        if (slotGaps.isEmpty()) {
            return null;
        }

        return new TimeSlots(slotGaps);
    }
}
