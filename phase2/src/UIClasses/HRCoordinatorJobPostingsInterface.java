package UIClasses;

import Managers.JobPostingManager;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.JobPosting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

class HRCoordinatorJobPostingsInterface extends UserInterface {
    /**
     * Interface for the HRCoordinator and all things to do with job postings.
     */

    // === Class variables ===
    private static int SKIP_FIELD_KEY = 0;
    private static LocalDate SKIP_DATE_KEY = LocalDate.parse("9999-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // === Instance variable ===
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorJobPostingsInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    // === Package-private methods

    /**
     * Run the job posting sub-menu.
     *
     */
    void runMenu() {
        if (this.HRC.getCompany().getJobPostingManager().getJobPostings().isEmpty()) {
            System.out.println("\nThere are no job postings for this company.");
            return;
        }
        int numOptions = this.displayMenu();
        int option = this.getMenuOption(numOptions);
        this.runMenuAction(option);
    }

    /**
     * Interface for viewing and possibly updating the job postings with no applications submitted.
     *
     */
    void viewPostingsWithNoApplicationsSubmitted() {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        for (JobPosting jobPosting : JPM.getClosedJobPostingsNoApplicationsSubmitted(today)) {
            System.out.println("\nNo applications have been submitted for this job posting:");
            System.out.println("\n" + jobPosting);
            System.out.println("Would you like to update its fields? ");
            String response = this.getInputToken("Enter 'N' for no or any other key for yes: ");
            this.sc.nextLine();
            if (!response.equals("N")) {
                this.updateJobPostingFields(jobPosting);
            } else {
                System.out.println("\nThis job posting will be removed from the system.");
                JPM.removeJobPosting(jobPosting);
            }
        }
    }

    /**
     * Interface for adding a new job posting to the system.
     *
     */
    void addJobPosting() {
        System.out.println("\n\nComplete the following categories for adding a job posting as they appear.");
        ArrayList<Object> fields = this.getFieldsForJobPosting(false);
        JobPosting jobPosting = this.HRC.addJobPosting((String) fields.get(0), (String) fields.get(1),
                (String) fields.get(2), (String) fields.get(3), (Integer) fields.get(4), this.today,
                (LocalDate) fields.get(5));
        System.out.println("\nYou have successfully added '" + jobPosting.getTitle() +
                "' (Job Posting ID: " + jobPosting.getId() + ") to the system.");
    }

    /**
     * Entire interface for updating a job posting.
     *
     */
    void updateJobPostingFull() {
        if (this.HRC.getCompany().getJobPostingManager().getOpenJobPostings(this.today).isEmpty()) {
            System.out.println("\nThere are no open job postings to be updated.");
            return;
        }
        JobPosting jobPosting = new HRCoordinatorInterface(this.HRC).getJobPosting();
        if (jobPosting != null) {
            if (jobPosting.isClosed(this.today)) {
                System.out.println("\nThis job posting is closed and can no longer be updated.");
            } else {
                this.updateJobPostingFields(jobPosting);
            }
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Interface for viewing the sub-menu for job postings.
     *
     * @return the number of options.
     */
    private int displayMenu() {
        System.out.println("\nPlease select an option below:");
        System.out.println("1 - Search for job posting");
        System.out.println("2 - View all open job postings");
        System.out.println("3 - View all closed job postings not yet filled");
        System.out.println("4 - View all filled job postings");
        System.out.println("5 - View all job postings");
        System.out.println("6 - Return to main menu");
        return 6;
    }

    /**
     * Run the action selected by the user in the job postings sub menu.
     *
     * @param option The option number selected.
     */
    private void runMenuAction(int option) {
        switch (option) {
            case 1: // Search job posting
                new HRCoordinatorInterface(this.HRC).getJobPosting();
                break;
            case 2: // View open job postings
                this.displayOpenPostings();
                break;
            case 3: // View closed job postings not yet filled
                this.displayClosedPostingsNotFilled();
                break;
            case 4: // View all filled job postings
                this.displayFilledPostings();
                break;
            case 5: // View all job postings in company
                this.displayAllPostings();
                break;
            case 6: // Return to main menu
                break;
        }
    }

    /**
     * View all open job postings.
     *
     */
    private void displayOpenPostings() {
        ArrayList<JobPosting> openJobPostings = this.HRC.getCompany().getJobPostingManager().
                getOpenJobPostings(this.today);
        System.out.println("\nOpen postings: ");
        this.printList(openJobPostings);
    }

    /**
     * View all closed postings not yet filled.
     *
     */
    private void displayClosedPostingsNotFilled() {
        ArrayList<JobPosting> closedJobPostingsNotFilled = this.HRC.getCompany().getJobPostingManager().
                getClosedJobPostingsNotFilled(this.today);
        System.out.println("\nClosed postings: ");
        this.printList(closedJobPostingsNotFilled);
    }

    /**
     * View all filled postings.
     */
    private void displayFilledPostings() {
        ArrayList<JobPosting> filledJobPostings = this.HRC.getCompany().getJobPostingManager().
                getFilledJobPostings();
        System.out.println("\nFilled postings: ");
        this.printList(filledJobPostings);
    }

    /**
     * View all postings.
     */
    private void displayAllPostings() {
        ArrayList<JobPosting> allJobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
        System.out.println("\nAll postings: ");
        this.printList(allJobPostings);
    }

    /**
     * Get the fields for a job posting by user input.
     *
     * @param update Whether or not the fields are being updated (true) vs. added (false)
     * @return a list of the fields inputted.
     */
    private ArrayList<Object> getFieldsForJobPosting(boolean update) {
        String title = this.getInputLine("Job title: ");
        String field = this.getInputLine("Job field: ");
        String description = this.getInputLine("Job description: ");
        String requirements = this.getInputLine("Job requirements: ");
        int numPositions;
        if (update) {
            numPositions = this.getNaturalNumber("Number of positions: ");
        } else {
            numPositions = this.getPositiveInteger("Number of positions: ");
        }
        LocalDate closeDate = this.getDateIncludingToday("Close date (yyyy-mm-dd): ");
        sc.nextLine();
        return new ArrayList<>(Arrays.asList(title, field, description, requirements, numPositions, closeDate));
    }

    /**
     * Interface for updating the fields for a job posting.
     *
     * @param jobPosting The job posting to be updated.
     */
    private void updateJobPostingFields(JobPosting jobPosting) {
        System.out.println("\nComplete the following categories for updating a job posting as they appear.");
        System.out.println("Enter '" + SKIP_FIELD_KEY + "' if you do not wish to update the category and enter " +
                SKIP_DATE_KEY + " if you do not wish to update the close date.\n");
        jobPosting.updateFields(SKIP_FIELD_KEY, SKIP_DATE_KEY, this.getFieldsForJobPosting(true));
        System.out.println("\nYou have successfully updated '" + jobPosting.getTitle() + "' (Job Posting ID: " +
                jobPosting.getId() + ").");
    }
}
