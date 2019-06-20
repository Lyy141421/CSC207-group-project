import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

class InterviewTime {

    // === Class variables ===
    // Time slots
    ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList("9-10 am", "10-11 am", "1-2 pm", "2-3 pm", "3-4 pm",
            "4-5 pm"));

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


}
