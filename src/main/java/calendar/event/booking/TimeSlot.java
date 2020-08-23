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

    private boolean doesNotHaveCommon(TimeSlot anotherTimeSlot) {
        return this.finishesBeforeStartingOf(anotherTimeSlot) || anotherTimeSlot.finishesBeforeStartingOf(this);
    }

    public TimeSlot common(TimeSlot anotherTimeSlot) throws InvalidTimeSlotException {
        if (this.doesNotHaveCommon(anotherTimeSlot)) {
            return null;
        }

        if (this.isSubset(anotherTimeSlot)) {
            return new TimeSlot(this);
        }

        if (anotherTimeSlot.isSubset(this)) {
            return new TimeSlot(anotherTimeSlot);
        }

        if (this.start.isBefore(anotherTimeSlot.start)) {
            return new TimeSlot(anotherTimeSlot.start, this.end);
        }

        return new TimeSlot(this.start, anotherTimeSlot.end);
    }

    public TimeSlot merge(TimeSlot anotherTimeSlot) throws InvalidTimeSlotException {
        if (hasGap(anotherTimeSlot)) {
            return null;
        }

        if (this.isSubset(anotherTimeSlot)) {
            return new TimeSlot(anotherTimeSlot);
        }

        if (anotherTimeSlot.isSubset(this)) {
            return new TimeSlot(this);
        }

        if (this.start.isBefore(anotherTimeSlot.start)) {
            return new TimeSlot(this.start, anotherTimeSlot.end);
        }

        return new TimeSlot(anotherTimeSlot.start, this.end);
    }

    private boolean hasGap(TimeSlot anotherTimeSlot) {
        return this.end.isBefore(anotherTimeSlot.start) || anotherTimeSlot.end.isBefore(this.start);
    }

    private boolean doesNotHaveGap(TimeSlot anotherTimeSlot) {
        return !this.hasGap(anotherTimeSlot);
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

    public TimeSlot startTimeGap(TimeSlot anotherTimeSlot) throws InvalidTimeSlotException {
        if (this.start.equals(anotherTimeSlot.start)) {
            return null;
        }
        if (this.start.isBefore(anotherTimeSlot.start)) {
            return new TimeSlot(this.start, anotherTimeSlot.start);
        }
        return new TimeSlot(anotherTimeSlot.start, this.start);
    }

    public TimeSlot gap(TimeSlot anotherTimeSlot) throws InvalidTimeSlotException {
        if (this.doesNotHaveGap(anotherTimeSlot)) {
            return null;
        }
        if (this.end.isBefore(anotherTimeSlot.start)) {
            return new TimeSlot(this.end, anotherTimeSlot.start);
        }
        return new TimeSlot(anotherTimeSlot.end, this.start);
    }

    public TimeSlot endTimeGap(TimeSlot anotherTimeSlot) throws InvalidTimeSlotException {
        if (this.end.equals(anotherTimeSlot.end)) {
            return null;
        }
        if (this.end.isBefore(anotherTimeSlot.end)) {
            return new TimeSlot(this.end, anotherTimeSlot.end);
        }
        return new TimeSlot(anotherTimeSlot.end, this.end);
    }

    public int compareStartTimeTo(TimeSlot anotherTimeSlot) {
        return this.start.compareTo(anotherTimeSlot.start);
    }
}
