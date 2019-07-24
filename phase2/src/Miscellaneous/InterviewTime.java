package Miscellaneous;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewTime {
    /**
     * The time and date for an interview.
     */

    // === Class variables ===
    // Time slots
    public static ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList("9-10 am", "10-11 am", "1-2 pm", "2-3 pm",
            "3-4 pm", "4-5 pm"));

    // === Instance variables ===
    // The date of this interview
    private LocalDate date;
    // The time slot for this interview
    private int timeSlot;

    // === Public methods ===
    // === Constructors ===
    public InterviewTime() {
    }

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

    // === Other methods ===

    /**
     * Get a string representation of the time slots for an interview.
     *
     * @return a string representation of the time slots for an interview.
     */
    public String getTimeSlotsString() {
        String s = "";
        for (int i = 1; i < InterviewTime.timeSlots.size(); i++) {
            s += i + ": " + InterviewTime.timeSlots.get(i - 1) + "\t";
        }
        return s;
    }

    @Override
    public String toString() {
        return date + " at " + getTimeSlotCorrespondingToInt(timeSlot);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterviewTime)) {
            return false;
        } else {
            InterviewTime otherTime = (InterviewTime) obj;
            return (date.equals(otherTime.getDate()) && timeSlot == otherTime.getTimeSlot());
        }
    }

    @Override
    public int hashCode() {
        return timeSlot;
    }

    // ============================================================================================================== //
    // === Package-private ===

    // === Setters ===
    void setDate(LocalDate date) {
        this.date = date;
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Get the time slot string corresponding to the int that represents it.
     *
     * @param timeSlot The timeslot integer.
     * @return the string that the integer represents.
     */
    private String getTimeSlotCorrespondingToInt(int timeSlot) {
        return InterviewTime.timeSlots.get(timeSlot);
    }
}
