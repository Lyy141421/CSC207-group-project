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
    static final String FILENAME = "Companies";

    // === Constructors ===

    public Company(String name) {
        this.name = name;
        if(FileSystem.IDinMemory(FILENAME, name)){ loadSelf(); }
    }

    Company(String name, ArrayList<HRCoordinator> hrCoordinators,
            HashMap<String, ArrayList<Interviewer>> fieldToInterviewers, JobPostingManager jobPostingManager) {
        this.name = name;
        this.hrCoordinators = hrCoordinators;
        this.fieldToInterviewers = fieldToInterviewers;
        this.jobPostingManager = jobPostingManager;
    }

    // === Getters ==

    String getName() {
        return this.name;
    }

    ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    JobPostingManager getJobPostingManager() {
        return this.jobPostingManager;
    }


    // === Setters ===

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
    public String getIdString() {
        return this.getName();
    }

    /**
     * Report whether this company is the same as obj.
     *
     * @param obj     The object to be compared with.
     * @return true iff obj is a company and has the same name as this company.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Company)) {
            return false;
        }
        else {
            return this.name.equalsIgnoreCase(((Company) obj).name);
        }
    }

    /**
     * Return a hashcode for this company.
     * @return an int; the same int should be returned for all companies equal to this company.
     */
    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < name.length(); i++) {
            sum += name.charAt(i);
        }
        return sum;
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(Company.FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        ArrayList<ArrayList<String>> hrcoords = new ArrayList<>();
        for(HRCoordinator x : this.hrCoordinators){
            hrcoords.add(new ArrayList<String>() {{
                add(HRCoordinator.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("hrCoordinators", hrcoords);
        HashMap<String, ArrayList<ArrayList<String>>> map = new HashMap<>();
        for(String field : this.fieldToInterviewers.keySet()){
            ArrayList<ArrayList<String>> interviewers = new ArrayList<>();
            for(Interviewer y : this.fieldToInterviewers.get(field)){
                interviewers.add(new ArrayList<String>() {{
                    add(Interviewer.FILENAME);
                    add(y.getIdString());
                }});
            }
            map.put(field, interviewers);
        }
        data.put("fields", map);
        ArrayList<ArrayList<String>> jobpostings = new ArrayList<>();
        for(JobPosting x : this.jobPostingManager.getJobPostings()){
            jobpostings.add(new ArrayList<String>() {{
                add(JobPosting.FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("jobpostings", jobpostings);
        FileSystem.write(Company.FILENAME, getIdString(), data);
    }

    /**
     * Load the preliminary data for this applicant.
     *
     * @param data The Company's Data
     */
    private void loadPrelimData(HashMap data) {

    }

    /**
     * Loads the HRCoordinators from memory
     *
     * @param data The Company's Data
     */
    private void loadHRCoordinators(HashMap data){
        ArrayList<HRCoordinator> hrcords = new ArrayList<>();
        for(Object x : (ArrayList)data.get("hrCoordinators")){
            hrcords.add((HRCoordinator) FileSystem.subLoader(HRCoordinator.class, (String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
        }
        this.hrCoordinators = hrcords;
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param data The Company's Data
     */
    private void loadFieldMap(HashMap data){
        HashMap<String, ArrayList<Interviewer>> fieldmap = new HashMap<>();
        for(String fields : ((HashMap<String, ArrayList<ArrayList<String>>>)data.get("fields")).keySet()){
            ArrayList<Interviewer> interviewers = new ArrayList<>();
            for(Object y : (ArrayList)((HashMap)data.get("fields")).get(fields)){
                interviewers.add((Interviewer) FileSystem.subLoader(Interview.class, (String)((ArrayList)y).get(0), (String)((ArrayList)y).get(1)));
            }
            fieldmap.put(fields, interviewers);
        }
        this.fieldToInterviewers = fieldmap;
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param data The Company's Data
     */
    private void loadJobPostingManager(HashMap data){
        ArrayList<JobPosting> jobpostings = new ArrayList<>();
        for(Object x : (ArrayList)data.get("jobpostings")){
                jobpostings.add((JobPosting) FileSystem.subLoader(JobPosting.class, (String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
        this.jobPostingManager = new JobPostingManager(jobpostings, this);
    }

    /**
     * Load this Company.
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(FILENAME, getIdString());
        this.loadHRCoordinators(data);
        this.loadFieldMap(data);
        this.loadJobPostingManager(data);

    }
}
