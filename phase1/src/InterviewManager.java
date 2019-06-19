import java.util.ArrayList;

class InterviewManager {

    // Elaine --- I'm doing this so that my code won't be underlined red
    // === Instance variables ===
    // List of interviews
    private ArrayList<Interview> interviews = new ArrayList<Interview>();
    private ArrayList<Applicant> applicantsInConsideration = new ArrayList<Applicant>();
    private ArrayList<Applicant> applicantsRejected = new ArrayList<Applicant>();
    private int currentRound;


    // === Other methods ===

    void addInterview(Interview interview) {
        this.interviews.add(interview);
    }

    void reject (Applicant applicant) {
        applicantsInConsideration.remove(applicant);
        applicantsRejected.add(applicant);
    }
}
