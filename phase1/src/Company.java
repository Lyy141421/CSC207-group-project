import java.util.ArrayList;
import java.util.HashMap;

class Company implements Storable{

    // === Instance variables ===
    // The name of this company (unique identifier)
    private String name;
    // The HR Coordinators for this company
    private ArrayList<HRCoordinator> hrCoordinators = new ArrayList<>();
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers = new HashMap<>();
    // The job posting manager for this company
    private JobPostingManager jobPostingManager = new JobPostingManager(this);
    // The filename under which this will be saved in the FileSystem
    public final String FILENAME = "Applicants";

    // === Constructors ===

    /**
     * Create a new company.
     *
     * @param name The company's name.
     */
    Company(String name) {
        this.name = name;
    } //todo make storable

    /**
     * Create a company -- from fileLoader.
     * @param name                  The company name.
     * @param hrCoordinators        The list of HR Coordinators for this
     * @param fieldToInterviewers   The map of field to interviewers for this company.
     */
    Company(String name, ArrayList<HRCoordinator> hrCoordinators,
            HashMap<String, ArrayList<Interviewer>> fieldToInterviewers, JobPostingManager jobPostingManager) {
        this.name = name;
        this.hrCoordinators = hrCoordinators;
        this.fieldToInterviewers = fieldToInterviewers;
        this.jobPostingManager = jobPostingManager;
    }

    // === Getters ==

    /**
     * Get the name of this company.
     *
     * @return the name of this company.
     */
    String getName() {
        return this.name;
    }

    /**
     * Get a list of all HRCoordinators for this company.
     *
     * @return a list of all HRCoordinators for this company.
     */
    ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    /**
     * Get the map of field to interviewers for this company.
     * @return the map of field to interviewers for this company.
     */
    HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    /**
     * Get the job posting manager for this company.
     *
     * @return the job posting manager for this company.
     */
    JobPostingManager getJobPostingManager() {
        return this.jobPostingManager;
    }


    // === Setters ===

    /**
     * Set the job posting manager for this company.
     *
     * @param jobPostingManager The job posting manager to be set.
     */
    void setJobPostingManager(JobPostingManager jobPostingManager) {
        this.jobPostingManager = jobPostingManager;
    }

    // === Other methods ===

    /**
     * Find the interviewer with the least amount of interviews in this job field.
     * @param jobField  The job field of the interviewer to be found.
     * @return the interviewer with the least amount of interviews in this field.
     */
    Interviewer findInterviewer(String jobField) {
        int minNumberOfInterviews = 0;
        Interviewer interviewerSoFar = null;
        for (Interviewer interviewer : this.fieldToInterviewers.get(jobField)) {
            int numberOfInterviews = interviewer.getInterviews().size();
            if (numberOfInterviews <= minNumberOfInterviews) {
                interviewerSoFar = interviewer;
            }
        }
        return interviewerSoFar;
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    void addInterviewer(Interviewer interviewer) {
        String field = interviewer.getField();
        if (!this.fieldToInterviewers.containsKey(field)) {
            this.fieldToInterviewers.put(field, new ArrayList<>());
        }
        this.fieldToInterviewers.get(field).add(interviewer);
    }

    /**
     * Report whether this company is the same as other.
     * @param other     The company to be compared with.
     * @return true iff this company is the same as other.
     */
    boolean equals(Company other) {
        return this.name.equals(other.getName());
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (JobApplication jobApp : applicant.getJobApplicationManager().getJobApplications()) {
            if (jobApp.getJobPosting().getCompany().equals(this)) {
                apps.add(jobApp);
            }
        }
        return apps;
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getId(){
        return this.getName();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        //A - Hrcords, H<String, A<Interviewrs>>, JobPotMan
        ArrayList<ArrayList> hrcords = new ArrayList<>();
        for(HRCoordinator x : this.hrCoordinators){
            hrcords.add(new ArrayList(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("hrCoordinators", hrcords);
        HashMap t = new HashMap();
        for(String x : this.fieldToInterviewers.keySet()){
            ArrayList<ArrayList<ArrayList>> field = new ArrayList<>();
            ArrayList<ArrayList> Interviewrs = new ArrayList<>();
            for(Interviewer y : this.fieldToInterviewers.get(x)){
                Interviewrs.add(new ArrayList(){{add(y.FILENAME); add(y.getId());}});
                field.add(Interviewrs);
            }
            t.put(x, field);
        }
        data.put("fields", t);
        ArrayList<ArrayList> jobpostings = new ArrayList();
        for(JobPosting x : this.jobPostingManager.getJobPostings()){
            jobpostings.add(new ArrayList(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("jobpostings", jobpostings);
        FileSystem.write(FILENAME, getId(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        ArrayList<HRCoordinator> hrcords = new ArrayList();
        for(Object x : (ArrayList)data.get("hrCoordinators")){
            if(FileSystem.isLoaded((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1))){
                hrcords.add((HRCoordinator) FileSystem.mapGet((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
            else{
                hrcords.add(new HRCoordinator((String)((ArrayList)x).get(1)));
            }
        }
        this.hrCoordinators = hrcords;
        HashMap field = new HashMap();
        for(Object x : ((HashMap)data.get("fields")).keySet()){
            ArrayList<Interviewer> interviewers = new ArrayList();
            for(Object y : (ArrayList)((HashMap)data.get("fields")).get(x)){
                if(FileSystem.isLoaded((String)((ArrayList)y).get(0), (String)((ArrayList)y).get(1))){
                    interviewers.add((Interviewer) FileSystem.mapGet((String)((ArrayList)y).get(0), (String)((ArrayList)y).get(1)));
                }
                else{
                    interviewers.add(new Interviewer((String)((ArrayList)y).get(1)));
                }
            }
            field.put(x, interviewers);
        }
        this.fieldToInterviewers = field;
        ArrayList<JobPosting> jobpostings = new ArrayList<>();
        for(Object x : (ArrayList)data.get("jobpostings")){
            if(FileSystem.isLoaded((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1))){
                jobpostings.add((JobPosting) FileSystem.mapGet((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
            else{
                jobpostings.add(new JobPosting((String)((ArrayList)x).get(1)));
            }
        }
    }
}
