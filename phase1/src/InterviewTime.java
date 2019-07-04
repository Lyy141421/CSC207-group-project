import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

class InterviewTime {

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

    /**
     * Create an interview time.
     *
     * @param date     The date of this interview.
     * @param timeSlot The time slot for this interview.
     */
    InterviewTime(LocalDate date, int timeSlot) {
        this.date = date;
        this.timeSlot = timeSlot;
    }

    // === Getters ===
    LocalDate getDate() {
        return this.date;
    }

    int getTimeSlot() {
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
     * @param otherTime The other time being compared to.
     * @return true iff these two times have the same date and timeslot.
     */
    public boolean equals(InterviewTime otherTime) {
        return this.date.isEqual(otherTime.getDate()) && this.timeSlot == otherTime.getTimeSlot();
    }

    /**
     * Get the time slot string corresponding to the int that represents it.
     *
     * @param timeSlot The timeslot integer.
     * @return the string that the integer represents.
     */
    static String getTimeSlotCorrespondingToInt(int timeSlot) {
        return InterviewTime.timeSlots.get(timeSlot);
    }

    /**
     * Get a string representation of the time slots for an interview.
     *
     * @return a string representation of the time slots for an interview.
     */
    static String timeSlotsString() {
        String s = "";
        for (int i = 1; i < InterviewTime.timeSlots.size(); i++) {
            s += i + " - " + InterviewTime.timeSlots.get(i - 1) + "\t";
        }
        return s;
    }
}
