import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class ApplicantAccount extends UserAccount {

    // === Instance variables ===
    // List of documents that this applicant has put into their account
    private ArrayList<File> documents;
    // List of jobs applied for in the past
    private ArrayList<JobPosting> previousJobsApplied;
    // A map of current jobs applying for and their respective statuses TODO
    private HashMap<JobPosting, ApplicationStatus> currentJobsApplying;
    // Date that last application closed
    private LocalDate dateOfLastClosing;

    // === Constructors ===

    /**
     * Create an applicant account.
     *
     * @param legalName   The applicant's legal name.
     * @param username    The username associated with this account.
     * @param password    The password associated with this account.
     * @param dateCreated The date this account was created.
     */
    ApplicantAccount(String legalName, String username, String password, LocalDate dateCreated) {
        super(legalName, username, password, dateCreated);
        this.documents = new ArrayList<>();
        this.previousJobsApplied = new ArrayList<>();
        this.currentJobsApplying = new HashMap<JobPosting, ApplicationStatus>();
    }

    /**
     * Creates an applicant account.
     *
     * @param legalName           The applicant's legal name.
     * @param username            The username associated with this account.
     * @param password            The password associated with this account.
     * @param dateCreated         The date this account was created.
     * @param documents           The documents that this applicant has stored in their account.
     * @param previousJobsApplied A list of jobs that this applicant previously applied for.
     * @param currentJobsApplying A map of jobs that this applicant is currently applying for to their statuses.
     */
    ApplicantAccount(String legalName, String username, String password, LocalDate dateCreated,
                     ArrayList<File> documents, ArrayList<JobPosting> previousJobsApplied,
                     HashMap<JobPosting, ApplicationStatus> currentJobsApplying) {
        super(legalName, username, password, dateCreated);
        this.documents = documents;
        this.previousJobsApplied = previousJobsApplied;
        this.currentJobsApplying = currentJobsApplying;
    }

    // === Getters ===

    /**
     * Get a list of documents that this applicant has submitted.
     *
     * @return a list of documents that this applicant has submitted.
     */
    ArrayList<File> getDocuments() {
        return this.documents;
    }

    /**
     * Get a list of previous jobs this applicant has applied for.
     *
     * @return a list of previous jobs that this applicant has applied for.
     */
    ArrayList<JobPosting> getPreviousJobsApplied() {
        return this.previousJobsApplied;
    }

    /**
     * Get a map of current jobs this applicant is applying for and their respective statuses.
     *
     * @return a map of current jobs this applicant is applying for and their respective statuses.
     */
    HashMap<JobPosting, ApplicationStatus> getCurrentJobsApplying() {
        return this.currentJobsApplying;
    }

    // === Setters ===

    /**
     * Set the documents submitted by this applicant.
     *
     * @param documents The documents submitted by this applicant.
     */
    void setDocuments(ArrayList<File> documents) {
        this.documents = documents;
    }

    /**
     * Set the previous jobs applied for by this applicant.
     *
     * @param previousJobsApplied The list of previous jobs this applicant has applied for.
     */
    void setPreviousJobsApplied(ArrayList<JobPosting> previousJobsApplied) {
        this.previousJobsApplied = previousJobsApplied;
    }

    /**
     * Set the current jobs that this applicant is applying for.
     *
     * @param currentJobsApplying The map of current jobs this applicant is applying for to their respective statuses.
     */
    void setCurrentJobsApplying(HashMap<JobPosting, ApplicationStatus> currentJobsApplying) {
        this.currentJobsApplying = currentJobsApplying;
    }

    // === Other methods ===

    /**
     * View job postings.
     *
     * @return a list of job postings in the system.
     */
    // TODO
    ArrayList<JobPosting> viewJobPosting() {
        // return JobPostingManager.get...;
        return null;
    }

    /**
     * Apply for a job.
     *
     * @param jobPosting The job posting that this applicant wants to apply for.
     */
    // TODO
    void applyForJob(JobPosting jobPosting) {
        // submit cover letter and CV
        // add themselves to a list of applicants for this job
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param jobPosting The job posting that this applicant wants to withdraw from.
     * @return true iff this applicant can successfully withdraw their application.
     */
    // TODO
    boolean withdrawApplication(JobPosting jobPosting) {
        // take themselves out of list of applicants
        // remove cover letter and CV
        return true;
    }

    /**
     * Return the status of this job application for this applicant.
     *
     * @param jobPosting The job application that is being checked.
     * @return the status of this job application for this applicant.
     */
    ApplicationStatus checkStatus(JobPosting jobPosting) {
        return this.currentJobsApplying.get(jobPosting);
    }

    /**
     * Submit this applicant's cover letter and CV for this job.
     *
     * @param coverLetter This applicant's cover letter.
     * @param CV          This applicant's CV.
     */
    // TODO
    void submitCoverLetterAndCV(File coverLetter, File CV, JobPosting jobPosting) {
        // something to do with job posting
    }

    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    boolean isInactive(LocalDate today) {
        return this.dateOfLastClosing.isEqual(today.minusDays(30));
    }


}
