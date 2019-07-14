package UsersAndJobObjects;

import Main.JobApplicationSystem;
import Managers.DocumentManager;
import Managers.JobApplicationManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Class variables ===
    // Number of days passed for user account to be deemed inActive
    private static final int INACTIVE_DAYS = 30;

    // === Instance variables ===
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager = new JobApplicationManager();

    // === Public methods ===
    // === Constructors ===

    public Applicant() throws IOException {
    }

    public Applicant(String id) throws IOException {
        this.setUsername(id);
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated) throws IOException {
        super(username, password, legalName, email, dateCreated);
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
                     DocumentManager documentManager) {
        super(username, password, legalName, email, dateCreated, documentManager);
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
                     JobApplicationManager jobApplicationManager, DocumentManager documentManager) {
        super(username, password, legalName, email, dateCreated, documentManager);
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Getters ===
    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    // === Other methods ===

    /**
     * Register this job application for this applicant.
     *
     * @param application The job application registered.
     */
    public void registerJobApplication(JobApplication application) {
        this.getDocumentManager().addDocuments(new ArrayList<>(Arrays.asList(application.getCoverLetter(),
                application.getCv())));
        jobApplicationManager.addJobApplication(application);
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param today      Today's date.
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application; else return false
     */
    public boolean withdrawJobApplication(LocalDate today, JobPosting jobPosting) {
        if (this.hasAppliedTo(jobPosting) && !jobPosting.isFilled()) {
            if (!jobPosting.isClosed(today)) {
                jobPosting.removeJobApplication(jobPosting.findJobApplication(this));
            }
            this.jobApplicationManager.removeJobApplication(jobPosting);
            return true;
        }
        return false;
    }

    /**
     * Report whether this applicant has already applied to this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return true iff this applicant has not applied to this job posting.
     */
    public boolean hasAppliedTo(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            if (jobApp.getApplicant().equals(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of open job postings not yet applied to.
     *
     * @return a list of open job postings not yet applied to.
     */
    public ArrayList<JobPosting> getOpenJobPostingsNotAppliedTo(JobApplicationSystem JAS) {
        ArrayList<JobPosting> jobPostingsNotAppliedTo = new ArrayList<>();
        for (Company company : JAS.getCompanies()) {
            for (JobPosting posting : company.getJobPostingManager().getOpenJobPostings(JAS.getToday()))
                if (!this.hasAppliedTo(posting)) {
                    jobPostingsNotAppliedTo.add(posting);
                }
        }
        return jobPostingsNotAppliedTo;
    }


    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    public boolean isInactive(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today) >= Applicant.INACTIVE_DAYS;
    }
}
