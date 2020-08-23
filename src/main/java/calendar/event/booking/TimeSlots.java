package calendar.event.booking;

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
        if (timeSlots.isEmpty()) {
            return new TimeSlots(List.of(boundary));
        }
        List<TimeSlot> slotGaps = new ArrayList<>();
        List<TimeSlot> sortedTimeSlots = new ArrayList<>(timeSlots);
        sortedTimeSlots.sort(TimeSlot::compareStartTimeTo);

        TimeSlot firstSlot = sortedTimeSlots.get(0);
        TimeSlot firstGap = boundary.startTimeGap(firstSlot);
        if (firstGap != null) {
            slotGaps.add(firstGap);
        }

        TimeSlot previousSlot = firstSlot;

        for (int i = 1; i < sortedTimeSlots.size(); i++) {
            TimeSlot currentSlot = sortedTimeSlots.get(i);

            TimeSlot gap = previousSlot.gap(currentSlot);
            if (gap != null) {
                slotGaps.add(gap);
            }

            previousSlot = currentSlot;
        }

        TimeSlot lastSlot = sortedTimeSlots.get(sortedTimeSlots.size() - 1);
        TimeSlot lastGap = lastSlot.endTimeGap(boundary);
        if (lastGap != null) {
            slotGaps.add(lastGap);
        }

        if (slotGaps.isEmpty()) {
            return null;
        }

        return new TimeSlots(slotGaps);
    }
}
