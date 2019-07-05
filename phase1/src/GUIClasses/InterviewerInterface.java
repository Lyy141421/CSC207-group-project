package GUIClasses;

import Miscellaneous.ExitException;
import Miscellaneous.InterviewTime;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobApplication;

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
    private Interviewer interviewer;

    // === Constructor ===

    public InterviewerInterface(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    /**
     * Run the UsersAndJobObjects.Interviewer interface.
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

    private int displayMainMenu() {
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - View schedule");
        System.out.println("2 - View job applications for all interviewees");
        System.out.println("3 - Search specific job application");
        System.out.println("4 - View all previous interviews for specific job application");
        System.out.println("5 - View a specific interview");
        System.out.println("6 - Conduct next interview");
        System.out.println("7 - Exit");
        return 7;
    }

    private void runMainMenu(Scanner sc) throws ExitException {
        int numOptions = this.displayMainMenu();
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
                this.conductInterview(sc);
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
        /*
        1. Display unscheduled interviews upon logging in
        2. For each interview, give prompt to set a date and time for it
                - Only display the timeslots that are available ??
         */
        ArrayList<Interview> unscheduledInterviews = this.interviewer.getUnscheduledInterviews();
        int i = 1;
        for (Interview interview : unscheduledInterviews) {
            System.out.println(i + ".");
            System.out.println(interview.toStringPrelimInfo());
            this.scheduleOneInterview(sc, today, interview);
        }
    }

    /**
     * Schedule one interview.
     *
     * @param sc        The scanner for user input.
     * @param interview The interview to be scheduled.
     */
    private void scheduleOneInterview(Scanner sc, LocalDate today, Interview interview) {
        System.out.println("Schedule the time and date below.");
        LocalDate interviewDate = this.getDate(sc, today, "Date (yyyy-mm-dd): ");
        System.out.println("Time slots: " + new InterviewTime().getTimeSlotsString());
        int timeSlot = this.getInteger(sc,
                "Enter the value that corresponds to the preferred time slot: ");
        InterviewTime interviewTime = new InterviewTime(interviewDate, timeSlot);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
        } else {
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
        List<Interview> interviews = this.interviewer.getInterviewsByDate(today);
        if (!interviews.isEmpty()) {
            System.out.println("Interviews for today: ");
            for (Interview interview : interviews) {
                System.out.println();
                System.out.println(interview);
            }
        }
    }

    /**
     * Interface for viewing all the applications of applicants this interviewer is going to interview.
     */
    private void viewAllJobApplications() {
        /*
        1. Display a list of applications that this interviewer has access to (they can only see the applications of those
        they have yet to interview. (Display only name of applicant and job posting?)
        2. UsersAndJobObjects.Interviewer selects job application that they want to see in more detail.
         */
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
            return this.getJobApplication(sc);
        }
        System.out.println(jobApplication);
        return jobApplication;
    }

    /**
     * Interface for viewing the previous interviews for this job application.
     *
     * @param sc The scanner for user input
     */
    private void viewPreviousInterviewsForJobApp(Scanner sc) {
        JobApplication jobApp = this.getJobApplication(sc);
        System.out.println("Previous interviews:");
        if (jobApp.getInterviews().isEmpty()) {
            System.out.println("None");
        } else {
            for (Interview interview : jobApp.getInterviews()) {
                System.out.println();
                System.out.println(interview);
            }
        }
    }

    /**
     * Interface for viewing a specific interview called by the user.
     *
     * @param sc The scanner for user input.
     */
    private void viewSpecificInterview(Scanner sc) {
        int id = this.getInteger(sc,"Enter the ID of the interview you wish to view: ");
        Interview interview = this.interviewer.findInterviewById(id);
        if (interview == null) {
            System.out.println("This interview cannot be found.");
            this.viewSpecificInterview(sc);
        }
        else {
            System.out.println(interview);
        }
    }

    /**
     * Interface for conducting an interview.
     */
    private void conductInterview(Scanner sc) {
        /*
        1. Display tab to click on for the next interview to be conducted by this interviewer.
        2. Display interface with job application info, a box for typing in interview notes and a button for pass/fail
        3. The interviewer should be able to look at any part of the job application (CV, cover letter, etc),
         write notes and select pass/fail
         */
        Interview interview = this.interviewer.getNextInterview();
        System.out.println();
        if (interview == null) {
            System.out.println("You do not have any interviews scheduled.");
        } else {
            System.out.println("Interview:");
            System.out.println(interview.toStringPrelimInfo() + "\n");
            System.out.println("UsersAndJobObjects.Applicant cover letter:");
            System.out.println(interview.getJobApplication().getCoverLetter() + "\n");
            System.out.println("UsersAndJobObjects.Applicant CV:");
            System.out.println(interview.getJobApplication().getCV() + "\n");
            String notes = this.getInputLine(sc, "Write interview notes below. Press enter when finished.\n");
            interview.setInterviewNotes(notes);
            System.out.println("Would you like to pass this applicant?");
            if (this.getInputToken(sc, "Enter 'N' for no or any other key for yes").equals("N")) {
                interviewer.failInterview(interview);
            }
            this.interviewer.removeInterview(interview);
        }
    }

}
