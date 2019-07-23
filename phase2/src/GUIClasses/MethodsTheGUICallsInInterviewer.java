package GUIClasses;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import Miscellaneous.InterviewTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodsTheGUICallsInInterviewer {
    // === Methods for GUI to call ===
    // InterviewerInterface class

    private Interviewer interviewer;

    MethodsTheGUICallsInInterviewer(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    public Interviewer getInterviewer() {
        return this.interviewer;
    }

    /**
     * Get a list of interviews that are incomplete.
     *
     * @param today Today's date.
     * @return a list of interviews that are incomplete.
     */
    public ArrayList<Interview> getIncompleteInterviews(LocalDate today) {
        return this.interviewer.getIncompleteInterviews(today);
    }

    /**
     * Get a list of interviews that need scheduling.
     *
     * @return a list of interviews that need scheduling.
     */
    public ArrayList<Interview> getInterviewsThatNeedScheduling() {
        return this.interviewer.getUnscheduledInterviews();
    }

    /**
     * Get a list of upcoming interviews.
     *
     * @return a list of upcoming interviews.
     */
    public ArrayList<Interview> getScheduledUpcomingInterviews() {
        return this.interviewer.getScheduledUpcomingInterviews();
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
     * Set this interview as pass or fail for one applicant.
     * @param interview The interview in question.
     * @param jobApp    The job application to be passed/failed.
     * @param result    The result of this interview (pass/fail)
     */
    public void setInterviewResult(Interview interview, JobApplication jobApp, boolean result) {
        interview.setResult(jobApp, result);
    }

    public void storeInterviewNotes(Interview interview, Interviewer interviewer, String notes) {
        if (interview.getInterviewCoordinator().equals(interviewer)) {
            interview.setInterviewCoordinatorNotes(notes);
        } else {
            interview.setOtherInterviewNotes(interviewer, notes);
        }
    }

    /**
     * Schedule this one-on-one interview on this date and time slot.
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
}
