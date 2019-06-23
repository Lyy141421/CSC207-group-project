import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

class JobApplicationManager {

    // === Instance variables ===
    // List of job applications submitted
    private ArrayList<JobApplication> jobApplications = new ArrayList<>();
    // Date that last application closed
    private LocalDate mostRecentCloseDate;

    // === Constructors ===

    /**
     * Create a job application manager.
     */
    JobApplicationManager() {
    }

    /**
     * Create a job application manager.
     *
     * @param jobApplications List of job applications that this applicant has submitted.
     */
    JobApplicationManager(ArrayList<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    // === Getters ===

    /**
     * Get the list of job applications submitted by this applicant.
     *
     * @return the list of job applications submitted by this applicant.
     */
    ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    /**
     * Get the most recent close date for this applicant.
     *
     * @return the most recent close date for this applicant.
     */
    LocalDate getMostRecentCloseDate() {
        return this.mostRecentCloseDate;
    }

    // === Setters ===

    /**
     * Set the job applications for this applicant.
     *
     * @param jobApplications The job applications for this applicant.
     */
    void setJobApplications(ArrayList<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    /**
     * Set the most recent job posting close date for this applicant.
     *
     * @param mostRecentCloseDate The most recent job posting close date for this applicant.
     */
    void setMostRecentCloseDate(LocalDate mostRecentCloseDate) {
        this.mostRecentCloseDate = mostRecentCloseDate;
    }

    // === Other methods ===

    /**
     * Add a job application to this applicant's application list.
     *
     * @param applicant       The applicant applying for a job.
     * @param jobPosting      The job posting to be applied to.
     * @param CV              The applicant's CV.
     * @param coverLetter     The applicant's cover letter.
     * @param applicationDate The date this application is submitted.
     */
    void addJobApplication(Applicant applicant, JobPosting jobPosting, File CV, File coverLetter,
                           LocalDate applicationDate) {
        JobApplication app = new JobApplication(applicant, jobPosting, CV, coverLetter, applicationDate);
        this.jobApplications.add(app);
    }

    /**
     * Remove this job application.
     *
     * @param jobPosting The job posting from which the job application is to be removed.
     */
    void removeJobApplication(JobPosting jobPosting) {
        JobApplication app = this.findJobApplication(jobPosting);
        this.jobApplications.remove(app);
    }

    /**
     * Find the job application associated with this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return the job application associated with this job posting.
     */
    JobApplication findJobApplication(JobPosting jobPosting) {
        for (JobApplication app : this.jobApplications) {
            if (app.getJobPosting().equals(jobPosting)) {
                return app;
            }
        }
        return null;
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
     * Get the number of days since the most recent job posting close date.
     *
     * @param today Today's date.
     * @return the number of days since the most recent job posting close date.
     */
    long getNumDaysSinceMostRecentCloseDate(LocalDate today) {
        return Math.max(0, DAYS.between(today, this.mostRecentCloseDate));
    }

}
