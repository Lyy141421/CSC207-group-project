import java.time.LocalDate;

class Interview {
    /**
     * An interview.
     */

    // === Instance variables ===
    // The total number of interviews conducted
    private static int total;
    // The unique identifier for this interview
    private int ID;
    // The job application associated with this interview
    private JobApplication jobApplication;
    // The interviewer
    private Interviewer interviewer;
    // The HRCoordinator who set up the interview
    private HRCoordinator hrCoordinator;
    // The date of this interview
    private LocalDate date;
    // The time slot of the interview
    private int timeSlot;
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
     * @param date           The date of this interview.
     * @param timeSlot       The interview time slot.
     * @param roundNumber    The round number.
     */
    Interview(JobApplication jobApplication, Interviewer interviewer, HRCoordinator hrCoordinator,
              InterviewManager interviewManager, LocalDate date, int timeSlot, int roundNumber) {
        this.jobApplication = jobApplication;
        this.interviewer = interviewer;
        this.hrCoordinator = hrCoordinator;
        this.interviewManager = interviewManager;
        this.date = date;
        this.timeSlot = timeSlot;
        this.roundNumber = roundNumber;
        this.ID = Interview.total;
        Interview.total++;
    }

    // === Getters ===

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
     * Get the interview date.
     * @return the interview date.
     */
    LocalDate getDate() {
        return this.date;
    }

    /**
     * Get the interview time slot.
     *
     * @return the interview time slot.
     */
    int getTimeSlot() {
        return this.timeSlot;
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
     * Set the date for this interview.
     *
     * @param date The interview date.
     */
    void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Set the time slot for this interview.
     *
     * @param timeSlot The time slot of this interview.
     */
    void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }


    // === Other methods ===

    /**
     * Record that this applicant has failed this interview.
     */
    void setFail() {
        this.pass = false;
    }
}
