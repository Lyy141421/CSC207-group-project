package UIClasses;

import Main.JobApplicationSystem;
import Managers.JobPostingManager;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

public class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */

    // === Instance variables ===
    // The HR Coordinator who is logged in
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorInterface(User user) {
        super(user);
        this.HRC = (HRCoordinator) this.user;
    }

    // === Inherited method ===
    /**
     * Run the main HR Coordinator interface.
     *
     * @param sc The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     */
    @Override
    public void run(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        System.out.println("Welcome, " + this.HRC.getLegalName() + ".\n");
        new HRCoordinatorJobPostingsInterface(this.HRC).viewPostingsWithNoApplicationsSubmitted(sc,
                jobApplicationSystem.getToday());
        this.viewPostingsWithNoApplicationsInConsideration(jobApplicationSystem.getToday());
        while (true) {
            try {
                this.runMainMenu(sc, jobApplicationSystem);
            } catch (ExitException ee) {
                break;
            }
        }
    }

    // === Methods inherited by subclasses ===

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     *
     * @param sc The scanner for user input.
     * @return the job posting being searched for.
     */
    JobPosting getJobPosting(Scanner sc) {
        int id = this.getPositiveInteger(sc, "\nEnter the job posting ID: ");
        JobPosting jobPosting = this.HRC.getCompany().getJobPostingManager().getJobPosting(id);
        if (jobPosting == null) {
            System.out.println("\nThis job posting was not found in " + this.HRC.getCompany().getName() + ".");
            return null;
        } else {
            System.out.println("\n" + jobPosting.toString());
        }
        return jobPosting;
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Interface for viewing job postings with no applications in consideration.
     *
     * @param today Today's date.
     */
    private void viewPostingsWithNoApplicationsInConsideration(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> jobPostingsNoAppsInConsideration =
                JPM.getClosedJobPostingsNoApplicationsInConsideration(today);
        if (!jobPostingsNoAppsInConsideration.isEmpty()) {
            System.out.println("Job postings with no applications in consideration: \n");
        }
        for (JobPosting jobPosting : jobPostingsNoAppsInConsideration) {
            System.out.println(jobPosting.toString());
            System.out.println("Each job posting will be automatically set to filled with number of positions 0.");
            System.out.println("You may want to consider opening other job postings with these job titles.");
            jobPosting.setNumPositions(0);
            jobPosting.setFilled();
            jobPosting.getInterviewManager().archiveRejected();
        }
    }

    /**
     * Interface for displaying main menu options.
     *
     * @return the number of options.
     */
    private int displayMainMenuOptions() {
        System.out.println("\nPlease select an option below:");
        System.out.println("1 - View high priority tasks");
        System.out.println("2 - Add a job posting");
        System.out.println("3 - Update fields for an open job posting");
        System.out.println("4 - View job postings in company");
        System.out.println("5 - View applications for a job posting");
        System.out.println("6 - View all applications a specific applicant has submitted to the company");
        System.out.println("7 - View all interviews associated with a job application");
        System.out.println("8 - Exit");
        return 8;
    }

    /**
     * Run the main menu for the HR Coordinator.
     *
     * @param sc    The Scanner for user input.
     * @throws ExitException Signals that user wants to exit the program
     */
    private void runMainMenu(Scanner sc, JobApplicationSystem jobApplicationSystem)
            throws ExitException {
        int numOptions = this.displayMainMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1: // View high priority tasks
                new HRCoordinatorHighPriorityInterface(this.HRC).runMenu(sc, jobApplicationSystem.getToday());
                break;
            case 2: // Add job posting
                new HRCoordinatorJobPostingsInterface(this.HRC).addJobPosting(sc, jobApplicationSystem.getToday());
                break;
            case 3: // Update fields
                new HRCoordinatorJobPostingsInterface(this.HRC).updateJobPostingFull(sc, jobApplicationSystem.getToday());
                break;
            case 4: // View job postings
                new HRCoordinatorJobPostingsInterface(this.HRC).runMenu(sc, jobApplicationSystem.getToday());
                break;
            case 5: // View job applications
                new HRCoordinatorJobAppsInterface(this.HRC).runMenu(sc, jobApplicationSystem);
                break;
            case 6: // View previous job apps to company
                this.viewAllJobAppsToCompany(sc, jobApplicationSystem);
                break;
            case 7: // View interviews associated with a job application
                this.viewAllInterviewsForJobApp(sc);
                break;
            case 8: // Exit
                throw new ExitException();
        }
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     * @param sc    The scanner for user input.
     * @param jobApplicationSystem  The job application system that is being used.
     */
    Applicant searchSpecificApplicant(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        String username = this.getInputToken(sc, "\nEnter the applicant username you would like to view: ");
        Applicant applicant = (Applicant) jobApplicationSystem.getUserManager().findUserByUsername(username);
        sc.nextLine();
        if (applicant == null) {
            System.out.println("This applicant cannot be found.");
            return null;
        }
        if (this.HRC.getCompany().hasAppliedHere(applicant)) {
            return applicant;
        }
        System.out.println("This applicant has not applied to this company.");
        return null;
    }

    /**
     * Interface for viewing all the previous job applications this applicant has submitted to this company.
     * @param sc   The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     */
    private void viewAllJobAppsToCompany(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        Applicant applicant = this.searchSpecificApplicant(sc, jobApplicationSystem);
        if (applicant != null) {
            ArrayList<JobApplication> jobApps = this.HRC.getCompany().getAllApplicationsToCompany(applicant);
            this.printList(jobApps);
        }
    }

}
