package Main;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.Company;
//import UIClasses.UserInterface;
import ApplicantStuff.Applicant;
import FileLoadingAndStoring.DataLoaderAndStorer;
import NotificationSystem.Notification;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JobApplicationSystem {

    // === Instance variables ===
    // List of companies registered in the system
    private static ArrayList<Company> companies = new ArrayList<>();
    // The user manager for the system
    private static UserManager userManager = new UserManager();
    // The date this program interprets as getToday.
    private LocalDate today;
    // The previous login date for this application
    private LocalDate previousLoginDate;

    // === Main method ===
    public static void main(String[] args) {
        //JobApplicationSystem.run();
    }


    // === Class methods ===
    public static void run() {
        JobApplicationSystem jobAppSystem = new JobApplicationSystem();
        DataLoaderAndStorer dataLoaderAndStorer = new DataLoaderAndStorer(jobAppSystem);
        dataLoaderAndStorer.loadAllData();
        while (true) {
            // Create and run the main frame
            // TODO these method calls would have to appear in main frame after user selects date
            jobAppSystem.notificationDate(jobAppSystem.getToday());
            jobAppSystem.applicant30Day();
            jobAppSystem.updateAllJobPostings();
        }
    }

    // === Public methods ===
    // === Getters ===

    public static UserManager getUserManager() {
        return userManager;
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
    public static Company getCompany(String name) {
        for (Company company : companies) {
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

    static  void notificationDate(LocalDate date) {
        if (!Notification.getDateStatic().equals(date)){
            Notification.setDate(date);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Methods that removes the files from one's account if user has not been active for at least 30 days.
     */
    private void applicant30Day() {
        for (Applicant app : this.userManager.getAllApplicants()) {
            app.getDocumentManager().removeFilesFromAccountIfInactive(this.today);
        }
    }

    /**
     * Updates all the interview rounds that have been completed.
     */
    private void updateAllJobPostings() {
        for (Company company : this.companies) {
            company.updateJobPostings(this.getToday());
        }
    }
}