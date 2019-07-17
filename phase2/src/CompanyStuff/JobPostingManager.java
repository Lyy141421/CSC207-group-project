package CompanyStuff;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class JobPostingManager implements Serializable {
    /**
     * A class that manages the job postings for each branch
     */

    // === Instance variables ===
    private ArrayList<JobPosting> jobPostings;

    // === Getters ===
    public ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    // === Other methods ===

    /**
     * Add a job posting to this branch.
     *
     * @param jobPosting The job posting to be added.
     */
    public void addJobPosting(JobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }

    /**
     * Remove a job posting from this branch.
     *
     * @param jobPosting The job posting to be removed.
     */
    public void removeJobPosting(JobPosting jobPosting) {
        this.jobPostings.remove(jobPosting);
    }

    /**
     * Get the job posting from this branch with this ID. Return null if not found.
     *
     * @param ID The id of the job posting in question.
     * @return the job posting with this ID or null if not found.
     */
    public JobPosting getJobPosting(int ID) {
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (jobPosting.getId() == ID) {
                return jobPosting;
            }
        }
        return null;

    }
}
