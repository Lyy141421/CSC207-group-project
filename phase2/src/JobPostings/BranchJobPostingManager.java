package JobPostings;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.InterviewManager;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPostingManager {
    /**
     * A class that manages the job postings for each branch
     */

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

    public ArrayList<BranchJobPosting> getBranchJobPostings() {
        return branchJobPostings;
    }

    public void addJobPosting(BranchJobPosting posting) {
        this.branchJobPostings.add(posting);
    }

    public ArrayList<BranchJobPosting> getJobPostingsNotAppliedToBy(Applicant applicant) {
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
            if (!(jobPosting.isClosed(today))) {
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
    public ArrayList<BranchJobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<BranchJobPosting> requestedPostings = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : branchJobPostings) {
            if ((branchJobPosting.isClosed(today)) && !(branchJobPosting.isFilled())) {
                requestedPostings.add(branchJobPosting);
            }
        }
        return requestedPostings;
    }

    /**
     * Get a list of job postings for this branch that have recently closed and have not yet been reviewed for interviews.
     *
     * @param today Today's date.
     * @return a list of closed job postings that have not yet started the interview process.
     */
    public ArrayList<BranchJobPosting> getClosedJobPostingsNoApplicantsChosen(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() == null) {
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
            InterviewManager IM = jobPosting.getInterviewManager();
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
    public ArrayList<BranchJobPosting> getJobPostingsWithRoundCompletedNotForHire(LocalDate today) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (BranchJobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_INTERVIEWS) {
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

    // For testing of observer pattern with documents
    public void notifyJobPostingClosed(LocalDate today) {
        for (BranchJobPosting jobPosting : this.getBranchJobPostings()) {
            jobPosting.isClosed(today);
        }
    }
}
