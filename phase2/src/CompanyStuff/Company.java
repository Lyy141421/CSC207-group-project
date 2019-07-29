package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import DocumentManagers.CompanyDocumentManager;
import DocumentManagers.DocumentManagerFactory;
import FileLoadingAndStoring.DataLoaderAndStorer;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Company implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;

    // === Instance variables ===
    // The name of this company (unique)
    private String name;
    // The branches in this company.
    private ArrayList<Branch> branches;
    // The list of company job postings for this company
    private ArrayList<CompanyJobPosting> companyJobPostings;
    // The document manager for this branch
    private CompanyDocumentManager documentManager;

    // === Constructor ===
    public Company(String name) {
        this.name = name;
        this.branches = new ArrayList<>();
        this.companyJobPostings = new ArrayList<>();
        this.documentManager = new DocumentManagerFactory().createCompanyDocumentManager(this);
    }

    // === Getters ===
    public String getName() {
        return name;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public ArrayList<CompanyJobPosting> getCompanyJobPostings() {
        return companyJobPostings;
    }

    public void addCompanyJobPosting(CompanyJobPosting posting) {
        companyJobPostings.add(posting);
    }

    public CompanyDocumentManager getDocumentManager() {
        return this.documentManager;
    }

    /**
     * Add a folder for this branch.
     *
     * @param branch The branch to be added.
     */
    private void addBranch(Branch branch) {
        this.branches.add(branch);
        this.getDocumentManager().createBranchFolder(branch);
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
    public Branch createBranch(String name, String postalCode) {
        HashMap<String, String> fsaHashMap = DataLoaderAndStorer.loadFSAHashMap();
        String cma = fsaHashMap.get(postalCode.substring(0,3).toUpperCase());
        for (Branch branch : branches) {
            if (branch.getName().equalsIgnoreCase(name))
                // branch by this name already exists
                return null;
        }
        Branch newBranch = new Branch(name, cma, this);
        this.addBranch(newBranch);
        return newBranch;
    }

    /**
     * Get the branch by name.
     *
     * @param name The name of the branch.
     * @return the branch with this name or null if it cannot be found.
     */
    public Branch getBranch(String name) {
        for (Branch branch : this.getBranches()) {
            if (branch.getName().equals(name)) {
                return branch;
            }
        }
        return null;
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    public ArrayList<JobApplication> getAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (Branch branch : branches) {
            for (BranchJobPosting posting : branch.getJobPostingManager().getBranchJobPostings()) {
                if (applicant.hasAppliedToPosting(posting))
                    apps.add(posting.findJobApplication(applicant));
            }
        }
        return apps;
    }

    /**
     * Update all the job postings in this company as appropriate.
     *
     * @param today Today's date.
     */
    public void updateJobPostings(LocalDate today) {
        for (Branch branch : this.getBranches()) {
            BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
            branchJobPostingManager.updateJobPostingsClosedForApplications(today);
            branchJobPostingManager.updateJobPostingsClosedForReferences(today);
            branchJobPostingManager.updateAllClosedUnfilledJobPostings(today);
        }
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
