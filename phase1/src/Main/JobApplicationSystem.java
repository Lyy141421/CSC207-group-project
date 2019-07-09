package Main;

import FileLoadingAndStoring.*;
import GUIClasses.MainFrame;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;
import Managers.UserManager;
import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interviewer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class JobApplicationSystem {

    // === Instance variables ===
    // The time in Milliseconds for the cyclicalTask to repeat
    private static final int CYCLE_PERIOD = 86400000;
    // List of companies registered in the system
    private static ArrayList<Company> companies = new ArrayList<>();
    // The user manager for the system
    private static UserManager userManager = new UserManager();
    // The date this program interprets as today (Defaults to today)
    public static LocalDate today = LocalDate.now();
    // The previous login date for this application
    public static LocalDate previousLoginDate;

    // === Public methods ===

    public static void main(String[] args) {
        mainStart();
        cyclicalTask();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame(today);
            }
        });
    }

    // === Getters ===

    public static UserManager getUserManager() {
        return JobApplicationSystem.userManager;
    }

    public static ArrayList<Company> getCompanies() {
        return JobApplicationSystem.companies;
    }

    public static LocalDate getToday() {
        return JobApplicationSystem.today;
    }

    public static LocalDate getPreviousLoginDate() {
        return JobApplicationSystem.previousLoginDate;
    }

    // === Setters ===
    public static void setPreviousLoginDate(LocalDate date) {
        JobApplicationSystem.previousLoginDate = date;
    }

    // === Other methods ===
    /**
     A method which triggers once a day from the time it is started.
     */
    public static void cyclicalTask(){
        TimerTask daily_tasks = new TimerTask() {
            public void run() {
                applicant30Day();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(daily_tasks, 0, CYCLE_PERIOD);
    }

    /**
     * To be called at the start of the program
     * Used to load all objects from json memory
     */
    public static void mainStart() {
        LoaderManager applicant = new LoaderManager(new ApplicantLoader(), Applicant.class, Applicant.FILENAME);
        LoaderManager hrcoordinator = new LoaderManager(new HRCoordinatorLoader(), HRCoordinator.class, HRCoordinator.FILENAME);
        LoaderManager interviewer = new LoaderManager(new InterviewerLoader(), Interviewer.class, Interviewer.FILENAME);
        LoaderManager company = new LoaderManager(new CompanyLoader(), Company.class, Company.FILENAME);
        LoaderManager interview = new LoaderManager(new InterviewLoader(), Interview.class, Interview.FILENAME);
        LoaderManager jobposting = new LoaderManager(new JobPostingLoader(), JobPosting.class, JobPosting.FILENAME);
        LoaderManager jobapplication = new LoaderManager(new JobApplicationLoader(), JobApplication.class, JobApplication.FILENAME);
        LoaderManager.startLoad();
        userManager.addUserList(applicant.getArray());
        userManager.addUserList(hrcoordinator.getArray());
        userManager.addUserList(interviewer.getArray());
        for(Object comp : company.getArray()){
            companies.add((Company)comp);
        }
    }

    /**
     * To be called at the end of the Program
     * Used to Save all Objects to json memory
     */
    public static void mainEnd(){
        StorerManager.flushStored();
        StorerManager applicant = new StorerManager(new ApplicantStorer(), Applicant.class, userManager.getAllApplicants());
        StorerManager hrcoordinator = new StorerManager(new HRCoordinatorStorer(), HRCoordinator.class, userManager.getAllHRCoordinator());
        StorerManager interviewer = new StorerManager(new InterviewerStorer(), Interviewer.class, userManager.getAllInterviewers());
        StorerManager company = new StorerManager(new CompanyStorer(), Company.class, companies);
        StorerManager interview = new StorerManager(new InterviewStorer(), Interview.class, new ArrayList());
        StorerManager jobposting = new StorerManager(new JobPostingStorer(), JobPosting.class, new ArrayList());
        StorerManager jobapplication = new StorerManager(new JobApplicationStorer(), JobApplication.class, new ArrayList());
        StorerManager.endSave();
    }

    public static void updateAllInterviewRounds() {
        for (Company company : JobApplicationSystem.companies) {
            for (JobPosting jobPosting : company.getJobPostingManager().getJobPostings()) {
                jobPosting.advanceInterviewRound();
            }
        }
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Setters ===

    static void setToday(LocalDate new_date){
        today = new_date;
    }

    static void setCompanies(ArrayList<Company> companies) {
        JobApplicationSystem.companies = companies;
    }

    static void setUserManager(UserManager userManager) {
        JobApplicationSystem.userManager = userManager;
    }

    // === Other methods ===
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
    public static Company createCompany(String name) {
        Company company = new Company(name);
        JobApplicationSystem.companies.add(company);
        return company;
    }

    // ============================================================================================================== //
    // === Private methods ===
    private static void applicant30Day(){
        for(Object app : userManager.getAllApplicants()){
          ((Applicant)app).removeFilesFromAccount(today);
        }
    }

}