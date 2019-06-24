import java.util.ArrayList;
import java.util.Arrays;

class Interview {
    /**
     * An interview.
     */

    // === Class variables ===
    // The list of descriptions for each round number.
    private static ArrayList<String> roundNumberDescriptions = new ArrayList<>(Arrays.asList("Phone interview, " +
            "In-person interview 1, " + "In-person interview 2", "In-person interview 3"));
    // The maximum number of in-person interview rounds
    private static final int MAX_NUM_ROUNDS = Interview.roundNumberDescriptions.size() - 1;
    // The total number of interviews conducted
    private static int total;

    // === Instance variables ===
    // The unique identifier for this interview
    private int ID;
    // The job application associated with this interview
    private JobApplication jobApplication;
    // The interviewer
    private Interviewer interviewer;
    // The HRCoordinator who set up the interview
    private HRCoordinator hrCoordinator;
    // The date and time of this interview
    private InterviewTime time;
    // Interview notes
    private String interviewNotes = "";
    // The result of the interview (if the applicant passed)
    private boolean pass = true;
    // Interview round
    private int roundNumber;
    // InterviewManager of the job posting this interview is held for
    private InterviewManager interviewManager;

    // === Representation invariants ===
    // ID >= 0

    // === Constructors ===

    /**
     * Create a new interview.
     */
    Interview() {
        this.ID = Interview.total;
        Interview.total++;
    }

    /**
     * Create a new interview.
     *
     * @param jobApplication The job application in question.
     * @param interviewer    The interviewer.
     * @param hrCoordinator  The HRCoordinator who set-up the interview.
     * @param roundNumber    The round number.
     */
    Interview(JobApplication jobApplication, Interviewer interviewer, HRCoordinator hrCoordinator,
              InterviewManager interviewManager, int roundNumber) {
        this.jobApplication = jobApplication;
        this.interviewer = interviewer;
        this.hrCoordinator = hrCoordinator;
        this.interviewManager = interviewManager;
        this.roundNumber = roundNumber;
        this.ID = Interview.total;
        Interview.total++;
    }

    /**
     * Create a new interview.
     *
     * @param jobApplication The job application in question.
     * @param interviewer    The interviewer.
     * @param hrCoordinator  The HRCoordinator who set-up the interview.
     * @param time           The interview time.
     * @param roundNumber    The round number.
     */
    Interview(JobApplication jobApplication, Interviewer interviewer, HRCoordinator hrCoordinator,
              InterviewManager interviewManager, InterviewTime time, int roundNumber) {
        this.jobApplication = jobApplication;
        this.interviewer = interviewer;
        this.hrCoordinator = hrCoordinator;
        this.interviewManager = interviewManager;
        this.time = time;
        this.roundNumber = roundNumber;
        this.ID = Interview.total;
        Interview.total++;
    }

    // === Getters ===

    /**
     * Get the maximum number of rounds of interviews.
     *
     * @return the maximum number of rounds of interviews.
     */
    static int getMaxNumRounds() {
        return Interview.MAX_NUM_ROUNDS;
    }

    /**
     * Get the job application associated with this interview.
     *
     * @return the job application associated with this interview.
     */
    JobApplication getJobApplication() {
        return this.jobApplication;
    }

    /**
     * Get the applicant.
     *
     * @return the applicant.
     */
    Applicant getApplicant() {
        return this.jobApplication.getApplicant();
    }

    /**
     * Get the interviewManager.
     *
     * @return the interviewManager.
     */
    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    /**
     * Get the interviewer.
     *
     * @return the interviewer.
     */
    Interviewer getInterviewer() {
        return this.interviewer;
    }

    /**
     * Get the HRCoordinator.
     *
     * @return the HRCoordinator.
     */
    HRCoordinator getHRCoordinator() {
        return this.hrCoordinator;
    }

    /**
     * Get the interview time.
     * @return the interview time.
     */
    InterviewTime getTime() {
        return this.time;
    }

    /**
     * Get the interview notes written by the interviewer.
     *
     * @return the interview notes.
     */
    String getInterviewNotes() {
        return this.interviewNotes;
    }

    /**
     * Get whether this interview has been passed.
     *
     * @return true iff the applicant has passed this interview.
     */
    Boolean isPassed() {
        return this.pass;
    }

    /**
     * Get the interview round number.
     *
     * @return the interview round number.
     */
    int getRoundNumber() {
        return this.roundNumber;
    }

    // === Setters ===

    /**
     * Set interview notes.
     *
     * @param notes The interview notes.
     */
    void setInterviewNotes(String notes) {
        this.interviewNotes = notes;
    }

    /**
     * Set the time for this interview.
     *
     * @param time The interview time
     */
    void setTime(InterviewTime time) {
        this.time = time;
    }

    // === Other methods ===

    /**
     * Record that this applicant has failed this interview.
     */
    void setFail() {
        this.pass = false;
    }
}
