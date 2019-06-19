import java.util.ArrayList;

class InterviewManager {

    // Elaine --- I'm doing this so that my code won't be underlined red
    // === Instance variables ===
    // List of interviews
    private ArrayList<Interview> ongoingInterviews = new ArrayList<Interview>();
    private ArrayList<Interview> completedInterviews = new ArrayList<Interview>();
    private ArrayList<Applicant> applicantsInConsideration = new ArrayList<Applicant>();
    private ArrayList<Applicant> applicantsRejected = new ArrayList<Applicant>();
    private int currentRound;


    // === Other methods ===

    void addInterview(Interview interview) {
        this.ongoingInterviews.add(interview);
    }

    void completeInterview(Interview interview) {
        this.ongoingInterviews.remove(interview);
        this.completedInterviews.add(interview);
    }

    void reject (Applicant applicant) {
        applicantsInConsideration.remove(applicant);
        applicantsRejected.add(applicant);
    }
}
