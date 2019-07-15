package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import DocumentManagers.CompanyDocumentManager;
import DocumentManagers.DocumentManager;
import DocumentManagers.DocumentManagerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Company implements Serializable {

    // === Instance variables ===
    // The name of this company (unique identifier)
    private String name;
    // The HR Coordinators for this company
    private ArrayList<HRCoordinator> hrCoordinators;
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers;
    // The job posting manager for this company
    private JobPostingManager jobPostingManager;
    // The document manager for this company
    private CompanyDocumentManager documentManager;

    // === Public methods ===
    // === Constructors ===
    public Company(String name) {
        this.name = name;
        this.hrCoordinators = new ArrayList<>();
        this.fieldToInterviewers = new HashMap<>();
        this.jobPostingManager = new JobPostingManager(this);
        this.documentManager = (CompanyDocumentManager) new DocumentManagerFactory().createDocumentManager(this);
    }

    // === Getters ==
    public String getId() {
        return getName();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    public JobPostingManager getJobPostingManager() {
        return this.jobPostingManager;
    }

    public CompanyDocumentManager getDocumentManager() {
        return this.documentManager;
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
     * Adds the HR Coordinator to the company.
     *
     * @param hrCoordinator The HR Coordinator to be added.
     */
    public void addHRCoordinator(HRCoordinator hrCoordinator) {
        this.hrCoordinators.add(hrCoordinator);
    }

    /**
     * Check whether this job field has interviewers.
     *
     * @param field The field in question.
     * @return true iff this company has an interviewer or this job field.
     */
    public boolean hasInterviewerForField(String field) {
        return this.getFieldToInterviewers().keySet().contains(field);
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    public void addInterviewer(Interviewer interviewer) {
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
    public ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostingManager().getJobPostings()) {
            for (JobApplication jobApp : jobPosting.getJobApplications()) {
                if (jobApp.getApplicant().equals(applicant)) {
                    apps.add(jobApp);
                }
            }
        }
        return apps;
    }

    /**
     * Check whether this applicant has applied to this company.
     *
     * @param applicant The applicant in question.
     * @return true iff this applicant has applied to this company.
     */
    public boolean hasAppliedHere(Applicant applicant) {
        for (JobPosting jobPosting : this.getJobPostingManager().getJobPostings()) {
            if (applicant.hasAppliedTo(jobPosting)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Company)) {
            return false;
        } else {
            return this.name.equalsIgnoreCase(((Company) obj).name);
        }
    }

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
    // === Other methods ===

    /**
     * Find the interviewer with the least amount of interviews in this job field.
     *
     * @param jobField The job field of the interviewer to be found.
     * @return the interviewer with the least amount of interviews in this field.
     */
    Interviewer findInterviewerByField(String jobField) {
        Interviewer interviewerSoFar = this.fieldToInterviewers.get(jobField).get(0);
        int minNumberOfInterviews = interviewerSoFar.getInterviews().size();
        for (Interviewer interviewer : this.fieldToInterviewers.get(jobField)) {
            int numberOfInterviews = interviewer.getInterviews().size();
            if (numberOfInterviews <= minNumberOfInterviews) {
                interviewerSoFar = interviewer;
                minNumberOfInterviews = numberOfInterviews;
            }
        }
        return interviewerSoFar;
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param round The interview round.
     */
    // TODO Interview factory?
    public void setUpInterview(JobApplication jobApplication, int round) {
        JobPosting jobPosting = jobApplication.getJobPosting();
        Interviewer interviewer = this.findInterviewerByField(jobPosting.getField());
        Interview interview = new Interview(jobApplication, jobPosting.getInterviewManager());
        // TODO make this action observable and notify jobApplication and interviewer
        jobApplication.addInterview(interview);
        interviewer.addInterview(interview);
        jobApplication.getStatus().advanceStatus();
    }
}
