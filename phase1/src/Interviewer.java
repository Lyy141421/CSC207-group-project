import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Interviewer extends User {

    // Elaine -- I'm just adding a stub so that my code won't be underlined red
    // === Class variables ===
    // Time slots
    ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList("9-10 am", "10-11 am", "1-2 pm", "2-3 pm", "3-4 pm",
            "4-5 pm"));     // put something reasonable -- the indexes correspond to the Integer part of schedule

    // === Instance variables ===
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The interviewer's schedule as a map of the date to a map of the time slot to whether or not it is filled
    private HashMap<LocalDate, HashMap<Integer, Boolean>> schedule = new HashMap<>();

    // === Constructors ===
    Interviewer(String email, Company company, String field, LocalDate dateCreated) {
        super(email, dateCreated);
        this.company = company;
        this.field = field;
    }

    // === Getters ===
    String getField() {
        return this.field;
    }

    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Other methods ===
    HashMap<LocalDate, Integer> getAvailableTime() {
        return new HashMap<>();
    }


}
