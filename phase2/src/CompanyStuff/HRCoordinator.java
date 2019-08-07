package CompanyStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Main.JobApplicationSystem;
import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;

    // === Instance variables ===
    // The branch that this HR Coordinator works for
    private Branch branch;

    // === Public methods ===
//    public HRCoordinator(String username, String password, String legalName, String email, Branch branch,
//                         LocalDate dateCreated) {
//        super(username, password, legalName, email, dateCreated);
//        this.branch = branch;
//        branch.addHRCoordinator(this);
//    }

    public HRCoordinator(String username, String password, String legalName, String email, Branch branch, LocalDate dateCreated) {
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
     * @param jobTitle          The job title.
     * @param jobField          The job field.
     * @param jobDescription    The job description.
     * @param requiredDocuments The required documents for this job posting.
     * @param tags              The tags for this job posting.
     * @param numPositions      The number of positions for this job.
     * @param postDate          The date this job posting was posted.
     * @param applicationCloseDate  The last day for application submissions.
     */
    public BranchJobPosting addJobPosting(String jobTitle, String jobField, String jobDescription,
                                          ArrayList<String> requiredDocuments, ArrayList<String> tags, int numPositions,
                                          LocalDate postDate, LocalDate applicationCloseDate) {
        CompanyJobPosting companyJobPosting = new CompanyJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, this.getBranch().getCompany());
        this.getBranch().getCompany().addCompanyJobPosting(companyJobPosting);
        return this.implementJobPosting(companyJobPosting, numPositions, postDate, applicationCloseDate);
    }

    public BranchJobPosting implementJobPosting(CompanyJobPosting companyJobPosting, int numPositions, LocalDate postDate,
                                                LocalDate applicationCloseDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(companyJobPosting.getTitle(), companyJobPosting.getField(),
                companyJobPosting.getDescription(), companyJobPosting.getRequiredDocuments(), companyJobPosting.getTags(),
                numPositions, this.getBranch(), postDate, applicationCloseDate, companyJobPosting.getId());
        companyJobPosting.addBranch(this.getBranch());
        this.getBranch().getJobPostingManager().addJobPosting(branchJobPosting);
        return branchJobPosting;
    }

    public void updateJobPosting(BranchJobPosting jobPosting, int numPositions, LocalDate applicationCloseDate) {
        jobPosting.updateFields(numPositions, applicationCloseDate);
        CompanyJobPosting companyJobPosting = this.getBranch().getCompany().getJobPostingWithID(jobPosting.getCompanyPostingId());
        if (!companyJobPosting.getBranches().contains(this.getBranch())) {
            jobPosting.addBranch(this.getBranch());
        }
    }

    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Username", "Legal Name", "Email", "Company Branch", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"HR Coordinator", this.getUsername(), this.getLegalName(), this.getEmail(),
                this.getBranch().getName(), this.getDateCreated().toString()};
    }
}
