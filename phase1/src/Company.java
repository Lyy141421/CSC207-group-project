import java.util.ArrayList;
import java.util.Collections;
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
    final String FILENAME = "Companies";

    // === Constructors ===

    /**
     * Create a new company.
     *
     * @param name The company's name.
     */
    Company(String name) {
        this.name = name;
        if(FileSystem.IDinMemory(FILENAME, name)){ loadSelf(); }
    }

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
            return this.name.equals(((Company) obj).name);
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
        ArrayList<ArrayList<String>> hrcoords = new ArrayList<>();
        for(HRCoordinator x : this.hrCoordinators){
            hrcoords.add(new ArrayList<>(){{add(x.FILENAME); add(x.getId());}});
        }
        data.put("hrCoordinators", hrcoords);
        HashMap<String, ArrayList<ArrayList<String>>> map = new HashMap<>();
        for(String field : this.fieldToInterviewers.keySet()){
            ArrayList<ArrayList<String>> interviewrs = new ArrayList<>();
            for(Interviewer y : this.fieldToInterviewers.get(field)){
                interviewrs.add(new ArrayList<>(){{add(y.FILENAME); add(y.getId());}});
            }
            map.put(field, interviewrs);
        }
        data.put("fields", map);
        ArrayList<ArrayList<String>> jobpostings = new ArrayList<>();
        for(JobPosting x : this.jobPostingManager.getJobPostings()){
            jobpostings.add(new ArrayList<>(){{add(x.FILENAME); add(x.getId());}});
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
        ArrayList<HRCoordinator> hrcords = new ArrayList<>();
        for(Object x : (ArrayList)data.get("hrCoordinators")){
            if(FileSystem.isLoaded((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1))){
                hrcords.add((HRCoordinator) FileSystem.mapGet((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
            else{
                hrcords.add(new HRCoordinator((String)((ArrayList)x).get(1)));
            }
        }
        this.hrCoordinators = hrcords;
        HashMap<String, ArrayList<Interviewer>> fieldmap = new HashMap<>();
        for(String fields : ((HashMap<String, ArrayList<ArrayList<String>>>)data.get("fields")).keySet()){
            ArrayList<Interviewer> interviewers = new ArrayList<>();
            for(Object y : (ArrayList)((HashMap)data.get("fields")).get(fields)){
                if(FileSystem.isLoaded((String)((ArrayList)y).get(0), (String)((ArrayList)y).get(1))){
                    interviewers.add((Interviewer) FileSystem.mapGet((String)((ArrayList)y).get(0), (String)((ArrayList)y).get(1)));
                }
                else{
                    interviewers.add(new Interviewer((String)((ArrayList)y).get(1)));
                }
            }
            fieldmap.put(fields, interviewers);
        }
        this.fieldToInterviewers = fieldmap;
        ArrayList<JobPosting> jobpostings = new ArrayList<>();
        for(Object x : (ArrayList)data.get("jobpostings")){
            if(FileSystem.isLoaded((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1))){
                jobpostings.add((JobPosting) FileSystem.mapGet((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
            else{
                jobpostings.add(new JobPosting((String)((ArrayList)x).get(1)));
            }
        }
        this.jobPostingManager = new JobPostingManager(jobpostings, this);
    }
}
