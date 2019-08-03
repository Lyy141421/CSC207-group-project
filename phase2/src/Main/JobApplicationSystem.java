package Main;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import ApplicantStuff.Applicant;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import CompanyStuff.JobPostings.CompanyJobPosting;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.MainFrame;
import NotificationSystem.Notification;

import java.time.LocalDate;
import java.util.ArrayList;

public class JobApplicationSystem {

    // === Instance variables ===
    // List of companies registered in the system
    private ArrayList<Company> companies = new ArrayList<>();
    // The user manager for the system
    private UserManager userManager = new UserManager();
    // The date this program interprets as getToday.
    private LocalDate today;
    // The previous login date for this application
    private LocalDate previousLoginDate;

    // === Main method ===
    public static void main(String[] args) {
        JobApplicationSystem jobAppSystem = new JobApplicationSystem();
        DataLoaderAndStorer dataLoaderAndStorer = new DataLoaderAndStorer(jobAppSystem);
        dataLoaderAndStorer.loadAllData();
        new MainFrame(jobAppSystem);
    }

    // === Public methods ===
    // === Getters ===

    public UserManager getUserManager() {
        return this.userManager;
    }

    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

    public LocalDate getToday() {
        return this.today;
    }

    public LocalDate getPreviousLoginDate() {
        return this.previousLoginDate;
    }

    // === Setters ===
    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public void setToday(LocalDate newDate) {
        this.today = newDate;
        Notification.setDate(newDate);
    }

    public void setPreviousLoginDate(LocalDate date) {
        this.previousLoginDate = date;
    }


    // === Other methods ===

    /**
     * Gets the company with this name.
     *
     * @param name The name of the company.
     * @return the company with this name or null if cannot be found.
     */
    public Company getCompany(String name) {
        for (Company company : this.companies) {
            if (company.getName().equalsIgnoreCase(name))
                return company;
        }
        return null;
    }

    /**
     * Add a company to the system.
     *
     * @param name The name of the company.
     * @return the company created.
     */
    public Company createCompany(String name) {
        Company company = new Company(name);
        this.companies.add(company);
        return company;
    }

    /**
     * Methods that removes the files from one's account if user has not been active for at least 30 days.
     */
    public void applicant30Day() {
        for (Applicant app : this.userManager.getAllApplicants()) {
            app.getDocumentManager().removeFilesFromAccountIfInactive(this.today);
        }
    }

    /**
     * Updates all the interview rounds that have been completed.
     */
    public void updateAllJobPostings() {
        for (Company company : this.companies) {
            company.updateJobPostings(this.getToday());
        }
    }

    public ArrayList<CompanyJobPosting> getAllOpenCompanyJobPostings() {
        ArrayList<CompanyJobPosting> allOpenCompanyJobPostings = new ArrayList<>();
        for (Company company : this.getCompanies()) {
            allOpenCompanyJobPostings.addAll(company.getAllOpenCompanyJobPostings());
        }
        return allOpenCompanyJobPostings;
    }

    public ArrayList<CompanyJobPosting> getOpenCompanyJobPostingsInField(String field) {
        ArrayList<CompanyJobPosting> openCompanyJobPostingsInField = new ArrayList<>();
        for (Company company : this.getCompanies()) {
            openCompanyJobPostingsInField.addAll(company.getAllOpenJobPostingsInField(field));
        }
        return openCompanyJobPostingsInField;
    }

    public ArrayList<CompanyJobPosting> getOpenCompanyJobPostingsInCompany(String companyName) {
        Company company = this.getCompany(companyName);
        if (company != null) {
            return company.getAllOpenCompanyJobPostings();
        }
        return new ArrayList<>();
    }

    public CompanyJobPosting getCompanyJobPostingWithID(int id) {
        for (Company company : this.getCompanies()) {
            CompanyJobPosting jobPosting = company.getJobPostingWithID(id);
            if (jobPosting != null) {
                return jobPosting;
            }
        }
        return null;
    }

    public ArrayList<CompanyJobPosting> getCompanyJobPostingsWithTags(String[] tags) {
        ArrayList<CompanyJobPosting> companyJobPostings = new ArrayList<>();
        for (Company company : this.getCompanies()) {
            companyJobPostings.addAll(company.getJobPostingsWithTags(tags));
        }
        return companyJobPostings;
    }

    /**
     * Get a list of open job postings not yet applied to.
     *
     * @return a list of open job postings not yet applied to.
     */
    public ArrayList<BranchJobPosting> getOpenJobPostingsNotAppliedTo(Applicant applicant) {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        for (Company company : this.getCompanies())
            for (Branch branch : company.getBranches()) {
                BranchJobPostingManager jpm = branch.getJobPostingManager();
                ArrayList<BranchJobPosting> openPostings = jpm.getOpenJobPostings(this.getToday());
                openPostings.retainAll(jpm.getJobPostingsNotAppliedToByApplicant(applicant));
                jobPostings.addAll(openPostings);
            }
        return jobPostings;
    }

    public ArrayList<CompanyJobPosting> getCompanyJobPostingsInCMA(Applicant applicant) {
        ArrayList<CompanyJobPosting> companyJobPostings = new ArrayList<>();
        for (Company company : this.getCompanies()) {
            for (CompanyJobPosting companyJobPosting : company.getAllOpenCompanyJobPostings()) {
                if (companyJobPosting.hasBranchJobPostingInCma(applicant.getCma(), this.today)) {
                    companyJobPostings.add(companyJobPosting);
                }
            }
        }
        return companyJobPostings;
    }

}