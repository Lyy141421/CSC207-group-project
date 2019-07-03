import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Interview implements Storable{
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
    // The filename under which this will be saved in the FileSystem
    static final String FILENAME = "Interviews";

    // === Representation invariants ===
    // ID >= 0

    // === Constructors ===

    public Interview(String id){
        this.ID = Integer.parseInt(id);
        Interview.total = Integer.max(this.ID, Interview.total);
        loadSelf();
    }

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

    JobApplication getJobApplication() {
        return this.jobApplication;
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

    InterviewTime getTime() {
        return this.time;
    }

    String getInterviewNotes() {
        return this.interviewNotes;
    }

    Boolean isPassed() {
        return this.pass;
    }

    int getRoundNumber() {
        return this.roundNumber;
    }

    // === Setters ===

    void setInterviewNotes(String notes) {
        this.interviewNotes = notes;
    }

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
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("interviewNotes", this.interviewNotes);
        data.put("pass", this.pass);
        data.put("roundNumber", this.roundNumber);
        data.put("JobApplication", new ArrayList(){{ add(getApplicant().FILENAME); add(getApplicant().getIdString()); }});
        data.put("interviewer", new ArrayList(){{ add(getInterviewer().FILENAME); add(getInterviewer().getIdString()); }});
        data.put("HRCoordinator", new ArrayList(){{ add(getHRCoordinator().FILENAME); add(getHRCoordinator().getIdString()); }});
        data.put("InterviewTimeDate", this.time.getDate());
        data.put("InterviewTimeTimeslot", this.time.getTimeSlot());
    }

    /**
     * Load the preliminary data for this Interviewer.
     *
     * @param data The Company's Data
     */
    private void loadPrelimData(HashMap data) {
        this.interviewNotes = (String)data.get("interviewNotes");
        this.pass = (boolean)data.get("pass");
        this.roundNumber = (int)data.get("roundNumber");
        this.interviewManager = this.jobApplication.getJobPosting().getInterviewManager();
        this.time = new InterviewTime(LocalDate.parse((String)data.get("InterviewTimeDate")), (int)data.get("InterviewTimeTimeslot"));
    }

    /**
     * loads the Object
     */
    public void loadSelf() {
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(FILENAME, getIdString());
        this.loadPrelimData(data);
        this.jobApplication = (JobApplication) FileSystem.subLoader(JobApplication.class, (String) ((ArrayList) data.get("JobApplication")).get(0), (String) ((ArrayList) data.get("JobApplication")).get(0));
        this.interviewer = (Interviewer) FileSystem.subLoader(Interviewer.class, (String) ((ArrayList) data.get("interviewer")).get(0), (String) ((ArrayList) data.get("interviewer")).get(0));
        this.hrCoordinator = (HRCoordinator) FileSystem.subLoader(HRCoordinator.class, (String) ((ArrayList) data.get("HRCoordinator")).get(0), (String) ((ArrayList) data.get("HRCoordinator")).get(0));
    }

}
