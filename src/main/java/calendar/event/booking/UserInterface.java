package calendar.event.booking;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UserInterface {
    public static void main(String[] args) throws InvalidTimeSlotException, InvalidTimeSlotsException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!\n");
        System.out.print("Meeting duration (in minutes): ");
        long minutes = scanner.nextLong();
        Duration meetingDuration = Duration.ofMinutes(minutes);

        System.out.print("Working Hours\n");
        System.out.print("Start: ");
        LocalTime workingHoursStart = LocalTime.parse(scanner.next());
        System.out.print("End: ");
        LocalTime workingHoursEnd = LocalTime.parse(scanner.next());
        TimeSlot workingHours = new TimeSlot(workingHoursStart, workingHoursEnd);


        System.out.print("How many people are going to attend this meeting? ");
        int numberOfPeople = scanner.nextInt();

        List<TimeSlots> busyTimeSlotsOfPeople = new ArrayList<>();
        System.out.println("Provide the busy time slots of the people in 24hr time format\n");
        for (int i = 0; i < numberOfPeople; i++) {
            System.out.print("Person Name: ");
            scanner.next();
            int busyTimeSlotCount = 1;
            List<TimeSlot> slots = new ArrayList<>();
            do {
                System.out.print("Slot " + busyTimeSlotCount + " start: ");
                LocalTime start = LocalTime.parse(scanner.next());

                System.out.print("Slot " + busyTimeSlotCount + " end: ");
                LocalTime end = LocalTime.parse(scanner.next());

                slots.add(new TimeSlot(start, end));
                busyTimeSlotCount++;

                System.out.print("More? [y/n] ");
            } while (Objects.equals(scanner.next(), "y"));

            busyTimeSlotsOfPeople.add(new TimeSlots(slots));
        }

        CalendarEventBooking calendarEventBooking = new CalendarEventBooking(busyTimeSlotsOfPeople, meetingDuration,
                workingHours);

        TimeSlots freeTimeSlotsForMeeting = calendarEventBooking.freeTimeSlotsForMeeting();

        if (freeTimeSlotsForMeeting == null) {
            System.out.println("There are no free time slots available for the meeting!");
            System.exit(0);
        }

        System.out.println("Free time slots available for meeting are: ");
        System.out.println(freeTimeSlotsForMeeting.toString());
    }
}
