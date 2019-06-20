import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Interviewer extends User {

    // === Class variables ===
    // Time slots
    ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList("9-10 am", "10-11 am", "1-2 pm", "2-3 pm", "3-4 pm",
            "4-5 pm"));     // put something reasonable -- the indexes correspond to the Integer part of schedule

    // === Instance variables ===
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The interviewer's schedule as a map of the date to a list of time slots that are filled.
    private HashMap<LocalDate, ArrayList<Integer>> schedule = new HashMap<>();

    // === Constructors ===

    /**
     * Create an interviewer.
     *
     * @param email       The interviewer's email.
     * @param company     The company that this interviewer works for.
     * @param field       The job field that this interviewer is in.
     * @param dateCreated The date this account was created.
     */
    Interviewer(String email, Company company, String field, LocalDate dateCreated) {
        super(email, dateCreated);
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
     * Write interview note for a given interview.
     *
     * @param interview   The interview which this interviewer wants to write note of.
     * @param note The interview note.
     */
    void writeInterviewNote(Interview interview, String note) {
        interview.setInterviewNotes(note);
    }

    /**
     * Add an interview for this interviewer.
     *
     * @param date     The date of the interview.
     * @param timeSlot The timeslot for this interview.
     */
    void addInterview(LocalDate date, Integer timeSlot) {
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
        this.interviews.remove(interview);
    }

    void advanceInterview(Interview interview) {
        this.interviews.remove(interview);
    }
}
