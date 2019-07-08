package GUIClasses;

import Main.JobApplicationSystem;
import Managers.InterviewManager;
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
    private HRCoordinator HRC = (HRCoordinator) this.user;

    // === Constructor ===
    HRCoordinatorInterface(User user) {
        super(user);
    }

    // === Methods that GUI will call ===

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


    // ============================================================================================================== //
    // === Methods for standard input === (BACK-UP)!!!
    /**
     * Run the main HR Coordinator interface.
     *
     * @param today Today's date.
     */
    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.viewHighPriorityJobPostings(sc, today);
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
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - Add a job posting");
        System.out.println("2 - View job postings in company");
        System.out.println("3 - View applications for a job posting");
        System.out.println("4 - View all applications a specific applicant has submitted to the company");
        System.out.println("5 - Exit");
        return 5;
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
            case 2: // View job postings
                this.runJobPostingSubMenu(sc, today);
                break;
            case 3: // View job applications
                this.runJobApplicationSubMenu(sc);
                break;
            case 4: // View previous job apps to company
                Applicant applicant = this.searchSpecificApplicant(sc);
                this.viewAllJobAppsToCompany(applicant);
                break;
            case 5: // Exit
                throw new ExitException();
        }
    }

    /**
     * Interface for viewing the sub-menu for job postings.
     *
     * @return the number of options.
     */
    private int displayJobPostingSubMenu() {
        System.out.println();
        System.out.println("Please select an option below:");
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
        int numOptions = this.displayJobPostingSubMenu();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1: // Search job posting
                this.getJobPosting(sc);
                break;
            case 2: // View open job postings
                this.viewAllOpenJobPostingsInCompany(today);
                break;
            case 3: // View closed job postings not yet filled
                this.viewAllClosedJobPostingsNotFilledInCompany(today);
                break;
            case 4: // View all filled job postings
                this.viewAllFilledJobPostingsInCompany();
                break;
            case 5: // View all job postings in company
                this.viewAllJobPostingsInCompany();
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
        System.out.println();
        System.out.println("Please select an option below:");
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
        int numOptions = this.displayJobApplicationSubMenu();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1: // Search job application
                this.searchSpecificJobApplication(sc, jobPosting);
                break;
            case 2: // View all apps in consideration
                this.viewAppsInConsiderationForJobPosting(jobPosting);
                break;
            case 3: // View all apps rejected
                this.viewAppsRejectedForJobPosting(jobPosting);
                break;
            case 4: // View email list of apps rejected
                this.viewEmailsOfRejected(jobPosting);
                break;
            case 5: // View all applications
                this.viewAllApplicationsForJobPosting(jobPosting);
                break;
            case 6: // Return to main menu
                break;
        }
    }

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     * @param sc    The scanner for user input.
     * @return the job posting being searched for.
     */
    private JobPosting getJobPosting(Scanner sc) {
        System.out.println();
        int id = this.getInteger(sc, "Enter the ID of the job posting you would like to view: ");
        JobPosting jobPosting = this.HRC.getCompany().getJobPostingManager().getJobPosting(id);
        if (jobPosting == null) {
            System.out.println("This job posting was not found in " + this.HRC.getCompany().getName() + ".");
            return this.getJobPosting(sc);
        } else {
            System.out.println(jobPosting);
        }
        return jobPosting;
    }

    /**
     * Interface for adding a new job posting to the system.
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    private void addJobPosting(Scanner sc, LocalDate today) {
        System.out.println();
        System.out.println("Complete the following categories for adding a job posting as they appear.");
        String title = this.getOnlyLetters(sc, "Job title: ");
        String field = this.getOnlyLetters(sc, "Job field: ");
        String description = this.getInputLine(sc, "Job description: ");
        String requirements = this.getInputLine(sc, "Job requirements: ");
        int numPositions = this.getInteger(sc, "Number of positions: ");
        LocalDate closeDate = this.getDate(sc, today,"Close date (yyyy-mm-dd): ");
        sc.nextLine();
        this.HRC.addJobPosting(title, field, description, requirements, numPositions, today, closeDate);
        System.out.println();
        System.out.println("You have successfully added " + title + " to the system.");
    }

    /**
     * Interface for viewing all job postings within the company.
     */
    private void viewAllJobPostingsInCompany() {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
        if (jobPostings.isEmpty()) {
            System.out.println();
            System.out.println("No job postings to view.");
        }
        else {
            for (JobPosting jobPosting : jobPostings) {
                System.out.println();
                System.out.println(jobPosting);
            }
        }
    }

    /**
     * Interface for viewing all open job posting within the company.
     *
     * @param today Today's date.
     */
    private void viewAllOpenJobPostingsInCompany(LocalDate today) {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getOpenJobPostings(today);
        if (jobPostings.isEmpty()) {
            System.out.println();
            System.out.println("No job postings to view.");
        }
        else {
            for (JobPosting jobPosting : jobPostings) {
                System.out.println();
                System.out.println(jobPosting);
            }
        }
    }

    /**
     * Interface for viewing all job postings not filled within the company
     *
     * @param today Today's date
     */
    private void viewAllClosedJobPostingsNotFilledInCompany(LocalDate today) {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().
                getClosedJobPostingsNotFilled(today);
        if (jobPostings.isEmpty()) {
            System.out.println();
            System.out.println("No job postings to view.");
        }
        else {
            for (JobPosting jobPosting : jobPostings) {
                System.out.println();
                System.out.println(jobPosting);
            }
        }
    }

    /**
     * Interface for viewing all filled job postings within the company.
     */
    private void viewAllFilledJobPostingsInCompany() {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getFilledJobPostings();
        if (jobPostings.isEmpty()) {
            System.out.println();
            System.out.println("No job postings to view.");
        }
        else {
            for (JobPosting jobPosting : jobPostings) {
                System.out.println();
                System.out.println(jobPosting);
            }
        }
    }

    /**
     * Get the task that the HR Coordinator or interviewer must accomplish at this moment for this job posting.
     * @param sc The scanner for user input.
     */
    void getTaskForJobPosting(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        jobPosting.getInterviewManager().getHrTask();
    }

    /**
     * Get a list of all applications for a specific job posting.
     * @param jobPosting The job posting in question.
     */
    private void viewAllApplicationsForJobPosting(JobPosting jobPosting) {
        ArrayList<JobApplication> jobApps = jobPosting.getJobApplications();
        if (jobApps.isEmpty()) {
            System.out.println();
            System.out.println("No applications to view.");
        }
        else {
            for (JobApplication jobApp : jobApps) {
                System.out.println();
                System.out.println(jobApp);
            }
        }
    }

    /**
     * Get a list of all the job applications in consideration for a job posting.
     * @param jobPosting The job posting in question.
     */
    private void viewAppsInConsiderationForJobPosting(JobPosting jobPosting) {
        InterviewManager interviewManager = jobPosting.getInterviewManager();
        if (interviewManager == null) {
            System.out.println();
            System.out.println("Job posting is still open. No applications to view.");
        }
        else {
            ArrayList<JobApplication> jobApps = interviewManager.getApplicationsInConsideration();
            if (jobApps.isEmpty()) {
                System.out.println();
                System.out.println("No applications to view.");
            }
            else {
                for (JobApplication jobApp : jobApps) {
                    System.out.println();
                    System.out.println(jobApp);
                }
            }
        }
    }

    /**
     * Get a list of all the job applications rejected for a job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void viewAppsRejectedForJobPosting(JobPosting jobPosting) {
        InterviewManager interviewManager = jobPosting.getInterviewManager();
        if (interviewManager == null) {
            System.out.println();
            System.out.println("Job posting is still open. No applications to view.");
        }
        else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsRejected();
            if (jobApps.isEmpty()) {
                System.out.println();
                System.out.println("No applications to view.");
            }
            else {
                for (JobApplication jobApp : jobApps) {
                    System.out.println();
                    System.out.println(jobApp);
                }
            }
        }
    }

    /**
     * Interface for viewing a list of emails of applicants who have been rejected to this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void viewEmailsOfRejected(JobPosting jobPosting) {
        InterviewManager interviewManager = jobPosting.getInterviewManager();
        if (interviewManager == null) {
            System.out.println();
            System.out.println("Job posting is still open. No applications to view.");
        }
        else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsRejected();
            if (jobApps.isEmpty()) {
                System.out.println();
                System.out.println("No applications to view.");
            }
            else {
                for (JobApplication jobApp : jobApps) {
                    System.out.println();
                    System.out.println(jobApp.getApplicant().getEmail());
                }
            }
        }
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     * @param sc    The scanner for user input.
     */
    private Applicant searchSpecificApplicant(Scanner sc) {
        String username = this.getInputToken(sc, "Enter the applicant username you would like to view: ");
        Applicant applicant = (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(username);
        sc.nextLine();
        if (applicant == null) {
            System.out.println("This applicant cannot be found.");
            return null;
        }
        for (JobPosting jobPosting : this.HRC.getCompany().getJobPostingManager().getJobPostings()) {
            if (applicant.hasAppliedTo(jobPosting)) {
                return applicant;
            }
        }
        System.out.println();
        System.out.println("This applicant has not applied to this company.");
        return null;
    }

    /**
     * Interface for viewing all the previous job applications this applicant has submitted to this company.
     * @param applicant The applicant in question.
     */
    private void viewAllJobAppsToCompany(Applicant applicant) {
        if (applicant == null) {
            return;
        }
        ArrayList<JobApplication> jobApps = this.HRC.getCompany().getAllApplicationsToCompany(applicant);
        if (jobApps.isEmpty()) {
            System.out.println();
            System.out.println("No applications to view.");
        }
        else {
            System.out.println("Job applications submitted by " + applicant.getUsername() + ": ");
            for (JobApplication jobApp : jobApps) {
                System.out.println();
                System.out.println(jobApp);
            }
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
     * @param sc The scanner for user input.
     */
    private void reviewApplicationsForJobPosting(Scanner sc, JobPosting jobPosting) {
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        InterviewManager interviewManager = new InterviewManager(jobPosting,
                (ArrayList<JobApplication>) jobPosting.getJobApplications().clone(), new ArrayList<>());
        jobPosting.setInterviewManager(interviewManager);
    }

    /**
     * Interface for selecting the job applications for phone interviews for this job posting.
     *
     * @param sc         The scanner for user input.
     * @param jobPosting The job posting in question.
     * @return  a list of job postings that have been selected for a phone interview.
     */
    private ArrayList<JobApplication> selectJobAppsForPhoneInterview(Scanner sc, JobPosting jobPosting) {
        ArrayList<JobApplication> jobAppsForPhoneInt = new ArrayList<>();
        System.out.println("Job applications submitted for this job posting: ");
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            System.out.println();
            System.out.println(jobApp);
            System.out.println();
            System.out.println("Would you like to give this applicant a phone interview?");
            String response = this.getInputToken(sc, "Enter 'N' for no or any other key for yes: ");
            sc.nextLine();
            if (response.equals("N")) {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }
        return jobAppsForPhoneInt;
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
                System.out.println();
                System.out.println(jobApp);
                this.setUpInterviewForJobApplication(jobApp);
            }
        }
    }

    /**
     * Interface for setting up an interview for a job application.
     * @param jobApplication    The job application for which an interview is to be set up.
     */
    private void setUpInterviewForJobApplication(JobApplication jobApplication) {
        jobApplication.setUpInterview(this.HRC, jobApplication.getStatus().getValue() + 1);
    }

    /**
     * Interface for hiring an applicant.
     * @param sc    The scanner for user input.
     * @param jobPosting    The job posting in question.
     */
    private void hireApplicant(Scanner sc, JobPosting jobPosting) {
        ArrayList<JobApplication> finalCandidates = jobPosting.getInterviewManager().getApplicationsInConsideration();
        JobApplication jobApp;
        if (finalCandidates.size() == jobPosting.getNumPositions()) {
            jobApp = finalCandidates.get(0);
        } else {
            jobApp = this.selectApplicationForHiring(sc, finalCandidates);
        }
        jobApp.getStatus().setHired();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        System.out.println("The new hire's email: " + jobApp.getApplicant().getEmail());
    }


    /**
     * Interface for viewing recently closed postings and selecting who moves on to phone interviews.
     * @param sc      The scanner for user input.
     * @param today   Today's date.
     */
    private void viewRecentlyClosedPostings(Scanner sc, LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyClosed = JPM.getClosedJobPostingsNoApplicantsChosen(today);
        System.out.println();
        System.out.println("Job postings that have recently closed: ");
        if (recentlyClosed.isEmpty()) {
            System.out.println("N/A");
        }
        for (JobPosting jobPosting : recentlyClosed) {
            System.out.println(jobPosting);
            this.reviewApplicationsForJobPosting(sc, jobPosting);
            this.selectJobAppsForPhoneInterview(sc, jobPosting);
        }
    }

    /**
     * The interface for viewing postings that need interviews scheduled and automatically scheduling them if possible.
     * @param today Today's date.
     */
    private void viewPostingsThatNeedInterviewsScheduled(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyCompletedRound = JPM.getJobPostingsWithRoundCompletedNotForHire(today);
        System.out.println("Job postings that need interviews scheduled: ");
        if (recentlyCompletedRound.isEmpty()) {
            System.out.println("N/A");
        }
        for (JobPosting jobPosting : recentlyCompletedRound) {
            System.out.println(jobPosting);
            this.setUpInterviewsForRound(jobPosting);
        }
    }

    /**
     * Interface for viewing postings ready for hiring and selecting the final candidate if need be.
     * @param sc      The scanner for user input.
     * @param today Today's date.
     */
    private void viewPostingsReadyForHiring(Scanner sc, LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        System.out.println("Job postings ready for hiring: ");
        ArrayList<JobPosting> readyForHiring = JPM.getJobPostingsForHiring(today);
        if (readyForHiring.isEmpty()) {
            System.out.println("N/A");
        }
        for (JobPosting jobPosting : readyForHiring) {
            System.out.println(jobPosting);
            this.hireApplicant(sc, jobPosting);
        }
    }

    /**
     * Interface for viewing high-priority job postings.
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    private void viewHighPriorityJobPostings(Scanner sc, LocalDate today) {
        this.viewRecentlyClosedPostings(sc, today);
        System.out.println();
        this.viewPostingsThatNeedInterviewsScheduled(today);
        System.out.println();
        this.viewPostingsReadyForHiring(sc, today);
    }

    /**
     * Interface for selecting the application of the applicant who is to be hired.
     *
     * @param sc The scanner for user input.
     * @param finalCandidates The list of final candidates after all interview rounds have been completed.
     * @return the job application selected.
     */
    private JobApplication selectApplicationForHiring(Scanner sc, ArrayList<JobApplication> finalCandidates) {
        int i = 1;
        for (JobApplication jobApp : finalCandidates) {
            System.out.println(i + ".");
            System.out.println(jobApp);
            i++;
        }
        String message = "Enter the value corresponding to the applicant that you would like to hire: ";
        int appNumber = this.getInteger(sc, message);
        return finalCandidates.get(appNumber - 1);
    }


}
