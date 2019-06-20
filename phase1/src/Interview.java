import java.time.LocalDate;

class Interview {
    /**
     * An interview.
     */

    // === Class variables ===
    private static final int MAX_NUM_ROUNDS = 3;
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
    // 0 <= roundNumber <= 3, where:
    //      - 0 -- phone interview
    //      - 1 -- in-person round 1
    //      - 2 -- in-person round 2
    //      - 3 -- in-person round 3

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
     * Get the ID associated with this interview.
     *
     * @return the ID associated with this interview.
     */
    int getID() {
        return this.ID;
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
