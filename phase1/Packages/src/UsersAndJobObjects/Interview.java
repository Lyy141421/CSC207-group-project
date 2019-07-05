package UsersAndJobObjects;

import FileLoadingAndStoring.FileSystem;
import FileLoadingAndStoring.Storable;
import Managers.InterviewManager;
import Miscellaneous.InterviewTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "Interviews";

    // === Instance variables ===
    // The unique identifier for this interview
    private int ID;
    // The job application associated with this interview
    private JobApplication jobApplication;
    // The interviewer
    private Interviewer interviewer;
    // The UsersAndJobObjects.HRCoordinator who set up the interview
    private HRCoordinator hrCoordinator;
    // The date and time of this interview
    private InterviewTime time;
    // UsersAndJobObjects.Interview notes
    private String interviewNotes = "";
    // The result of the interview (if the applicant passed)
    private boolean pass = true;
    // UsersAndJobObjects.Interview round
    private int roundNumber;
    // Managers.InterviewManager of the job posting this interview is held for
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

    // === Setters ===

    public void setInterviewNotes(String notes) {
        this.interviewNotes = notes;
    }

    public void setTime(InterviewTime time) {
        this.time = time;
    }

    // === Other methods ===

    /**
     * Record that this applicant has failed this interview.
     */
    public void setFail() {
        this.pass = false;
    }

    /**
     * Get a string representation of the preliminary input for this interview.
     *
     * @return a string representation of the preliminary input for this interview.
     */
    public String toStringPrelimInfo() {
        String s = "UsersAndJobObjects.Interview ID: " + this.getId() + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle();
        s += "Interviewee: " + this.getApplicant().getLegalName() + " (" + this.getApplicant().getUsername() + ")" +
                "\n";
        s += "UsersAndJobObjects.Interview type: " + Interview.roundNumberDescriptions.get(this.getRoundNumber()) + "\n";
        return s;
    }

    /**
     * Get a string representation of all the information for this interview.
     * @return  a string representation of all the information for this interview.
     */
    @Override
    public String toString() {
        String s = "UsersAndJobObjects.Interview time: " + this.time.toString() + "\n";
        s += this.toStringPrelimInfo();
        s += "UsersAndJobObjects.Interview notes: \n" + this.getInterviewNotes();
        s += "Passed: " + this.isPassed();
        return s;
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString(){
        return Integer.toString(this.ID);
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(Interview.FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("interviewNotes", this.interviewNotes);
        data.put("pass", this.pass);
        data.put("roundNumber", this.roundNumber);
        data.put("UsersAndJobObjects.JobApplication", new ArrayList() {{
            add(Applicant.FILENAME);
            add(getApplicant().getIdString());
        }});
        data.put("interviewer", new ArrayList() {{
            add(Interviewer.FILENAME);
            add(getInterviewer().getIdString());
        }});
        data.put("UsersAndJobObjects.HRCoordinator", new ArrayList() {{
            add(HRCoordinator.FILENAME);
            add(getHRCoordinator().getIdString());
        }});
        data.put("InterviewTimeDate", this.time.getDate());
        data.put("InterviewTimeTimeslot", this.time.getTimeSlot());
    }

    /**
     * loads the Object
     */
    public void loadSelf() {
        FileSystem.mapPut(Interview.FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(Interview.FILENAME, getIdString());
        this.jobApplication = (JobApplication) FileSystem.subLoader(JobApplication.class, (String) ((ArrayList)
                data.get("UsersAndJobObjects.JobApplication")).get(0), (String) ((ArrayList) data.get("UsersAndJobObjects.JobApplication")).get(1));
        this.interviewer = (Interviewer) FileSystem.subLoader(Interviewer.class, (String) ((ArrayList)
                data.get("interviewer")).get(0), (String) ((ArrayList) data.get("interviewer")).get(1));
        this.hrCoordinator = (HRCoordinator) FileSystem.subLoader(HRCoordinator.class, (String)
                ((ArrayList) data.get("UsersAndJobObjects.HRCoordinator")).get(0), (String) ((ArrayList) data.get("UsersAndJobObjects.HRCoordinator")).get(1));
        this.loadPrelimData(data);
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

    int getId() {
        return this.ID;
    }

    JobPosting getJobPosting() {
        return this.jobApplication.getJobPosting();
    }

    Applicant getApplicant() {
        return this.jobApplication.getApplicant();
    }

    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    Interviewer getInterviewer() {
        return this.interviewer;
    }

    HRCoordinator getHRCoordinator() {
        return this.hrCoordinator;
    }

    String getInterviewNotes() {
        return this.interviewNotes;
    }

    Boolean isPassed() {
        return this.pass;
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Load the preliminary data for this UsersAndJobObjects.Interviewer.
     *
     * @param data The UsersAndJobObjects.Company's Data
     */
    private void loadPrelimData(HashMap data) {
        this.interviewNotes = (String)data.get("interviewNotes");
        this.pass = (boolean)data.get("pass");
        this.roundNumber = (int)data.get("roundNumber");
        this.interviewManager = this.jobApplication.getJobPosting().getInterviewManager();
        this.time = new InterviewTime(LocalDate.parse((String) data.get("InterviewTimeDate")),
                (int) data.get("InterviewTimeTimeslot"));
    }

}
