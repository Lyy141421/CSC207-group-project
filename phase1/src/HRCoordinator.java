import java.time.LocalDate;

class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Instance variables ===
    // The company that this HR Coordinator works for
    private Company company;

    // === Constructors ===

    /**
     * Create an HR Coordinator account.
     */
    HRCoordinator() {
    }

    /**
     * Create an HR Coordinator account.
     *
     * @param username    The HR Coordinator's account username.
     * @param password    The HR Coordinator's account password.
     * @param legalName   The HR Coordinator's legal name.
     * @param email       The HR Coordinator's email.
     * @param company     The company that this HR Coordinator works for.
     * @param dateCreated The date this account was created.
     */
    HRCoordinator(String username, String password, String legalName, String email, Company company,
                  LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
    }

    // === Getters ===

    /**
     * Get the company that this HR Coordinator works for.
     *
     * @return the company that this HR Coordinator works for.
     */
    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    /**
     * Set the company that this HR Coordinator works for.
     */
    void setCompany(Company company) {
        this.company = company;
    }

    // === Other methods ===

    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle       The job title.
     * @param jobDescription The job description.
     * @param requirements   The requirements for this job.
     * @param numPositions   The number of positions for this job.
     * @param postDate       The date this job posting was posted.
     * @param closeDate      The date this job posting is closed.
     */
    void addJobPosting(String jobTitle, String jobField, String jobDescription, String requirements,
                       int numPositions, LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new JobPosting(jobTitle, jobField, jobDescription, requirements,
                numPositions, this.company, postDate, closeDate);
        this.company.getJobPostingManager().addJobPosting(jobPosting);
    }


}
