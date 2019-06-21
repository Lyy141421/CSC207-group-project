import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Instance variables ===
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager = new JobApplicationManager();

    // === Constructors ===

    Applicant() {
    }

    /**
     * Create an applicant account.
     *
     * @param username    The username associated with this account.
     * @param password    The password associated with this account.
     * @param legalName   The applicant's legal name.
     * @param email       The applicant's email.
     * @param dateCreated The date this account was created.
     */
    Applicant(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
    }

    /**
     * Creates an applicant account.
     *
     * @param username        The username associated with this account.
     * @param password        The password associated with this account.
     * @param legalName       The applicant's legal name.
     * @param email           The applicant's email.
     * @param dateCreated     The date this account was created.
     * @param jobApplicationManager The job application manager for this applicant.
     */
    Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
              JobApplicationManager jobApplicationManager) {
        super(username, password, legalName, email, dateCreated);
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Getters ===

    /**
     * Get the job application manager for this applicant.
     *
     * @return the job application manager for this applicant.
     */
    JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    // === Setters ===

    /**
     * Set the job application manager for this applicant.
     *
     * @param jobApplicationManager The job application manager for this applicant.
     */
    void setJobApplicationManager(JobApplicationManager jobApplicationManager) {
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Other methods ===

    /**
     * View job postings.
     *
     * @return a list of job postings in the system.
     */
    ArrayList<JobPosting> viewJobPostings() {
        return JobApplicationSystem.getAllJobPostings();
    }

    /**
     * Apply for a job.
     *
     * @param jobPosting The job posting that this applicant wants to apply for.
     * @param CV        The applicant's CV.
     * @param coverLetter   The applicant's cover letter.
     * @param applicationDate   The date this application was submitted.
     * @return true iff this application is successfully submitted (ie before closing date and has not already applied)
     */
    boolean applyForJob(JobPosting jobPosting, File CV, File coverLetter, LocalDate applicationDate) {
        if (LocalDate.now().isBefore(jobPosting.getCloseDate()) && !this.hasAppliedTo(jobPosting)) {
            this.jobApplicationManager.addJobApplication(this, jobPosting, CV, coverLetter, applicationDate);
        }
        return false;
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application.
     */
    boolean withdrawApplication(JobPosting jobPosting) {
        if (this.hasAppliedTo(jobPosting) && !jobPosting.isFilled()) {
            jobPosting.removeJobApplication(this);
            this.jobApplicationManager.removeJobApplication(jobPosting);
        }
        return true;
    }

    /**
     * Report whether this applicant has already applied to this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return true iff this applicant has not applied to this job posting.
     */
    private boolean hasAppliedTo(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getApplications()) {
            if (jobApp.getApplicant().equals(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all job applications for this applicant.
     *
     * @return a list of all job applications submitted by this applicant.
     */
    ArrayList<JobApplication> getAllJobApplications() {
        return this.jobApplicationManager.getJobApplications();
    }

    /**
     * Get the previous job applications for this applicant.
     *
     * @return a list of previous job applications submitted where the posting is now filled.
     */
    ArrayList<JobApplication> getPreviousJobApplications() {
        return this.jobApplicationManager.getPreviousJobApplications();
    }

    /**
     * Get the current job applications for this applicant.
     *
     * @return a list of current job applications submitted (posting is not yet filled).
     */
    ArrayList<JobApplication> getCurrentJobApplications() {
        return this.jobApplicationManager.getCurrentJobApplications();
    }

    /**
     * Get the number of days since the most recent job posting close date.
     *
     * @param today Today's date.
     * @return the number of days since the most recent job posting close date.
     */
    long getNumDaysSinceMostRecentClosing(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today);
    }

    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    boolean isInactive(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today) == 30;
    }

//    /**
//     * Create a reference account.
//     * @param referenceName   The reference's legal name.
//     * @param referenceEmail    The reference's email.
//     * @param today             Today's date.
//     * @return  the user account for the reference.
//     */
//    User addReference(String referenceName, String referenceEmail, LocalDate today) {
//        User reference = new ProfessionalReference(referenceName, referenceEmail, referenceName, today);
//        UserManager.addAccount(reference);
//        return reference;
//    }

}
