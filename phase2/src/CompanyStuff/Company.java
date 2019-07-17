package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;

import java.util.ArrayList;

public class Company {
    private String name;
    private ArrayList<Branch> branches;
    private ArrayList<JobPosting> postings;

    public Company(String name) {
        this.branches = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public ArrayList<JobPosting> getPostings() {
        return postings;
    }

    public void addJobPosting(JobPosting posting) {
        postings.add(posting);
    }

    /** Attempt to create a branch of this company with the given name and return it.
     * Return null if name is already taken by another branch of this company.
     *
     * @param name  The name of the branch
     * @return  The branch created, or null if no branch was created
     */
    Branch createBranch(String name) {
        for (Branch branch : branches) {
            if (branch.getName().equalsIgnoreCase(name))
                // branch by this name already exists
                return null;
        }
        return new Branch(name);
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    public ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (Branch branch : branches)
            for (BranchJobPosting posting : branch.getBranchJobPostingManager().getBranchJobPostings())
                for (JobApplication app : posting.getJobApplications()) {
                    if (app.getApplicant().equals(applicant))
                        apps.add(app);
            }
        return apps;
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
