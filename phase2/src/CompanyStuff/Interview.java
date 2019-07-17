package CompanyStuff;

import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTime;
import ApplicantStuff.Applicant;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Interview implements Serializable {
    /**
     * An interview.
     */

    // === Class variables ===
    // The list of descriptions for each round number.
    private static ArrayList<String> roundNumberDescriptions = new ArrayList<>(Arrays.asList("Phone interview",
            "In-person interview 1", "In-person interview 2", "In-person interview 3"));
    // The maximum number of in-person interview rounds
    static final int MAX_NUM_ROUNDS = 4;
    // The total number of interviews conducted
    private static int totalNumOfInterviews;

    // === Instance variables ===
    // The unique identifier for this interview
    private int id;
    // The job application associated with this interview
    private JobApplication jobApplication;
    // The interviewer
    private Interviewer interviewer;
    // The date and time of this interview
    private InterviewTime time;
    // Interview notes
    private String interviewNotes = "";
    // The result of the interview (null if incomplete, true if passed, false if failed)
    private Boolean pass = null;
    // Interview round
    private int roundNumber;
    // InterviewManager of the job posting this interview is held for
    private InterviewManager interviewManager;

    // === Representation invariants ===
    // ID >= 1


    // === Public methods ===

    // === Getters ===
    public int getId() {
        return this.id;
    }

    public JobApplication getJobApplication() {
        return this.jobApplication;
    }

    public InterviewTime getTime() {
        return this.time;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public String getRoundNumberDescription(int number) {
        return roundNumberDescriptions.get(number);
    }

    public Applicant getApplicant() {
        return this.jobApplication.getApplicant();
    }

    public Interviewer getInterviewer() {
        return this.interviewer;
    }

    private String getInterviewNotes() {
        return this.interviewNotes;
    }

    private boolean isPassed() {
        return this.pass;
    }

    // === Setters ===
    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public void setInterviewer(Interviewer interviewer) {
        this.interviewer = interviewer;
    }


    public void setTime(InterviewTime time) {
        this.time = time;
    }

    public void setInterviewNotes(String notes) {
        this.interviewNotes = notes;
    }

    void setPass(boolean pass) {
        this.pass = pass;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    // === Other methods ===

    /**
     * Check whether interview is completed.
     *
     * @return true iff the interview is completed.
     */
    public boolean isComplete() {
        return this.pass != null;
    }

    /**
     * Get a string representation of the preliminary input for this interview.
     *
     * @return a string representation of the preliminary input for this interview.
     */
    public String toStringPrelimInfo() {
        String s = "Interview ID: " + this.getId() + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + "\n";
        s += "Interviewee: " + this.getApplicant().getLegalName() + " (" + this.getApplicant().getUsername() + ")" +
                "\n";
        s += "Interview type: " + Interview.roundNumberDescriptions.get(this.getRoundNumber()) + "\n";
        return s;
    }

    @Override
    public String toString() {
        String s = toStringPrelimInfo() + "\n";
        if (this.isComplete()) {
            s += "Interview time: " + this.time.toString() + "\n";
            s += "Interview notes: \n" + this.getInterviewNotes() + "\n";
            s += "Passed: " + this.isPassed();
        }
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods === //

    // === Constructors ===

    Interview(JobApplication jobApplication, InterviewManager interviewManager) {
        Interview.totalNumOfInterviews++;
        this.jobApplication = jobApplication;
        this.interviewManager = interviewManager;
        this.id = Interview.totalNumOfInterviews;
    }

    // === Getters ===
    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    // ============================================================================================================== //
    // === Private methods ===

    // === Getter ===
    private JobPosting getJobPosting() {
        return this.jobApplication.getBranchJobPosting();
    }

}
