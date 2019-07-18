package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import FileLoadingAndStoring.DataLoaderAndStorer;
import JobPostings.AbstractJobPostingManager;
import JobPostings.CompanyJobPosting;
import JobPostings.CompanyJobPostingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Company {

    // === Instance variables ===
    // The name of this company (unique)
    private String name;
    // The branches in this company.
    private ArrayList<Branch> branches;
    // The company job posting manager
    private AbstractJobPostingManager jobPostingManager = new CompanyJobPostingManager(this);

    public Company(String name) {
        this.branches = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public AbstractJobPostingManager getJobPostingManager() {
        return jobPostingManager;
    }

    /**
     * Attempt to create a branch of this company with the given name and return it.
     * Return null if name is already taken by another branch of this company.
     *
     * Precondition: postalCode is a valid Canadian postal code
     *
     * @param name       The name of the branch
     * @param postalCode The postal code of this branch
     * @return           The branch created, or null if no branch was created
     */
    Branch createBranch(String name, String postalCode) {
        HashMap<String, String> FSAToCMA = DataLoaderAndStorer.loadFSAHashMap();
        String CMA = FSAToCMA.get(postalCode.substring(0,4));
        for (Branch branch : branches) {
            if (branch.getName().equalsIgnoreCase(name))
                // branch by this name already exists
                return null;
        }
        return new Branch(name, CMA);
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    public ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (CompanyJobPosting jobPosting : this.getJobPostingManager().getJobPostings()) {
            apps.addAll(jobPosting.getJobApplications());
        }
        return apps;
    }

    /**
     * Check whether this applicant has applied to this company.
     *
     * @param applicant The applicant in question.
     * @return true iff this applicant has applied to this company.
     */
    public boolean hasAppliedHere(Applicant applicant) {
        for (CompanyJobPosting jobPosting : this.getJobPostingManager().getJobPostings()) {
            if (applicant.hasAppliedTo(jobPosting)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Company))
            return false;
        else
            return name.equalsIgnoreCase(((Company) obj).name);
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < name.length(); i++)
            sum += name.charAt(i);
        return sum;
    }
}
