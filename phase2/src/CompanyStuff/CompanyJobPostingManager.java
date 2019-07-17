package CompanyStuff;

import java.time.LocalDate;
import java.util.ArrayList;

public class CompanyJobPostingManager extends JobPostingManager {
    /**
     * A class that manages the job postings for each branch
     */

    // === Instance variables ===
    // The company that this job posting manager is for
    private Company company;


    // === Public methods ===

    // === Constructors ===
    public CompanyJobPostingManager(Company company) {
        this.company = company;
    }

    /**
     * Get a list of open job postings for this branch.
     *
     * @param today Today's date.
     * @return a list of open job postings for this branch.
     */
    public ArrayList<JobPosting> getOpenJobPostings(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (!jobPosting.isClosed(today)) {
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
    public ArrayList<JobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (!jobPosting.isFilled() && jobPosting.isClosed(today)) {
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
    public ArrayList<JobPosting> getFilledJobPostings() {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (jobPosting.isFilled()) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Getters ===
    Company getCompany() {
        return this.company;
    }

    // === Setters ===
    void setCompany(Company company) {
        this.company = company;
    }
}
