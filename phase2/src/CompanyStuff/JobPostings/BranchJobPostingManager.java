package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.InterviewManager;
import DocumentManagers.CompanyDocumentManager;

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
    private ArrayList<BranchJobPosting> branchJobPostings;

    // === Public methods ===

    // === Constructors ===
    public BranchJobPostingManager(Branch branch) {
        this.branch = branch;
        this.branchJobPostings = new ArrayList<>();
    }

    public BranchJobPosting getBranchJobPosting(int id) {
        for (BranchJobPosting posting : branchJobPostings)
            if (posting.id == id)
                return posting;
        return null;
    }

    public ArrayList<BranchJobPosting> getBranchJobPostings() {
        return branchJobPostings;
    }

    public void addJobPosting(BranchJobPosting posting) {
        this.branchJobPostings.add(posting);
    }

    public ArrayList<BranchJobPosting> getJobPostingsNotAppliedToByApplicant(Applicant applicant) {
        ArrayList<BranchJobPosting> requestedPostings = new ArrayList<>();
        for (BranchJobPosting posting : branchJobPostings) {
            if (!(applicant.hasAppliedToPosting(posting)))
                requestedPostings.add(posting);

        }
        return requestedPostings;
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
            if (!(jobPosting.isClosedForApplications(today))) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of closed job postings for this branch that have not yet been filled.
     *
     * @param today Today's date.
     * @return a list job postings that can no longer have anything submitted for this branch that have not yet been filled.
     */
    public ArrayList<BranchJobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<BranchJobPosting> requestedPostings = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            if ((branchJobPosting.isClosedForReferences(today)) && !(branchJobPosting.isFilled())) {
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
            if (jobPosting.isClosedForApplications(today) && jobPosting.getInterviewManager() == null) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings for this branch that have recently closed for reference letters, ie, applicants still
     * need to be chosen for the first round.
     *
     * @param today Today's date.
     * @return a list of recently closed job postings for references.
     */
    public ArrayList<BranchJobPosting> getJobPostingsRecentlyClosedForReferences(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.SELECT_APPS_FOR_FIRST_ROUND) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get job postings that need interview configurations set up.
     *
     * @param today Today's date.
     * @return the list of job postings that need interview configurations set up.
     */
    public ArrayList<BranchJobPosting> getJobPostingsThatNeedInterviewProcessConfigured(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            InterviewManager interviewManager = jobPosting.getInterviewManager();
            if (interviewManager.getHrTask() == InterviewManager.SET_INTERVIEW_CONFIGURATION) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings with no applications in consideration.
     *
     * @param today Today's date.
     * @return a list of job postings with no applications in consideration.
     */
    public ArrayList<BranchJobPosting> getClosedJobPostingsNoApplicationsInConsideration(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            InterviewManager interviewManager = jobPosting.getInterviewManager();
            if (interviewManager.getHrTask() == InterviewManager.CLOSE_POSTING_NO_HIRE) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that have closed with no applications submitted.
     *
     * @param today Today's date
     * @return a list of job postings with no applications.
     */
    public ArrayList<BranchJobPosting> getClosedJobPostingsNoApplicationsSubmitted(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.hasNoApplicationsSubmitted()) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of all filled job postings at this branch.
     *
     * @return a list of filled job postings at this branch.
     */
    public ArrayList<BranchJobPosting> getFilledJobPostings() {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.branchJobPostings) {
            if (jobPosting.isFilled()) {
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
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_GROUP_INTERVIEWS) {
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
    public ArrayList<BranchJobPosting> getJobPostingsForHiring(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.HIRE_APPLICANTS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle          The job title.
     * @param jobDescription    The job description.
     * @param requiredDocuments The required documents for this job posting.
     * @param tags              The tags for this job posting.
     * @param numPositions      The number of positions for this job.
     * @param postDate          The date this job posting was posted.
     * @param applicationCloseDate  The last date for application submissions.
     * @param referenceCloseDate    The last date for reference letter submissions.
     */
    public BranchJobPosting addJobPosting(String jobTitle, String jobField, String jobDescription,
                                          ArrayList<String> requiredDocuments, ArrayList<String> tags, int numPositions,
                                          LocalDate postDate, LocalDate applicationCloseDate, LocalDate referenceCloseDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, numPositions, this.branch, postDate, applicationCloseDate, referenceCloseDate);
        this.branch.getJobPostingManager().addJobPosting(branchJobPosting);
        return branchJobPosting;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Getters ===
    Branch getBranch() {
        return this.branch;
    }

    // === Setters ===
    void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * Update the job postings that are closed for further applications and create an interview manager.
     *
     * @param today Today's date.
     */
    // TODO this method must be called after the user enters the date and before program launches
    public void updateJobPostingsClosedForApplications(LocalDate today) {
        CompanyDocumentManager companyDocManager = this.getBranch().getCompany().getDocumentManager();
        for (BranchJobPosting jobPosting : this.getJobPostingsRecentlyClosedForApplications(today)) {
            // Creates an interview manager so that if applicant withdraws their application from this point on,
            // they are automatically rejected.
            jobPosting.createInterviewManager();
            if (!companyDocManager.applicationDocumentsTransferred(jobPosting)) {
                companyDocManager.transferApplicationDocuments(jobPosting);
            }
        }
    }

    /**
     * Update the references of job postings that are closed for further reference letter submissions.
     *
     * @param today Today's date.
     */
    // TODO this method must be called after the user enters the date and before program launches
    public void updateJobPostingsClosedForReferences(LocalDate today) {
        for (BranchJobPosting jobPosting : this.getJobPostingsRecentlyClosedForReferences(today)) {
            if (jobPosting.isClosedForReferences(today)) {
                for (Reference reference : jobPosting.getAllReferences()) {
                    // If the reference still has not yet submitted their reference letter for a job app for this job
                    // posting, remove it from their list
                    reference.removeJobPosting(jobPosting);
                }
            }
        }
    }
}
