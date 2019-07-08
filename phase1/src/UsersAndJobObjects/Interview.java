package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;
import Managers.InterviewManager;
import Miscellaneous.InterviewTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Interview implements Storable {
    /**
     * An interview.
     */

    // === Class variables ===
    // The list of descriptions for each round number.
    private static ArrayList<String> roundNumberDescriptions = new ArrayList<>(Arrays.asList("Phone interview",
            "In-person interview 1", "In-person interview 2", "In-person interview 3"));
    // The maximum number of in-person interview rounds
    private static final int MAX_NUM_ROUNDS = Interview.roundNumberDescriptions.size() - 1;
    // The total number of interviews conducted
    private static int total;
    // The filename under which this will be saved in the FileSystem
    public static final String FILENAME = "Interviews";

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
    // UsersAndJobObjects.Interview notes
    private String interviewNotes = "";
    // The result of the interview (null if incomplete, true if passed, false if failed)
    private Boolean pass = null;
    // Interview round
    private int roundNumber;
    // InterviewManager of the job posting this interview is held for
    private InterviewManager interviewManager;

    // === Representation invariants ===
    // ID >= 0


    // === Public methods ===
    // === Constructors ===

    public Interview(String id){
        this.ID = Integer.parseInt(id);
        Interview.total = Integer.max(this.ID, Interview.total);
    }

    // === Getters ===
    public String getId() {
        return Integer.toString(this.ID);
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

    public HRCoordinator getHRCoordinator() {
        return this.hrCoordinator;
    }

    public String getInterviewNotes() {
        return this.interviewNotes;
    }

    public boolean isPassed() {
        return this.pass;
    }

    // === Setters ===

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public void setInterviewer(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    public void setHrCoordinator(HRCoordinator hrCoordinator) {
        this.hrCoordinator = hrCoordinator;
    }

    public void setTime(InterviewTime time) {
        this.time = time;
    }

    public void setInterviewNotes(String notes) {
        this.interviewNotes = notes;
    }

    public void setPass(boolean pass) {
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
     * @return  true iff the interview is completed.
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

    /**
     * Get a string representation of all the information for this interview.
     * @return  a string representation of all the information for this interview.
     */
    @Override
    public String toString() {
        String s = "Interview time: " + this.time.toString() + "\n";
        s += this.toStringPrelimInfo();
        s += "Interview notes: \n" + this.getInterviewNotes();
        s += "Passed: " + this.isPassed();
        return s;
    }

    // ============================================================================================================== //


    // === Package-private methods === //
    // === Constructors ===
    Interview() {
        this.ID = Interview.total;
        Interview.total++;
    }

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

    static int getMaxNumRounds() {
        return Interview.MAX_NUM_ROUNDS;
    }

    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    private JobPosting getJobPosting() {
        return this.jobApplication.getJobPosting();
    }

    /**
     * Check whether this interview is scheduled before this date.
     * @param date  The date in question.
     * @return  true iff this interview is scheduled before this date.
     */
    boolean isBeforeDate(LocalDate date) {
        return this.getTime().getDate().isBefore(date);
    }

    /**
     * Check whether this interview is scheduled on this date.
     * @param date  The date in question.
     * @return  true iff this interview is scheduled on this date.
     */
    boolean isOnDate(LocalDate date) {
        return this.getTime().getDate().isEqual(date);
    }

}
