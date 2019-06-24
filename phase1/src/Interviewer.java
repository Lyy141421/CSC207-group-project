import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class Interviewer extends User {

    // === Instance variables ===
    // The username belonging to this interviewer
    private String username;
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The interviewer's schedule as a map of the date to a list of time slots that are filled.
    private HashMap<LocalDate, ArrayList<Integer>> schedule = new HashMap<>();

    // === Representation invariants ===
    // interviews is sorted in terms of date.

    // === Constructors ===

    /**
     * Create an interviewer.
     *
     * @param username    The interviewer's username.
     * @param password    The interviewer's password.
     * @param legalName   The interviewer's legal name.
     * @param email       The interviewer's email.
     * @param company     The company that this interviewer works for.
     * @param field       The job field that this interviewer is in.
     * @param dateCreated The date this account was created.
     */
    Interviewer(String username, String password, String legalName, String email, Company company, String field,
                LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
        this.field = field;
    }

    // === Getters ===
    /**
     * Get the company that this interviewer works for.
     *
     * @return the company that this interviewer works for.
     */
    Company getCompany() {
        return this.company;
    }

    /**
     * Get the field this interviewer specializes in.
     *
     * @return the field that this interview specializes in.
     */
    String getField() {
        return this.field;
    }

    /**
     * Get the list of interviews that this interviewer has conducted in the past and is currently conducting.
     * @return the list of all interviews involving this interviewer.
     */
    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    /**
     * Get the schedule for this interviewer.
     *
     * @return the schedule for this interviewer.
     */
    HashMap<LocalDate, ArrayList<Integer>> getSchedule() {
        return this.schedule;
    }


    // === Other methods ===

    /**
     * Add an interview to this interviewer's list of interviews in sorted order.
     *
     * @param interview The interview to be added.
     */
    void addInterview(Interview interview) {
        this.interviews.add(interview);
        this.interviews.sort(new InterviewTimeComparator());
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
     * Write interview note for a given interview.
     *
     * @param interview   The interview which this interviewer wants to write note of.
     * @param note The interview note.
     */
    void writeInterviewNotes(Interview interview, String note) {
        interview.setInterviewNotes(note);
    }

    /**
     * Schedule an interview on this date and at this time.
     *
     * @param interview The interview to be scheduled.
     */
    // TODO: Get date and time slot from interface
    void scheduleInterview(Interview interview) {
        if (interview.getTime() == null) {
            this.setInterviewTime(interview, interviewTime);
            this.addInterview(interview);
            this.updateSchedule(interviewTime.getDate(), interviewTime.getTimeSlot());
        }
    }

    /**
     * Set the date and time slot for the interview.
     *
     * @param interview The interview being scheduled.
     * @param time      The time this interview is scheduled for.
     */
    private void setInterviewTime(Interview interview, InterviewTime time) {
        interview.setTime(time);
    }

    /**
     * Add an interview that occurs on this date and time to this interviewer's schedule.
     *
     * @param date    The date on which this interview occurs.
     * @param timeSlot  The time slot at which this interview occurs.
     */
    private void updateSchedule(LocalDate date, Integer timeSlot) {
        if (!this.schedule.containsKey(date)) {
            this.schedule.put(date, new ArrayList<>());
        }
        this.schedule.get(date).add(timeSlot);
    }

    /**
     * Record that this interview has been failed.
     *
     * @param interview The interview that has been conducted.
     */
    void failInterview(Interview interview) {
        interview.setFail();
        interview.getInterviewManager().reject(interview.getJobApplication());
    }
}
