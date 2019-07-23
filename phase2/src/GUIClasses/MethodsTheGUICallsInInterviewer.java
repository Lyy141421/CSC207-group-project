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

    /**
     * Get a list of interviews that are incomplete.
     *
     * @param today Today's date.
     * @return a list of interviews that are incomplete.
     */
    ArrayList<Interview> getIncompleteInterviews(LocalDate today) {
        return this.interviewer.getIncompleteInterviews(today);
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
     * Set this interview as pass or fail for each applicant.
     *
     * @param interview      The interview in question/
     * @param jobAppToResult The hash map of job application to result(boolean pass/fail) for this interview.
     */
    void setInterviewResults(Interview interview, HashMap<JobApplication, Boolean> jobAppToResult) {
        interview.setResults(jobAppToResult);
    }

    /**
     * Store interview notes for this interview by the interview coordinator.
     *
     * @param interview The interview for which the notes are written.
     * @param notes     The notes taken during the interview.
     */
    void storeInterviewCoordinatorNotes(Interview interview, String notes) {
        interview.setInterviewCoordinatorNotes(notes);
    }

    /**
     * Store interview notes for this interview by a secondary interviewer.
     *
     * @param interview   The interview in question.
     * @param interviewer The interviewer who wrote notes.
     * @param notes       The notes the interviewer wrote for this interview.
     */
    void storeOtherInterviewerNotes(Interview interview, Interviewer interviewer, String notes) {
        interview.setOtherInterviewNotes(interviewer, notes);
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
