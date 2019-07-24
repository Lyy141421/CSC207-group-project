package CompanyStuff;

import Miscellaneous.InterviewTime;
import Main.User;
import ApplicantStuff.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;

public class Interviewer extends User {

    // === Class variables ===
    static final long serialVersionUID = 1L;

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
        branch.addInterviewer(this);
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
     * Get the earliest time available one week from today when the interviewer is available.
     *
     * @param today   Today's date.
     * @param numDays The number of days from today when interviews can be scheduled for.
     * @return the earliest time available one week from today when the interviewer is available.
     */
    public InterviewTime getEarliestTimeAvailableNumDaysAfterToday(LocalDate today, int numDays) {
        LocalDate date = today.plusDays(numDays);
        while (!this.isAvailable(date)) {
            date = date.plusDays(1);
        }
        String earliestTimeSlot = this.getTimeSlotsAvailableOnDate(date).get(0);
        return new InterviewTime(date, InterviewTime.timeSlots.indexOf(earliestTimeSlot));
    }

    /**
     * Find the job application with this id from the list of job applications that this interviewer can view.
     *
     * @param id The id in question.
     * @return the job application associated with this id or null if not found.
     */
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
        for (Interview interview : this.getInterviewsOnDate(interviewTime.getDate())) {
            if (interview.getTime().equals(interviewTime)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAvailable(LocalDate date) {
        return this.getInterviewsOnDate(date).size() < InterviewTime.timeSlots.size();
    }

    /**
     * Get a list of time slots for which this interviewer is available on this date.
     * @param date  Today's date.
     * @return a list of time slots available on this date.
     */
    public ArrayList<String> getTimeSlotsAvailableOnDate(LocalDate date) {
        ArrayList<Interview> interviewsOnDate = this.getInterviewsOnDate(date);
        ArrayList<String> timeSlots = (ArrayList<String>) InterviewTime.timeSlots.clone();
        for (Interview interview : interviewsOnDate) {
            timeSlots.remove(interview.getTime().getTimeSlotsString());
        }
        return timeSlots;
    }

    /**
     * Get interviews on this date.
     *
     * @param date The date in question.
     * @return the interviews that occur on this date.
     */
    private ArrayList<Interview> getInterviewsOnDate(LocalDate date) {
        ArrayList<Interview> interviewsOnDate = new ArrayList<>();
        for (Interview interview : this.getScheduledUpcomingInterviews(date)) {
            if (interview.getTime().getDate().isEqual(date)) {
                interviewsOnDate.add(interview);
            }
        }
        return interviewsOnDate;
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
            if (!interview.isScheduled()) {
                unscheduledInterviews.add(interview);
            }
        }
        return unscheduledInterviews;
    }

    /**
     * Get a list of scheduled interviews for this interviewer.
     * @param today Today's date.
     * @return a list of scheduled interviews for this interviewer.
     */
    public ArrayList<Interview> getScheduledUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> scheduledInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.getTime() != null && !interview.isComplete() && !interview.getTime().getDate().isBefore(today)) {
                scheduledInterviews.add(interview);
            }
        }
        return scheduledInterviews;
    }

    /**
     * Get a list of incomplete interviews for this interviewer.
     * @return a list of incomplete interviews for this interviewer.
     */
    public ArrayList<Interview> getAllIncompleteInterviews() {
        ArrayList<Interview> incompleteInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.getTime() != null && !interview.isComplete()) {
                incompleteInterviews.add(interview);
            }
        }
        return incompleteInterviews;
    }

    /**
     * Get a list of incomplete interviews for this interviewer that have already occurred.
     * @param today Today's date.
     * @return a list of incomplete interviews for this interviewer.
     */
    public ArrayList<Interview> getIncompleteInterviewsAlreadyOccurred(LocalDate today) {
        ArrayList<Interview> incompleteInterviews = new ArrayList<>();
        for (Interview interview : this.getAllIncompleteInterviews()) {
            if (interview.getTime().getDate().isBefore(today)) {
                incompleteInterviews.add(interview);
            }
        }
        return incompleteInterviews;
    }

    /**
     * Get a list of incomplete interviews for which this interviewer is a coordinator.
     *
     * @param today Today's date.
     * @return a list fo incomplete interviews for which this interviewer is a coordinator.
     */
    public ArrayList<Interview> getIncompleteInterviewsAlreadyOccuredAsCoordinator(LocalDate today) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Interview interview : this.getIncompleteInterviewsAlreadyOccurred(today)) {
            if (interview.getInterviewCoordinator().equals(this)) {
                interviews.add(interview);
            }
        }
        return interviews;
    }

    /**
     * Get a list of incomplete interviews for which this interviewer is not a coordinator.
     *
     * @param today Today's date.
     * @return a list fo incomplete interviews for which this interviewer is not a coordinator.
     */
    public ArrayList<Interview> getIncompleteInterviewsAlreadyOccurredNotAsCoordinator(LocalDate today) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Interview interview : this.getIncompleteInterviewsAlreadyOccurred(today)) {
            if (!interview.getInterviewCoordinator().equals(this)) {
                interviews.add(interview);
            }
        }
        return interviews;
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

    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Username", "Legal Name", "Email", "Company Branch", "Field", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"Interviewer", this.getUsername(), this.getLegalName(), this.getEmail(),
                this.getBranch().getName(), this.getField(), this.getDateCreated().toString()};
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
