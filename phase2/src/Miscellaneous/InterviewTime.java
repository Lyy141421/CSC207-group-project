package Miscellaneous;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewTime implements Serializable {
    /**
     * The time and date for an interview.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Possible interview times
    public static String NINE_TO_TEN_AM = "9-10 am";
    public static String TEN_TO_ELEVEN_AM = "10-11 am";
    public static String ELEVEN_AM_TO_NOON = "11 am - 12 pm";
    public static String NOON_TO_ONE_PM = "12-1 pm";
    public static String ONE_TO_TWO_PM = "1-2 pm";
    public static String TWO_TO_THREE_PM = "2-3 pm";
    public static String THREE_TO_FOUR_PM = "3-4 pm";
    public static String FOUR_TO_FIVE_PM = "4-5 pm";
    // A list of string representations of the time slots
    public static ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList(NINE_TO_TEN_AM, TEN_TO_ELEVEN_AM, ELEVEN_AM_TO_NOON,
            NOON_TO_ONE_PM, ONE_TO_TWO_PM, TWO_TO_THREE_PM, THREE_TO_FOUR_PM, FOUR_TO_FIVE_PM));


    // === Instance variables ===
    // The date of this interview
    private LocalDate date;
    // The time slot for this interview
    private String timeSlot;

    // === Representation invariants ===
    // timeSlot of one of the string options in the timeSlots list.

    // === Public methods ===
    // === Constructors ===
    public InterviewTime() {
    }

    public InterviewTime(LocalDate date, String timeSlot) {
        this.date = date;
        this.timeSlot = timeSlot;
    }

    // === Getters ===
    public LocalDate getDate() {
        return this.date;
    }

    public String getTimeSlot() {
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
        return date + " at " + timeSlot;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterviewTime)) {
            return false;
        } else {
            InterviewTime otherTime = (InterviewTime) obj;
            return (date.equals(otherTime.getDate()) && timeSlot.equals(otherTime.getTimeSlot()));
        }
    }

    @Override
    public int hashCode() {
        return InterviewTime.timeSlots.indexOf(timeSlot);
    }

}
