# Calendar Event Booking Problem

## Problem
Given the calendar data of N people, where the data tells you if a person is
busy at what time slot, find time slot for a meeting for a given duration and
within the work hour timings. 

We get the data for a day. 

If no time slot can be found for the meeting, which can accommodate everyone's
calendar, give that as result.

Extension - find a time slot where we can accommodate enough number of people
if not all. The minimum number of people need to be more than max(2, N/2)

## Approach

Since we get the busy time slots for a day, we can find the free (or non-busy) time slots.
To accommodate the concept of work hour timings - let's just get the
inverse of it and add it to the busy time slots list as "busy with life"
or something. 

Once we get the non-busy time slots for everyone, we can find the intersection of
these time slots and find an appropriate time for the meeting.

To avoid AM/PM kind of input and parsing the "AM" and "PM" string, we get the
time in the form of 24 hr clock maybe? Like "10:00" and "15:30", or like the
army like "1000" and "1530". We gotta see how we are going to parse the time,
like if some built in library exists, or use some external library may be.

Since all the input is for a given day, we can just ignore the date.
Another assumption is that everyone is in the same timezone. We can
consider different date time and timezone stuff later if it comes up
as a need.

Now the problem boils down to how we do the following:
1. Find non-busy time slots given busy time slots. The basic operation
is, given the whole day, with some slots, find the inverse, or the
remaining slots present in the day. That will be the non-busy slots.
2. Find the intersection or the common slot between two slots. This
operation can result in a proper slot or no slot if there's nothing
common.
3. Based on the solution to problem 2, we can extrapolate it to find
the common non-busy slots between two list of non-busy slots.
4. With the output of 3, we can then find the duration of these slots.
For this we need to have some code to find time difference based
on the start and end time of the slot.
5. With the output of 4, we can check if the duration of the meeting
is less than or equal to the duration of the available non-busy
slots, and if it is, then these are the suggested slots. If the any slot
duration is more and not exactly equal to the meeting duration, we can
find one slot within the time slot, based on the time duration and
suggest it to the user.

---

I'm going to solve the most basic problem here, which is, find the
common or intersection slot between two time slots.

I'm introducing a time slot class, with start and end time

The time data type can be `LocalTime` I think

https://www.baeldung.com/java-8-date-time-intro
https://www.baeldung.com/java-8-date-time-intro#2-working-with-localtime

Using hamcrest to assert not null or null
https://mkyong.com/unittest/hamcrest-how-to-assertthat-check-null-value/

Adding hamcrest to gradle
http://hamcrest.org/JavaHamcrest/distributables
http://hamcrest.org/JavaHamcrest/distributables#using-hamcrest-in-a-gradle-project

---

One thing to note is - the operation common or intersection is a
commutative operation, that is

A ∩ B = B ∩ A

In code, it is

timeSlot.common(anotherTimeSlot) == anotherTimeSlot.common(timeSlot) // true 

I need to write tests for these too later

---

I just realized that in TimeSlot class, when we initialize start time and end time,
we expect start time to be less than end time, if that's not the case, we need to
see what to do in the constructor.

https://stackoverflow.com/questions/6086334/is-it-good-practice-to-make-the-constructor-throw-an-exception

Custom exceptions -
https://www.javatpoint.com/custom-exception

---

Now I'm thinking about - should I expose the subset method or not?
This is part of the common method, which will internally use subset
to tell if a time slot is a subset of another timeslot. Only if I
expose, I can test various scenarios actually. Or may be not? common
method brings in different scenarios. It will be easier to test
subset separately. I think I'm just going to expose it. Later this
method can be used to find different time slots which are subsets
of another time slot, a non-busy one, so that the subset time slot
can be suggested to the user then! :)

https://stackoverflow.com/questions/34571/how-do-i-test-a-private-function-or-a-class-that-has-private-methods-fields-or

I'm using a copy of the timeslot as the result of common() method.

https://www.baeldung.com/java-deep-copy

I create the copy using copy constructor. Was planning to use clone()
method, but it was tedious, and it wasn't necessary as LocalTime seems
to be immutable, so any references to it getting copied is fine, as
anyone having the reference cannot change the value however! :)

I had to generate the equals and hashcode method using IntelliJ. The code
looks quite good now! The assertions work!

I also handled null start or end time, as the equals and hashcode method
depend on these fields for their functioning and even if one of them
is null, things can go crazy. I could have handled the null values
in the equals or hashcode, however, come to think of it, it does NOT
make sense to have a null start or null end in a time slot as that 
does not have a meaning, so it's best to do it this way. Rather than
handling possible null values everywhere - don't allow to create a time slot
with null start or null end time.

I was thinking if it makes sense to have a time slot with start and end time
equal. I mean, for the common method, for time slot 10:00 to 11:00 and
11:00 to 12:00, is the result null or is it a time slot 11:00 to 11:00.
The latter does NOT make sense. I might have to call that an
invalid time slot too I guess, in the constructor that is. In the
common method, make sure we return null, as the way I thought about it
initially.

Okay, I finally implemented all the common time slot logic that I had in
my mind with tests too!


