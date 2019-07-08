package GUIClasses;

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

    // === Methods for GUI to call ===

    /**
     * Get a list of list of interviews for this interviewer by date.
     * @param today Today's date.
     * @return  a list of lists of interviews for this interviewer by date.
     */
    ArrayList<ArrayList<Interview>> getInterviewsBeforeOnAndAfterToday(LocalDate today) {
        return this.interviewer.getInterviewsBeforeOnAndAfterDate(today);
    }

    /**
     * Set this interview as pass or fail.
     * @param pass  Whether or not the applicant passed this interview.
     */
    void passOrFailInterview(Interview interview, boolean pass) {
        if (pass) {
            this.interviewer.passInterview(interview);
        }
        else {
            this.interviewer.failInterview(interview);
        }
    }

    /**
     * Store interview notes for this interview.
     * @param interview The interview for which the notes are written.
     * @param notes The notes taken during the interview.
     */
    void storeInterviewNotes(Interview interview, String notes) {
        interview.setInterviewNotes(notes);
    }

    /**
     * Schedule this interview on this date and time slot.
     *
     * @param interview The interview to be scheduled.
     * @param date      The date chosen.
     * @param timeSlot  The time slot chosen.
     * @return true iff this interview can be scheduled on this date and at this time.
     */
    boolean scheduleInterview(Interview interview, LocalDate date, int timeSlot) {
        InterviewTime interviewTime = new InterviewTime(date, timeSlot);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Get a list of unscheduled interviews for this interviewer.
     *
     * @return a list of unscheduled interviews for this interviewer.
     */
    ArrayList<Interview> getUnscheduledInterviews() {
        return this.interviewer.getUnscheduledInterviews();
    }

    // ============================================================================================================== //

    /**
     * Run the Interviewer interface.
     * @param today Today's date.
     */
    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.scheduleInterviews(sc, today);
        this.viewInterviewsForToday(today);
        while (true) {
            try {
                this.runMainMenu(sc);
            } catch (ExitException ee) {
                break;
            }
        }
    }

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
        System.out.println("3 - Search specific job application");
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
                this.viewSchedule();
                break;
            case 2: // View job applications of all interviewees
                this.viewAllJobApplications();
                break;
            case 3: // Search specific job application
                this.getJobApplication(sc);
                break;
            case 4: // View all previous interviews for specific job app
                this.viewPreviousInterviewsForJobApp(sc);
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
        int i = 1;
        System.out.println("Interviews that need to be scheduled: ");
        if (unscheduledInterviews.isEmpty()) {
            System.out.println("N/A");
        }
        else {
            for (Interview interview : unscheduledInterviews) {
                System.out.println(i + ".");
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
     * Interface for viewing one's schedule.
     */
    private void viewSchedule() {
        String schedule = this.interviewer.getScheduleString();
        System.out.println();
        if (schedule == null) {
            System.out.println("Schedule is empty.");
        } else {
            System.out.println(schedule);
        }
    }

    /**
     * Interface for viewing the interviews that this interviewer is going to conduct today.
     * @param today Today's date
     */
    private void viewInterviewsForToday(LocalDate today) {
        List<Interview> interviews = this.interviewer.getInterviewsBeforeOnAndAfterDate(today).get(1);
        if (!interviews.isEmpty()) {
            System.out.println("Interviews for today: ");
            for (Interview interview : interviews) {
                System.out.println();
                System.out.println(interview.toStringPrelimInfo() + "\n" + "Interview time: " + interview.getTime());
            }
        }
    }

    /**
     * Interface for viewing all the applications of applicants this interviewer is going to interview.
     */
    private void viewAllJobApplications() {
        ArrayList<JobApplication> jobApps = this.interviewer.getListOfIntervieweeJobApplications();
        System.out.println();
        if (jobApps.isEmpty()) {
            System.out.println("You have no interviewees.");
        }
        else {
            System.out.println("Your interviewees' job applications:");
            for (JobApplication jobApplication : jobApps) {
                System.out.println();
                System.out.println(jobApplication);
            }
        }
    }

    /**
     * Get the job application with the id that the user inputs.
     *
     * @param sc The scanner for user input.
     * @return the job application with the id that the user inputs or null if not found.
     */
    private JobApplication getJobApplication(Scanner sc) {
        System.out.println();
        int id = this.getInteger(sc, "Enter the ID of the job application you wish to view: ");
        JobApplication jobApplication = this.interviewer.findJobAppById(id);
        if (jobApplication == null) {
            System.out.println("This job application cannot be found.");
        }
        else {
            System.out.println(jobApplication);
        }
        return jobApplication;
    }

    /**
     * Interface for viewing the previous interviews for this job application (all except the one that is currently scheduled)
     *
     * @param sc The scanner for user input
     */
    private void viewPreviousInterviewsForJobApp(Scanner sc) {
        JobApplication jobApp = this.getJobApplication(sc);
        System.out.println();
        System.out.println("Previous interviews:");
        if (jobApp.getInterviews().size() == 1) {
            System.out.println("None");
        } else {
            ArrayList<Interview> interviews = jobApp.getInterviews();
            for (Interview interview : interviews.subList(0, interviews.size() - 1)) {
                System.out.println();
                System.out.println(interview);
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
        int id = this.getInteger(sc,"Enter the ID of the interview you wish to view: ");
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
        if (input.equals("N"))
            this.interviewer.failInterview(interview);
        else
            this.interviewer.passInterview(interview);
    }

    /**
     * Interface for conducting an interview.
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
