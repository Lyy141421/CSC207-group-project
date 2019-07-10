package GUIClasses;

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
import java.util.Scanner;

public class InterviewerInterface extends UserInterface {
    /**
     * The general interviewer interface.
     */

    // === Instance variables ===
    // The interviewer who is logged in
    private Interviewer interviewer = (Interviewer) this.user;

    // === Constructor ===
    InterviewerInterface(User user) {
        super(user);
    }

    // === Inherited method ===
    /**
     * Run the Interviewer interface.
     * @param sc The scanner for user input
     * @param jobApplicationSystem The job application system being used.
     */
    @Override
    public void run(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        System.out.println("Welcome, " + this.user.getLegalName() + ".\n");
        this.scheduleInterviews(sc, jobApplicationSystem.getToday());
        System.out.println();
        this.viewInterviewsToCompleteAfterInterviewDateHasPassed(jobApplicationSystem.getToday());
        while (true) {
            try {
                this.runMainMenu(sc);
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
     * @param sc The scanner for user input.
     * @throws ExitException if user exits.
     */
    private void runMainMenu(Scanner sc) throws ExitException {
        int numOptions = this.displayMainMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1: // View schedule
                this.viewScheduledInterviews();
                break;
            case 2: // View job applications of all interviewees
                ArrayList<JobApplication> jobApps = this.interviewer.getListOfIntervieweeJobApplications();
                System.out.println("Your interviewees' applications: ");
                new PrintItems<JobApplication>().printList(jobApps);
                break;
            case 3: // Search specific job application
                this.getJobApplication(sc);
                break;
            case 4: // View all previous interviews for specific job app
                this.viewAllInterviewsForJobApp(sc);
                break;
            case 5: // View specific interview
                this.viewSpecificInterview(sc);
                break;
            case 6: //  Conduct next interview
                this.completeInterview(sc);
                break;
            case 7: // Exit
                throw new ExitException();
        }
    }

    /**
     * Interface for scheduling interviews set by the HR Coordinator.
     * @param sc The scanner for local input
     * @param today Today's date
     */
    private void scheduleInterviews(Scanner sc, LocalDate today) {
        ArrayList<Interview> unscheduledInterviews = this.interviewer.getUnscheduledInterviews();
        System.out.println("Interviews that need to be scheduled: ");
        if (unscheduledInterviews.isEmpty()) {
            System.out.println("N/A");
        }
        else {
            for (Interview interview : unscheduledInterviews) {
                System.out.println(interview.toStringPrelimInfo());
                this.scheduleOneInterview(sc, today, interview);
            }
        }
    }

    /**
     * Schedule one interview.
     *
     * @param sc        The scanner for user input.
     * @param interview The interview to be scheduled.
     */
    private void scheduleOneInterview(Scanner sc, LocalDate today, Interview interview) {
        System.out.println("Schedule the interview date and time below.");
        LocalDate interviewDate = this.getDate(sc, today, "Date (yyyy-mm-dd): ");
        sc.nextLine();
        System.out.println();
        System.out.println("Time slots: " + new InterviewTime().getTimeSlotsString());
        int timeSlot = this.getInteger(sc,
                "Enter the value that corresponds to the preferred time slot: ");
        InterviewTime interviewTime = new InterviewTime(interviewDate, timeSlot - 1);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
        } else {
            System.out.println("You already have an interview scheduled at this time. Please select another time.");
            System.out.println();
            this.scheduleOneInterview(sc, today, interview);
        }
    }

    /**
     * Interface for viewing the interviews that this interviewer is going to conduct today.
     * @param today Today's date
     */
    private void viewInterviewsToCompleteAfterInterviewDateHasPassed(LocalDate today) {
        List<Interview> interviews = this.interviewer.getIncompleteInterviewsForWhichInterviewHasOccurred(today);
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
            System.out.println("Schedule is empty.");
        } else {
            System.out.println("Interviews scheduled: ");
            for (Interview interview : interviews) {
                System.out.println();
                System.out.println(interview.toStringPrelimInfo() + "\n" + "Interview time: " + interview.getTime());
            }
        }
    }

    /**
     * Interface for viewing a specific interview called by the user.
     *
     * @param sc The scanner for user input.
     * @return the interview that this interviewer wishes to view.
     */
    private Interview viewSpecificInterview(Scanner sc) {
        System.out.println();
        int id = this.getInteger(sc, "Enter the interview ID: ");
        Interview interview = this.interviewer.findInterviewById(id);
        if (interview == null) {
            System.out.println("This interview cannot be found.");
            return null;
        }
        else {
            if (interview.isComplete()) {
                System.out.println(interview);
            } else {
                System.out.println(interview.toStringPrelimInfo());
            }
            return interview;
        }
    }

    /**
     * Interface for viewing the interview and application info for this interview to be conducted.
     * @param interview The interview to be conducted.
     */
    private void viewInterviewInfoAndApplicationInfo(Interview interview) {
        System.out.println("Job Posting: ");
        System.out.println(interview.getJobApplication().getJobPosting().toStringStandardInput());
        System.out.println("Interview:");
        System.out.println(interview.toStringPrelimInfo() + "\n");
        System.out.println("Applicant cover letter:");
        System.out.println(interview.getJobApplication().getCoverLetter().getContents() + "\n");
        System.out.println("Applicant CV:");
        System.out.println(interview.getJobApplication().getCV().getContents() + "\n");
    }

    /**
     * Interface for determining pass or fail for an interview.
     * @param sc        The scanner for user input.
     * @param interview The interview in question.
     */
    private void determinePassOrFailInterview(Scanner sc, Interview interview) {
        System.out.println("Would you like to pass this applicant?");
        String input = this.getInputToken(sc, "Enter 'N' for no or any other key for yes: ");
        sc.nextLine();
        if (input.equals("N")) {
            this.interviewer.failInterview(interview);
        } else {
            this.interviewer.passInterview(interview);
        }
    }

    /**
     * Interface for completing an interview.
     * @param sc    The scanner for user input
     */
    private void completeInterview(Scanner sc) {
        if (this.interviewer.getInterviews().isEmpty()) {
            System.out.println("You do not have any interviews to complete.");
            return;
        }
        Interview interview = this.viewSpecificInterview(sc);
        if (interview != null && !interview.isComplete()) {
            System.out.println();
            this.viewInterviewInfoAndApplicationInfo(interview);
            String notes = this.getInputLinesUntilDone
                    (sc, "Write interview notes below. Press enter twice when finished.\n");
            interview.setInterviewNotes(notes);
            this.determinePassOrFailInterview(sc, interview);
            this.interviewer.removeInterview(interview);
        }
    }
}
