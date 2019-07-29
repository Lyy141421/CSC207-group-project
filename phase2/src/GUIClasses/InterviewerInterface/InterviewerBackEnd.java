package GUIClasses.InterviewerInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import Main.JobApplicationSystem;
import Miscellaneous.InterviewTime;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class InterviewerBackEnd {
    /**
     * Back end of the interviewer GUI.
     */

    // === Instance variables ===
    private Interviewer interviewer;    // The interviewer who logged in
    private JobApplicationSystem jobAppSystem;  // The job application system being used.

    // === Constructor ===
    InterviewerBackEnd(JobApplicationSystem jobAppSystem, Interviewer interviewer) {
        this.interviewer = interviewer;
        this.jobAppSystem = jobAppSystem;
    }

    // === Getter ===
    Interviewer getInterviewer() {
        return this.interviewer;
    }

    // === Other methods ===

    /**
     * Check whether the date selected by the interviewer is after getToday's date as set in the job application system.
     *
     * @param date The date that the interviewer inputted.
     * @return true iff the date is after getToday's date.
     */
    boolean dateSelectedIsAfterToday(LocalDate date) {
        return date.isAfter(this.jobAppSystem.getToday());
    }

    /**
     * Gets tomorrow's date.
     *
     * @return tomorrow's date as set in the job application system.
     */
    int[] getTomorrow() {
        LocalDate tomorrow = jobAppSystem.getToday().plusDays(1);
        return new int[]{tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth()};
    }

    /**
     * Checks whether this interviewer has already written notes for this interview.
     *
     * @param interview The interview in question.
     * @return true iff this interviewer has already written notes for this interview.
     */
    boolean hasAlreadyWrittenNotes(Interview interview) {
        return interview.hasAlreadyWrittenNotesForInterview(interviewer);
    }

    /**
     * Gets the folder for this job application in the company "server".
     *
     * @param jobApp The job application selected by the interviewer to view.
     * @return the file object that holds the files submitted for this job application.
     */
    File getFolderForJobApplication(JobApplication jobApp) {
        return this.interviewer.getBranch().getCompany().getDocumentManager().getFolderForJobApplication(jobApp);
    }

    /**
     * Get a list of interviews that are incomplete.
     *
     * @return a list of interviews that are incomplete.
     */
    ArrayList<Interview> getAllIncompleteInterviews() {
        return this.interviewer.getAllIncompleteInterviews();
    }

    /**
     * Get a list of incomplete interviews that have already occurred.
     *
     * @return a list of incomplete interviews that have already occurred.
     */
    ArrayList<Interview> getIncompleteInterviewsAlreadyOccurred() {
        return this.interviewer.getIncompleteInterviewsAlreadyOccurred(this.jobAppSystem.getToday());
    }

    /**
     * Checks whether this interviewer is the coordinator for this interview.
     *
     * @param interview The interview that has been selected.
     * @return true iff this interviewer is the coordinator for this interview.
     */
    boolean isCoordinator(Interview interview) {
        return interview.getInterviewCoordinator().equals(this.interviewer);
    }

    /**
     * Get a list of interviews that need scheduling.
     *
     * @return a list of interviews that need scheduling.
     */
    ArrayList<Interview> getInterviewsThatNeedScheduling() {
        return this.interviewer.getUnscheduledInterviews();
    }

    /**
     * Get a list of upcoming interviews that have been scheduled.
     *
     * @return a list of upcoming interviews.
     */
    ArrayList<Interview> getScheduledUpcomingInterviews() {
        return this.interviewer.getScheduledUpcomingInterviews(this.jobAppSystem.getToday());
    }

    /**
     * Set this interview as pass or fail for each applicant.
     *
     * @param interview      The interview in question.
     * @param jobAppToResult The hash map of job application to result(boolean pass/fail) for this interview.
     */
    void setInterviewResults(Interview interview, HashMap<JobApplication, Boolean> jobAppToResult) {
        interview.setResults(jobAppToResult);
    }

    /**
     * Store this interviewer's notes for this interview.
     *
     * @param interview The interview for which notes have been written for.
     * @param notes     The notes that this interviewer has written.
     */
    void storeInterviewNotes(Interview interview, String notes) {
        if (interview.getInterviewCoordinator().equals(interviewer)) {
            interview.setInterviewCoordinatorNotes(notes);
        } else {
            interview.setOtherInterviewNotes(interviewer, notes);
        }
    }

    /**
     * Get a map of interviewers to notes for this interview.
     *
     * @param interview The interview being viewed.
     * @return a map of interviewers to notes for this interview.
     */
    HashMap<Interviewer, String> getInterviewerToNotes(Interview interview) {
        HashMap<Interviewer, String> interviewerToNotes = new HashMap<>();
        interviewerToNotes.putAll(interview.getInterviewCoordinatorToNotes());
        interviewerToNotes.putAll(interview.getOtherInterviewersToNotes());
        return interviewerToNotes;
    }

    /**
     * Gets a list of available times for this interviewer on this date.
     *
     * @param date The date chosen by the interviewer.
     * @return an array of available time slots for this interviewer on this date.
     */
    String[] getAvailableTimes(LocalDate date) {
        ArrayList<String> timeSlots = this.interviewer.getTimeSlotsAvailableOnDate(date);
        return timeSlots.toArray(new String[timeSlots.size()]);
    }

    /**
     * Schedule this one-on-one interview on this date and time slot.
     *
     * @param interview The interview to be scheduled.
     * @param date      The date chosen.
     * @param timeSlot  The time slot chosen.
     * @return true iff this interview can be scheduled on this date and at this time.
     * is invalid, ie, it's before getToday's date.
     * Precondition: the date is after getToday's date.
     */
    boolean scheduleInterview(Interview interview, LocalDate date, String timeSlot) {
        InterviewTime interviewTime = new InterviewTime(date, timeSlot);
        return this.interviewer.scheduleInterview(interview, interviewTime);
    }
}
