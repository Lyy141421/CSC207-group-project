import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class JobPosting implements Storable{

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
    private ArrayList<JobApplication> jobApplications; // The list of job applications for this job posting
    private InterviewManager interviewManager; // Interview manager for this job posting
    // The filename under which this will be saved in the FileSystem
    public final String FILENAME = "JobPostings";

    // === Constructors ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    public JobPosting(String id){
        this.id = Integer.parseInt(id);
        JobPosting.postingsCreated = Integer.max(this.id, JobPosting.postingsCreated);
        loadSelf();
    }

    JobPosting() {
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

    int getID() {
        return this.id;
    }

    String getTitle() {
        return this.title;
    }

    String getField() {
        return this.field;
    }

    String getDescription() {
        return this.description;
    }

    String getRequirements() {
        return this.requirements;
    }


    int getNumPositions() {
        return this.numPositions;
    }

    Company getCompany() {
        return this.company;
    }

    LocalDate getPostDate() {
        return this.postDate;
    }

    LocalDate getCloseDate() {
        return this.closeDate;
    }

    ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    boolean isFilled() {
        return this.filled;
    }

    InterviewManager getInterviewManager () {
        return this.interviewManager;
    }


    // == Setters ===
    void setFilled() {
        this.filled = true;
    }

    void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }

    void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    // === Other methods ===

    /**
     * Add this job application for this job posting.
     *
     * @param jobApplication The job application to be added.
     */
    void addJobApplication(JobApplication jobApplication) {
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
    JobApplication findJobApplication(Applicant applicant) {
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
    void reviewApplications() {
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
        }
        else {
            return Integer.toString(this.id).equals(((JobPosting) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getId(){
        return Integer.toString(this.id);
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.getTitle());
        data.put("field", this.getField());
        data.put("description", this.description);
        data.put("requirements", this.requirements);
        data.put("numPositions", this.getNumPositions());
        data.put("postDate", this.postDate);
        data.put("closeDate", this.closeDate);
        data.put("filled", this.filled);
        ArrayList jobapplications = new ArrayList();
        for(JobApplication x : this.jobApplications){
            jobapplications.add(new ArrayList(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("jobapplications", jobapplications);
        ArrayList applicationsInConsideration = new ArrayList();
        for(JobApplication x : this.interviewManager.getApplicationsInConsideration()){
            applicationsInConsideration.add(new ArrayList(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("applicationsInConsideration", applicationsInConsideration);
        ArrayList applicationsRejected = new ArrayList();
        for(JobApplication x : this.interviewManager.getApplicationsRejected()){
            applicationsRejected.add(new ArrayList(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("applicationsRejected", applicationsRejected);
        data.put("currentRound", this.interviewManager.getCurrentRound());
        // todo HRTask???
        FileSystem.write(FILENAME, getId(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        this.title = (String)data.get("title");
        this.field = (String)data.get("field");
        this.description = (String)data.get("description");
        this.requirements = (String)data.get("requirements");
        this.numPositions = (int)data.get("numPositions");
        this.postDate = LocalDate.parse((String)data.get("postDate"));
        this.closeDate = LocalDate.parse((String)data.get("closeDate"));
        this.filled = (boolean)data.get("filled");
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for(ArrayList x : (ArrayList<ArrayList>)data.get("jobapplications")){
            if(FileSystem.isLoaded((String)(x.get(0)), (String)(x.get(1)))){
                jobApplications.add((JobApplication) FileSystem.mapGet((String)(x.get(0)), (String)(x.get(1))));
            }
            else{
                jobApplications.add(new JobApplication((String)(x.get(1))));
            }
        }
        this.jobApplications = jobApplications;
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        for(ArrayList x : (ArrayList<ArrayList>)data.get("applicationsInConsideration")){
            if(FileSystem.isLoaded((String)(x.get(0)), (String)(x.get(1)))){
                applicationsInConsideration.add((JobApplication) FileSystem.mapGet((String)(x.get(0)), (String)(x.get(1))));
            }
            else{
                applicationsInConsideration.add(new JobApplication((String)(x.get(1))));
            }
        }
        ArrayList<JobApplication> applicationsRejected = new ArrayList<>();
        for(ArrayList x : (ArrayList<ArrayList>)data.get("applicationsRejected")){
            if(FileSystem.isLoaded((String)(x.get(0)), (String)(x.get(1)))){
                applicationsRejected.add((JobApplication) FileSystem.mapGet((String)(x.get(0)), (String)(x.get(1))));
            }
            else{
                applicationsRejected.add(new JobApplication((String)(x.get(1))));
            }
        }
        this.interviewManager = new InterviewManager(this, applicationsInConsideration, applicationsRejected, (int)data.get("currentRound"));
    }
}
