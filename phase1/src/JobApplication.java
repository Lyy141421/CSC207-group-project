import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;//test change

class JobApplication implements Storable {
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // Map of status number to description
    private static HashMap<Integer, String> statuses = new HashMap<Integer, String>() {{
        put(-3, "Archived");
        put(-2, "Submitted");
        put(-1, "Under review");
        put(0, "Phone interview");
        put(1, "In-person interview 1");
        put(2, "In-person interview 2");
        put(3, "In-person interview 3");
        put(4, "Hired");
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
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getId() {
        return Integer.toString(this.ID);
    }

    /**
     * Saves the Object
     */
    public void saveSelf() {
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("CV", this.getCV());
        data.put("CoverLetter", this.getCoverLetter());
        data.put("Status", this.getStatus());
        data.put("ApplicationDate", this.getApplicationDate());
        data.put("Applicant", new ArrayList() {{
            add(getApplicant().FILENAME);
            add(getApplicant().getId());
        }});
        data.put("JobPosting", new ArrayList() {{
            add(getJobPosting().FILENAME);
            add(getJobPosting().getId());
        }});
        ArrayList interviews = new ArrayList();
        for (Interview x : this.interviews) {
            interviews.add(new ArrayList() {{
                add(x.FILENAME);
                add(x.getId());
            }});
        }
        data.put("interviews", interviews);
        FileSystem.write(FILENAME, getId(), data);
    }

    /**
     * Load this job application.
     */
    public void loadSelf() {
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        this.loadPrelimData(data);
        this.loadApplicant(data);
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
        this.setApplicant((Applicant) FileSystem.subLoader(Applicant.class, (String) ((ArrayList) data.get("Applicant")).get(0),
                    (String) ((ArrayList) data.get("Applicant")).get(1)));
    }

    /**
     * Load the application for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadPosting(HashMap data) {
        this.jobPosting = ((JobPosting) FileSystem.subLoader(JobPosting.class, (String) ((ArrayList) data.get("JobPosting")).get(0),
                    (String) ((ArrayList) data.get("JobPosting")).get(1)));
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
