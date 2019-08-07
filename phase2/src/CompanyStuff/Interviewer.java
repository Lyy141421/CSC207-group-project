package CompanyStuff;

import Main.JobApplicationSystem;
import Miscellaneous.InterviewTime;
import Main.User;
import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTimeComparator;

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

    public Interviewer(String username, String password, String legalName, String email, Branch branch, String field, LocalDate dateCreated) {
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

    // === Other methods ===

    /**
     * Get the earliest time available one week from getToday when the interviewer is available.
     *
     * @param today   Today's date.
     * @param numDays The number of days from getToday when interviews can be scheduled for.
     * @return the earliest time available one week from getToday when the interviewer is available.
     */
    public InterviewTime getEarliestTimeAvailableForNewInterview(LocalDate today, int numDays) {
        LocalDate date = today.plusDays(numDays);
        while (!this.isAvailable(date)) {
            date = date.plusDays(1);
        }
        String earliestTimeSlot = this.getTimeSlotsAvailableOnDate(date).get(0);
        return new InterviewTime(date, earliestTimeSlot);
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
     * Get the first date that this interviewer is available on or after the date specified.
     *
     * @param date The date in question.
     * @return the first date that this interviewer is available on or after the date specified.
     */
    public LocalDate getFirstDateAvailableOnOrAfterDate(LocalDate date) {
        LocalDate availableDate = date;
        while (!this.isAvailable(availableDate)) {
            availableDate = availableDate.plusDays(1);
        }
        return availableDate;
    }

    /**
     * Get a list of time slots that are filled on this date.
     *
     * @param date The date in question.
     * @return a list of time slots that are filled on this date.
     */
    public ArrayList<String> getTimeSlotsFilledOnDate(LocalDate date) {
        ArrayList<String> timeSlots = (ArrayList<String>) InterviewTime.timeSlots.clone();
        timeSlots.removeAll(this.getTimeSlotsAvailableOnDate(date));
        return timeSlots;
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
            timeSlots.remove(interview.getTime().getTimeSlot());
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
     * Set up an interview time for this interview.
     *
     * @param interview     The interview that needs to be scheduled.
     * @param interviewTime The interview time chosen.
     * @return true iff this interview can be scheduled at this time.
     */
    public boolean scheduleInterview(Interview interview, InterviewTime interviewTime) {
        if (this.isAvailable(interviewTime)) {
            interview.setTime(interviewTime);
            interview.getInterviewManager().addInterviewForJobApplications(interview);
            return true;
        }
        return false;
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
            if (interview.getTime() != null && interview.isIncomplete() && !interview.getTime().getDate().isBefore(today)) {
                scheduledInterviews.add(interview);
            }
        }
        scheduledInterviews.sort(new InterviewTimeComparator());
        return scheduledInterviews;
    }

    /**
     * Get a list of incomplete interviews for this interviewer.
     * @return a list of incomplete interviews for this interviewer.
     */
    public ArrayList<Interview> getAllIncompleteInterviews() {
        ArrayList<Interview> incompleteInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.isIncomplete()) {
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
            if (interview.getTime() != null && interview.getTime().getDate().isBefore(today)) {
                incompleteInterviews.add(interview);
            }
        }
        return incompleteInterviews;
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