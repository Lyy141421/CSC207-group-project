package GUIClasses;

import Main.JobApplicationSystem;
import Managers.InterviewManager;
import Managers.JobPostingManager;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

public class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */

    // === Class variables ===
    private static int SKIP_FIELD_KEY = -1;
    private static LocalDate SKIP_DATE_KEY = LocalDate.parse("9999-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // === Instance variables ===
    // The HR Coordinator who is logged in
    private HRCoordinator HRC = (HRCoordinator) this.user;

    // === Constructor ===
    HRCoordinatorInterface(User user) {
        super(user);
    }

    // === Methods that GUI will call ===

    /**
     * Get the task that the HR Coordinator must accomplish at this moment for this job posting.
     */
    int getTaskForJobPosting(JobPosting jobPosting) {
        return jobPosting.getInterviewManager().getHrTask();
    }

    /**
     * Gets a list of lists of job postings that include high priority and all postings for the company.
     * @param today Today's date.
     * @return  the list of lists of job postings required.
     */
    ArrayList<ArrayList<JobPosting>> getHighPriorityAndAllJobPostings(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<ArrayList<JobPosting>> jobPostingsList = new ArrayList<>();
        jobPostingsList.add(JPM.getClosedJobPostingsNoApplicantsChosen(today));
        jobPostingsList.add(JPM.getJobPostingsWithRoundCompletedNotForHire(today));
        jobPostingsList.add(JPM.getJobPostingsForHiring(today));
        jobPostingsList.add(JPM.getJobPostings());
        return jobPostingsList;
    }

    /**
     * Add a job posting for this company.
     * @param today  Today's date.
     * @param jobPostingFields  The fields that the user inputs.
     */
    void addJobPosting(LocalDate today, Object[] jobPostingFields) {
        String title = (String) jobPostingFields[0];
        String field = (String) jobPostingFields[1];
        String description = (String) jobPostingFields[2];
        String requirements = (String) jobPostingFields[3];
        int numPositions = (Integer) jobPostingFields[4];
        LocalDate closeDate = (LocalDate) jobPostingFields[5];
        this.HRC.addJobPosting(title, field, description, requirements, numPositions, today, closeDate);
    }

    /**
     * Get all job applications submitted by this applicant with this username.
     * @param applicantUsername The applicant username inputted.
     * @return  a list of job applications submitted by this applicant with this username.
     */
    ArrayList<JobApplication> getAllJobApplicationsToCompany(String applicantUsername) {
        Applicant applicant = (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(applicantUsername);
        if (applicant == null) {
            return new ArrayList<>();
        }
        return this.HRC.getCompany().getAllApplicationsToCompany(applicant);
    }

    /**
     * Hire or reject an application.
     *
     * @param jobApp The job application in question.
     * @param toHire Whether or not the HR Coordinator wants to hire the applicant.
     */
    boolean hireOrRejectApplication(JobApplication jobApp, boolean toHire) {
        if (toHire) {
            jobApp.getStatus().setHired();
        } else {
            jobApp.getStatus().setArchived();
        }
        JobPosting jobPosting = jobApp.getJobPosting();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        if (jobPosting.getInterviewManager().getNumOpenPositions() > 0) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether this job application has been rejected.
     *
     * @param jobApplication The job application in question.
     * @return true iff this job application has been rejected.
     */
    boolean isRejected(JobApplication jobApplication) {
        return jobApplication.getJobPosting().getInterviewManager().getApplicationsRejected().contains(jobApplication);
    }

    /**
     * Choose whether this application moves on for phone interviews.
     *
     * @param jobApp   The job application in question.
     * @param selected Whether or not the application is selected to move on.
     */
    void selectApplicationForPhoneInterview(JobApplication jobApp, boolean selected) {
        if (!selected) {
            jobApp.getJobPosting().getInterviewManager().reject(jobApp);
        }
    }

    /**
     * Set up interviews for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    boolean setUpInterviews(JobPosting jobPosting) {
        Company company = jobPosting.getCompany();
        String field = jobPosting.getField();
        if (!company.hasInterviewerForField(field)) {
            return false;
        } else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
            if (jobApps.isEmpty()) {
                return false;
            }
            for (JobApplication jobApp : jobApps) {
                jobApp.setUpInterview(this.HRC, jobApp.getStatus().getValue() + 1);
            }
            return true;
        }
    }


    // ============================================================================================================== //
    // === Methods for standard input === (BACK-UP)!!!
    /**
     * Run the main HR Coordinator interface.
     *
     * @param today Today's date.
     */
    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.viewPostingsWithNoApplicationsSubmitted(sc, today);
        this.viewPostingsWithNoApplicationsInConsideration(today);
        while (true) {
            try {
                this.runMainMenu(sc, today);
            } catch (ExitException ee) {
                break;
            }
        }
    }

    /**
     * Interface for displaying main menu options.
     *
     * @return the number of options.
     */
    private int displayMainMenuOptions() {
        System.out.println("Please select an option below:");
        System.out.println("1 - Add a job posting");
        System.out.println("2 - Update fields for an open job posting");
        System.out.println("3 - View job postings in company");
        System.out.println("4 - View applications for a job posting");
        System.out.println("5 - View all applications a specific applicant has submitted to the company");
        System.out.println("6 - View all interviews associated with a job application");
        System.out.println("7 - Select applicants to receive a phone interview");
        System.out.println("8 - Schedule interviews for a job posting");
        System.out.println("9 - Hire applicants for a job posting");
        System.out.println("10 - Exit");
        return 10;
    }

    /**
     * Run the main menu for the HR Coordinator.
     *
     * @param sc    The Scanner for user input.
     * @param today Today's date.
     * @throws ExitException Signals that user wants to exit the program
     */
    private void runMainMenu(Scanner sc, LocalDate today) throws ExitException {
        int numOptions = this.displayMainMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1: // Add job posting
                this.addJobPosting(sc, today);
                break;
            case 2: // Update fields
                this.viewUpdateJobPosting(sc, today);
                break;
            case 3: // View job postings
                this.runJobPostingSubMenu(sc, today);
                break;
            case 4: // View job applications
                this.runJobApplicationSubMenu(sc);
                break;
            case 5: // View previous job apps to company
                this.viewAllJobAppsToCompany(sc);
                break;
            case 6: // View interviews associated with a job application
                this.viewAllInterviewsForJobApp(sc);
                break;
            case 7: // Select applicants for phone interview
                this.selectJobAppsForPhoneInterview(sc, today);
                break;
            case 8: // Schedule interviews for job posting
                this.viewPostingsThatNeedInterviewsScheduled(today);
                break;
            case 9: // Hire applicants for a job posting
                this.hireApplicants(sc, today);
                break;
            case 10: // Exit
                throw new ExitException();
        }
    }

    /**
     * Interface for viewing the sub-menu for job postings.
     *
     * @return the number of options.
     */
    private int displayJobPostingSubMenu() {
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
     * Run the job posting sub-menu.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    private void runJobPostingSubMenu(Scanner sc, LocalDate today) {
        if (this.HRC.getCompany().getJobPostingManager().getJobPostings().isEmpty()) {
            System.out.println("\nThere are no job postings for this company.");
            return;
        }
        int numOptions = this.displayJobPostingSubMenu();
        int option = this.getMenuOption(sc, numOptions);
        PrintItems<JobPosting> printPostings = new PrintItems<>();
        switch (option) {
            case 1: // Search job posting
                this.getJobPosting(sc);
                break;
            case 2: // View open job postings
                ArrayList<JobPosting> openJobPostings = this.HRC.getCompany().getJobPostingManager().
                        getOpenJobPostings(today);
                System.out.println("\nOpen postings: ");
                printPostings.printList(openJobPostings);
                break;
            case 3: // View closed job postings not yet filled
                ArrayList<JobPosting> closedJobPostingsNotFilled = this.HRC.getCompany().getJobPostingManager().
                        getClosedJobPostingsNotFilled(today);
                System.out.println("\nClosed postings: ");
                printPostings.printList(closedJobPostingsNotFilled);
                break;
            case 4: // View all filled job postings
                ArrayList<JobPosting> filledJobPostings = this.HRC.getCompany().getJobPostingManager().
                        getFilledJobPostings();
                System.out.println("\nFilled postings: ");
                printPostings.printList(filledJobPostings);
                break;
            case 5: // View all job postings in company
                ArrayList<JobPosting> allJobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
                System.out.println("\nAll postings: ");
                printPostings.printList(allJobPostings);
                break;
            case 6: // Return to main menu
                break;
        }
    }

    /**
     * Display the job application sub-menu.
     *
     * @return the number of options.
     */
    private int displayJobApplicationSubMenu() {
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
     * Run the job application sub menu.
     *
     * @param sc the scanner for user input.
     *
     */
    private void runJobApplicationSubMenu(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        if (jobPosting == null || this.noItemsToViewForJobPosting(jobPosting)) {
            return;
        }
        int numOptions = this.displayJobApplicationSubMenu();
        int option = this.getMenuOption(sc, numOptions);
        PrintItems<JobApplication> printApps = new PrintItems<>();
        switch (option) {
            case 1: // Search job application
                this.searchSpecificJobApplication(sc, jobPosting);
                break;
            case 2: // View all apps in consideration
                ArrayList<JobApplication> jobAppsInConsideration = jobPosting.getInterviewManager().
                        getApplicationsInConsideration();
                System.out.println("Applications in consideration: ");
                printApps.printList(jobAppsInConsideration);
                break;
            case 3: // View all apps rejected
                ArrayList<JobApplication> jobAppsRejected = jobPosting.getInterviewManager().getApplicationsRejected();
                System.out.println("Applications rejected: ");
                printApps.printList(jobAppsRejected);
                break;
            case 4: // View email list of apps rejected
                ArrayList<String> emailListRejected = jobPosting.getEmailsForRejectList();
                System.out.println("Emails of rejected applicants: ");
                new PrintItems<String>().printList(emailListRejected);
                break;
            case 5: // View all applications
                ArrayList<JobApplication> jobApps = jobPosting.getJobApplications();
                System.out.println("All applications: ");
                printApps.printList(jobApps);
                break;
            case 6: // Return to main menu
                break;
        }
    }

    /**
     * Print that no applications can be viewed.
     *
     * @param jobPosting Job posting in question.
     * @return true iff there are no items to view for this job posting.
     */
    private boolean noItemsToViewForJobPosting(JobPosting jobPosting) {
        if (jobPosting.getInterviewManager() == null) {
            System.out.println("Job posting is still open. No applications to view.");
            return true;
        }
        return false;
    }

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     * @param sc    The scanner for user input.
     * @return the job posting being searched for.
     */
    private JobPosting getJobPosting(Scanner sc) {
        int id = this.getInteger(sc, "\nEnter the job posting ID: ");
        JobPosting jobPosting = this.HRC.getCompany().getJobPostingManager().getJobPosting(id);
        if (jobPosting == null) {
            System.out.println("\nThis job posting was not found in " + this.HRC.getCompany().getName() + ".");
            return null;
        } else {
            System.out.println("\n" + jobPosting.toStringStandardInput());
        }
        return jobPosting;
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
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    private void addJobPosting(Scanner sc, LocalDate today) {
        System.out.println("\nComplete the following categories for adding a job posting as they appear.");
        ArrayList<Object> fields = this.getFieldsForJobPosting(sc, today);
        this.HRC.addJobPosting((String) fields.get(0), (String) fields.get(1), (String) fields.get(2),
                (String) fields.get(3), (Integer) fields.get(4), today, (LocalDate) fields.get(5));
        System.out.println("\nYou have successfully added " + fields.get(0) + " to the system.");
    }

    /**
     * Interface for updating a job posting.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    private void viewUpdateJobPosting(Scanner sc, LocalDate today) {
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
     * @param sc        The scanner for user input.
     * @param today     Today's date.
     * @param jobPosting    The job posting to be updated.
     */
    private void updateJobPostingFields(Scanner sc, LocalDate today, JobPosting jobPosting) {
        System.out.println("Complete the following categories for updating a job posting as they appear.");
        System.out.println("Enter '" + SKIP_FIELD_KEY + "' if you do not wish to update the category and enter " +
                SKIP_DATE_KEY + " if you do not wish to update the close date.");
        jobPosting.updateFields(SKIP_FIELD_KEY, SKIP_DATE_KEY, this.getFieldsForJobPosting(sc, today));
        System.out.println("\nYou have successfully updated " + jobPosting.getTitle() + ".");
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     * @param sc    The scanner for user input.
     */
    private Applicant searchSpecificApplicant(Scanner sc) {
        String username = this.getInputToken(sc, "\nEnter the applicant username you would like to view: ");
        Applicant applicant = (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(username);
        sc.nextLine();
        if (applicant == null) {
            System.out.println("This applicant cannot be found.");
            return null;
        }
        if (this.HRC.getCompany().hasApplicantAppliedHere(applicant)) {
            return applicant;
        }
        System.out.println("This applicant has not applied to this company.");
        return null;
    }

    /**
     * Interface for viewing all the previous job applications this applicant has submitted to this company.
     */
    private void viewAllJobAppsToCompany(Scanner sc) {
        Applicant applicant = this.searchSpecificApplicant(sc);
        if (applicant != null) {
            ArrayList<JobApplication> jobApps = this.HRC.getCompany().getAllApplicationsToCompany(applicant);
            new PrintItems<JobApplication>().printList(jobApps);
        }
    }

    /**
     * Interface for searching for a specific job application.
     * @param sc The scanner for user input.
     * @param jobPosting The job posting in question.
     */
    private void searchSpecificJobApplication(Scanner sc, JobPosting jobPosting) {
        Applicant applicant = this.searchSpecificApplicant(sc);
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
     * Interface for reviewing all job applications after a job posting has closed.
     */
    private void reviewApplicationsForJobPosting(JobPosting jobPosting) {
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        jobPosting.createInterviewManager();
    }

    /**
     * Interface for selecting the job applications for phone interviews for this job posting.
     *
     * @param sc         The scanner for user input.
     * @param today     Today's date.
     */
    private void selectJobAppsForPhoneInterview(Scanner sc, LocalDate today) {
        ArrayList<JobPosting> jobPostings = this.viewRecentlyClosedPostings(today);
        if (jobPostings.isEmpty()) {
            return;
        }
        JobPosting jobPosting = this.selectJobPosting(sc, jobPostings);
        this.reviewApplicationsForJobPosting(jobPosting);
        ArrayList<JobApplication> jobApps = jobPosting.getJobApplications();
        System.out.println("Job applications submitted for this job posting: ");
        for (JobApplication jobApp : jobApps) {
            System.out.println("\n" + jobApp + "\n");
            System.out.println("Would you like to give this applicant a phone interview?");
            String response = this.getInputToken(sc, "Enter 'N' for no or any other key for yes: ");
            sc.nextLine();
            if (response.equals("N")) {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }
    }

    /**
     * Interface for setting up interviews for an entire interview round.
     * @param jobPosting    The job posting in question.
     */
    private void setUpInterviewsForRound(JobPosting jobPosting) {
        Company company = jobPosting.getCompany();
        String field = jobPosting.getField();
        if (!company.hasInterviewerForField(field)) {
            System.out.println("Interviews cannot be set-up for this job posting as there are no interviewers for this field.");
        }
        else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
            if (jobApps.isEmpty()) {
                System.out.println("No interviews to schedule.");
                return;
            }
            System.out.println("The following job applications will have interviews set-up automatically.");
            for (JobApplication jobApp : jobApps) {
                jobApp.setUpInterview(this.HRC, jobApp.getStatus().getValue() + 1);
                System.out.println("\n" + jobApp);
            }
        }
    }

    /**
     * Interface for viewing recently closed postings and selecting a job posting for which the HR Coordinator wants to
     * select phone interview candidates.
     * @param today   Today's date.
     * @return the job posting selected.
     */
    private ArrayList<JobPosting> viewRecentlyClosedPostings(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyClosed = JPM.getClosedJobPostingsNoApplicantsChosen(today);
        if (recentlyClosed.isEmpty()) {
            System.out.println("\nThere are no job postings that have recently closed.");
        } else {
            System.out.println("\nJob postings that have recently closed: ");
            new PrintItems<JobPosting>().printListToSelectFrom(recentlyClosed);
        }
        return recentlyClosed;
    }

    /**
     * Select job posting for phone interview candidate selection.
     *
     * @param sc          The scanner for user input.
     * @param jobPostings The list of job postings to select from.
     * @return the job posting selected.
     */
    private JobPosting selectJobPosting(Scanner sc, ArrayList<JobPosting> jobPostings) {
        System.out.println("Enter the job posting number for which you would like to select phone interview candidates.");
        int option = this.getMenuOption(sc, jobPostings.size());
        return jobPostings.get(option - 1);
    }

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
            System.out.println(jobPosting.toStringStandardInput());
            System.out.println("Each job posting will be automatically set to filled with number of positions 0.");
            System.out.println("You may want to consider opening other job postings with these job titles.");
            jobPosting.setNumPositions(0);
            jobPosting.setFilled();
            jobPosting.getInterviewManager().archiveRejected();
        }
    }

    /**
     * Interface for viewing and possibly updating the job postings with no applications submitted.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    private void viewPostingsWithNoApplicationsSubmitted(Scanner sc, LocalDate today) {
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

    /**
     * The interface for viewing postings that need interviews scheduled and automatically scheduling them if possible.
     * @param today Today's date.
     */
    private void viewPostingsThatNeedInterviewsScheduled(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyCompletedRound = JPM.getJobPostingsWithRoundCompletedNotForHire(today);
        if (recentlyCompletedRound.isEmpty()) {
            System.out.println("\nNo job postings need to have interviews scheduled.");
        } else {
            System.out.println("\nJob postings that need interviews scheduled: ");
            for (JobPosting jobPosting : recentlyCompletedRound) {
                System.out.println(jobPosting.toStringStandardInput());
                this.setUpInterviewsForRound(jobPosting);
            }
        }
    }

    /**
     * Interface for hiring an applicant.
     *
     * @param sc         The scanner for user input.
     * @param today     Today's date.
     */
    private void hireApplicants(Scanner sc, LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> readyForHiring = JPM.getJobPostingsForHiring(today);
        if (readyForHiring.isEmpty()) {
            System.out.println("\nNo job postings ready for hiring.");
            return;
        }
        System.out.println("\nJob postings ready for hiring: ");
        new PrintItems<JobPosting>().printListToSelectFrom(readyForHiring);
        JobPosting jobPosting = this.selectJobPosting(sc, readyForHiring);
        InterviewManager IM = jobPosting.getInterviewManager();
        ArrayList<JobApplication> jobApps;
        if (!IM.isNumApplicantUnderOrAtThreshold()) {   // Number of applications greater than num of positions
            jobApps = this.selectApplicationsForHiring(sc, jobPosting, IM.getApplicationsInConsideration());
        } else {
            this.printMessagesForHiring(IM.getNumOpenPositions());
            jobApps = IM.getApplicationsInConsideration();
        }
        for (JobApplication jobApp : jobApps) {
            jobApp.getStatus().setHired();
            System.out.println("The new hire's email: " + jobApp.getApplicant().getEmail());
        }
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
    }

    /**
     * Print messages for hiring.
     *
     * @param numberOfOpenPositions The number of positions still open.
     */
    private void printMessagesForHiring(int numberOfOpenPositions) {
        if (numberOfOpenPositions == 0) {
            System.out.println("The number of final candidates equals the number of positions for this job.");
            System.out.println("These candidates will be hired automatically and the job posting will be set as filled.");
        } else {
            System.out.println("The number of final candidates is less than the number of positions for this job.");
            System.out.println("Only these final candidates will be hired automatically.");
            System.out.println("The job posting will be set as filled with the number of positions as the number of " +
                    "people actually hired.");
            System.out.println("Remaining positions: " + numberOfOpenPositions);
            System.out.println("You may want to consider opening another job posting for the same title " +
                    "in order to fill the remaining positions.");
        }
    }

    /**
     * Interface for selecting the application of the applicant who is to be hired.
     *
     * @param sc The scanner for user input.
     * @param finalCandidates The list of final candidates after all interview rounds have been completed.
     * @return the job application selected.
     */
    private ArrayList<JobApplication> selectApplicationsForHiring(Scanner sc, JobPosting jobPosting,
                                                                 ArrayList<JobApplication> finalCandidates) {
        ArrayList<JobApplication> hires = new ArrayList<>();
        int i = 1;
        System.out.println("The number of applications in consideration exceeds the number of positions available.");
        System.out.println("The final candidates' applications: ");
        for (JobApplication jobApp : finalCandidates) {
            System.out.println(i + ".");
            System.out.println(jobApp + "\n");
            i++;
        }
        int numPositions = jobPosting.getNumPositions();
        System.out.println("You must select " + numPositions + " applicant(s).");
        for (int j = 0; j < numPositions; j++) {
            String message = "\nEnter the value corresponding to an applicant that you would like to hire: ";
            int appNumber = this.getInteger(sc, message);
            hires.add(finalCandidates.get(appNumber - 1));
        }
        return hires;
    }

}
