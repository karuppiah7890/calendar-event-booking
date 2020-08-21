package calendar.event.booking;

import java.time.LocalTime;
import java.util.*;

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
        List<TimeSlot> sortedTimeSlots = new ArrayList<>(timeSlots);

        sortedTimeSlots.sort(Comparator.comparing(TimeSlot::getStart));

        for (TimeSlot timeSlot : sortedTimeSlots) {
            LocalTime gapEnd = timeSlot.getStart();

            if (!gapStart.equals(gapEnd)) {
                slotGaps.add(new TimeSlot(gapStart, gapEnd));
            }

            gapStart = timeSlot.getEnd();
        }

        if (!gapStart.equals(boundary.getEnd())) {
            slotGaps.add(new TimeSlot(gapStart, boundary.getEnd()));
        }

        if (slotGaps.isEmpty()) {
            return null;
        }

        return new TimeSlots(slotGaps);
    }
}
