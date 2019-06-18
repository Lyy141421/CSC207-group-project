import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

class Interviewer extends User {

    // Elaine -- I'm just adding a stub so that my code won't be underlined red
    // === Instance variables ===
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The interviewer's schedule
    private HashMap<LocalDate, HashMap<Integer, Boolean>> schedule = new HashMap<>();
    ;

    // === Constructors ===
    Interviewer(String email, Company company, String field, LocalDate dateCreated) {
        super(email, dateCreated);
        this.company = company;
        this.field = field;
    }

    // === Getters ===
    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Other methods ===
    int getAvailableTime() {
        return 0;
    }


}
