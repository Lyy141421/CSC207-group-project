import java.util.ArrayList;
import java.util.HashMap;

class Company {

    // === Instance variables ===
    // The name of this company (unique identifier)
    private String name;
    // The HR Coordinators for this company
    private ArrayList<HRCoordinator> hrCoordinators = new ArrayList<>();
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers = new HashMap<>();


    // === Constructors ===

    /**
     * Create a new company.
     *
     * @param name The company's name.
     */
    Company(String name) {
        this.name = name;
    }

    /**
     * Create a company -- from fileLoader.
     * @param name                  The company name.
     * @param hrCoordinators        The list of HR Coordinators for this
     * @param fieldToInterviewers   The map of field to interviewers for this company.
     */
    Company(String name, ArrayList<HRCoordinator> hrCoordinators,
            HashMap<String, ArrayList<Interviewer>> fieldToInterviewers) {
        this.name = name;
        this.hrCoordinators = hrCoordinators;
        this.fieldToInterviewers = fieldToInterviewers;
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
     * Get all the job postings for this company.
     *
     * @return the list of all job postings for this company.
     */
    ArrayList<JobPosting> getAllJobPostings() {
        ArrayList<JobPosting> allJobPostings = new ArrayList<>();
        for (HRCoordinator HRC : this.hrCoordinators) {
            allJobPostings.addAll(HRC.getAllJobPostings());
        }
        return allJobPostings;
    }

    /**
     * Search the job posting by title within this company.
     *
     * @param jobTitle The job title.
     * @return the job posting with this job title.
     */
    JobPosting searchJobPostingByTitle(String jobTitle) {
        for (HRCoordinator HRC : this.hrCoordinators) {
            for (JobPosting jobPosting : HRC.getAllJobPostings()) {
                if (jobPosting.getTitle().equals(jobTitle)) {
                    return jobPosting;
                }
            }

        }
        return null;
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    ArrayList<JobApplication> viewAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (JobApplication jobApp : applicant.getJobApplicationManager().getJobApplications()) {
            if (jobApp.getJobPosting().getCompany().equals(this)) {
                apps.add(jobApp);
            }
        }
        return apps;
    }


}
