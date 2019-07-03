import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

class JobApplication implements Storable {
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // Job application statuses as constants
    private static final int ARCHIVED = -3;
    private static final int SUBMITTED = -2;
    private static final int UNDER_REVIEW = -1;
    private static final int PHONE_INTERVIEW = 0;
    private static final int IN_PERSON_1 = 1;
    private static final int IN_PERSON_2 = 2;
    private static final int IN_PERSON_3 = 3;
    private static final int HIRED = 4;
    // Map of statuses and their identifying integers
    private static HashMap<Integer, String> statuses = new HashMap<Integer, String>() {{
        put(JobApplication.ARCHIVED, "Archived");
        put(JobApplication.SUBMITTED, "Submitted");
        put(JobApplication.UNDER_REVIEW, "Under review");
        put(JobApplication.PHONE_INTERVIEW, "Phone interview");
        put(JobApplication.IN_PERSON_1, "In-person interview round 1");
        put(JobApplication.IN_PERSON_2, "In-person interview round 2");
        put(JobApplication.IN_PERSON_3, "In-person interview round 3");
        put(JobApplication.HIRED, "Hired");
    }};

    // === Instance variables ===
    // Unique identifier for a submitted job application
    private int ID;
    // The applicant for a job
    private Applicant applicant;
    // The JobPosting that was applied for
    private JobPosting jobPosting;
    // The file name of the CV submitted for this application
    private String CV;
    // The the file name of the cover letter submitted for this application
    private String coverLetter;
    // The status of this application
    private int status = -2;
    // The date this application was submitted
    private LocalDate applicationDate;
    // The interviews conducted for this application
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The filename under which this will be saved in the FileSystem
    public final String FILENAME = "JobApplications";

    // === Constructors ===

    public JobApplication(String id) {
        this.ID = Integer.parseInt(id);
        JobApplication.totalNumOfApplications = Integer.max(this.ID, JobApplication.totalNumOfApplications);
        loadSelf();
    }

    JobApplication(LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    JobApplication(JobPosting jobPosting, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.jobPosting = jobPosting;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    JobApplication(Applicant applicant, JobPosting jobPosting, String CV, String coverletter, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    JobApplication(int ID, Applicant applicant, JobPosting jobPosting, String CV, String coverletter, int status,
                   LocalDate applicationDate) {
        this.ID = ID;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.applicationDate = applicationDate;
        this.status = status;
        JobApplication.totalNumOfApplications++;
    }

    // === Getters ===
    /**
     * Get the application ID.
     *
     * @return the application ID.
     */
    int getID() {
        return this.ID;
    }

    Applicant getApplicant() {
        return this.applicant;
    }

    JobPosting getJobPosting() {
        return this.jobPosting;
    }

    String getCV() {
        return this.CV;
    }

    String getCoverLetter() {
        return this.coverLetter;
    }

    int getStatus() {
        return this.status;
    }

    LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Setters ===

    void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    void setCV(String CV) {
        this.CV = CV;
    }

    void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    void setStatus(int status) {
        this.status = status;
    }

    void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    // === Other methods ===
    void advanceStatus() {
        this.status++;
    }

    /**
     * Add an interview for this job application.
     *
     * @param interview The interview to be added.
     */
    void addInterview(Interview interview) {
        this.interviews.add(interview);
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param hrCoordinator The HR Coordinator who set up this interview
     * @param round         The interview round.
     */
    void setUpInterview(HRCoordinator hrCoordinator, int round) {
        JobPosting jobPosting = this.getJobPosting();
        String jobField = jobPosting.getField();
        Interviewer interviewer = hrCoordinator.getCompany().findInterviewer(jobField);
        Interview interview = new Interview(this, interviewer, hrCoordinator,
                jobPosting.getInterviewManager(), round);
        this.addInterview(interview);
        interviewer.addInterview(interview);
        this.advanceStatus();
    }

    /**
     * Archive the job application.
     */
    void setArchived() {
        this.setStatus(JobApplication.ARCHIVED);
    }

    /**
     * Set this job application status to hired.
     */
    void setHired() {
        this.setStatus((JobApplication.HIRED));
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString() {
        return Integer.toString(this.ID);
    }

    /**
     * Get a string representation of this job application.
     *
     * @return a string representation of this job application.
     */
    @Override
    public String toString() {
        String s = "Application ID: " + this.getID() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "CV: " + "\n" + this.getCV() + "\n";
        s += "Cover letter: " + this.getCoverLetter() + "\n";
        s += "Status: " + JobApplication.statuses.get(this.getStatus()) + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf) + "\n";
        s += "Interview IDs: " + this.getInterviewIDs();
        return s;
    }

    /**
     * Get the interview IDs for this job application.
     *
     * @return the IDs of the interviews that this job application has undergone.
     */
    private String getInterviewIDs() {
        String s = "";
        for (Interview interview : this.interviews) {
            s += interview.getId() + ", ";
        }
        return s.substring(0, s.length() - 2); // remove last comma and space
    }

    /**
     * Saves the Object
     */
    public void saveSelf() {
        FileSystem.mapPut(FILENAME, this.getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("CV", this.getCV());
        data.put("CoverLetter", this.getCoverLetter());
        data.put("Status", this.getStatus());
        data.put("ApplicationDate", this.getApplicationDate());
        data.put("Applicant", new ArrayList() {{
            add(getApplicant().FILENAME);
            add(getApplicant().getIdString());
        }});
        data.put("JobPosting", new ArrayList() {{
            add(getJobPosting().FILENAME);
            add(getJobPosting().getIdString());
        }});
        ArrayList interviews = new ArrayList();
        for (Interview x : this.interviews) {
            interviews.add(new ArrayList() {{
                add(x.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("interviews", interviews);
        FileSystem.write(FILENAME, getIdString(), data);
    }

    /**
     * Load this job application.
     */
    public void loadSelf() {
        FileSystem.mapPut(FILENAME, this.getIdString(), this);
        HashMap data = FileSystem.read(FILENAME, this.getIdString());
        this.loadPrelimData(data);
        this.loadApplicant(data);
        this.loadPosting(data);
        this.loadInterviews(data);
    }

    /**
     * Load the preliminary data for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadPrelimData(HashMap data) {
        this.setCV((String) data.get("CV"));
        this.setCoverLetter((String) data.get("CoverLetter"));
        this.setStatus((int) data.get("Status"));
        this.setApplicationDate((LocalDate.parse((String) data.get("dateCreated"))));
    }

    /**
     * Load the application for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadApplicant(HashMap data) {
        this.setApplicant((Applicant) FileSystem.subLoader(Applicant.class, (String)
                ((ArrayList) data.get("Applicant")).get(0), (String)
                ((ArrayList) data.get("Applicant")).get(1)));
    }

    /**
     * Load the application for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadPosting(HashMap data) {
        this.jobPosting = ((JobPosting) FileSystem.subLoader(JobPosting.class, (String) ((ArrayList)
                data.get("JobPosting")).get(0), (String) ((ArrayList) data.get("JobPosting")).get(1)));
    }

    /**
     * Load the interviews for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadInterviews(HashMap data) {
        ArrayList<Interview> temp = new ArrayList();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("interviews")) {
            temp.add((Interview)FileSystem.subLoader(Interview.class, (String)x.get(0), (String)x.get(1)));
        }
        this.setInterviews(temp);
    }
}
