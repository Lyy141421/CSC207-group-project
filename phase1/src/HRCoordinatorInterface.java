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

    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.viewHighPriorityJobPostings(sc, today);
        while (true) {
            try {
                this.displayMenuOption();
                int option = this.getMenuOption(sc);
                this.fufillAction(option);
            } catch (NullPointerException npe) {
                continue;
            }
        }
        // Give options to:
        // - view all job postings on the application: this.viewAllJobPostings();
        //  - view a job posting: this.viewAllApplicationsForJobPosting();
        //  - add a job posting: this.addJobPosting();
        //  - go back to the 'main' page: this.viewHighPriorityJobPostings();
    }


    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     * @return the job posting being searched for.
     */
    JobPosting getJobPosting(Scanner sc) {
        try {
            System.out.println("Enter the ID of the job posting you would like to view: ");
            int id = sc.nextInt();
            JobPosting jobPosting = this.HRC.getCompany().getJobPostingManager().getJobPostingByID(id);
            System.out.println(jobPosting);
            return jobPosting;
        } catch (NullPointerException npe) {
            System.out.println("This job posting was not found in " + this.HRC.getCompany() + ".");
            return null;
        }
    }

    /**
     * Interface for adding a new job posting to the system.
     */
    void addJobPosting(Scanner sc, LocalDate today) {
        System.out.println("Complete the following categories for adding a job posting as they appear.");
        System.out.print("\nJob title: ");
        String title = sc.nextLine();
        System.out.print("\nJob field: ");
        String field = sc.nextLine();
        System.out.print("\nJob description: ");
        String description = sc.nextLine();
        System.out.print("\nJob requirements: ");
        String requirements = sc.nextLine();
        System.out.println("\nNumber of positions: ");
        int numPositions = sc.nextInt();
        System.out.print("\nClose date (yyyy-mm-dd): ");
        String closeDateString = sc.nextLine();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        LocalDate closeDate = LocalDate.parse(closeDateString, dtf);
        this.HRC.addJobPosting(title, field, description, requirements, numPositions, today, closeDate);
    }

    /**
     * Interface for viewing all job postings within the company.
     */
    void viewAllJobPostingsInCompany() {
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().getJobPostingManager().getJobPostings();
        for (JobPosting jobPosting : jobPostings) {
            System.out.println(jobPosting + "\n");
        }
    }

    /**
     * Get the task that the HR Coordinator or interviewer must accomplish at this moment for this job posting.
     */
    void getTaskForJobPosting(Scanner sc, LocalDate today) {
        JobPosting jobPosting = this.getJobPosting(sc);
        jobPosting.getInterviewManager().getHrTask(today);
    }

    /**
     * Get a list of all applications for a specific job posting.
     */
    void viewAllApplicationsForJobPosting(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            System.out.println(jobApp + "\n");
        }
    }

    /**
     * Get a list of all the job applications in consideration for a job posting.
     */
    void viewAppsInConsiderationForJobPosting(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
        for (JobApplication jobApp : jobApps) {
            System.out.println(jobApp + "\n");
        }
    }

    /**
     * Searching for a specific applicant who has applied to the company.
     */
    Applicant searchSpecificApplicant(Scanner sc) {
        try {
            System.out.print("Enter the applicant username you would like to view: ");
            String username = sc.nextLine();
            return (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(username);
        } catch (NullPointerException npe) {
            System.out.println("This applicant does not exist.");
            return null;
        }
    }

    /**
     * Interface for viewing all the previous job applications this applicant has submitted to this company.
     */
    void viewPreviousJobAppsToCompany(Applicant applicant) {
        for (JobApplication jobApp : this.HRC.getCompany().getAllApplicationsToCompany(applicant)) {
            System.out.println(jobApp + "\n");
        }
    }

    /**
     * Interface for searching for a specific job application.
     */
    void searchSpecificJobApplication(Scanner sc) {
        try {
            JobPosting jobPosting = this.getJobPosting(sc);
            Applicant applicant = this.searchSpecificApplicant(sc);
            jobPosting.findJobApplication(applicant);
        } catch (NullPointerException npe) {
            System.out.println("Job application cannot be found.");
        }
    }

    /**
     * Interface for reviewing all job applications after a job posting has closed.
     */
    void reviewApplicationsForJobPosting(Scanner sc) {
        /*
        1. Get the job posting title
        2. Show all applications for job posting with a button on the side to advance (default is reject)
        */

        JobPosting jobPosting = this.getJobPosting(sc);
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        InterviewManager interviewManager = new InterviewManager(jobPosting, jobPosting.getJobApplications(),
                new ArrayList<>());
        jobPosting.setInterviewManager(interviewManager);
        this.selectJobAppsForPhoneInterview(sc, jobPosting);
    }

    void selectJobAppsForPhoneInterview(Scanner sc, JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            System.out.println(jobApp);
            System.out.println();
            System.out.println("Would do like to advance this applicant for phone interviews?");
            System.out.print("\nEnter 'Y' for yes or any other key for no: ");
            String response = sc.nextLine();
            if (response.equals("Y")) {
                jobApp.advanceStatus();
            } else {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }
    }

    /**
     * Interface for setting up interviews for an entire interview round.
     */
    void setUpInterviewsForRound(JobPosting jobPosting) {
        /*
        1. Get the job posting by title.
        2. Show list of applications still in the running
        3. Set up an interview for each job posting
         */
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
    void setUpInterviewForJobApplication(JobApplication jobApplication) {
        /*
        1. Find the interviewer in the specific job field that has the least number of interviews scheduled
        2. Match interviewer with application
         */
        jobApplication.setUpInterview(this.HRC, jobApplication.getStatus());
    }

    /**
     * Interface for hiring an applicant.
     */
    void hireApplicant(Scanner sc, JobPosting jobPosting) {
        /*
        1. Get the job posting or automatically display to HR Coordinator when logging in that the interview process
        for this job posting has been completed.
        2. Get a list of final candidates
        3. Choose applicant and hire
        4. Set the posting as filled
         */
        ArrayList<JobApplication> finalCandidates = jobPosting.getInterviewManager().getApplicationsInConsideration();
        JobApplication jobApp;
        if (finalCandidates.size() == 1) {
            jobApp = finalCandidates.get(0);
        } else {
            jobApp = this.selectApplicationForHiring(sc, finalCandidates);
        }
        jobApp.advanceStatus();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        System.out.println("The new hire's email: " + jobApp.getApplicant().getEmail());
    }

    /**
     * Interface for viewing a list of emails of applicants who have been rejected to this job posting.
     */
    void viewEmailsOfRejected(Scanner sc) {
        JobPosting jobPosting = this.getJobPosting(sc);
        for (JobApplication jobApp : jobPosting.getInterviewManager().getApplicationsRejected()) {
            System.out.println(jobApp.getApplicant().getEmail());
        }
    }

    /**
     * Interface for viewing high-priority job postings.
     *
     * @param today Today's date.
     */
    void viewHighPriorityJobPostings(Scanner sc, LocalDate today) {
        /*
        1. View list of job postings that have
            a) Closed, but have not yet started the interview process
            b) Finished an interview round (not ready for hiring)
            c) Ready for hiring
            d) Finished an interview round with <= positions available number of applicants left
        2. Give option to set up interviews or to hire given the circumstances
            a) Choose candidates for phone interview and set-up interviews (this can be two separate steps)
            b) Set-up next round of interviews
            c) Choose applicant(s) to be hired.
            d) Choose to hire all applicants left or review applications failed in most recent round.
         */
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<JobPosting> recentlyClosed = JPM.getClosedJobPostingsNoInterview(today);
        for (JobPosting jobPosting : recentlyClosed) {
            this.selectJobAppsForPhoneInterview(sc, jobPosting);
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
     * @param finalCandidates The list of final candidates after all interview rounds have been completed.
     * @return the job application selected.
     */
    JobApplication selectApplicationForHiring(Scanner sc, ArrayList<JobApplication> finalCandidates) {
        int i = 1;
        for (JobApplication jobApp : finalCandidates) {
            System.out.println(i + ".");
            System.out.println(jobApp);
            i++;
        }
        System.out.println("Enter the value corresponding to the applicant that you would like to hire: ");
        int appNumber = sc.nextInt();
        return finalCandidates.get(appNumber - 1);
    }


}
