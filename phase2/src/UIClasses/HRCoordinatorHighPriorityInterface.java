package UIClasses;

import ApplicantStuff.JobApplication;
import CompanyStuff.*;
import CompanyStuff.Branch;

import java.util.ArrayList;

class HRCoordinatorHighPriorityInterface extends UserInterface {
    /**
     * Interface for the HRCoordinator high priority sub menu.
     */

    // === Instance variable ===
    // The HR Coordinator who logged in
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorHighPriorityInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    /**
     * Run the job posting sub-menu.
     *
     */
    void runMenu() {
        if (this.HRC.getBranch().getJobPostingManager().getJobPostings().isEmpty()) {
            System.out.println("\nThere are no job postings for this company.");
            return;
        }
        int numOptions = this.displayMenu();
        int option = this.getMenuOption(numOptions);
        this.runMenuAction(option);
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Display the sub menu for high priority tasks.
     */
    private int displayMenu() {
        System.out.println("\nPlease select an option below:");
        System.out.println("1 - Select applicants to receive a phone interview");
        System.out.println("2 - Schedule interviews");
        System.out.println("3 - Hire applicants");
        System.out.println("4 - Exit");
        return 4;
    }

    /**
     * Run the action selected by the user in the high priority submenu.
     *
     * @param option The option number selected.
     */
    private void runMenuAction(int option) {
        switch (option) {
            case 1: // Select applicants for phone interview
                this.selectJobAppsForPhoneInterview();
                break;
            case 2: // Schedule interviews for job posting
                this.displayPostingsThatNeedInterviewsScheduled();
                break;
            case 3: // Hire applicants for a job posting
                this.hireApplicants();
                break;
            case 4: // Exit
                break;
        }
    }

    /**
     * Interface for selecting the job applications for phone interviews for this job posting.
     *
     */
    private void selectJobAppsForPhoneInterview() {
        ArrayList<JobPosting> jobPostings = this.displayRecentlyClosedPostings();
        if (jobPostings.isEmpty()) {
            return;
        }
        JobPosting jobPosting = this.selectJobPostingForPhoneInterview(jobPostings);
        this.reviewApplicationsForJobPosting(jobPosting);
        ArrayList<JobApplication> jobApps = jobPosting.getJobApplications();
        System.out.println("Job applications submitted for this job posting: ");
        for (JobApplication jobApp : jobApps) {
            System.out.println("\n" + jobApp + "\n");
            System.out.println("Would you like to give this applicant a phone interview?");
            String response = this.getInputToken("Enter 'N' for no or any other key for yes: ");
            this.sc.nextLine();
            if (response.equals("N")) {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }
    }

    /**
     * Interface for viewing recently closed postings and selecting a job posting for which the HR Coordinator wants to
     * select phone interview candidates.
     *
     * @return the job posting selected.
     */
    private ArrayList<JobPosting> displayRecentlyClosedPostings() {
        JobPostingManager JPM = this.HRC.getBranch().getJobPostingManager();
        ArrayList<JobPosting> recentlyClosed = JPM.getClosedJobPostingsNoApplicantsChosen(today);
        if (recentlyClosed.isEmpty()) {
            System.out.println("\nThere are no job postings that have recently closed.");
        } else {
            System.out.println("\nJob postings that have recently closed: ");
            this.printListToSelectFrom(recentlyClosed);
        }
        return recentlyClosed;
    }

    /**
     * Select job posting for phone interview candidate selection.
     *
     * @param jobPostings The list of job postings to select from.
     * @return the job posting selected.
     */
    private JobPosting selectJobPostingForPhoneInterview(ArrayList<JobPosting> jobPostings) {
        System.out.println("\nEnter the job posting number for which you would like to select phone interview candidates.");
        int option = this.getMenuOption(jobPostings.size());
        return jobPostings.get(option - 1);
    }

    /**
     * Interface for reviewing all job applications after a job posting has closed.
     */
    private void reviewApplicationsForJobPosting(JobPosting jobPosting) {
        jobPosting.reviewJobApplications();     // This advances the jobApp status to "under review"
        jobPosting.createInterviewManager();
    }

    /**
     * The interface for viewing postings that need interviews scheduled and automatically scheduling them if possible.
     *
     */
    private void displayPostingsThatNeedInterviewsScheduled() {
        JobPostingManager JPM = this.HRC.getBranch().getJobPostingManager();
        ArrayList<JobPosting> recentlyCompletedRound = JPM.getJobPostingsWithRoundCompletedNotForHire(today);
        if (recentlyCompletedRound.isEmpty()) {
            System.out.println("\nNo job postings need to have interviews scheduled.");
        } else {
            System.out.println("\nJob postings that need interviews scheduled: ");
            for (JobPosting jobPosting : recentlyCompletedRound) {
                System.out.println(jobPosting.toString());
                this.setUpInterviewsForRound(jobPosting);
            }
        }
    }

    /**
     * Interface for setting up interviews for an entire interview round.
     *
     * @param jobPosting The job posting in question.
     */
    private void setUpInterviewsForRound(JobPosting jobPosting) {
        Branch branch = jobPosting.getBranch();
        String field = jobPosting.getField();
        if (!branch.hasInterviewerForField(field)) {
            System.out.println("\nInterviews cannot be set-up for this job posting as there are no interviewers for this field.");
        } else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
            if (jobApps.isEmpty()) {
                System.out.println("\nNo interviews to schedule.");
                return;
            }
            System.out.println("\nThe following job applications will have interviews set-up automatically.");
            for (JobApplication jobApp : jobApps) {
                jobApp.setUpInterview(this.HRC, jobApp.getStatus().getValue() + 1);
                System.out.println("\n" + jobApp);
            }
        }
    }

    /**
     * Interface for hiring an applicant.
     *
     */
    private void hireApplicants() {
        JobPostingManager JPM = this.HRC.getBranch().getJobPostingManager();
        ArrayList<JobPosting> readyForHiring = JPM.getJobPostingsForHiring(today);
        if (readyForHiring.isEmpty()) {
            System.out.println("\nNo job postings ready for hiring.");
            return;
        }
        System.out.println("\nJob postings ready for hiring: ");
        this.printListToSelectFrom(readyForHiring);
        JobPosting jobPosting = this.selectJobPostingForPhoneInterview(readyForHiring);
        InterviewManager IM = jobPosting.getInterviewManager();
        ArrayList<JobApplication> jobApps;
        if (!IM.isNumApplicationsUnderOrAtThreshold()) {   // Number of applications greater than num of positions
            jobApps = this.selectApplicationsForHiring(jobPosting, IM.getApplicationsInConsideration());
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
            System.out.println("\nThe number of final candidates equals the number of positions for this job.");
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
     * Interface for selecting the application of the applicant(s) who are to be hired.
     *
     * @param jobPosting      The job posting for which the applicant(s) will be hired.
     * @param finalCandidates The list of final candidates after all interview rounds have been completed.
     * @return the job application(s) selected.
     */
    private ArrayList<JobApplication> selectApplicationsForHiring(JobPosting jobPosting,
                                                                  ArrayList<JobApplication> finalCandidates) {
        ArrayList<JobApplication> hires = new ArrayList<>();
        System.out.println("\nThe number of applications in consideration exceeds the number of positions available.");
        System.out.println("The final candidates' applications: ");
        this.printListToSelectFrom(finalCandidates);
        int numPositions = jobPosting.getNumPositions();
        System.out.println("You must select " + numPositions + " applicant(s).");
        for (int j = 0; j < numPositions; j++) {
            String message = "\nEnter the value corresponding to an applicant that you would like to hire: ";
            int appNumber = this.getPositiveInteger(message);
            hires.add(finalCandidates.get(appNumber - 1));
        }
        return hires;
    }
}
