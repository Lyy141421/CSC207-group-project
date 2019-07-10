package DELETE_BEFORE_SUBMISSION;

import Miscellaneous.InterviewTime;
import UsersAndJobObjects.Interview;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class MethodsTheGUICallsInInterviewer {
    // === Methods for GUI to call ===
    // InterviewerInterface class
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
    // Job application class
    /*    *//**
     * Get a string of the overview of this job application, including id, applicant name, job posting, status,
     * and application date.
     *
     * @return a string of the overview of this job application.
     *//*
    public String getOverview() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);

        return s;
    }*/

    // Interviewer class
    /**
     * Get a string representation of this interviewer's up-coming schedule.
     *
     * @return a string representation of this interviewer's schedule
     *//*
    public String getScheduleString() {
        if (this.interviews.isEmpty()) {
            return null;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String s = "";
        InterviewTime interviewTime = this.interviews.get(0).getTime();
        s += interviewTime.getDate().format(dtf) + ": ";
        for (Interview interview : this.interviews) {
            if (!interview.getTime().isOnSameDate(interviewTime)) {
                s = s.substring(0, s.length() - 2); // Remove extra comma and space from previous ling
                s += "\n" + interview.getTime().getDate().format(dtf) + ": ";
            }
            s += new InterviewTime().getTimeSlotCorrespondingToInt(interview.getTime().getTimeSlot()) + ", ";
        }
        return s.substring(0, s.length() - 2); // Remove extra comma and space
    }*/

    // Interviewer class
    /**
     * Get a list of interviews scheduled for this date.
     *
     * @param date The date in question.
     * @return a list of interviews scheduled for this date.
     *//*
    public ArrayList<ArrayList<Interview>> getInterviewsBeforeOnAndAfterDate(LocalDate date) {
        ArrayList<Interview> interviewsBefore = new ArrayList<>();
        ArrayList<Interview> interviewsToday = new ArrayList<>();
        ArrayList<Interview> interviewsAfter = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.isBeforeDate(date)) {
                interviewsBefore.add(interview);
            }
            else if (interview.isOnDate(date)) {
                interviewsToday.add(interview);
            }
            else {
                interviewsAfter.add(interview);
            }
        }
        return new ArrayList<>(Arrays.asList(interviewsBefore, interviewsToday, interviewsAfter));
    }*/

    // Interview class
    /**
     * Check whether this interview is scheduled on this date.
     * @param date  The date in question.
     * @return true iff this interview is scheduled on this date.
     *//*
    boolean isOnDate(LocalDate date) {
        return this.getTime().getDate().isEqual(date);
    }*/

    // InterviewTime class
    /**
     * Check whether these two interview times occur on the same date.
     *
     * @param otherTime The other time in question.
     * @return true iff these two times have the same date.
     *//*
    public boolean isOnSameDate(InterviewTime otherTime) {
        return this.date.isEqual(otherTime.getDate());
    }*/

}
