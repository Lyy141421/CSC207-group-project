import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class JobApplicationSystem {

    // === Instance variables ===
    // List of companies registered in the system
    static ArrayList<Company> companies = new ArrayList<>();
    // The user manager for the system
    static UserManager userManager;

    public static void main(String[] args) {
        cyclicalTask();
    }

    // === Getters ===

    /**
     * Get a list of companies registered in this system.
     *
     * @return a list of companies registered in this system.
     */
    static ArrayList<Company> getCompanies() {
        return JobApplicationSystem.companies;
    }

    /**
     * Get the UserManager for this system.
     *
     * @return the UserManager for this system.
     */
    static UserManager getUserManager() {
        return JobApplicationSystem.userManager;
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
    /*
    A method which triggers once a day from the time it is started.
     */
    private static void cyclicalTask(){
        TimerTask daily_tasks = new TimerTask() {
            public void run() {
                // Insert any daily methods here:
                // todo CleanUpfunction() here for example
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(daily_tasks, 0, 86400000);
    }

    /**
     * Get all the job postings in this system.
     * @return all the job postings in this system.
     */
    static ArrayList<JobPosting> getAllJobPostings() {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (Company company : JobApplicationSystem.companies) {
            jobPostings.addAll(company.getJobPostings());
        }
        return jobPostings;
    }

    /**
     * Add a company to the system.
     *
     * @param name The name of the company.
     * @return the company with this name.
     */
    static Company addCompany(String name) {
        return new Company(name);
    }
}
