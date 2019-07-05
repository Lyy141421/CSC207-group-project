package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;
import FileLoadingAndStoring.Subable;
import Managers.JobPostingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Company implements Storable {

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "Companies";

    // === Instance variables ===
    // The name of this company (unique identifier)
    private String name;
    // The HR Coordinators for this company
    private ArrayList<HRCoordinator> hrCoordinators = new ArrayList<>();
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers = new HashMap<>();
    // The job posting manager for this company
    private JobPostingManager jobPostingManager = new JobPostingManager(this);


    // === Public methods ===
    // === Constructors ===

    public Company(String name) {
        this.name = name;
    }

    // === Getters ==

    public String getId(){ return getName(); }

    public String getName() {
        return this.name;
    }

    public ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    public HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    public JobPostingManager getJobPostingManager() {
        return this.jobPostingManager;
    }

    // === Setters ===

    public void setHrCoordinators(ArrayList<HRCoordinator> hrCoordinators) {
        this.hrCoordinators = hrCoordinators;
    }

    public void setFieldToInterviewers(HashMap<String, ArrayList<Interviewer>> fieldToInterviewers) {
        this.fieldToInterviewers = fieldToInterviewers;
    }

    public void setJobPostingManager(JobPostingManager jobPostingManager) {
        this.jobPostingManager = jobPostingManager;
    }

    // === Other methods ===

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    public ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (JobApplication jobApp : applicant.getJobApplicationManager().getJobApplications()) {
            if (jobApp.getJobPosting().getCompany().equals(this)) {
                apps.add(jobApp);
            }
        }
        return apps;
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

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Constructors ===
    Company(String name, ArrayList<HRCoordinator> hrCoordinators,
            HashMap<String, ArrayList<Interviewer>> fieldToInterviewers, JobPostingManager jobPostingManager) {
        this.name = name;
        this.hrCoordinators = hrCoordinators;
        this.fieldToInterviewers = fieldToInterviewers;
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

}
