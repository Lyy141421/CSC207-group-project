package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.InterviewManager;
import DocumentManagers.CompanyDocumentManager;
import Main.JobApplicationSystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPostingManager implements Serializable {
    /**
     * A class that manages the job postings for each branch
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;

    // === Instance variables ===
    // The branch that this job posting manager is for
    private Branch branch;
    // The BranchJobPostings stored in this job posting manager
    private volatile ArrayList<BranchJobPosting> branchJobPostings = new ArrayList<>();

    // === Public methods ===

    // === Constructors ===
    public BranchJobPostingManager(Branch branch) {
        this.branch = branch;
    }

    public BranchJobPosting getBranchJobPosting(int id) {
        for (BranchJobPosting posting : branchJobPostings)
            if (posting.getId() == id)
                return posting;
        return null;
    }

    public ArrayList<BranchJobPosting> getBranchJobPostings() {
        return branchJobPostings;
    }

    public void addJobPosting(BranchJobPosting posting) {
        this.branchJobPostings.add(posting);
    }

    /**
     * Get a list of open job postings for this branch.
     *
     * @param today Today's date.
     * @return a list of open job postings for this branch.
     */
    public ArrayList<BranchJobPosting> getOpenJobPostings(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.branchJobPostings) {
            if (!(jobPosting.isClosed(today))) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get all closed job postings for this branch.
     *
     * @param today Today's date.
     * @return a list of all closed job postings at this branch.
     */
    private ArrayList<BranchJobPosting> getAllClosedJobPostings(LocalDate today) {
        ArrayList<BranchJobPosting> closedPostings = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            if ((branchJobPosting.isClosed(today))) {
                closedPostings.add(branchJobPosting);
            }
        }
        return closedPostings;
    }

    /**
     * Get a list of closed job postings for this branch that have not yet been filled.
     *
     * @param today Today's date.
     * @return a list job postings that can no longer have anything submitted for this branch that have not yet been filled.
     */
    public ArrayList<BranchJobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<BranchJobPosting> requestedPostings = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : this.getAllClosedJobPostings(today)) {
            if (!(branchJobPosting.isFilled())) {
                requestedPostings.add(branchJobPosting);
            }
        }
        return requestedPostings;
    }

    /**
     * Get a list of job postings for this branch that have recently closed for applications, ie, they do not have an
     * interview manager.
     *
     * @param today Today's date.
     * @return a list of recently closed job postings for applicants.
     */
    public ArrayList<BranchJobPosting> getJobPostingsRecentlyClosedForApplications(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getBranchJobPostings()) {
            if (jobPosting.isClosed(today) && jobPosting.getInterviewManager() == null) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that have closed with no applications submitted and thus, may need an extension.
     *
     * @param getToday Today's date
     * @return a list of job postings with no applications submitted.
     */
    public ArrayList<BranchJobPosting> getJobPostingsToReview(LocalDate getToday) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(getToday)) {
            if (jobPosting.getInterviewManager() != null && jobPosting.getInterviewManager().getHrTask() == InterviewManager.SELECT_APPS_FOR_FIRST_ROUND) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that have closed with no applications submitted and thus, may need an extension.
     *
     * @param getToday Today's date
     * @return a list of job postings with no applications submitted.
     */
    public ArrayList<BranchJobPosting> getJobPostingsThatNeedDeadlineExtensions(LocalDate getToday) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(getToday)) {
            if (jobPosting.getInterviewManager() != null && jobPosting.getInterviewManager().getHrTask() == InterviewManager.EXTEND_APPLICATION_DEADLINE) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings where a current interview round is over.
     *
     * @param today Today's date/
     * @return a list of job postings where a current interview round is over.
     */
    public ArrayList<BranchJobPosting> getJobPostingsThatNeedGroupInterviewsScheduled(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() != null && jobPosting.getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_GROUP_INTERVIEWS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that are ready for the final hiring step.
     *
     * @param today Today's date.
     * @return a list of job postings that are ready for the final hiring step.
     */
    public ArrayList<BranchJobPosting> getJobPostingsThatNeedHRSelectionForHiring(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() != null && jobPosting.getInterviewManager().getHrTask() == InterviewManager.SELECT_APPS_TO_HIRE) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get all filled job postings.
     *
     * @return all filled job postings.
     */
    public ArrayList<BranchJobPosting> getAllFilledJobPostings() {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.branchJobPostings) {
            if (jobPosting.isFilled()) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Updates all closed unfilled job postings.
     *
     * @param today Today's date.
     *              Note: method is called every time someone logs in
     */
    public void updateAllClosedUnfilledJobPostings(LocalDate today) {
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() != null) {
                jobPosting.getInterviewManager().updateJobPostingFilledStatus();
                jobPosting.advanceInterviewRound();
            }
        }
    }

    /**
     * Get all the applicants who have applied to this branch.
     *
     * @param today Today's date.
     * @return a list of all applicants who have applied to this branch
     */
    public ArrayList<Applicant> getAllApplicantsToBranch(LocalDate today) {
        ArrayList<Applicant> applicants = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getAllClosedJobPostings(today)) {
            for (JobApplication jobApplication : jobPosting.getJobApplications()) {
                Applicant applicant = jobApplication.getApplicant();
                if (!applicants.contains(applicant)) {
                    applicants.add(applicant);
                }
            }
        }
        return applicants;
    }

    /**
     * Get a list of company job postings that this branch has not yet extended.
     *
     * @return a list of company job postings that this branch has not yet extended.
     */
    public ArrayList<CompanyJobPosting> getExtendableCompanyJobPostings() {
        ArrayList<CompanyJobPosting> cjpThatCanBeExtended = new ArrayList<>();
        ArrayList<CompanyJobPosting> companyJobPostings = this.getBranch().getCompany().getCompanyJobPostings();
        for (CompanyJobPosting companyJobPosting : companyJobPostings) {
            if (!companyJobPosting.getBranches().contains(this.getBranch())) {
                cjpThatCanBeExtended.add(companyJobPosting);
            }
        }
        return cjpThatCanBeExtended;
    }

    public ArrayList<BranchJobPosting> getUpdatableJobPostings(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostingsThatCanBeUpdated = new ArrayList<>();
        jobPostingsThatCanBeUpdated.addAll(this.getOpenJobPostings(today));
        jobPostingsThatCanBeUpdated.addAll(this.getJobPostingsThatNeedDeadlineExtensions(today));
        return jobPostingsThatCanBeUpdated;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Getters ===
    Branch getBranch() {
        return this.branch;
    }

    /**
     * Update the job postings that are closed for further applications and create an interview manager.
     *
     * @param today Today's date.
     */
    public void updateJobPostingsClosedForApplications(LocalDate today) {
        CompanyDocumentManager companyDocManager = this.getBranch().getCompany().getDocumentManager();
        for (BranchJobPosting jobPosting : this.getJobPostingsRecentlyClosedForApplications(today)) {
            if (!jobPosting.hasNoApplicationsSubmitted()) {
                // HR Coordinator has the option to reopen the job posting if closed
//                 Creates an interview manager so that if applicant withdraws their application from this point on,
//                 they are automatically rejected.
                jobPosting.createInterviewManager();
                if (!companyDocManager.applicationDocumentsTransferred(jobPosting)) {
                    companyDocManager.transferApplicationDocuments(jobPosting);
                }
                for (Reference reference : jobPosting.getAllReferences()) {
                    // If the reference still has not yet submitted their reference letter for a job app for this job
                    // posting, remove it from their list
                    reference.removeJobPosting(jobPosting);
                }
                this.getBranch().getCompany().getJobPostingWithID(jobPosting.getCompanyPostingId()).removeBranch(this.getBranch());
            }
        }
    }
}
