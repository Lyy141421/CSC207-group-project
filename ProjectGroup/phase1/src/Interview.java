import java.time.LocalDateTime;

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
    private Interviewer interviewerAccount;
    // The HRCoordinator who set up the interview
    private HRCoordinator hrCoordinatorAccount;
    // The time of the interview
    private LocalDateTime time;
    // The result of the interview (if the applicant passed)
    private boolean pass = false;
    // Interview round
    private int roundNumber;

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
     * @param jobApplication       The job application in question.
     * @param interviewerAccount   The interviewer.
     * @param hrCoordinatorAccount The HRCoordinator who set-up the interview.
     * @param time                 The interview time.
     * @param roundNumber          The round number.
     */
    Interview(JobApplication jobApplication, Interviewer interviewerAccount,
              HRCoordinator hrCoordinatorAccount, LocalDateTime time, int roundNumber) {
        this.jobApplication = jobApplication;
        this.interviewerAccount = interviewerAccount;
        this.hrCoordinatorAccount = hrCoordinatorAccount;
        this.time = time;
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
     * Get the interviewer.
     *
     * @return the interviewer.
     */
    Interviewer getInterviewerAccount() {
        return this.interviewerAccount;
    }

    /**
     * Get the HRCoordinator.
     *
     * @return the HRCoordinator.
     */
    HRCoordinator getHrCoordinatorAccount() {
        return this.hrCoordinatorAccount;
    }

    /**
     * Get the interview time.
     *
     * @return the interview time.
     */
    LocalDateTime getTime() {
        return this.time;
    }

    /**
     * Get the interview round number.
     *
     * @return the interview round number.
     */
    int getRoundNumber() {
        return this.roundNumber;
    }
}
