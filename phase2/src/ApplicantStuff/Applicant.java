package ApplicantStuff;

import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.Company;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.DocumentManagerFactory;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import Main.JobApplicationSystem;
import Main.User;
import NotificationSystem.Observable;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Class variables ===
    // Number of days passed for user account to be deemed inActive
    private static final int INACTIVE_DAYS = 30;

    // === Instance variables ===
    // The Census Metropolitan Area or Census Agglomeration that this applicant is assumed to be closest to
    private String CMA;
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager;
    // The applicant's document manager
    private ApplicantDocumentManager documentManager;

    // === Public methods ===
    // === Constructors ===

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
                     String CMA) {
        super(username, password, legalName, email, dateCreated);
        this.CMA = CMA;
        this.jobApplicationManager = new JobApplicationManager();
        this.documentManager = new DocumentManagerFactory().getApplicantDocumentManager(this);
    }

    // === Getters ===
    public String getCMA() {
        return this.CMA;
    }

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    public ApplicantDocumentManager getDocumentManager() {
        return this.documentManager;
    }

    // === Other methods ===

    /**
     * Register this job application for this applicant. Note: This is done prior to file submission.
     *
     * @param application The job application registered.
     */
    public void registerJobApplication(JobApplication application) {
        jobApplicationManager.addJobApplication(application);
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param today      Today's date.
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application; else return false
     */
    public boolean withdrawJobApplication(LocalDate today, BranchJobPosting jobPosting) {
        JobApplication jobApp = jobPosting.findJobApplication(this);
        if (this.hasAppliedToPosting(jobPosting) && !jobPosting.isFilled()) {
            if (!jobPosting.isClosedForApplications(today)) {   // Company still does not have access to application
                jobPosting.removeJobApplication(jobApp);
            }
            if (!jobPosting.isClosedForReferences(today)) { // Company has access to application, but before interview selection
                jobApp.removeAppFromAllReferences();    // Cancel reference letter submissions
            }
            jobPosting.getInterviewManager().withdrawApplication(jobApp);   // Update application from the company end
            this.jobApplicationManager.removeJobApplication(jobPosting);    // Remove this application from applicant end
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
    public boolean hasAppliedToPosting(BranchJobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            if (jobApp.getApplicant().equals(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAppliedToBranch(Branch branch) {
        for (BranchJobPosting posting : branch.getJobPostingManager().getBranchJobPostings())
            if (this.hasAppliedToPosting(posting))
                return true;
        return false;
    }

    /**
     * Get a list of open job postings not yet applied to.
     *
     * @return a list of open job postings not yet applied to.
     */
    public ArrayList<BranchJobPosting> getOpenJobPostingsNotAppliedTo(JobApplicationSystem JAS) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (Company company : JAS.getCompanies())
            for (Branch branch : company.getBranches()) {
                BranchJobPostingManager jpm = branch.getJobPostingManager();
                ArrayList<BranchJobPosting> openPostings = jpm.getOpenJobPostings(JAS.getToday());
                openPostings.retainAll(jpm.getJobPostingsNotAppliedToBy(this));
                jobPostings.addAll(openPostings);
                }
        return jobPostings;
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


    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Username", "Legal Name", "Email", "Postal Code", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"Applicant", this.getUsername(), this.getLegalName(), this.getEmail(), this.getCMA(),
                this.getDateCreated().toString()};
    }
}
