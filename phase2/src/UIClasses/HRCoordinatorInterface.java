package UIClasses;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPosting;
import Main.JobApplicationSystem;
import CompanyStuff.BranchJobPostingManager;
import Miscellaneous.ExitException;
import ApplicantStuff.Applicant;
import CompanyStuff.HRCoordinator;
import Main.User;

import java.util.ArrayList;

public class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */

    // === Instance variables ===
    // The HR Coordinator who is logged in
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorInterface(JobApplicationSystem JAS, User user) {
        super(JAS, user);
        this.HRC = (HRCoordinator) this.user;
    }

    // === Inherited method ===

    /**
     * Run the main HR Coordinator interface.
     *
     */
    @Override
    public void run() {
        System.out.println("Welcome, " + this.HRC.getLegalName() + ".\n");
        new HRCoordinatorJobPostingsInterface(this.JAS, this.HRC).viewPostingsWithNoApplicationsSubmitted(
        );
        this.viewPostingsWithNoApplicationsInConsideration();
        while (true) {
            try {
                this.runMainMenu();
            } catch (ExitException ee) {
                break;
            }
        }
    }

    // === Methods inherited by subclasses ===

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     *
     * @return the job posting being searched for.
     */
    JobPosting getJobPosting() {
        int id = this.getPositiveInteger("\nEnter the job posting ID: ");
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
     */
    private void viewPostingsWithNoApplicationsInConsideration() {
        BranchJobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
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
     * @throws ExitException Signals that user wants to exit the program
     */
    private void runMainMenu() throws ExitException {
        int numOptions = this.displayMainMenuOptions();
        int option = this.getMenuOption(numOptions);
        switch (option) {
            case 1: // View high priority tasks
                new HRCoordinatorHighPriorityInterface(this.HRC).runMenu();
                break;
            case 2: // Add job posting
                new HRCoordinatorJobPostingsInterface(this.JAS, this.HRC).addJobPosting();
                break;
            case 3: // Update fields
                new HRCoordinatorJobPostingsInterface(this.JAS, this.HRC).updateJobPostingFull();
                break;
            case 4: // View job postings
                new HRCoordinatorJobPostingsInterface(this.JAS, this.HRC).runMenu();
                break;
            case 5: // View job applications
                new HRCoordinatorJobAppsInterface(this.JAS, this.HRC).runMenu();
                break;
            case 6: // View previous job apps to company
                this.viewAllJobAppsToCompany();
                break;
            case 7: // View interviews associated with a job application
                this.viewAllInterviewsForJobApp();
                break;
            case 8: // Exit
                throw new ExitException();
        }
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     *
     */
    Applicant searchSpecificApplicant() {
        String username = this.getInputToken("\nEnter the applicant username you would like to view: ");
        Applicant applicant = (Applicant) this.JAS.getUserManager().findUserByUsername(username);
        this.sc.nextLine();
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
     *
     */
    private void viewAllJobAppsToCompany() {
        Applicant applicant = this.searchSpecificApplicant();
        if (applicant != null) {
            ArrayList<JobApplication> jobApps = this.HRC.getCompany().getAllApplicationsToCompany(applicant);
            this.printList(jobApps);
        }
    }

}
