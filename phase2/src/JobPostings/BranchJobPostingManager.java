package JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.InterviewManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPostingManager extends AbstractJobPostingManager {
    /**
     * A class that manages the job postings for each branch
     */

    // === Instance variables ===
    // The branch that this job posting manager is for
    private Branch branch;


    // === Public methods ===

    // === Constructors ===
    public BranchJobPostingManager(Branch branch) {
        this.branch = branch;
    }

    /**
     * Get a list of open job postings for this branch.
     *
     * @param today Today's date.
     * @return a list of open job postings for this branch.
     */
    public ArrayList<CompanyJobPosting> getOpenJobPostings(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getJobPostings()) {
            if (!((BranchJobPosting) jobPosting).isClosed(today)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of closed job postings for this branch that have not yet been filled.
     *
     * @param today Today's date.
     * @return a list of closed job postings for this branch that have not yet been filled.
     */
    public ArrayList<CompanyJobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getJobPostings()) {
            if (!((BranchJobPosting) jobPosting).isFilled() && ((BranchJobPosting) jobPosting).isClosed(today)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings for this branch that have recently closed and have not yet been reviewed for interviews.
     *
     * @param today Today's date.
     * @return a list of closed job postings that have not yet started the interview process.
     */
    public ArrayList<CompanyJobPosting> getClosedJobPostingsNoApplicantsChosen(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (((BranchJobPosting) jobPosting).getInterviewManager() == null) {
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
    public ArrayList<CompanyJobPosting> getClosedJobPostingsNoApplicationsInConsideration(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            InterviewManager IM = ((BranchJobPosting) jobPosting).getInterviewManager();
            if (IM != null && IM.getHrTask() == InterviewManager.CLOSE_POSTING_NO_HIRE) {
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
    public ArrayList<CompanyJobPosting> getClosedJobPostingsNoApplicationsSubmitted(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (((BranchJobPosting) jobPosting).hasNoApplicationsSubmitted()) {
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
    public ArrayList<CompanyJobPosting> getFilledJobPostings() {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getJobPostings()) {
            if (((BranchJobPosting) jobPosting).isFilled()) {
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
    public ArrayList<CompanyJobPosting> getJobPostingsWithRoundCompletedNotForHire(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (((BranchJobPosting) jobPosting).getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_INTERVIEWS) {
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
    public ArrayList<CompanyJobPosting> getJobPostingsForHiring(LocalDate today) {
        ArrayList<CompanyJobPosting> jobPostings = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (((BranchJobPosting) jobPosting).getInterviewManager().getHrTask() == InterviewManager.HIRE_APPLICANTS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
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
}
