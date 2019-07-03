import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class InterviewerInterface extends UserInterface {
    /**
     * The general interviewer interface.
     */

    // === Instance variables ===
    // The interviewer who is logged in
    private Interviewer interviewer;

    // === Constructor ===

    /**
     * Create the interface for this interviewer.
     *
     * @param interviewer The interviewer who is logged in.
     */
    InterviewerInterface(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    /**
     * Run the Interviewer interface.
     */
    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.scheduleInterviews(sc);
        this.viewInterviewsForToday(today);
        while (true) {
            try {
                this.runMainMenu(sc);
            } catch (NullPointerException npe) {
                continue;
            } catch (ExitException ee) {
                break;
            }
        }
    }

    private int displayMainMenu() {
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
     */
    private void scheduleInterviews(Scanner sc) {
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
            this.scheduleOneInterview(sc, interview);
        }
    }

    /**
     * Schedule one interview.
     *
     * @param sc        The scanner for user input.
     * @param interview The interview to be scheduled.
     */
    private void scheduleOneInterview(Scanner sc, Interview interview) {
        System.out.println("Schedule the time and date below.");
        System.out.print("\nDate (yyyy-mm-dd): ");
        String interviewDateString = sc.nextLine();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        LocalDate interviewDate = LocalDate.parse(interviewDateString, dtf);
        System.out.println("Time slots: " + InterviewTime.timeSlotsString());
        System.out.println("\nEnter the value that corresponds to the preferred time slot: ");
        int timeSlot = sc.nextInt();
        InterviewTime interviewTime = new InterviewTime(interviewDate, timeSlot);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
//            this.interviewer.updateSchedule(interviewTime);
        } else {
            this.scheduleOneInterview(sc, interview);
        }
    }

    /**
     * Interface for viewing one's schedule.
     */
    private void viewSchedule() {
        System.out.println(this.interviewer.getScheduleString());
    }

    /**
     * Interface for viewing the interviews that this interviewer is going to conduct today.
     * @param today Today's date
     */
    private void viewInterviewsForToday(LocalDate today) {
        List<Interview> interviews = this.interviewer.getInterviewsByDate(today);
        for (Interview interview : interviews) {
            System.out.println(interview);
        }
    }

    /**
     * Interface for viewing all the applications of applicants this interviewer is going to interview.
     */
    private void viewAllJobApplications() {
        /*
        1. Display a list of applications that this interviewer has access to (they can only see the applications of those
        they have yet to interview. (Display only name of applicant and job posting?)
        2. Interviewer selects job application that they want to see in more detail.
         */
        ArrayList<JobApplication> jobApps = this.interviewer.getListOfIntervieweeJobApplications();
        System.out.println("Your interviewees' job applications");
        for (JobApplication jobApplication : jobApps) {
            System.out.println(jobApplication);
        }
    }

    /**
     * Get the job application with the id that the user inputs.
     *
     * @param sc The scanner for user input.
     * @return the job application with the id that the user inputs or null if not found.
     */
    private JobApplication getJobApplication(Scanner sc) {
        try {
            System.out.println("Enter the ID of the job application you would wish to view: ");
            int id = sc.nextInt();
            JobApplication jobApplication = this.interviewer.findJobAppById(id);
            System.out.println(jobApplication);
            return jobApplication;
        } catch (NullPointerException npe) {
            System.out.println("This job application cannot be found.");
            return null;
        }
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
        try {
            System.out.println("Enter interview ID: ");
            int id = sc.nextInt();
            System.out.println(this.interviewer.findInterviewById(id));
        } catch (NullPointerException npe) {
            System.out.println("This interview cannot be found.");
            this.viewSpecificInterview(sc);
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
        System.out.println("Interview:");
        System.out.println(interview.toStringPrelimInfo() + "\n");
        System.out.println("Applicant cover letter:");
        System.out.println(interview.getJobApplication().getCoverLetter() + "\n");
        System.out.println("Applicant CV:");
        System.out.println(interview.getJobApplication().getCV() + "\n");
        System.out.println("Write interview notes below. Press enter when finished.");
        String notes = sc.nextLine();
        interview.setInterviewNotes(notes);
        System.out.print("\nWould you like to pass this applicant? ('Y' - yes, 'N' - no)");
        if (sc.nextLine().equals("N")) {
            interview.setFail();
        }
        this.interviewer.removeInterview(interview);
    }

}
