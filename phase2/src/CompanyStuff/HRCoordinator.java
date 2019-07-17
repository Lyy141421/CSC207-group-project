package CompanyStuff;

import Main.User;
import ApplicantStuff.JobApplication;

import java.io.IOException;
import java.time.LocalDate;

public class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Instance variables ===
    // The branch that this HR Coordinator works for
    private Branch branch;


    // === Public methods ===
    // === Constructors ===
    public HRCoordinator() {
    }

    public HRCoordinator(String username, String password, String legalName, String email, Branch branch,
                         LocalDate dateCreated) throws IOException {
        super(username, password, legalName, email, dateCreated);
        this.branch = branch;
    }

    // === Getters ===
    public Branch getBranch() {
        return this.branch;
    }

    // === Setters ===
    public void setBranch(Branch branch) {
        this.branch = branch;
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
    public JobPosting addJobPosting(String jobTitle, String jobField, String jobDescription, String requirements,
                                    int numPositions, LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new JobPosting(jobTitle, jobField, jobDescription, requirements,
                numPositions, this.branch, postDate, closeDate);
        this.branch.getJobPostingManager().addJobPosting(jobPosting);
        return jobPosting;
    }

    /**
     * Get a job application with this ID.
     *
     * @param ID The ID in question.
     * @return the job application with this ID or null if cannot be accessed/found.
     */
    @Override
    public JobApplication findJobAppById(int ID) {
        for (BranchJobPosting branchJobPosting : this.branch.getBranchJobPostings()) {
            for (JobApplication jobApplication : branchJobPosting.getJobApplications()) {
                if (Integer.parseInt(jobApplication.getId()) == ID) {
                    return jobApplication;
                }
            }
        }
        return null;
    }
}
