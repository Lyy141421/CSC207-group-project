import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    void openingScreen() {
        this.scheduleInterviews();
    }

    /**
     * Interface for scheduling interviews set by the HR Coordinator.
     */
    void scheduleInterviews() {
        /*
        1. Display unscheduled interviews upon logging in
        2. For each interview, give prompt to set a date and time for it
                - Only display the timeslots that are available
         */
        /*
        ArrayList<Interview> unscheduledInterviews = this.interviewer.getUnscheduledInterviews();
        for (Interview interview : unscheduledInterviews) {
            LocalDate date = // choose date;
            int timSlot = // choose timeSlot;
            interview.setTime(new InterviewTime(date, timeSlot));
            this.interviewer.updateSchedule(date, timeSlot);
        }*/
    }

    /**
     * Interface for viewing one's schedule.
     */
    void viewSchedule() {
        // HashMap<LocalDate, Integer> schedule = this.interviewer.getSchedule();
        // Interface stuff
    }

    /**
     * Interface for viewing the interviews that this interviewer is going to conduct today.
     */
    void viewInterviewsForToday() {
        // List<Interview> interviews = this.interviewer.getInterviewsByDate(today);
        // Interface stuff
    }

    /**
     * Interface for viewing the applications of an applicant this interviewer is going to interview.
     */
    void viewJobApplication() {
        /*
        1. Display a list of applications that this interviewer has access to (they can only see the applications of those
        they have yet to interview. (Display only name of applicant and job posting?)
        2. Interviewee selects job application that they want to see in more detail.
         */
        // ArrayList<JobApplication> jobApps = this.interviewer.getListOfIntervieweeJobApplications();
        // Interface stuff
    }

    /**
     * Interface for conducting an interview.
     */
    void conductInterview() {
        /*
        1. Display tab to click on for the next interview to be conducted by this interviewer.
        2. Display interface with job application info, a box for typing in interview notes and a button for pass/fail
        3. The interviewer should be able to look at any part of the job application (CV, cover letter, etc),
         write notes and select pass/fail
         */
        /*Interview interview = this.interviewer.getNextInterview();
        interview.setInterviewNotes(notes);
        if (Interviewer pressed fail) {
            interview.setFail();
        }
        this.interviewer.removeInterview(interview);*/
    }

}
