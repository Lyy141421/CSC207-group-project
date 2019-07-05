package UsersAndJobObjects;

import FileLoadingAndStoring.FileSystem;
import FileLoadingAndStoring.Storable;
import Managers.InterviewManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class JobPosting implements Storable {

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "JobPostings";


    // === Instance variables ===
    private static int postingsCreated; // Total number of postings created
    private int id; // Unique identifier for this job posting
    private String title; // The job title
    private String field; // The job field
    private String description; // The job description
    private String requirements; // The requirements for this job posting
    private int numPositions;
    private Company company; // The company that listed this job posting
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private boolean filled; // Whether the job posting is filled
    private ArrayList<JobApplication> jobApplications = new ArrayList<>(); // The list of job applications for this job posting
    private InterviewManager interviewManager; // UsersAndJobObjects.Interview manager for this job posting

    // === Constructors ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    public JobPosting(String id){
        this.id = Integer.parseInt(id);
        JobPosting.postingsCreated = Integer.max(this.id, JobPosting.postingsCreated);
    }

    JobPosting(String title, String field, String description, String requirements, int numPositions, Company company,
               LocalDate postDate, LocalDate closeDate) {
        postingsCreated++;
        this.id = postingsCreated;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requirements = requirements;
        this.numPositions = numPositions;
        this.company = company;
        this.filled = false;
        this.postDate = postDate;
        this.closeDate = closeDate;
    }

    // === Getters ===

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getField() {
        return this.field;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public int getNumPositions() {
        return this.numPositions;
    }

    public Company getCompany() {
        return this.company;
    }

    public LocalDate getPostDate() {
        return this.postDate;
    }

    public LocalDate getCloseDate() {
        return this.closeDate;
    }

    public ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public InterviewManager getInterviewManager () {
        return this.interviewManager;
    }


    // == Setters ===
    void setTitle(String title) {
        this.title = title;
    }

    void setField(String field) {
        this.field = field;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }

    void setCompany(Company company) {
        this.company = company;
    }

    void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public void setFilled() {
        this.filled = true;
    }

    void setJobApplications(ArrayList<JobApplication> jobApps) {
        this.jobApplications = jobApps;
    }

    public void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    // === Other methods ===

    /**
     * Add this job application for this job posting.
     *
     * @param jobApplication The job application to be added.
     */
    public void addJobApplication(JobApplication jobApplication) {
        this.jobApplications.add(jobApplication);
    }

    /**
     * Remove this job application for this job posting.
     *
     * @param jobApplication The job application to be removed.
     */
    void removeJobApplication(JobApplication jobApplication) {
        this.jobApplications.remove(jobApplication);
    }

    /**
     * Find the job application associated with this applicant.
     * @param applicant The applicant whose application is to be searched for.
     * @return the application of this applicant or null if not found.
     */
    public JobApplication findJobApplication(Applicant applicant) {
        for (JobApplication jobApp : this.jobApplications) {
            if (jobApp.getApplicant().equals(applicant)) {
                return jobApp;
            }
        }
        return null;
    }

    /**
     * Review the applications submitted for this job posting.
     */
    public void reviewApplications() {
        for (JobApplication jobApp : this.getJobApplications()) {
            jobApp.advanceStatus();
        }
    }

    /**
     * Advance the round of interviews for this job posting.
     *
     * @param today Today's date.
     */
    void advanceInterviewRound(LocalDate today) {
        InterviewManager interviewManager = this.getInterviewManager();
        if (interviewManager.getCurrentRound() < Interview.getMaxNumRounds()) {
            if (interviewManager.isCurrentRoundOver(today)) {
                interviewManager.advanceRound();
            }
        }
    }

    /**
     * Get an array of a list of job applications selected and a list of job applications rejected for a phone
     * interview for this job posting.
     *
     * @return an array of lists of job applications selected and rejected for a phone interview.
     */
    private ArrayList<JobApplication>[] getApplicationsForPhoneInterview() {
        ArrayList<JobApplication>[] jobApps = new ArrayList[2];
        ArrayList<JobApplication> jobApplicationsInConsideration = new ArrayList<>();
        ArrayList<JobApplication> jobApplicationsRejected = new ArrayList<>();
        jobApps[0] = jobApplicationsInConsideration;
        jobApps[1] = jobApplicationsRejected;
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (jobApplication.getStatus() == 0) {
                jobApplicationsInConsideration.add(jobApplication);
            } else {
                jobApplicationsRejected.add(jobApplication);
            }
        }
        return jobApps;
    }

    /**
     * Create an interview manager for this job posting.
     */
    void createInterviewManager() {
        ArrayList<JobApplication>[] jobApps = this.getApplicationsForPhoneInterview();
        InterviewManager interviewManager = new InterviewManager(this, jobApps[0], jobApps[1]);
        this.setInterviewManager(interviewManager);
    }

    /**
     * Get the current round of interviews for this job posting.
     *
     * @return the current round of interviews for this job posting.
     */
    int getCurrentInterviewRound() {
        return this.interviewManager.getCurrentRound();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobPosting)) {
            return false;
        } else {
            return Integer.toString(this.id).equals(((JobPosting) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Get a string representation of this job posting.
     *
     * @return a string representation of this job posting.
     */
    @Override
    public String toString() {
        String s = "Job ID: " + this.getId() + "\n";
        s += "Title: " + this.getTitle() + "\n";
        s += "Field: " + this.getField() + "\n";
        s += "Description: " + this.getDescription() + "\n";
        s += "Requirements: " + this.getRequirements() + "\n";
        s += "Number of positions: " + this.getNumPositions() + "\n";
        s += "UsersAndJobObjects.Company: " + this.getCompany().getName() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Post date: " + this.getPostDate().format(dtf) + "\n";
        s += "Close date: " + this.getCloseDate().format(dtf) + "\n";
        s += "Filled: " + this.isFilled() + "\n";
        return s;
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString() {
        return Integer.toString(this.id);
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.getTitle());
        data.put("field", this.getField());
        data.put("description", this.description);
        data.put("requirements", this.requirements);
        data.put("numPositions", this.getNumPositions());
        data.put("postDate", this.postDate);
        data.put("closeDate", this.closeDate);
        data.put("filled", this.filled);
        Company c = this.company;
        data.put("UsersAndJobObjects.Company", new ArrayList(){{add(Company.FILENAME); add(c.getIdString());}});
        ArrayList jobapplications = new ArrayList();
        for(JobApplication x : this.jobApplications){
            jobapplications.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("jobapplications", jobapplications);
        ArrayList applicationsInConsideration = new ArrayList();
        for(JobApplication x : this.interviewManager.getApplicationsInConsideration()){
            applicationsInConsideration.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("applicationsInConsideration", applicationsInConsideration);
        ArrayList applicationsRejected = new ArrayList();
        for(JobApplication x : this.interviewManager.getApplicationsRejected()){
            applicationsRejected.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("applicationsRejected", applicationsRejected);
        data.put("currentRound", this.interviewManager.getCurrentRound());
        FileSystem.write(JobPosting.FILENAME, getIdString(), data);
    }

    /**
     * Loads the job posting.
     */
    public void loadSelf(){
        FileSystem.mapPut(JobPosting.FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(JobPosting.FILENAME, getIdString());
        this.loadPrelimData(data);
        this.loadJobApps(data);
        ArrayList<JobApplication> appsInConsideration = this.loadAppsInConsideration(data);
        ArrayList<JobApplication> appsRejected = this.loadAppsRejected(data);
        this.interviewManager = new InterviewManager(this, appsInConsideration, appsRejected,
                (int) data.get("currentRound"));
        this.company = (Company) FileSystem.subLoader(Company.class, (String)((ArrayList)data.get("UsersAndJobObjects.Company")).get(0),
                (String)((ArrayList)data.get("UsersAndJobObjects.Company")).get(1));
    }

    /**
     * Load the preliminary data for this job posting.
     *
     * @param data The data for this job posting.
     */
    private void loadPrelimData(HashMap data) {
        this.setTitle((String) data.get("title"));
        this.setField((String) data.get("field"));
        this.setDescription((String) data.get("description"));
        this.setRequirements((String) data.get("requirements"));
        this.setNumPositions((int) data.get("numPositions"));
        this.setPostDate(LocalDate.parse((String) data.get("postDate")));
        this.setCloseDate(LocalDate.parse((String) data.get("closeDate")));
        if ((boolean) data.get("filled")) {
            this.setFilled();
        }
    }

    /**
     * Load the job applications for this job posting.
     *
     * @param data The data for this job posting.
     */
    private void loadJobApps(HashMap data) {
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("jobapplications")) {
            jobApplications.add((JobApplication) FileSystem.subLoader(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        this.setJobApplications(jobApplications);
    }

    /**
     * Load the applications in consideration for this job posting.
     *
     * @param data The data for this job posting.
     * @return a list of job applications in consideration.
     */
    private ArrayList<JobApplication> loadAppsInConsideration(HashMap data) {
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("applicationsInConsideration")) {
            applicationsInConsideration.add((JobApplication) FileSystem.subLoader(JobApplication.class,
                    (String) x.get(0), (String) x.get(1)));
        }
        return applicationsInConsideration;
    }

    /**
     * Load the applications rejected for this job posting.
     *
     * @param data The data for this job posting.
     * @return a list of job applications rejected for this job posting.
     */
    private ArrayList<JobApplication> loadAppsRejected(HashMap data) {
        ArrayList<JobApplication> applicationsRejected = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("applicationsRejected")) {
            applicationsRejected.add((JobApplication) FileSystem.subLoader(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        return applicationsRejected;
    }
}
