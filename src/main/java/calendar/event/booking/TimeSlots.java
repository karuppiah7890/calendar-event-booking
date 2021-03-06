package calendar.event.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TimeSlots represents a list of time slots or a collection
// of time slots
public class TimeSlots {
    private final List<TimeSlot> slots;

    public TimeSlots(List<TimeSlot> slots) throws InvalidTimeSlotsException {
        if (slots == null) {
            throw new InvalidTimeSlotsException("time slots is null");
        }
        this.slots = Collections.unmodifiableList(slots);
    }

    public TimeSlots filter(Predicate<? super TimeSlot> predicate) throws InvalidTimeSlotsException {
        List<TimeSlot> filteredSlots = slots.stream().filter(predicate).collect(Collectors.toUnmodifiableList());

        if (filteredSlots.isEmpty()) {
            return null;
        }

        return new TimeSlots(filteredSlots);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlots timeSlots1 = (TimeSlots) o;
        return slots.equals(timeSlots1.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slots);
    }

    public TimeSlots gaps(TimeSlot boundary) throws InvalidTimeSlotException, InvalidTimeSlotsException {
        if (boundary == null) {
            return null;
        }

        if (slots.isEmpty()) {
            return new TimeSlots(List.of(boundary));
        }

        List<TimeSlot> slotsWithinBoundary = new ArrayList<>();
        for (TimeSlot slot : slots) {
            TimeSlot common = slot.common(boundary);
            if (common != null) {
                slotsWithinBoundary.add(common);
            }
        }

        if (slotsWithinBoundary.isEmpty()) {
            return new TimeSlots(List.of(boundary));
        }

        List<TimeSlot> slotGaps = new ArrayList<>();
        List<TimeSlot> sortedSlots = new ArrayList<>(slotsWithinBoundary);
        sortedSlots.sort(TimeSlot::compareStartTimeTo);

        TimeSlot firstSlot = sortedSlots.get(0);
        TimeSlot firstGap = boundary.startTimeGap(firstSlot);
        if (firstGap != null) {
            slotGaps.add(firstGap);
        }

        TimeSlot previousSlot = firstSlot;

        for (int i = 1; i < sortedSlots.size(); i++) {
            TimeSlot currentSlot = sortedSlots.get(i);

            TimeSlot gap = previousSlot.gap(currentSlot);
            if (gap != null) {
                slotGaps.add(gap);
            }

            previousSlot = currentSlot;
        }

        TimeSlot lastSlot = sortedSlots.get(sortedSlots.size() - 1);
        TimeSlot lastGap = lastSlot.endTimeGap(boundary);
        if (lastGap != null) {
            slotGaps.add(lastGap);
        }

        if (slotGaps.isEmpty()) {
            return null;
        }

        return new TimeSlots(slotGaps);
    }

    public TimeSlots common(TimeSlots anotherTimeSlots) throws InvalidTimeSlotException, InvalidTimeSlotsException {
        if (anotherTimeSlots == null) {
            return null;
        }
        List<TimeSlot> commonSlots = new ArrayList<>();
        for (TimeSlot slot : this.slots) {
            for (TimeSlot timeSlot : anotherTimeSlots.slots) {
                TimeSlot common = slot.common(timeSlot);
                if (common != null) {
                    commonSlots.add(common);
                }
            }
        }

        if (commonSlots.isEmpty()) {
            return null;
        }

        return new TimeSlots(commonSlots);
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        for (TimeSlot slot : slots) {
            data.append("* ");
            data.append(slot.toString());
            data.append("\n");
        }
        return data.toString();
    }
}
