import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

class Applicant extends UserAccount {
    /**
     * An account for a job applicant.
     */

    // === Instance variables ===
    // List of job applications submitted
    private ArrayList<JobApplication> jobApplications;
    // Date that last application closed
    private LocalDate dateOfMostRecentClosing;

    // === Constructors ===

    /**
     * Create an applicant account.
     *
     * @param legalName   The applicant's legal name.
     * @param username    The username associated with this account.
     * @param password    The password associated with this account.
     * @param dateCreated The date this account was created.
     */
    Applicant(String legalName, String username, String password, LocalDate dateCreated) {
        super(legalName, username, password, dateCreated);
        this.jobApplications = new ArrayList<>();
    }

    /**
     * Creates an applicant account.
     *
     * @param legalName       The applicant's legal name.
     * @param username        The username associated with this account.
     * @param password        The password associated with this account.
     * @param dateCreated     The date this account was created.
     * @param jobApplications The job applications that this applicant has previously submitted.
     */
    Applicant(String legalName, String username, String password, LocalDate dateCreated,
              ArrayList<JobApplication> jobApplications) {
        super(legalName, username, password, dateCreated);
        this.jobApplications = jobApplications;
    }

    // === Getters ===

    /**
     * Get all the job applications submitted.
     *
     * @return a list of all job applications submitted.
     */
    ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    /**
     * Get the most recent closing date of all the job applications that this applicant has submitted.
     *
     * @return the most recent closing date of all the job applications that this applicant has submitted.
     */
    LocalDate getDateOfMostRecentClosing() {
        return this.dateOfMostRecentClosing;
    }

    // === Setters ===

    /**
     * Set the job applications that this applicant has submitted.
     *
     * @param jobApplications The list of jobs that have been applied for.
     */
    void setJobApplications(ArrayList<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    /**
     * Set the most recent closing date of all the job applications that this applicant has submitted.
     *
     * @param date The most recent closing date of all the job applications that this applicant has submitted.
     */
    void setDateOfMostRecentClosing(LocalDate date) {
        this.dateOfMostRecentClosing = date;
    }

    // === Other methods ===

    /**
     * Find the most recent closing date of all the job applications that this applicant has submitted.
     *
     * @return the most recent closing date.
     */
    LocalDate findDateOfMostRecentClosing() {
        LocalDate dateOfMostRecentClosing = this.getJobApplications().get(0).getJobPosting().getClosingDate();
        for (JobApplication jobApp : this.getJobApplications()) {
            LocalDate closingDate = jobApp.getJobPosting().getClosingDate();
            if (dateOfMostRecentClosing.isBefore(closingDate)) {
                dateOfMostRecentClosing = closingDate;
            }
        }
        return dateOfMostRecentClosing;
    }

    /**
     * Update the date of most recent closing for this applicant.
     */
    void updateDateOfMostRecentClosing() {
        this.setDateOfMostRecentClosing(this.findDateOfMostRecentClosing());
    }

    /**
     * View job postings.
     *
     * @return a list of job postings in the system.
     */
    ArrayList<JobPosting> viewJobPosting() {
        return JobPostingManager.getJobPostings();
    }

    /**
     * Apply for a job.
     *
     * @param jobPosting The job posting that this applicant wants to apply for.
     * @return true iff this application is successfully submitted (ie before closing date and has not already applied)
     */
    boolean applyForJob(JobPosting jobPosting, File CV, File coverLetter) {
        if (LocalDate.now().isBefore(jobPosting.getCloseDate()) && !this.hasAppliedTo(jobPosting)) {
            JobApplication jobApplication = new JobApplication(this, jobPosting, CV, coverLetter);
            jobPosting.addApplication(jobApplication);
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
            jobPosting.removeApplication(this);
        }
        return true;
    }

    /**
     * Get the previous job applications for this applicant.
     *
     * @return a list of previous job applications submitted where the posting is now filled.
     */
    ArrayList<JobApplication> getPreviousJobApplications() {
        ArrayList<JobApplication> previousJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (jobApplication.getStatus() == -1) {
                previousJobApps.add(jobApplication);
            }
        }
        return previousJobApps;
    }

    /**
     * Get the current job applications for this applicant.
     *
     * @return a list of current job applications submitted (posting is not yet filled).
     */
    ArrayList<JobApplication> getCurrentJobApplications() {
        ArrayList<JobApplication> currentJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (!this.getPreviousJobApplications().contains(jobApplication)) {
                currentJobApps.add(jobApplication);
            }
        }
        return currentJobApps;
    }

    /**
     * Get the number of days since the most recent job posting close date.
     *
     * @param today Today's date.
     * @return the number of days since the most recent job posting close date.
     */
    long getNumDaysSinceMostRecentClosing(LocalDate today) {
        return DAYS.between(today, this.dateOfMostRecentClosing);
    }

    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    boolean isInactive(LocalDate today) {
        return getNumDaysSinceMostRecentClosing(today) == 30;
    }

//    /**
//     * Create a reference account.
//     * @param referenceName   The reference's legal name.
//     * @param referenceEmail    The reference's email.
//     * @param today             Today's date.
//     * @return  the user account for the reference.
//     */
//    UserAccount addReference(String referenceName, String referenceEmail, LocalDate today) {
//        UserAccount reference = new ProfessionalReference(referenceName, referenceEmail, referenceName, today);
//        UserAccountManager.addAccount(reference);
//        return reference;
//    }

}
