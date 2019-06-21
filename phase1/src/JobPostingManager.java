import java.util.ArrayList;

class JobPostingManager {
    /**
     * A class that manages the job postings for each company
     */

    // === Instance variables ===
    // A list of job postings for positions in this company.
    private ArrayList<JobPosting> jobPostings = new ArrayList<>();
    // The company that this job posting manager is for
    private Company company;

    /**
     * Create a new job posting manager for this company.
     *
     * @param company The company that this manager is for.
     */
    JobPostingManager(Company company) {
        this.company = company;
    }

    /**
     * Create a new job posting manager for this company;
     *
     * @param jobPostings The list of job postings for this company.
     * @param company     The company that this manager is for.
     */
    JobPostingManager(ArrayList<JobPosting> jobPostings, Company company) {
        this.jobPostings = jobPostings;
        this.company = company;
    }

    // === Getters ===

    /**
     * Get the job postings for positions in this company.
     *
     * @return the list of job postings for positions in this company.
     */
    ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    /**
     * Get the company that this job posting manager is for.
     *
     * @return the company that this job posting manager is for.
     */
    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    /**
     * Set the job postings for this job posting manager.
     *
     * @param jobPostings The list of job postings to be set.
     */
    void setJobPostings(ArrayList<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

    /**
     * Set the company for this job posting manager.
     *
     * @param company The company to be set.
     */
    void setCompany(Company company) {
        this.company = company;
    }

    // === Other methods ===

    /**
     * Add a job posting to this company.
     *
     * @param jobPosting The job posting to be added.
     */
    void addJobPosting(JobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }


}
