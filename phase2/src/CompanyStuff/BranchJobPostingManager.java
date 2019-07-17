package CompanyStuff;

import ApplicantStuff.Applicant;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPostingManager implements Serializable {
    /**
     * A class that manages the job postings for each company
     */

    // === Instance variables ===
    // A list of branch job postings for positions in this branch.
    private ArrayList<BranchJobPosting> postings = new ArrayList<>();
    // The company that this job posting manager is for
    private Branch branch;


    // === Public methods ===

    // === Constructors ===
    public BranchJobPostingManager(Branch branch) {
        this.branch = branch;
    }

    public BranchJobPostingManager(ArrayList<BranchJobPosting> postings, Branch branch) {
        this.postings = postings;
        this.branch = branch;
    }

    // === Getters ===
    public ArrayList<BranchJobPosting> getBranchJobPostings() {
        return this.postings;
    }

    // === Other methods ===

    /**
     * Add a job posting to this company.
     *
     * @param posting The job posting to be added.
     */
    public void addBranchJobPosting(BranchJobPosting posting) {

        this.postings.add(posting);
    }

    /**
     * Remove a job posting from this company.
     *
     * @param posting The job posting to be removed.
     */
    public void removeBranchJobPosting(BranchJobPosting posting) {
        this.postings.remove(posting);
    }

    /**
     * Get the job posting from this company with this ID. Return null if not found.
     *
     * @param ID The id of the job posting in question.
     * @return the job posting with this ID or null if not found.
     */
    public BranchJobPosting getBranchJobPosting(int ID) {
        for (BranchJobPosting posting : this.getBranchJobPostings()) {
            if (posting.getId() == ID) {
                return posting;
            }
        }
        return null;

    }

    /**
     * Check whether this applicant has applied to this company.
     *
     * @param applicant The applicant in question.
     * @return true iff this applicant has applied to this company.
     */
    public boolean hasAppliedHere(Applicant applicant) {
        for (BranchJobPosting posting : this.postings) {
            if (applicant.hasAppliedTo(posting)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of open job postings for this company.
     *
     * @param today Today's date.
     * @return a list of open job postings for this company.
     */
    public ArrayList<BranchJobPosting> getOpenBranchJobPostings(LocalDate today) {
        ArrayList<BranchJobPosting> openPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getBranchJobPostings()) {
            if (!posting.isClosed(today)) {
                openPostings.add(posting);
            }
        }
        return openPostings;
    }

    /**
     * Get a list of closed job postings for this company that have not yet been filled.
     *
     * @param today Today's date.
     * @return a list of closed job postings for this company that have not yet been filled.
     */
    public ArrayList<BranchJobPosting> getClosedBranchJobPostingsNotFilled(LocalDate today) {
        ArrayList<BranchJobPosting> unfilledPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getBranchJobPostings()) {
            if (!posting.isFilled() && posting.isClosed(today)) {
                unfilledPostings.add(posting);
            }
        }
        return unfilledPostings;
    }

    /**
     * Get a list of job postings for this company that have recently closed and have not yet been reviewed for interviews.
     *
     * @param today Today's date.
     * @return a list of closed job postings that have not yet started the interview process.
     */
    public ArrayList<BranchJobPosting> getClosedBranchJobPostingsNoApplicantsChosen(LocalDate today) {
        ArrayList<BranchJobPosting> recentlyClosedPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getClosedBranchJobPostingsNotFilled(today)) {
            if (posting.getInterviewManager() == null) {
                recentlyClosedPostings.add(posting);
            }
        }
        return recentlyClosedPostings;
    }

    /**
     * Get a list of job postings with no applications in consideration.
     *
     * @param today Today's date.
     * @return a list of job postings with no applications in consideration.
     */
    public ArrayList<BranchJobPosting> getClosedBranchJobPostingsNoApplicationsInConsideration(LocalDate today) {
        ArrayList<BranchJobPosting> deadPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getClosedBranchJobPostingsNotFilled(today)) {
            if (posting.getInterviewManager() != null &&
                    posting.getInterviewManager().getHrTask() == InterviewManager.CLOSE_POSTING_NO_HIRE) {
                deadPostings.add(posting);
            }
        }
        return deadPostings;
    }

    /**
     * Get a list of job postings that have closed with no applications submitted.
     *
     * @param today Today's date
     * @return a list of job postings with no applications.
     */
    public ArrayList<BranchJobPosting> getClosedBranchJobPostingsNoApplicationsSubmitted(LocalDate today) {
        ArrayList<BranchJobPosting> ignoredPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getClosedBranchJobPostingsNotFilled(today)) {
            if (posting.hasNoApplicationsSubmitted()) {
                ignoredPostings.add(posting);
            }
        }
        return ignoredPostings;
    }

    /**
     * Get a list of all filled job postings at this company.
     *
     * @return a list of filled job postings at this company.
     */
    public ArrayList<BranchJobPosting> getFilledBranchJobPostings() {
        ArrayList<BranchJobPosting> filledPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.postings) {
            if (posting.isFilled()) {
                filledPostings.add(posting);
            }
        }
        return filledPostings;
    }

    /**
     * Get a list of job postings where a current interview round is over.
     *
     * @param today Today's date/
     * @return a list of job postings where a current interview round is over.
     */
    public ArrayList<BranchJobPosting> getBranchJobPostingsWithRoundCompletedNotForHire(LocalDate today) {
        ArrayList<BranchJobPosting> roundCompletedJobPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getClosedBranchJobPostingsNotFilled(today)) {
            if (posting.getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_INTERVIEWS) {
                roundCompletedJobPostings.add(posting);
            }
        }
        return roundCompletedJobPostings;
    }

    /**
     * Get a list of job postings that are ready for the final hiring step.
     *
     * @param today Today's date.
     * @return a list of job postings that are ready for the final hiring step.
     */
    public ArrayList<BranchJobPosting> getBranchJobPostingsForHiring(LocalDate today) {
        ArrayList<BranchJobPosting> readyPostings = new ArrayList<>();
        for (BranchJobPosting posting : this.getClosedBranchJobPostingsNotFilled(today)) {
            if (posting.getInterviewManager().getHrTask() == InterviewManager.HIRE_APPLICANTS) {
                readyPostings.add(posting);
            }
        }
        return readyPostings;
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
