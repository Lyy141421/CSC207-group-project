package Main;

import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.Company;
//import UIClasses.UserInterface;
import ApplicantStuff.Applicant;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JobApplicationSystem {

    // === Instance variables ===
    // List of companies registered in the system
    private ArrayList<Company> companies = new ArrayList<>();
    // The user manager for the system
    private UserManager userManager = new UserManager();
    // The date this program interprets as today.
    private LocalDate today;
    // The previous login date for this application
    private LocalDate previousLoginDate;

    // === Main method ===
    public static void main(String[] args) {
        //JobApplicationSystem.run();
    }


/*    // === Class methods ===
    public static void run() throws ClassNotFoundException, IOException {
        JobApplicationSystem JAS = new JobApplicationSystem();
        UserInterface UI = new UserInterface(JAS);
        DataLoaderAndStorer dataLoaderAndStorer = new DataLoaderAndStorer(JAS, "./files/users.ser",
                "./files/companies.ser", "./files/previousLoginDate.txt");
        dataLoaderAndStorer.loadAllData();
        while (true) {
            try {
                UI.getTodaysDateValid();
                JAS.applicant30Day();
                JAS.updateAllInterviewRounds();
                UI.run();
            } catch (ExitException ee) {
                dataLoaderAndStorer.storeAllData();
                System.exit(0);
            }
        }
    }*/

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

    public void setToday(LocalDate new_date) {
        this.today = new_date;
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

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Methods that removes the files from one's account if user has not been active for at least 30 days.
     */
    private void applicant30Day() {//todo remove
        for (Applicant app : this.userManager.getAllApplicants()) {
            app.getDocumentManager().removeFilesFromAccountIfInactive(this.today);
        }
    }

    /**
     * Updates all the interview rounds that have been completed.
     */
    private void updateJobPostings() {
        for (Company company : this.companies) {
            company.updateJobPostings(this.getToday());
        }
    }
}