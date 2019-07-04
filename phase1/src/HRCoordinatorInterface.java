import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */


    // === Instance variables ===
    // The HR Coordinator who is logged in
    private HRCoordinator HRC;

    // === Constructor ===

    /**
     * Create the interface for this HR Coordinator.
     *
     * @param HRC The HR Coordinator who is logged in.
     */
    HRCoordinatorInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

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
            } catch (NullPointerException npe) {
                continue;
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
                this.viewPreviousJobAppsToCompany(applicant);
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
        System.out.println("1 - Search for job posting");
        System.out.println("2 - View all open job postings");
        System.out.println("3 - View all closed job postings not yet filled");
        System.out.println("4 - View all filled job postings");
        System.out.println("5 - View all job postings");
        System.out.println("6 - Exit");
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
            case 6: // Exit
                break;
        }
    }

    /**
     * Display the job application sub-menu.
     *
     * @return the number of options.
     */
    private int displayJobApplicationSubMenu() {
        System.out.println("1 - Search for specific application");
        System.out.println("2 - View all applications in consideration");
        System.out.println("3 - View all applications rejected");
        System.out.println("4 - View email list of applicants rejected");
        System.out.println("5 - View all applications");
        System.out.println("6 - Exit");
        return 6;
    }

    /**
     * Run the job application sub menu.
     *
     * @param sc the scanner for user input.
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
            case 6: // Exit
                break;
        }
    }

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     * @param sc    The scanner for user input.
     * @return the job posting being searched for.
     */
    JobPosting getJobPosting(Scanner sc) {
        try {
            int id = this.getInteger(sc,
                    "Enter the ID of the job posting you would like to view: ");
            JobPosting jobPosting = this.HRC.getCompany().getJobPostingManager().getJobPosting(id);
            System.out.println(jobPosting);
            return jobPosting;
        } catch (NullPointerException npe) {
            System.out.println("This job posting was not found in " + this.HRC.getCompany() + ".");
            return null;
        }
    }

    /**
     * Interface for adding a new job posting to the system.
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    void addJobPosting(Scanner sc, LocalDate today) {
        System.out.println();
        System.out.println("Complete the following categories for adding a job posting as they appear.");
        String title = this.getInputLine(sc, "Job title: ");
        String field = this.getInputLine(sc, "Job field: ");
        String description = this.getInputLine(sc, "Job description: ");
        String requirements = this.getInputLine(sc, "Job requirements: ");
        int numPositions = this.getInteger(sc, "Number of positions");
        // TODO Exception handling for dates
        String closeDateString = this.getInputToken(sc, "Close date (yyyy-mm-dd): ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        LocalDate closeDate = LocalDate.parse(closeDateString, dtf);
        this.HRC.addJobPosting(title, field, description, requirements, numPositions, today, closeDate);
    }

    /**
     * Interface for viewing all job postings within the company.
     */
    private void viewAllJobPostingsInCompany() {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
        for (JobPosting jobPosting : jobPostings) {
            System.out.println(jobPosting);
        }
    }

    /**
     * Interface for viewing all open job posting within the company.
     *
     * @param today Today's date.
     */
    private void viewAllOpenJobPostingsInCompany(LocalDate today) {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getOpenJobPostings(today);
        for (JobPosting jobPosting : jobPostings) {
            System.out.println(jobPosting);
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
        for (JobPosting jobPosting : jobPostings) {
            System.out.println(jobPosting);
        }
    }

    /**
     * Interface for viewing all filled job postings within the company.
     */
    private void viewAllFilledJobPostingsInCompany() {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getFilledJobPostings();
        for (JobPosting jobPosting : jobPostings) {
            System.out.println(jobPosting);
        }
    }

    /**
     * Get the task that the HR Coordinator or interviewer must accomplish at this moment for this job posting.
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    void getTaskForJobPosting(Scanner sc, LocalDate today) {
        JobPosting jobPosting = this.getJobPosting(sc);
        jobPosting.getInterviewManager().getHrTask(today);
    }

    /**
     * Get a list of all applications for a specific job posting.
     * @param jobPosting The job posting in question.
     */
    private void viewAllApplicationsForJobPosting(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            System.out.println(jobApp);
        }
    }

    /**
     * Get a list of all the job applications in consideration for a job posting.
     * @param jobPosting The job posting in question.
     */
    private void viewAppsInConsiderationForJobPosting(JobPosting jobPosting) {
        ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
        for (JobApplication jobApp : jobApps) {
            System.out.println(jobApp);
        }
    }

    /**
     * Get a list of all the job applications rejected for a job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void viewAppsRejectedForJobPosting(JobPosting jobPosting) {
        ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsRejected();
        for (JobApplication jobApp : jobApps) {
            System.out.println(jobApp);
        }
    }

    /**
     * Interface for viewing a list of emails of applicants who have been rejected to this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    private void viewEmailsOfRejected(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getInterviewManager().getApplicationsRejected()) {
            System.out.println(jobApp.getApplicant().getEmail());
        }
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     * @param sc    The scanner for user input.
     */
    private Applicant searchSpecificApplicant(Scanner sc) {
        try {
            String username = this.getInputToken(sc, "Enter the applicant username you would like to view: ");
            Applicant applicant = (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(username);
            for (JobPosting jobPosting : this.HRC.getCompany().getJobPostingManager().getJobPostings()) {
                if (applicant.hasAppliedTo(jobPosting)) {
                    return applicant;
                }
            }
        throw new NullPointerException();
        } catch (NullPointerException npe) {
            System.out.println("This applicant cannot be found.");
            return null;
        }
    }

    /**
     * Interface for viewing all the previous job applications this applicant has submitted to this company.
     * @param applicant The applicant in question.
     */
    private void viewPreviousJobAppsToCompany(Applicant applicant) {
        for (JobApplication jobApp : this.HRC.getCompany().getAllApplicationsToCompany(applicant)) {
            System.out.println(jobApp);
        }
    }

    /**
     * Interface for searching for a specific job application.
     * @param sc The scanner for user input.
     * @param jobPosting The job posting in question.
     */
    private void searchSpecificJobApplication(Scanner sc, JobPosting jobPosting) {
        try {
            Applicant applicant = this.searchSpecificApplicant(sc);
            jobPosting.findJobApplication(applicant);
        } catch (NullPointerException npe) {
            System.out.println("Job application cannot be found.");
        }
    }

    /**
     * Interface for reviewing all job applications after a job posting has closed.
     * @param sc The scanner for user input.
     */
    private void reviewApplicationsForJobPosting(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        InterviewManager interviewManager = new InterviewManager(jobPosting, jobPosting.getJobApplications(),
                new ArrayList<>());
        jobPosting.setInterviewManager(interviewManager);
        this.selectJobAppsForPhoneInterview(sc, jobPosting);
    }

    /**
     * Interface for selecting the job applications for phone interviews for this job posting.
     *
     * @param sc         The scanner for user input.
     * @param jobPosting The job posting in question.
     */
    private void selectJobAppsForPhoneInterview(Scanner sc, JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            System.out.println(jobApp);
            System.out.println();
            System.out.println("Would you like to advance this applicant for phone interviews?");
            String response = this.getInputToken(sc, "Enter 'Y' for yes or any other key for no: ");
            if (response.equals("Y")) {
                jobApp.advanceStatus();
            } else {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }
    }

    /**
     * Interface for setting up interviews for an entire interview round.
     * @param jobPosting    The job posting in question.
     */
    private void setUpInterviewsForRound(JobPosting jobPosting) {
        System.out.println("The following job applications will have interviews set-up automatically.");
        for (JobApplication jobApp : jobPosting.getInterviewManager().getApplicationsInConsideration()) {
            System.out.println(jobApp);
            this.setUpInterviewForJobApplication(jobApp);
        }
    }

    /**
     * Interface for setting up an interview for a job application.
     * @param jobApplication    The job application for which an interview is to be set up.
     */
    private void setUpInterviewForJobApplication(JobApplication jobApplication) {
        jobApplication.setUpInterview(this.HRC, jobApplication.getStatus());
    }

    /**
     * Interface for hiring an applicant.
     * @param sc    The scanner for user input.
     * @param jobPosting    The job posting in question.
     */
    private void hireApplicant(Scanner sc, JobPosting jobPosting) {
        ArrayList<JobApplication> finalCandidates = jobPosting.getInterviewManager().getApplicationsInConsideration();
        JobApplication jobApp;
        if (finalCandidates.size() == 1) {
            jobApp = finalCandidates.get(0);
        } else {
            jobApp = this.selectApplicationForHiring(sc, finalCandidates);
        }
        jobApp.setHired();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        System.out.println("The new hire's email: " + jobApp.getApplicant().getEmail());
    }

    /**
     * Interface for viewing high-priority job postings.
     * @param sc The scanner for user input.
     * @param today Today's date.
     */
    private void viewHighPriorityJobPostings(Scanner sc, LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyClosed = JPM.getClosedJobPostingsNoInterview(today);
        for (JobPosting jobPosting : recentlyClosed) {
            this.reviewApplicationsForJobPosting(sc);
            this.setUpInterviewsForRound(jobPosting);
        }
        ArrayList<JobPosting> recentlyCompletedRound = JPM.getJobPostingsWithRoundCompletedNotForHire(today);
        for (JobPosting jobPosting : recentlyCompletedRound) {
            this.setUpInterviewsForRound(jobPosting);
        }
        ArrayList<JobPosting> readyForHiring = JPM.getJobPostingsForHiring(today);
        for (JobPosting jobPosting : readyForHiring) {
            this.hireApplicant(sc, jobPosting);
        }
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
        int appNumber = Integer.parseInt(this.getInputToken(sc, message));
        return finalCandidates.get(appNumber - 1);
    }


}
