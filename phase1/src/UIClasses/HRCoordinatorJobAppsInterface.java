package UIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.util.ArrayList;
import java.util.Scanner;

class HRCoordinatorJobAppsInterface extends UserInterface {
    /**
     * Interface for the HRCoordinator job applications sub menu.
     */

    // === Instance variable ===
    // The HR Coordinator who logged in
    private HRCoordinator HRC;


    // === Constructor ===
    HRCoordinatorJobAppsInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }


    /**
     * Run the job application sub menu.
     *
     * @param sc                   the scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     */
    void runMenu(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        JobPosting jobPosting = new HRCoordinatorInterface(this.HRC).getJobPosting(sc);
        if (jobPosting == null || this.noItemsToViewForJobPosting(jobPosting)) {
            return;
        }
        int numOptions = this.displayMenu();
        int option = this.getMenuOption(sc, numOptions);
        this.runMenuAction(sc, jobApplicationSystem, jobPosting, option);
    }

    // ============================================================================================================== //
    // === Private methods ===
    /**
     * Display the job application sub-menu.
     *
     * @return the number of options.
     */
    private int displayMenu() {
        System.out.println("\nPlease select an option below:");
        System.out.println("1 - Search for specific application");
        System.out.println("2 - View all applications in consideration");
        System.out.println("3 - View all applications rejected");
        System.out.println("4 - View email list of applicants rejected");
        System.out.println("5 - View all applications");
        System.out.println("6 - Return to main menu");
        return 6;
    }

    /**
     * Print that no applications can be viewed.
     *
     * @param jobPosting Job posting in question.
     * @return true iff there are no items to view for this job posting.
     */
    private boolean noItemsToViewForJobPosting(JobPosting jobPosting) {
        if (jobPosting.getInterviewManager() == null) {
            System.out.println("\nJob posting is still open. No applications to view.");
            return true;
        }
        return false;
    }

    /**
     * Run the action selected by the user in the job application sub menu.
     *
     * @param sc                   The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     * @param jobPosting           The job posting selected.
     * @param option               The option number selected.
     */
    private void runMenuAction(Scanner sc, JobApplicationSystem jobApplicationSystem, JobPosting jobPosting, int option) {
        switch (option) {
            case 1: // Search job application
                this.searchSpecificJobApplication(sc, jobApplicationSystem, jobPosting);
                break;
            case 2: // View all apps in consideration
                this.displayAppsInConsideration(jobPosting);
                break;
            case 3: // View all apps rejected
                this.displayAppsRejected(jobPosting);
                break;
            case 4: // View email list of apps rejected
                this.displayRejectedEmailList(jobPosting);
                break;
            case 5: // View all applications
                this.displayAllApps(jobPosting);
                break;
            case 6: // Return to main menu
                break;
        }
    }

    /**
     * Interface for searching for a specific job application.
     *
     * @param sc                   The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     * @param jobPosting           The job posting in question.
     */
    private void searchSpecificJobApplication(Scanner sc, JobApplicationSystem jobApplicationSystem,
                                              JobPosting jobPosting) {
        Applicant applicant = new HRCoordinatorInterface(this.HRC).searchSpecificApplicant(sc, jobApplicationSystem);
        if (applicant != null) {
            JobApplication jobApp = jobPosting.findJobApplication(applicant);
            if (jobApp == null) {
                System.out.println("Job application cannot be found.");
                return;
            }
            System.out.println(jobApp);
        }
    }

    /**
     * Display all apps in consideration for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void displayAppsInConsideration(JobPosting jobPosting) {
        ArrayList<JobApplication> jobAppsInConsideration = jobPosting.getInterviewManager().
                getApplicationsInConsideration();
        System.out.println("Applications in consideration: ");
        this.printList(jobAppsInConsideration);
    }

    /**
     * Display all apps rejected for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void displayAppsRejected(JobPosting jobPosting) {
        ArrayList<JobApplication> jobAppsRejected = jobPosting.getInterviewManager().getApplicationsRejected();
        System.out.println("Applications rejected: ");
        this.printList(jobAppsRejected);
    }

    /**
     * Display the email list of all applicants rejected for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void displayRejectedEmailList(JobPosting jobPosting) {
        ArrayList<String> emailListRejected = jobPosting.getEmailsForRejectList();
        System.out.println("Emails of rejected applicants: ");
        this.printList(emailListRejected);
    }

    /**
     * Display all the applications for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void displayAllApps(JobPosting jobPosting) {
        ArrayList<JobApplication> jobApps = jobPosting.getJobApplications();
        System.out.println("All applications: ");
        this.printList(jobApps);
    }
}
