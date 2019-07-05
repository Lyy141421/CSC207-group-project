package Miscellaneous;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewTime {

    // === Class variables ===
    // Time slots
    static ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList("9-10 am", "10-11 am", "1-2 pm", "2-3 pm",
            "3-4 pm", "4-5 pm"));

    // === Instance variables ===
    // The date of this interview
    private LocalDate date;
    // The time slot for this interview
    private int timeSlot;

    // === Constructors ===

    public InterviewTime(){};

    public InterviewTime(LocalDate date, int timeSlot) {
        this.date = date;
        this.timeSlot = timeSlot;
    }

    // === Getters ===
    public LocalDate getDate() {
        return this.date;
    }

    public int getTimeSlot() {
        return this.timeSlot;
    }

    // === Setters ===
    void setDate(LocalDate date) {
        this.date = date;
    }

    void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    // === Other methods ===

    /**
     * Return a string representation of this interview time.
     *
     * @return a string representation of this interview time.
     */
    @Override
    public String toString() {
        return date + " at " + getTimeSlotCorrespondingToInt(timeSlot);
    }

    /**
     * Check whether these two interview times occur on the same date.
     *
     * @param otherTime The other time in question.
     * @return true iff these two times have the same date.
     */
    public boolean isOnSameDate(InterviewTime otherTime) {
        return this.date.isEqual(otherTime.getDate());
    }

    /**
     * Check whether these two times are equal.
     *
     * @param obj The object being compared to.
     * @return true iff obj is an interview time and the two times have the same date and timeslot.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterviewTime)) {
            return false;
        }
        else {
            InterviewTime otherTime = (InterviewTime)obj;
            return (date.equals(otherTime.getDate()) && timeSlot == otherTime.getTimeSlot());
        }
    }

    /**
     * Return a hashcode for this interview time.
     * @return an int; the same int should be returned for all interview times equal to this interview time.
     */
    @Override
    public int hashCode() {
        return timeSlot;
    }

    /**
     * Get the time slot string corresponding to the int that represents it.
     *
     * @param timeSlot The timeslot integer.
     * @return the string that the integer represents.
     */
    public String getTimeSlotCorrespondingToInt(int timeSlot) {
        return InterviewTime.timeSlots.get(timeSlot);
    }

    /**
     * Get a string representation of the time slots for an interview.
     *
     * @return a string representation of the time slots for an interview.
     */
    public String getTimeSlotsString() {
        String s = "";
        for (int i = 1; i < InterviewTime.timeSlots.size(); i++) {
            s += i + " - " + InterviewTime.timeSlots.get(i - 1) + "\t";
        }
        return s;
    }
}
