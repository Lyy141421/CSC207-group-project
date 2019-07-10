package GUIClasses;

import Managers.JobPostingManager;
import UsersAndJobObjects.JobPosting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class HRCoordinatorJobPostingsInterface extends HRCoordinatorInterface {

    // === Class variables ===
    private static int SKIP_FIELD_KEY = -1;
    private static LocalDate SKIP_DATE_KEY = LocalDate.parse("9999-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * Run the job posting sub-menu.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    void runMenu(Scanner sc, LocalDate today) {
        if (this.HRC.getCompany().getJobPostingManager().getJobPostings().isEmpty()) {
            System.out.println("\nThere are no job postings for this company.");
            return;
        }
        int numOptions = this.displayMenu();
        int option = this.getMenuOption(sc, numOptions);
        this.runMenuAction(sc, today, option);
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
        System.out.println("5 - View all ");
        System.out.println("5 - View all job postings");
        System.out.println("6 - Return to main menu");
        return 6;
    }

    /**
     * Run the action selected by the user in the job postings sub menu.
     *
     * @param sc     The scanner for user input.
     * @param today  Today's date.
     * @param option The option number selected.
     */
    private void runMenuAction(Scanner sc, LocalDate today, int option) {
        switch (option) {
            case 1: // Search job posting
                this.getJobPosting(sc);
                break;
            case 2: // View open job postings
                this.displayOpenPostings(today);
                break;
            case 3: // View closed job postings not yet filled
                this.displayClosedPostingsNotFilled(today);
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
     * @param today Today's date.
     */
    private void displayOpenPostings(LocalDate today) {
        ArrayList<JobPosting> openJobPostings = this.HRC.getCompany().getJobPostingManager().
                getOpenJobPostings(today);
        System.out.println("\nOpen postings: ");
        new PrintItems<JobPosting>().printList(openJobPostings);
    }

    /**
     * View all closed postings not yet filled.
     *
     * @param today Today's date.
     */
    private void displayClosedPostingsNotFilled(LocalDate today) {
        ArrayList<JobPosting> closedJobPostingsNotFilled = this.HRC.getCompany().getJobPostingManager().
                getClosedJobPostingsNotFilled(today);
        System.out.println("\nClosed postings: ");
        new PrintItems<JobPosting>().printList(closedJobPostingsNotFilled);
    }

    /**
     * View all filled postings.
     */
    private void displayFilledPostings() {
        ArrayList<JobPosting> filledJobPostings = this.HRC.getCompany().getJobPostingManager().
                getFilledJobPostings();
        System.out.println("\nFilled postings: ");
        new PrintItems<JobPosting>().printList(filledJobPostings);
    }

    /**
     * View all postings.
     */
    private void displayAllPostings() {
        ArrayList<JobPosting> allJobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
        System.out.println("\nAll postings: ");
        new PrintItems<JobPosting>().printList(allJobPostings);
    }

    /**
     * Get the fields for a job posting by user input.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     * @return a list of the fields inputted.
     */
    private ArrayList<Object> getFieldsForJobPosting(Scanner sc, LocalDate today) {
        String title = this.getInputLine(sc, "Job title: ");
        String field = this.getInputLine(sc, "Job field: ");
        String description = this.getInputLine(sc, "Job description: ");
        String requirements = this.getInputLine(sc, "Job requirements: ");
        int numPositions = this.getInteger(sc, "Number of positions: ");
        LocalDate closeDate = this.getDate(sc, today, "Close date (yyyy-mm-dd): ");
        sc.nextLine();
        return new ArrayList<>(Arrays.asList(title, field, description, requirements, numPositions, closeDate));
    }

    /**
     * Interface for adding a new job posting to the system.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    void addJobPosting(Scanner sc, LocalDate today) {
        System.out.println("\nComplete the following categories for adding a job posting as they appear.");
        ArrayList<Object> fields = this.getFieldsForJobPosting(sc, today);
        JobPosting jobPosting = this.HRC.addJobPosting((String) fields.get(0), (String) fields.get(1),
                (String) fields.get(2), (String) fields.get(3), (Integer) fields.get(4), today,
                (LocalDate) fields.get(5));
        System.out.println("\nYou have successfully added '" + jobPosting.getTitle() +
                "' (Job Posting ID: " + jobPosting.getId() + ") to the system.");
    }

    /**
     * Entire interface for updating a job posting.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    void updateJobPostingFull(Scanner sc, LocalDate today) {
        if (this.HRC.getCompany().getJobPostingManager().getOpenJobPostings(today).isEmpty()) {
            System.out.println("\nThere are no open job postings to be updated.");
            return;
        }
        JobPosting jobPosting = this.getJobPosting(sc);
        if (jobPosting != null) {
            if (jobPosting.isClosed(today)) {
                System.out.println("This job posting is closed and can no longer be updated.");
            } else {
                this.updateJobPostingFields(sc, today, jobPosting);
            }
        }
    }

    /**
     * Interface for updating the fields for a job posting.
     *
     * @param sc         The scanner for user input.
     * @param today      Today's date.
     * @param jobPosting The job posting to be updated.
     */
    private void updateJobPostingFields(Scanner sc, LocalDate today, JobPosting jobPosting) {
        System.out.println("Complete the following categories for updating a job posting as they appear.");
        System.out.println("Enter '" + SKIP_FIELD_KEY + "' if you do not wish to update the category and enter " +
                SKIP_DATE_KEY + " if you do not wish to update the close date.\n");
        jobPosting.updateFields(SKIP_FIELD_KEY, SKIP_DATE_KEY, this.getFieldsForJobPosting(sc, today));
        System.out.println("\nYou have successfully updated '" + jobPosting.getTitle() + "' (Job Posting ID: " +
                jobPosting.getId() + ").");
    }

    /**
     * Interface for viewing and possibly updating the job postings with no applications submitted.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    void viewPostingsWithNoApplicationsSubmitted(Scanner sc, LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        for (JobPosting jobPosting : JPM.getClosedJobPostingsNoApplicationsSubmitted(today)) {
            System.out.println("\nNo applications have been submitted for this job posting:");
            System.out.println("\n" + jobPosting);
            System.out.println("Would you like to update its fields? ");
            String response = this.getInputToken(sc, "Enter 'N' for no or any other key for yes: ");
            sc.nextLine();
            if (!response.equals("N")) {
                this.updateJobPostingFields(sc, today, jobPosting);
            } else {
                System.out.println("\nThis job posting will be removed from the system.");
                JPM.removeJobPosting(jobPosting);
            }
        }
    }
}
