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
    // List of filenames uploaded to account
    private ArrayList<String> filesSubmitted = new ArrayList<>();

    // === Constructors ===

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
     * Add a file to one's account.
     *
     * @param fileName The file name of the file to be added.
     */
    void addFile(String fileName) {
        this.filesSubmitted.add(fileName);
    }

    /**
     * Remove a file from one's account.
     * @param fileName The file name of the file to be removed.
     */
    void removeFile(String fileName) {
        this.filesSubmitted.remove(fileName);
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
    // TODO Store files submitted
    boolean applyForJob(JobPosting jobPosting, File CV, File coverLetter, LocalDate applicationDate) {
        if (LocalDate.now().isBefore(jobPosting.getCloseDate()) && !this.hasAppliedTo(jobPosting)) {
            this.jobApplicationManager.addJobApplication(this, jobPosting, CV.getName(), coverLetter.getName(),
                    applicationDate);
            this.addFile(CV.getName());
            this.addFile(coverLetter.getName());
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
            jobPosting.removeJobApplication(jobPosting.findJobApplication(this));
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
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            if (jobApp.getApplicant().equals(this)) {
                return true;
            }
        }
        return false;
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

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    void removeFilesFromAccount(LocalDate today) {
        if (this.isInactive(today)) {
            JobApplication lastClosedJobApp = this.jobApplicationManager.getLastClosedJobApp();
            ArrayList<String> files = this.jobApplicationManager.getFilesSubmittedForApplication(lastClosedJobApp);
            this.filesSubmitted.removeAll(files);
        }
    }

}
