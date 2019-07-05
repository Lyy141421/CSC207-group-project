package UsersAndJobObjects;

import GUIClasses.ApplicantInterface;
import Main.JobApplicationSystem;
import Managers.JobApplicationManager;

import java.time.LocalDate;
import java.util.ArrayList;

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
    // List of filenames uploaded to account
    private ArrayList<String> filesSubmitted = new ArrayList<>();

    // === Public methods ===
    // === Constructors ===

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        super.setUserInterface(new ApplicantInterface(this));
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
              JobApplicationManager jobApplicationManager) {
        super(username, password, legalName, email, dateCreated);
        this.jobApplicationManager = jobApplicationManager;
        super.setUserInterface(new ApplicantInterface(this));
    }

    // === Getters ===

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    public ArrayList<String> getFilesSubmitted() {
        return this.filesSubmitted;
    }

    // === Setters ===

    public void setFilesSubmitted(ArrayList<String> filesSubmitted) {
        this.filesSubmitted = filesSubmitted;
    }

    public void setJobApplicationManager(JobApplicationManager jobApplicationManager) {
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Other methods ===

    /**
     * Apply for a job.
     *
     * @param jobPosting The job posting that this applicant wants to apply for.
     * @param CV        The applicant's CV.
     * @param coverLetter   The applicant's cover letter.
     * @return true iff this application is successfully submitted (ie before closing date and has not already applied)
     */
    public boolean applyForJob(JobPosting jobPosting, String CV, String coverLetter) {
        if (LocalDate.now().isBefore(jobPosting.getCloseDate()) && !this.hasAppliedTo(jobPosting)) {
            this.jobApplicationManager.addJobApplication(this, jobPosting, CV, coverLetter,
                    LocalDate.now());
            this.addFile(CV);
            this.addFile(coverLetter);
        }
        return false;
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application; else return false
     */
    public boolean withdrawApplication(JobPosting jobPosting) {
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
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    public boolean isInactive(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today) >= 30;
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccount(LocalDate today) {
        if (this.isInactive(today)) {
            JobApplication lastClosedJobApp = this.jobApplicationManager.getLastClosedJobApp();
            ArrayList<String> files = this.jobApplicationManager.getFilesSubmittedForApplication(lastClosedJobApp);
            this.filesSubmitted.removeAll(files);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===
    // === Other methods ===

    /**
     * Add a file to one's account.
     *
     * @param file The file contents to be added.
     */
    private void addFile(String file) {
        this.filesSubmitted.add(file);
    }

    /**
     * Remove a file from one's account.
     * @param file The file to be removed.
     */
    private void removeFile(String file) {
        this.filesSubmitted.remove(file);
    }

}
