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
on the start and end time of the slot
5. With the output of 4, we can check if the duration of the meeting
is less than or equal to the duration of the available non-busy
slots, and if it is, then these are the suggested slots. If the any slot
duration is more and not exactly equal to the meeting duration, we can
find one slot within the time slot, based on the time duration and
suggest it to the user.


