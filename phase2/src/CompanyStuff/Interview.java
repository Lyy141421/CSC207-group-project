package CompanyStuff;

import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class Interview implements Serializable {
    /**
     * An interview.
     */

    // === Class variables ===
    // The total number of interviews conducted
    private static int totalNumOfInterviews;

    // === Instance variables ===
    // The unique identifier for this interview
    private int id;
    // The interview coordinator
    private Interviewer interviewCoordinator;
    // The interview notes
    private String notes;
    // The date and time of this interview
    private InterviewTime time;
    // Interview round
    private int roundNumber;
    // InterviewManager of the job posting this interview is held for
    private InterviewManager interviewManager;

    // === Representation invariants ===
    // ID >= 1

    // === Constructor ===
    Interview() {
    }

    Interview(Interviewer interviewCoordinator, InterviewManager interviewManager) {
        Interview.totalNumOfInterviews++;
        this.id = Interview.totalNumOfInterviews;
        this.interviewCoordinator = interviewCoordinator;
        this.interviewManager = interviewManager;
        this.roundNumber = this.interviewManager.getCurrentRound();
    }

    // === Public methods ===
    // === Getters ===
    public int getId() {
        return this.id;
    }

    public Interviewer getInterviewCoordinator() {
        return this.interviewCoordinator;
    }

    public InterviewTime getTime() {
        return this.time;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    // === Setters ===
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTime(InterviewTime time) {
        this.time = time;
    }

    // === Abstract methods ===
    abstract void setResults(HashMap<JobApplication, Boolean> jobAppToResult);

    abstract boolean isComplete();

    abstract int getNumApplications();

    abstract ArrayList<Interviewer> getAllInterviewers();

    abstract JobApplication findJobAppById(int id);

    abstract Collection<JobApplication> getJobApplications();

}
