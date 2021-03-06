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

---

Next I want to introduce a list of time slots and introduce the concept of
gaps - so that, given a list of time slots, we can find the gaps in it -
why - because, let's say you have the busy time slots, you would need the
gaps in it to find the free time slots, which you will later use for finding
common time slots across everyone's free time slots

---

Some more ideas I had -

Sort busy time slots by slot start times

Merge consecutive slots if they have common time slots. It can be called merge / union.

Now we have all timeslots that have nothing in common, from consecutive timeslots, find the gap in them. These
are free timeslots.

Now get the free timeslots of all people and take common among these lists to get free timeslots for all the people

With the free timeslots, compare the meeting duration with the duration free timeslots. Any timeslot that can
accommodate the duration should be filtered in and given as output.

From these timeslots, we can choose a timeslot for the meeting duration. For starters, we
can use start time of free timeslot + meeting duration time, if free timeslots are bigger.
Later, we can also show the free timeslots available and ask users to type some timeslot
from them in case they want to choose one themselves

---

Some more thoughts -

We need to be able to do the following actions
- Sorting time slots based on start time
- Smoothening / Merging of overlapping time slots or time slots which are close by, like
stuck to each other

One case to handle - boundary time slot bigger and smaller than the list of time slots that
are there.

---

We need to write a method to help with the smoothening of time slots

---

We also need to use `List.of()` to create lists easily and succinctly. It creates an
unmodifiable list too!

---

Before writing a method for smoothening / merging the time slots in TimeSlots class, we need to write
a merge method in the TimeSlot class!

---

I have written the merge method in TimeSlot class now

---

Some more thoughts I had - 

Find the boundary of everyone's working hours. Find the common of it all and use that as the boundary for everyone
to find gaps. OR fir each person, use their own boundary. If it's common for all, all cool! :)

Find free slots that can accommodate everyone

From algorithm point of view, it's safe to ignore any free slot that can't accommodate the meeting duration,
UNLESS there's a requirement that the meeting duration could be tweaked a bit to accommodate more people or
even everyone, or else, it can just be blindly ignored

If we cannot accommodate everyone, first check who all have NO or ZERO free slots that can accommodate
the meeting for its duration. Ignore them.

Now for others, who do have free slots long enough for the meeting, we have to either see priority of
people OR see which slot can accommodate the max number of people, for which common slots for different
combination of people have to be checked.
One thing that can be done is, for every free slot, big enough to accommodate a meeting, we can see how
many other free slots if others have something in common with it that can accommodate the meeting. Have
to see how many comparisons have to be done though. We could also sort the list of list of timeslots
based on this one timeslot, and it's friends on other people's free timeslots around the same time. The
wider and common it is for people, better, the thinner it gets, lesser common timeslot and less
possibility of meeting of the least duration common timeslot is smaller than meeting duration.

---

Now we have to write a method to smoothen the time slots that we have. Not sure if this should be a
mutating operation or a non mutating operation. Hmm. Mutating is easier. Do it once and don't have to
do it again. However it's OOPS. It doesn't make sense to not store state. If I didn't want state, then
I could do everything with static methods and no class members. Hmm

---

I have found gaps after sorting. There's still two things left out -
* Smoothening input time slots in case they are overlapping. It's easier
to do this, if it's all sorted.
* Cutting out parts of time slots that are outside the boundary, so that
the process of finding gaps is easier. It's easier to do this if it's all
sorted. We can do the smoothening first. But it doesn't make sense to
smoothen if it's outside the boundaries. Too much effort. Hmm. We could
then cut out parts first. To do this, we need to see if the time slots
and the boundary time slot have anything in common, if no, we cut it
off, if it does have anything in common, we take the common thing
and keep that alone and get rid of the remaining. If the time slot
is a subset of the boundary, we take it completely.

Sorting array list -   
https://stackoverflow.com/questions/23701943/sorting-arraylist-with-lambda-in-java-8#23702076

---

Get rid of getters in TimeSlot. For this we need:

TimeSlot startTimeDifference method
TimeSlot endTimeDifference method

TimeSlot gap method - should be commutative

---

Try to reduce the size of all methods to 5 lines or so

---

https://www.baeldung.com/java-iterate-list

---

It's possible that the time slots are outside the boundary. So,
we need to get time slots within the boundary. How?

Time Slot does not have anything in common with the boundary? Then chuck it.

Time Slot is subset of boundary? Then keep it.

TimeSlot and boundary have some partially common slot? Then have that partially common slot alone.

Just do common method over all time slots with the boundary and store it maybe?
That's better!! As common has all this logic!

---

Slots within the boundary are retained now, and others chucked.

Next is, smoothening overlapping time slots.

Looks like smoothening may not be needed with the current algorithm to
find the gaps. Wow! :D

If I try to do smoothening now, it's only a waste of computation to do
something extra with an extra traversal of the list of slots, when the
existing algorithm is able to take care of things!

---

I have also added a common method to the TimeSlots class now

---

Adding a duration method to Time Slot class

Checked out these examples

https://stackoverflow.com/questions/28353725/java-subtract-localtime#28353857
http://tutorials.jenkov.com/java-date-time/duration.html
https://mkyong.com/java8/java-8-period-and-duration-examples/

---

I realized I have used a lot of null in my program. I think I need to use
Optional probably.

https://duckduckgo.com/?t=ffab&q=optional+vs+null&ia=web&iax=qa
https://stackoverflow.com/questions/28746482/optional-vs-null-what-is-the-purpose-of-optional-in-java-8
https://stackoverflow.com/questions/23454952/uses-for-optional/
https://www.geeksforgeeks.org/optional-ofnullable-method-in-java-with-examples/

I can see lots of arguments to both sides - null and Optional, and some talk around
performance and stuff.

For now, I'm going to defer it and go ahead with null. Later I'll think about converting
to Optional based on what I find.

---

Gotta dig through null checks and possibilities of nulls in different places!

---

I wrote some tests for null checks

---

I just checked how to parse duration if given a string

I saw the Java code and there's also this

https://www.logicbig.com/how-to/code-snippets/jcode-java-8-date-time-api-duration-parse.html

I'm just going to ask the user to type the meeting duration in minutes!

---

Scanner stuff - https://duckduckgo.com/?t=ffab&q=java+scanner+nextline&ia=web
https://www.javatpoint.com/post/java-scanner-nextline-method
https://www.geeksforgeeks.org/scanner-nextline-method-in-java-with-examples/
https://www.tutorialspoint.com/java/util/scanner_nextline.htm

https://www.geeksforgeeks.org/ways-to-read-input-from-console-in-java/
