import GUIClasses.MainFrame;

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

    /**
     * Get the UserManager for this system.
     *
     * @return the UserManager for this system.
     */
    static UserManager getUserManager() {
        return JobApplicationSystem.userManager;
    }

    static ArrayList<Company> getCompanies() {
        return companies;
    }

    // === Setters ===

    /**
     * Set the companies using this system.
     *
     * @param companies The companies using this system.
     */
    static void setCompanies(ArrayList<Company> companies) {
        JobApplicationSystem.companies = companies;
    }

    /**
     * Set the user manager for this system.
     *
     * @param userManager The user manager for this system.
     */
    static void setUserManager(UserManager userManager) {
        JobApplicationSystem.userManager = userManager;
    }

    // === Other methods ===

    /**
     * To be called at the start of the program
     * Used to load all objects from json memory
     */
    static void mainStart(){
        Loader applicant = new Loader(Applicant.class, Applicant.FILENAME);
        Loader interviewer = new Loader(Interviewer.class, Interviewer.FILENAME);
        Loader hrcoordinator = new Loader(HRCoordinator.class, HRCoordinator.FILENAME);
        Loader company = new Loader(Company.class, Company.FILENAME);
        Loader jobposting = new Loader(JobPosting.class, JobPosting.FILENAME);
        Loader jobapplication = new Loader(JobApplication.class, JobApplication.FILENAME);
        Loader interview = new Loader(Interview.class, Interview.FILENAME);
        Loader.startLoad();
        //TODO Check if all files are loaded to their managers
    }

    /**
     * To be called at the end of the Program
     * Used to Save all Objects to json memory
     */
    static void mainEnd(){
        Loader.endSave(); //todo add methods to save from managers
    }

    /**
     A method which triggers once a day from the time it is started.
     */
    private static void cyclicalTask(){
        TimerTask daily_tasks = new TimerTask() {
            public void run() {
                applicant30Day();
                // todo CleanUpfunction() here for example
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(daily_tasks, 0, CYCLE_PERIOD);
    }

    private static void applicant30Day(){
        for(Object app : userManager.getAllApplicants()){
          //((Applicant)app).removeFilesFromAccount();
            // todo add date);
        }
    }

    /**
     * Add a company to the system.
     *
     * @param name The name of the company.
     * @return the company created.
     */
    static Company createCompany(String name) {
        Company company = new Company(name);
        JobApplicationSystem.companies.add(company);
        return company;
    }

    static Company getCompany(String name) {
        for (Company company : companies) {
            if (company.getName().equalsIgnoreCase(name))
                return company;
        }
        return null;
    }

}