package DELETE_BEFORE_SUBMISSION;

import Miscellaneous.InterviewTime;
import UsersAndJobObjects.Interview;

import java.time.LocalDate;
import java.util.ArrayList;

public class MethodsTheGUICallsInInterviewer {
    // === Methods for GUI to call ===

    /*
     */
/**
 * Get a list of list of interviews for this interviewer by date.
 * @param today Today's date.
 * @return a list of lists of interviews for this interviewer by date.
 *//*

    ArrayList<ArrayList<Interview>> getInterviewsBeforeOnAndAfterToday(LocalDate today) {
        return this.interviewer.getInterviewsBeforeOnAndAfterDate(today);
    }

    */
/**
 * Set this interview as pass or fail.
 * @param pass  Whether or not the applicant passed this interview.
 *//*

    void passOrFailInterview(Interview interview, boolean pass) {
        if (pass) {
            this.interviewer.passInterview(interview);
        }
        else {
            this.interviewer.failInterview(interview);
        }
    }

    */
/**
 * Store interview notes for this interview.
 * @param interview The interview for which the notes are written.
 * @param notes The notes taken during the interview.
 *//*

    void storeInterviewNotes(Interview interview, String notes) {
        interview.setInterviewNotes(notes);
    }

    */
/**
 * Schedule this interview on this date and time slot.
 *
 * @param interview The interview to be scheduled.
 * @param date      The date chosen.
 * @param timeSlot  The time slot chosen.
 * @return true iff this interview can be scheduled on this date and at this time.
 *//*

    boolean scheduleInterview(Interview interview, LocalDate date, int timeSlot) {
        InterviewTime interviewTime = new InterviewTime(date, timeSlot);
        if (interviewer.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
            return true;
        } else {
            return false;
        }

    }

    */
/**
 * Get a list of unscheduled interviews for this interviewer.
 *
 * @return a list of unscheduled interviews for this interviewer.
 *//*

    ArrayList<Interview> getUnscheduledInterviews() {
        return this.interviewer.getUnscheduledInterviews();
    }
*/

}
