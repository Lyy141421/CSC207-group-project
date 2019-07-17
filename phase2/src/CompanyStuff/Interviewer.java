package CompanyStuff;

import Miscellaneous.InterviewTime;
import Main.User;
import ApplicantStuff.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;

public class Interviewer extends User {

    // === Instance variables ===
    // The branch that this interviewer works for
    private Branch branch;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo in chronological order
    private ArrayList<Interview> interviews = new ArrayList<>();

    // === Representation invariants ===
    // interviews is sorted in terms of date.


    // === Public methods ===
    // === Constructors ===

    public Interviewer() {
    }

    public Interviewer(String username, String password, String legalName, String email, Branch branch, String field,
                       LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.branch = branch;
        this.field = field;
    }

    // === Getters ===
    public Branch getBranch() {
        return this.branch;
    }

    public String getField() {
        return this.field;
    }

    public ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Setters ===
    public void setField(String field) {
        this.field = field;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    // === Other methods ===

    /**
     * Find the job application with this id from the list of job applications that this interviewer can view.
     *
     * @param id The id in question.
     * @return the job application associated with this id or null if not found.
     */
    @Override
    public JobApplication findJobAppById(int id) {
        for (Interview interview : this.interviews) {
            JobApplication jobApp = interview.findJobAppById(id);
            if (jobApp != null) {
                return jobApp;
            }
        }
        return null;
    }

    /**
     * Find the interview with this id from the interviews that this interviewer has access to.
     *
     * @param id The id in question
     * @return the interview associated with this id or null if not found.
     */
    public Interview findInterviewById(int id) {
        for (Interview interview : this.interviews) {
            if (interview.getId() == id) {
                return interview;
            }
        }
        return null;
    }

    /**
     * Check whether this interviewer is available at this specific time.
     *
     * @param interviewTime The time in question.
     * @return true iff this interviewer is available at this time.
     */
    public boolean isAvailable(InterviewTime interviewTime) {
        for (Interview interview : this.getScheduledInterviews()) {
            if (interview.getTime().equals(interviewTime)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove an interview from this interviewer's list of interviews.
     *
     * @param interview The interview to be removed.
     */
    void removeInterview(Interview interview) {
        this.interviews.remove(interview);
    }

    /**
     * Get a list of unscheduled interviews for this interviewer.
     *
     * @return a list of unscheduled interviews for this interviewer.
     */
    public ArrayList<Interview> getUnscheduledInterviews() {
        ArrayList<Interview> unscheduledInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.getTime() == null) {
                unscheduledInterviews.add(interview);
            }
        }
        return unscheduledInterviews;
    }

    /**
     * Get a list of scheduled interviews for this interviewer.
     *
     * @return a list of scheduled interviews for this interviewer.
     */
    public ArrayList<Interview> getScheduledInterviews() {
        ArrayList<Interview> scheduledInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (!this.getUnscheduledInterviews().contains(interview)) {
                scheduledInterviews.add(interview);
            }
        }
        return scheduledInterviews;
    }

    /**
     * Get a list of incomplete interviews for this interviewer.
     *
     * @return a list of incomplete interviews for this interviewer.
     */
    public ArrayList<Interview> getIncompleteInterviews(LocalDate today) {
        ArrayList<Interview> incompleteInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (!interview.isComplete() && interview.getTime().getDate().isBefore(today)) {
                incompleteInterviews.add(interview);
            }
        }
        return incompleteInterviews;
    }

    /**
     * Get a list of job applications of this interviewer's interviewees.
     *
     * @return a list of job applications of the applicants that are being interviewed by this interviewer.
     */
    public ArrayList<JobApplication> getListOfIntervieweeJobApplications() {
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for (Interview interview : this.interviews) {
            jobApplications.addAll(interview.getJobApplications());
        }
        return jobApplications;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    /**
     * Add an interview to this interviewer's list of interviews in sorted order.
     *
     * @param interview The interview to be added.
     */
    void addInterview(Interview interview) {
        this.interviews.add(interview);
    }
}
