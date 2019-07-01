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
    final String FILENAME = "HRCoordinators";

    // === Constructors ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    HRCoordinator(String id){
        this.setUsername(id);
        loadSelf();
    }

    /**
     * Create an HR Coordinator account.
     */
    HRCoordinator() {
    }

    /**
     * Create an HR Coordinator account.
     *
     * @param username    The HR Coordinator's account username.
     * @param password    The HR Coordinator's account password.
     * @param legalName   The HR Coordinator's legal name.
     * @param email       The HR Coordinator's email.
     * @param company     The company that this HR Coordinator works for.
     * @param dateCreated The date this account was created.
     */
    HRCoordinator(String username, String password, String legalName, String email, Company company,
                  LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
    }

    // === Getters ===

    /**
     * Get the company that this HR Coordinator works for.
     *
     * @return the company that this HR Coordinator works for.
     */
    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    /**
     * Set the company that this HR Coordinator works for.
     */
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
    public String getId(){
        return this.getUsername();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", this.getPassword());
        data.put("legalName", this.getLegalName());
        data.put("email", this.getEmail());
        data.put("dateCreated", this.getDateCreated());
        data.put("company", new String[] {this.company.FILENAME,
                this.company.getName()});
        FileSystem.write(FILENAME, getId(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        this.setPassword((String)data.get("password"));
        this.setLegalName((String)data.get("legalName"));
        this.setEmail((String)data.get("email"));
        this.setDateCreated(LocalDate.parse((String)data.get("password")));
        if(FileSystem.isLoaded((String)((ArrayList)data.get("company")).get(1),
                (String)((ArrayList)data.get("company")).get(1))){
            this.company = (Company) FileSystem.mapGet((String)((ArrayList)data.get("company")).get(1),
                    (String)((ArrayList)data.get("company")).get(1));
        }
        else{
            this.company = new Company((String)((ArrayList)data.get("company")).get(1));
        }
    }

}
