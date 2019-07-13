package Main;

import FileLoadingAndStoring.*;
import Miscellaneous.ExitException;
import UIClasses.UserInterface;
import UsersAndJobObjects.*;
import Managers.UserManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JobApplicationSystem.run();
    }


    // === Class methods ===
    public static void run() throws ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        JobApplicationSystem JAS = new JobApplicationSystem();
        UserInterface UI = new UserInterface();
        DataLoaderAndStorer dataLoaderAndStorer = new DataLoaderAndStorer(JAS, "users.ser",
                "companies.ser", "previousLoginDate.txt");
        dataLoaderAndStorer.loadAllData();
        UI.getTodaysDateValid(sc, JAS);
        while (true) {
            try {
                UI.run(sc, JAS);
                UI.getTodaysDateValid(sc, JAS);
            } catch (ExitException ee) {
                dataLoaderAndStorer.storeAllData();
                System.exit(0);
            }
        }
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
    public void setToday(LocalDate new_date) {
        this.today = new_date;
    }

    public void setPreviousLoginDate(LocalDate date) {
        this.previousLoginDate = date;
    }

    // === Other methods ===

    /**
     * Updates all the interview rounds that have been completed.
     */
    public void updateAllInterviewRounds() {
        for (Company company : this.companies) {
            for (JobPosting jobPosting : company.getJobPostingManager().getJobPostings()) {
                jobPosting.advanceInterviewRound();
            }
        }
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
        for (Object app : this.userManager.getAllApplicants()) {
            ((Applicant) app).removeFilesFromAccount(today);
        }
    }
}