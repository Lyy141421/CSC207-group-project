package ApplicantStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.DocumentManager;
import DocumentManagers.DocumentManagerFactory;
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
    public static final int INACTIVE_DAYS = 30;
    // The category names for the reject list
    public static final String[] REJECT_LIST_CATEGORIES = new String[]{"Legal Name", "Username", "Email"};

    // === Instance variables ===
    // The Census Metropolitan Area or Census Agglomeration that this applicant is assumed to be closest to
    private String cma;
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager;
    // The applicant's document manager
    private DocumentManager documentManager;

    // === Public methods ===
    // === Constructor ===
    public Applicant(String username, String password, String legalName, String email, String cma, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.cma = cma;
        this.documentManager = new DocumentManagerFactory().createDocumentManager(this);
    }

    // === Getters ===
    public String getCma() {
        return this.cma;
    }

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    public ApplicantDocumentManager getDocumentManager() {
        return (ApplicantDocumentManager) this.documentManager;
    }

    // === Setter ===
    public void setNewJobApplicationManager() {
        this.jobApplicationManager = new JobApplicationManager();
    }

    public void resetJobApplicationManager(JobApplicationSystem jobApplicationSystem) {
        Applicant applicant = (Applicant) jobApplicationSystem.getUserManager().findUserByUsername(this.getUsername());
        this.jobApplicationManager = applicant.getJobApplicationManager();
    }

    public void setJobApplicationManager(JobApplicationManager jobApplicationManager) {
        this.jobApplicationManager = jobApplicationManager;
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
            if (!jobPosting.isClosed(today)) {   // Company still does not have access to application
                jobPosting.removeJobApplication(jobApp);
                jobApp.removeAppFromAllReferences();    // Cancel reference letter submissions
            } else {    // Company has access to application
                jobPosting.getInterviewManager().updateForApplicationWithdrawal(jobApp);   // Update application from the company end
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

    public ArrayList<BranchJobPosting> getApplicableBranchJobPostings(CompanyJobPosting companyJobPosting, LocalDate today) {
        ArrayList<BranchJobPosting> applicableBranchJobPostings = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : companyJobPosting.getOpenBranchJobPostingList(today)) {
            if (!this.hasAppliedToPosting(branchJobPosting)) {
                applicableBranchJobPostings.add(branchJobPosting);
            }
        }
        return applicableBranchJobPostings;
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

    public String[] getCategoryValuesForRejectList() {
        return new String[]{this.getLegalName(), this.getUsername(), this.getEmail()};
    }

    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Username", "Legal Name", "Email", "City/Town", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"Applicant", this.getUsername(), this.getLegalName(), this.getEmail(), this.getCma(),
                this.getDateCreated().toString()};
    }
}