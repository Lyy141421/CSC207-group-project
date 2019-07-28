package ApplicantStuff;

import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.Company;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.DocumentManagerFactory;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import Main.JobApplicationSystem;
import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Number of days passed for user account to be deemed inActive
    private static final int INACTIVE_DAYS = 30;

    // === Instance variables ===
    // The Census Metropolitan Area or Census Agglomeration that this applicant is assumed to be closest to
    private String cma;
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager;
    // The applicant's document manager
    private ApplicantDocumentManager documentManager;

    // === Public methods ===
    // === Constructor ===
    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
                     String cma) {
        super(username, password, legalName, email, dateCreated);
        this.cma = cma;
        this.jobApplicationManager = new JobApplicationManager();
        this.documentManager = new DocumentManagerFactory().getApplicantDocumentManager(this);
    }

    // === Getters ===
    public String getCma() {
        return this.cma;
    }

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    public ApplicantDocumentManager getDocumentManager() {
        return this.documentManager;
    }

    // === Other methods ===

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
                jobPosting.removeJobApplication(jobApp);    // TODO observer design pattern?
            } else {    // Company has access to application
                jobPosting.getInterviewManager().updateForApplicationWithdrawal(jobApp);   // Update application from the company end
            }
            if (!jobPosting.isClosedForReferences(today)) {
                // Company has access to application, but reference letters may not all be submitted
                jobApp.removeAppFromAllReferences();    // Cancel reference letter submissions
            }
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

    /**
     * Checks whether this applicant has already applied to this branch.
     *
     * @param branch The branch in question.
     * @return true iff this applicant has already applied to this branch
     */
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
    public ArrayList<BranchJobPosting> getOpenJobPostingsNotAppliedTo(JobApplicationSystem jobAppSystem) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (Company company : jobAppSystem.getCompanies())
            for (Branch branch : company.getBranches()) {
                BranchJobPostingManager jpm = branch.getJobPostingManager();
                ArrayList<BranchJobPosting> openPostings = jpm.getOpenJobPostings(jobAppSystem.getToday());
                openPostings.retainAll(jpm.getJobPostingsNotAppliedToByApplicant(this));
                jobPostings.addAll(openPostings);
            }
        return jobPostings;
    }


    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from getToday.
     *
     * @param today Today's date.
     * @return true iff getToday's date is 30 days after the closing date for the last job this applicant applied to.
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
        return new String[]{"Applicant", this.getUsername(), this.getLegalName(), this.getEmail(), this.getCma(),
                this.getDateCreated().toString()};
    }
}