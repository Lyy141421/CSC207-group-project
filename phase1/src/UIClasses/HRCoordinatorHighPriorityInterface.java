package UIClasses;

import Managers.InterviewManager;
import Managers.JobPostingManager;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

class HRCoordinatorHighPriorityInterface extends HRCoordinatorInterface {
    /**
     * Interface for the HRCoordinator high priority sub menu.
     */

    // === Instance variable ===
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorHighPriorityInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

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
     * @param sc     The scanner for user input.
     * @param today  Today's date.
     * @param option The option number selected.
     */
    private void runMenuAction(Scanner sc, LocalDate today, int option) {
        switch (option) {
            case 1: // Select applicants for phone interview
                this.selectJobAppsForPhoneInterview(sc, today);
                break;
            case 2: // Schedule interviews for job posting
                this.displayPostingsThatNeedInterviewsScheduled(today);
                break;
            case 3: // Hire applicants for a job posting
                this.hireApplicants(sc, today);
                break;
            case 4: // Exit
                break;
        }
    }

    /**
     * Interface for selecting the job applications for phone interviews for this job posting.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
     */
    private void selectJobAppsForPhoneInterview(Scanner sc, LocalDate today) {
        ArrayList<JobPosting> jobPostings = this.displayRecentlyClosedPostings(today);
        if (jobPostings.isEmpty()) {
            return;
        }
        JobPosting jobPosting = this.selectJobPostingForPhoneInterview(sc, jobPostings);
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
     * Interface for viewing recently closed postings and selecting a job posting for which the HR Coordinator wants to
     * select phone interview candidates.
     *
     * @param today Today's date.
     * @return the job posting selected.
     */
    private ArrayList<JobPosting> displayRecentlyClosedPostings(LocalDate today) {
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
    private JobPosting selectJobPostingForPhoneInterview(Scanner sc, ArrayList<JobPosting> jobPostings) {
        System.out.println("Enter the job posting number for which you would like to select phone interview candidates.");
        int option = this.getMenuOption(sc, jobPostings.size());
        return jobPostings.get(option - 1);
    }

    /**
     * Interface for reviewing all job applications after a job posting has closed.
     */
    private void reviewApplicationsForJobPosting(JobPosting jobPosting) {
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        jobPosting.createInterviewManager();
    }

    /**
     * The interface for viewing postings that need interviews scheduled and automatically scheduling them if possible.
     *
     * @param today Today's date.
     */
    private void displayPostingsThatNeedInterviewsScheduled(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
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
        Company company = jobPosting.getCompany();
        String field = jobPosting.getField();
        if (!company.hasInterviewerForField(field)) {
            System.out.println("Interviews cannot be set-up for this job posting as there are no interviewers for this field.");
        } else {
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
     * Interface for hiring an applicant.
     *
     * @param sc    The scanner for user input.
     * @param today Today's date.
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
        JobPosting jobPosting = this.selectJobPostingForPhoneInterview(sc, readyForHiring);
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
     * @param sc              The scanner for user input.
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
