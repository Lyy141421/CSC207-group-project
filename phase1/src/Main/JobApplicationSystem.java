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
    private static LocalDate today = LocalDate.now();

    // === Public methods ===

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
        cyclicalTask();
    }

    // === Getters ===

    public static UserManager getUserManager() {
        return JobApplicationSystem.userManager;
    }

    public static ArrayList<Company> getCompanies() {
        return companies;
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Setters ===

    static void setDate(LocalDate new_date){
        today = new_date;
    }

    static void setCompanies(ArrayList<Company> companies) {
        JobApplicationSystem.companies = companies;
    }

    static void setUserManager(UserManager userManager) {
        JobApplicationSystem.userManager = userManager;
    }

    // === Other methods ===

    /**
     * To be called at the start of the program
     * Used to load all objects from json memory
     */
    static void mainStart() {
        LoaderManager applicant = new LoaderManager(new ApplicantLoader(), Applicant.class, Applicant.FILENAME);
        LoaderManager hrcoordinator = new LoaderManager(new HRCoordinatorLoader(), HRCoordinator.class, HRCoordinator.FILENAME);
        LoaderManager interviewer = new LoaderManager(new InterviewerLoader(), Interviewer.class, Interviewer.FILENAME);
        LoaderManager company = new LoaderManager(new CompanyLoader(), Company.class, Company.FILENAME);
        LoaderManager interview = new LoaderManager(new InterviewLoader(), Interview.class, Interview.FILENAME);
        LoaderManager jobposting = new LoaderManager(new JobPostingLoader(), JobPosting.class, JobPosting.FILENAME);
        LoaderManager jobapplication = new LoaderManager(new JobApplicationLoader(), JobApplication.class, JobApplication.FILENAME);
        LoaderManager.startLoad();
        //todo Assign = applicant.getArray()
        //todo Assign = hrcoordinator.getArray()
        //todo Assign = interviewer.getArray()
        //todo Assign = company.getArray()
        //todo Assign = interview.getArray()
        //todo Assign = jobposting.getArray()
        //todo Assign = jobapplication.getArray()
    }

    /**
     * To be called at the end of the Program
     * Used to Save all Objects to json memory
     */
//    static void mainEnd(){
//        Loader.endSave(); //todo add methods to save from managers
//    }

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
    /**
     A method which triggers once a day from the time it is started.
     */
    private static void cyclicalTask(){
        TimerTask daily_tasks = new TimerTask() {
            public void run() {
                applicant30Day();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(daily_tasks, 0, CYCLE_PERIOD);
    }

    private static void applicant30Day(){
        for(Object app : userManager.getAllApplicants()){
          ((Applicant)app).removeFilesFromAccount(today);
        }
    }

}