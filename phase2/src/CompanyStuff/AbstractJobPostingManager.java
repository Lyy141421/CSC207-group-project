package CompanyStuff;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class AbstractJobPostingManager implements Serializable {
    /**
     * A class that manages the job postings for each branch
     */

    // === Instance variables ===
    private ArrayList<CompanyJobPosting> jobPostings;

    // === Getters ===
    public ArrayList<CompanyJobPosting> getJobPostings() {
        return this.jobPostings;
    }

    // === Other methods ===

    /**
     * Add a job posting to this branch.
     *
     * @param jobPosting The job posting to be added.
     */
    public void addJobPosting(CompanyJobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }

    /**
     * Remove a job posting from this branch.
     *
     * @param jobPosting The job posting to be removed.
     */
    public void removeJobPosting(CompanyJobPosting jobPosting) {
        this.jobPostings.remove(jobPosting);
    }

    /**
     * Get the job posting from this branch with this ID. Return null if not found.
     *
     * @param ID The id of the job posting in question.
     * @return the job posting with this ID or null if not found.
     */
    public CompanyJobPosting getJobPosting(int ID) {
        for (CompanyJobPosting jobPosting : this.getJobPostings()) {
            if (jobPosting.getId() == ID) {
                return jobPosting;
            }
        }
        return null;

    }

    public abstract ArrayList<CompanyJobPosting> getOpenJobPostings(LocalDate today);
}
