package calendar.event.booking;

import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {
    private final LocalTime start;
    private final LocalTime end;

    TimeSlot(LocalTime start, LocalTime end) throws InvalidTimeSlotException {
        if (start == null) {
            throw new InvalidTimeSlotException("start time is null");
        }
        if (end == null) {
            throw new InvalidTimeSlotException("end time is null");
        }
        if (start == end) {
            throw new InvalidTimeSlotException("start time is equal to end time");
        }
        if (start.isAfter(end)) {
            throw new InvalidTimeSlotException("start time is greater than end time");
        }

        this.start = start;
        this.end = end;
    }

    TimeSlot(TimeSlot timeSlot) {
        this.start = timeSlot.start;
        this.end = timeSlot.end;
    }

    public TimeSlot common(TimeSlot anotherTimeSlot) {
        if (this.isSubset(anotherTimeSlot)) {
            return new TimeSlot(this);
        }

        return null;
    }

    public boolean isSubset(TimeSlot anotherTimeSlot) {
        return (this.start.isAfter(anotherTimeSlot.start) ||
                this.start.equals(anotherTimeSlot.start)) &&
                (this.end.isBefore(anotherTimeSlot.end) ||
                        this.end.equals(anotherTimeSlot.end));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return start.equals(timeSlot.start) &&
                end.equals(timeSlot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public boolean finishesBeforeStartingOf(TimeSlot anotherTimeSlot) {
        return this.end.isBefore(anotherTimeSlot.start) || this.end.equals(anotherTimeSlot.start);
    }
}
