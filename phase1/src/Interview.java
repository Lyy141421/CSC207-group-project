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
    public final String FILENAME = "Interviews";

    // === Representation invariants ===
    // ID >= 0

    // === Constructors ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    public Interview(String id){
        this.ID = Integer.parseInt(id);
        Interview.total = Integer.max(this.ID, Interview.total);
        loadSelf();
    }

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

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getId(){
        return Integer.toString(this.ID);
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("interviewNotes", this.interviewNotes);
        data.put("pass", this.pass);
        data.put("roundNumber", this.roundNumber);
        data.put("JobApplication", new ArrayList(){{ add(getApplicant().FILENAME); add(getApplicant().getId()); }});
        data.put("interviewer", new ArrayList(){{ add(getInterviewer().FILENAME); add(getInterviewer().getId()); }});
        data.put("HRCoordinator", new ArrayList(){{ add(getHRCoordinator().FILENAME); add(getHRCoordinator().getId()); }});
        data.put("InterviewTimeDate", this.time.getDate());
        data.put("InterviewTimeTimeslot", this.time.getTimeSlot());
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        this.interviewNotes = (String)data.get("interviewNotes");
        this.pass = (boolean)data.get("pass");
        this.roundNumber = (int)data.get("roundNumber");
        if(FileSystem.isLoaded((String)((ArrayList)data.get("JobApplication")).get(0), (String)((ArrayList)data.get("JobApplication")).get(0))){
            this.jobApplication = (JobApplication) FileSystem.mapGet((String)((ArrayList)data.get("JobApplication")).get(0), (String)((ArrayList)data.get("JobApplication")).get(0));
        }
        else{
            this.jobApplication = (new JobApplication((String)((ArrayList)data.get("JobApplication")).get(0)));
        }
        if(FileSystem.isLoaded((String)((ArrayList)data.get("interviewer")).get(0), (String)((ArrayList)data.get("interviewer")).get(0))){
            this.interviewer = (Interviewer) FileSystem.mapGet((String)((ArrayList)data.get("interviewer")).get(0), (String)((ArrayList)data.get("interviewer")).get(0));
        }
        else{
            this.interviewer = (new Interviewer((String)((ArrayList)data.get("interviewer")).get(0)));
        }
        if(FileSystem.isLoaded((String)((ArrayList)data.get("HRCoordinator")).get(0), (String)((ArrayList)data.get("HRCoordinator")).get(0))){
            this.hrCoordinator = (HRCoordinator) FileSystem.mapGet((String)((ArrayList)data.get("HRCoordinator")).get(0), (String)((ArrayList)data.get("HRCoordinator")).get(0));
        }
        else{
            this.hrCoordinator = (new HRCoordinator((String)((ArrayList)data.get("interviewer")).get(0)));
        }
        this.interviewManager = this.jobApplication.getJobPosting().getInterviewManager();
        this.time = new InterviewTime(LocalDate.parse((String)data.get("InterviewTimeDate")), (int)data.get("InterviewTimeTimeslot"));
    }

}
