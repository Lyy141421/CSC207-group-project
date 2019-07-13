package UIClasses;

import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import Miscellaneous.InterviewTime;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InterviewerInterface extends UserInterface {
    /**
     * The general interviewer interface.
     */

    // === Instance variables ===
    // The interviewer who is logged in
    private Interviewer interviewer = (Interviewer) this.user;

    // === Constructor ===
    InterviewerInterface(JobApplicationSystem JAS, User user) {
        super(JAS, user);
    }

    // === Inherited method ===

    /**
     * Run the Interviewer interface.
     *
     */
    @Override
    public void run() {
        System.out.println("Welcome, " + this.user.getLegalName() + ".\n");
        this.scheduleInterviews();
        System.out.println();
        this.viewInterviewsToCompleteAfterInterviewDateHasPassed();
        while (true) {
            try {
                this.runMainMenu();
            } catch (ExitException ee) {
                break;
            }
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Interface for displaying the main menu options.
     *
     * @return the total number of options.
     */
    private int displayMainMenuOptions() {
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - View schedule");
        System.out.println("2 - View job applications for all interviewees");
        System.out.println("3 - Find specific job application for interviewee");
        System.out.println("4 - View all previous interviews for specific job application");
        System.out.println("5 - View a specific interview");
        System.out.println("6 - Complete an interview");
        System.out.println("7 - Exit");
        return 7;
    }

    /**
     * Interface for running the main menu.
     *
     * @throws ExitException if user exits.
     */
    private void runMainMenu() throws ExitException {
        int numOptions = this.displayMainMenuOptions();
        int option = this.getMenuOption(numOptions);
        switch (option) {
            case 1: // View schedule
                this.viewScheduledInterviews();
                break;
            case 2: // View job applications of all interviewees
                ArrayList<JobApplication> jobApps = this.interviewer.getListOfIntervieweeJobApplications();
                System.out.println("Your interviewees' applications: ");
                this.printList(jobApps);
                break;
            case 3: // Search specific job application
                this.getJobApplication();
                break;
            case 4: // View all previous interviews for specific job app
                this.viewAllInterviewsForJobApp();
                break;
            case 5: // View specific interview
                this.viewSpecificInterview();
                break;
            case 6: //  Conduct next interview
                this.completeInterview();
                break;
            case 7: // Exit
                throw new ExitException();
        }
    }

    /**
     * Interface for scheduling interviews set by the HR Coordinator.
     *
     */
    private void scheduleInterviews() {
        ArrayList<Interview> unscheduledInterviews = this.interviewer.getUnscheduledInterviews();
        System.out.println("Interviews that need to be scheduled: ");
        if (unscheduledInterviews.isEmpty()) {
            System.out.println("N/A");
        } else {
            for (Interview interview : unscheduledInterviews) {
                System.out.println("\n" + interview.toStringPrelimInfo());
                this.scheduleOneInterview(interview);
            }
        }
    }

    /**
     * Schedule one interview.
     *
     * @param interview The interview to be scheduled.
     */
    private void scheduleOneInterview(Interview interview) {
        System.out.println("Schedule the interview date and time below.");
        LocalDate interviewDate = this.getDateAfterToday("Date (yyyy-mm-dd): ");
        this.sc.nextLine();
        System.out.println();
        System.out.println("Time slots: " + new InterviewTime().getTimeSlotsString());
        int timeSlot = this.getPositiveInteger("Enter the value that corresponds to the preferred time slot: ");
        InterviewTime interviewTime = new InterviewTime(interviewDate, timeSlot - 1);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
        } else {
            System.out.println("You already have an interview scheduled at this time. Please select another time.");
            System.out.println();
            this.scheduleOneInterview(interview);
        }
    }

    /**
     * Interface for viewing the interviews that this interviewer is going to conduct today.
     *
     */
    private void viewInterviewsToCompleteAfterInterviewDateHasPassed() {
        List<Interview> interviews = this.interviewer.getIncompleteInterviews(today);
        if (!interviews.isEmpty()) {
            System.out.println("Interviews to complete: ");
            for (Interview interview : interviews) {
                System.out.println();
                System.out.println(interview.toStringPrelimInfo());
            }
        }
    }

    /**
     * Interface for viewing scheduled interviews.
     */
    private void viewScheduledInterviews() {
        List<Interview> interviews = this.interviewer.getScheduledInterviews();
        if (interviews.isEmpty()) {
            System.out.println("\nSchedule is empty.");
        } else {
            System.out.println("\nInterviews scheduled: ");
            for (Interview interview : interviews) {
                System.out.println("\n" + interview.toStringPrelimInfo() + "\n" + "Interview time: " + interview.getTime());
            }
        }
    }

    /**
     * Interface for viewing a specific interview called by the user.
     *
     * @return the interview that this interviewer wishes to view.
     */
    private Interview viewSpecificInterview() {
        System.out.println();
        int id = this.getPositiveInteger("Enter the interview ID: ");
        Interview interview = this.interviewer.findInterviewById(id);
        if (interview == null) {
            System.out.println("\nThis interview cannot be found.");
            return null;
        } else {
            if (interview.isComplete()) {
                System.out.println("\n" + interview);
            } else {
                System.out.println("\n" + interview.toStringPrelimInfo());
            }
            return interview;
        }
    }

    /**
     * Interface for viewing the interview and application info for this interview to be conducted.
     *
     * @param interview The interview to be conducted.
     */
    private void viewInterviewInfoAndApplicationInfo(Interview interview) {
        System.out.println("Job Posting: ");
        System.out.println(interview.getJobApplication().getJobPosting().toString());
        System.out.println("Interview:");
        System.out.println(interview.toStringPrelimInfo() + "\n");
        System.out.println("Applicant cover letter:");
        System.out.println(interview.getJobApplication().getCoverLetter().getContents() + "\n");
        System.out.println("Applicant CV:");
        System.out.println(interview.getJobApplication().getCv().getContents() + "\n");
    }

    /**
     * Interface for determining pass or fail for an interview.
     *
     * @param interview The interview in question.
     */
    private void determinePassOrFailInterview(Interview interview) {
        System.out.println("Would you like to pass this applicant?");
        String input = this.getInputToken("Enter 'N' for no or any other key for yes: ");
        this.sc.nextLine();
        if (input.equals("N")) {
            this.interviewer.failInterview(interview);
        } else {
            this.interviewer.passInterview(interview);
        }
    }

    /**
     * Interface for completing an interview.
     *
     */
    private void completeInterview() {
        if (this.interviewer.getInterviews().isEmpty()) {
            System.out.println("You do not have any interviews to complete.");
            return;
        }
        Interview interview = this.viewSpecificInterview();
        if (interview != null && !interview.isComplete()) {
            if (interview.getTime().getDate().isAfter(today)) {
                System.out.println("\nThe interview is scheduled for after today.");
                return;
            }
            System.out.println();
            this.viewInterviewInfoAndApplicationInfo(interview);
            String notes = this.getInputLinesUntilDone
                    ("Write interview notes below. Press enter twice when finished.\n");
            interview.setInterviewNotes(notes);
            this.determinePassOrFailInterview(interview);
            this.interviewer.removeInterview(interview);
        }
    }
}
