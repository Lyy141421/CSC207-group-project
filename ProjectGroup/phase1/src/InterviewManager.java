import java.util.ArrayList;
import java.util.HashMap;

class InterviewManager {

    // Elaine --- I'm doing this so that my code won't be underlined red
    // === Instance variables ===
    // Map of job field to interviewers
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers;
    // List of interviews
    private ArrayList<Interview> interviews;


    // === Other methods ===
    Interviewer findInterviewer(String field) {
        return null;
    }

    void addInterview(Interview interview) {
        this.interviews.add(interview);
    }


}
