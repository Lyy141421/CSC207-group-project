import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Instance variables ===
    // The company that this HR Coordinator works for
    private Company company;
    // The filename under which this will be saved in the FileSystem
    static final String FILENAME = "HRCoordinators";

    // === Constructors ===

    HRCoordinator(String id){
        this.setUsername(id);
        loadSelf();
    }

    HRCoordinator(String username, String password, String legalName, String email, Company company,
                  LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
        this.setUserInterface(new HRCoordinatorInterface(this));
    }

    // === Getters ===

    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    void setCompany(Company company) {
        this.company = company;
    }

    // === Other methods ===

    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle       The job title.
     * @param jobDescription The job description.
     * @param requirements   The requirements for this job.
     * @param numPositions   The number of positions for this job.
     * @param postDate       The date this job posting was posted.
     * @param closeDate      The date this job posting is closed.
     */
    void addJobPosting(String jobTitle, String jobField, String jobDescription, String requirements,
                       int numPositions, LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new JobPosting(jobTitle, jobField, jobDescription, requirements,
                numPositions, this.company, postDate, closeDate);
        this.company.getJobPostingManager().addJobPosting(jobPosting);
    }

    /**
     * Get the task that the HR Coordinator must accomplish for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param today      Today's date.
     * @return an integer that represents the task that the HR Coordinator must accomplish for this job posting.
     */
    int getTask(JobPosting jobPosting, LocalDate today) {
        if (this.company.getJobPostingManager().getClosedJobPostingsNoInterview(today).contains(jobPosting)) {
            return InterviewManager.SELECT_APPS_FOR_PHONE_INTERVIEW;
        } else {
            return jobPosting.getInterviewManager().getHrTask(today);
        }
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString() {
        return this.getUsername();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", this.getPassword());
        data.put("legalName", this.getLegalName());
        data.put("email", this.getEmail());
        data.put("dateCreated", this.getDateCreated());
        data.put("company", new String[] {this.company.FILENAME,
                this.company.getName()});
        FileSystem.write(FILENAME, getIdString(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(FILENAME, getIdString());
        this.loadPrelimData(data);
        this.loadCompany(data);
    }

    /**
     * Load the preliminary data for this HR Coordinator.
     *
     * @param data The data for this HR Coordinator.
     */
    private void loadPrelimData(HashMap data) {
        this.setPassword((String) data.get("password"));
        this.setLegalName((String) data.get("legalName"));
        this.setEmail((String) data.get("email"));
        this.setDateCreated(LocalDate.parse((String) data.get("password")));
    }

    /**
     * Load this HR Coordinator's company.
     *
     * @param data The data for this HR Coordinator.
     */
    private void loadCompany(HashMap data) {
        this.setCompany((Company)FileSystem.subLoader(Company.class, (String) ((ArrayList) data.get("company")).get(0),
                (String) ((ArrayList) data.get("company")).get(1)));
    }
}
