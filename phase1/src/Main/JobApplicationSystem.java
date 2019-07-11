package Main;

import FileLoadingAndStoring.*;
import Miscellaneous.ExitException;
import UIClasses.UserInterface;
import UsersAndJobObjects.*;
import Managers.UserManager;

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
    private LocalDate today = LocalDate.MIN;
    // The previous login date for this application
    private LocalDate previousLoginDate;

    // === Main method ===
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        JobApplicationSystem JAS = new JobApplicationSystem();
        UserInterface UI = new UserInterface();
        new PreviousLoginDateLoaderAndStorer().loadPreviousLoginDate(JAS);
        UI.getTodaysDateValid(sc, JAS);
        JAS.mainStart(JAS);
        while (true) {
            try {
                UI.run(sc, JAS);
                UI.getTodaysDateValid(sc, JAS);
            } catch (ExitException ee) {
                JAS.mainEnd(JAS);
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

    // === Private methods ===
    /**
     * To be called at the start of the program
     * Used to load all objects from json memory
     */
    private void mainStart(JobApplicationSystem JAS) {
        LoaderManager applicant = new LoaderManager(new ApplicantLoader(), Applicant.class, Applicant.FILENAME);
        LoaderManager hrcoordinator = new LoaderManager(new HRCoordinatorLoader(), HRCoordinator.class, HRCoordinator.FILENAME);
        LoaderManager interviewer = new LoaderManager(new InterviewerLoader(), Interviewer.class, Interviewer.FILENAME);
        LoaderManager company = new LoaderManager(new CompanyLoader(), Company.class, Company.FILENAME);
        LoaderManager interview = new LoaderManager(new InterviewLoader(), Interview.class, Interview.FILENAME);
        LoaderManager jobposting = new LoaderManager(new JobPostingLoader(), JobPosting.class, JobPosting.FILENAME);
        LoaderManager jobapplication = new LoaderManager(new JobApplicationLoader(), JobApplication.class, JobApplication.FILENAME);
        LoaderManager.startLoad(this);
        JAS.userManager.addUserList(applicant.getArray());
        JAS.userManager.addUserList(hrcoordinator.getArray());
        JAS.userManager.addUserList(interviewer.getArray());
        for (Object comp : company.getArray()) {
            JAS.companies.add((Company) comp);
        }
    }

    /**
     * To be called at the end of the Program
     * Used to Save all Objects to json memory
     */
    private void mainEnd(JobApplicationSystem JAS) {
        StorerManager.flushStored();
        StorerManager applicant = new StorerManager(new ApplicantStorer(), Applicant.class, JAS.userManager.getAllApplicants());
        StorerManager hrcoordinator = new StorerManager(new HRCoordinatorStorer(), HRCoordinator.class, JAS.userManager.getAllHRCoordinator());
        StorerManager interviewer = new StorerManager(new InterviewerStorer(), Interviewer.class, JAS.userManager.getAllInterviewers());
        StorerManager company = new StorerManager(new CompanyStorer(), Company.class, JAS.companies);
        StorerManager interview = new StorerManager(new InterviewStorer(), Interview.class, new ArrayList());
        StorerManager jobposting = new StorerManager(new JobPostingStorer(), JobPosting.class, new ArrayList());
        StorerManager jobapplication = new StorerManager(new JobApplicationStorer(), JobApplication.class, new ArrayList());
        StorerManager.endSave(this);
    }
}