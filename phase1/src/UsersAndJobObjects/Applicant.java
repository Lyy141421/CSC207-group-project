package UsersAndJobObjects;

import Main.JobApplicationSystem;
import Managers.DocumentManager;
import Managers.JobApplicationManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "Applicants";

    // === Instance variables ===
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager = new JobApplicationManager();
    // The applicant's document manager
    private DocumentManager documentManager = new DocumentManager();

    // === Public methods ===
    // === Constructors ===

    public Applicant() {}

    public Applicant(String id) {
        this.setUsername(id);
    }
    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
              JobApplicationManager jobApplicationManager) {
        super(username, password, legalName, email, dateCreated);
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Getters ===

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    // === Setters ===

    public void setJobApplicationManager(JobApplicationManager jobApplicationManager) {
        this.jobApplicationManager = jobApplicationManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    // === Other methods ===

    /**
     * Register this job application for this applicant.
     * @param application   The job application registered.
     */
    public void registerJobApplication(JobApplication application) {
        this.documentManager.addDocuments(new ArrayList<>(Arrays.asList(application.getCoverLetter(),
                application.getCV())));
        jobApplicationManager.addJobApplication(application);
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application; else return false
     */
    public boolean withdrawJobApplication(JobPosting jobPosting) {
        if (this.hasAppliedTo(jobPosting) && !jobPosting.isFilled()) {
            jobPosting.removeJobApplication(jobPosting.findJobApplication(this));
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
     * @param today Today's date.
     * @return a list of open job postings not yet applied to.
     */
    public ArrayList<JobPosting> getOpenJobPostingsNotAppliedTo(LocalDate today) {
        ArrayList<JobPosting> jobPostingsNotAppliedTo = new ArrayList<>();
        for (Company company : JobApplicationSystem.getCompanies()) {
            for (JobPosting posting : company.getJobPostingManager().getJobPostings())
                if (today.isBefore(posting.getCloseDate()) && !this.hasAppliedTo(posting)) {
                    jobPostingsNotAppliedTo.add(posting);
                }
        }
        return jobPostingsNotAppliedTo;
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    // TODO I feel like this shouldn't be here, but if we put it in DocumentManager, there would be coupling
    // (user must be an instance variable)
    public void removeFilesFromAccount(LocalDate today) {
        if (this.isInactive(today)) {
            JobApplicationManager JAM = this.getJobApplicationManager();
            JobApplication lastClosedJobApp = JAM.getLastClosedJobApp();
            ArrayList<JobApplicationDocument> documents = JAM.getFilesSubmittedForApplication(lastClosedJobApp);
            this.documentManager.removeDocuments(documents);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    private boolean isInactive(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today) >= 30;
    }
}
