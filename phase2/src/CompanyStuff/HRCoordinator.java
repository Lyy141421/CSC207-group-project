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
     * @param jobPosting         The job posting.
     * @param branchJobPosting   The branch job posting.
     */
    public JobPosting addJobPosting(JobPosting jobPosting, BranchJobPosting branchJobPosting) {
        this.branch.getCompany().addJobPosting(jobPosting);
        this.branch.getBranchJobPostingManager().addBranchJobPosting(branchJobPosting);
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
        for (BranchJobPosting branchJobPosting : this.branch.getBranchJobPostingManager().getBranchJobPostings()) {
            for (JobApplication jobApplication : branchJobPosting.getJobApplications()) {
                if (Integer.parseInt(jobApplication.getId()) == ID) {
                    return jobApplication;
                }
            }
        }
        return null;
    }
}
