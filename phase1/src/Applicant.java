import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Instance variables ===
    // List of job applications submitted
    private ArrayList<JobApplication> jobApplications;
    // Date that last application closed
    private LocalDate mostRecentCloseDate;

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
        this.jobApplications = new ArrayList<>();
    }

    /**
     * Creates an applicant account.
     *
     * @param username        The username associated with this account.
     * @param password        The password associated with this account.
     * @param legalName       The applicant's legal name.
     * @param email           The applicant's email.
     * @param dateCreated     The date this account was created.
     * @param jobApplications The job applications that this applicant has previously submitted.
     */
    Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
              ArrayList<JobApplication> jobApplications) {
        super(username, password, legalName, email, dateCreated);
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
    LocalDate getMostRecentCloseDate() {
        return this.mostRecentCloseDate;
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
    void setMostRecentCloseDate(LocalDate date) {
        this.mostRecentCloseDate = date;
    }

    // === Other methods ===

    /**
     * Find the most recent closing date of all the job applications that this applicant has submitted.
     *
     * @return the most recent closing date.
     */
    private LocalDate findMostRecentCloseDate() {
        LocalDate mostRecentCloseDate = this.getJobApplications().get(0).getJobPosting().getCloseDate();
        for (JobApplication jobApp : this.getJobApplications()) {
            LocalDate closingDate = jobApp.getJobPosting().getCloseDate();
            if (mostRecentCloseDate.isBefore(closingDate)) {
                mostRecentCloseDate = closingDate;
            }
        }
        return mostRecentCloseDate;
    }

    /**
     * Update the date of most recent closing for this applicant.
     */
    void updateMostRecentCloseDate() {
        this.setMostRecentCloseDate(this.findMostRecentCloseDate());
    }

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
     * Get the previous job applications for this applicant.
     *
     * @return a list of previous job applications submitted where the posting is now filled.
     */
    ArrayList<JobApplication> getPreviousJobApplications() {
        ArrayList<JobApplication> previousJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (jobApplication.getStatus() == -3) {
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
        return DAYS.between(today, this.mostRecentCloseDate);
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
//    User addReference(String referenceName, String referenceEmail, LocalDate today) {
//        User reference = new ProfessionalReference(referenceName, referenceEmail, referenceName, today);
//        UserManager.addAccount(reference);
//        return reference;
//    }

}
