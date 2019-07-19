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

    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle          The job title.
     * @param jobDescription    The job description.
     * @param requiredDocuments The required documents for this job posting.
     * @param tags              The tags for this job posting.
     * @param numPositions      The number of positions for this job.
     * @param postDate          The date this job posting was posted.
     * @param closeDate         The date this job posting is closed.
     */
    public BranchJobPosting addJobPosting(String jobTitle, String jobField, String jobDescription,
                                          ArrayList<String> requiredDocuments, ArrayList<String> tags, int numPositions,
                                          LocalDate postDate, LocalDate closeDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, numPositions, this.branch, postDate, closeDate);
        this.branch.getJobPostingManager().addJobPosting(branchJobPosting);
        return branchJobPosting;
    }

    /**
     * Create and add a job posting to the system.
     *
     * @param jobPosting   The company job posting to which this branch job posting is to be added
     * @param numPositions The number of positions for this job.
     * @param postDate     The date this job posting was posted.
     * @param closeDate    The date this job posting is closed.
     */
    public BranchJobPosting addJobPosting(CompanyJobPosting jobPosting, int numPositions,
                                          LocalDate postDate, LocalDate closeDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(jobPosting, numPositions, this.branch, postDate,
                closeDate);
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
}
