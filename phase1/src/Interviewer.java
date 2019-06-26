import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Interviewer extends User {

    // === Instance variables ===
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo in chronological order
    private ArrayList<Interview> interviews = new ArrayList<>();
    // The interviewer's schedule as a map of the date to a list of time slots that are filled.
    private HashMap<LocalDate, ArrayList<Integer>> schedule = new HashMap<>();
    // The filename under which this will be saved in the FileSystem
    public final String FILENAME = "HRCoordinators";

    // === Representation invariants ===
    // interviews is sorted in terms of date.

    // === Constructors ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    public Interviewer(String id){
        this.setUsername(id);
        loadSelf();
    }

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
     * Get the next interview to be conducted by this interviewer.
     *
     * @return the next interview to be conducted by this interviewer.
     */
    Interview getNextInterview() {
        return this.interviews.get(0);
    }

    /**
     * Get a list of interviews scheduled for this date.
     *
     * @param date The date in question.
     * @return a list of interviews scheduled for this date.
     */
    List<Interview> getInterviewsByDate(LocalDate date) {
        int start = 0;
        Interview interview = this.interviews.get(start);
        while (interview.getTime().getDate().isBefore(date)) {
            start++;
            interview = this.interviews.get(start);
        }
        if (start == this.interviews.size()) {      // All interviews are before this date
            return this.interviews.subList(0, 0);   // Empty list
        }
        int end = start;
        while (interview.getTime().getDate().isEqual(date)) {
            end++;
            interview = this.interviews.get(end);
        }
        return this.interviews.subList(start, end);
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
     * Get a list of unscheduled interviews for this interviewer.
     * @return a list of unscheduled interviews for this interviewer.
     */
    ArrayList<Interview> getUnscheduledInterviews() {
        ArrayList<Interview> unscheduledInterviews = new ArrayList<>();
        for (Interview interview : this.interviews) {
            if (interview.getTime() == null) {
                unscheduledInterviews.add(interview);
            }
        }
        return unscheduledInterviews;
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
     * Get a list of job applications of this interviewer's interviewees.
     *
     * @return a list of job applications of the applicants that are being interviewed by this interviewer.
     */
    ArrayList<JobApplication> getListOfIntervieweeJobApplications() {
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for (Interview interview : this.interviews) {
            jobApplications.add(interview.getJobApplication());
        }
        return jobApplications;
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

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getId(){
        return this.getUsername();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", this.getPassword());
        data.put("legalName", this.getLegalName());
        data.put("email", this.getEmail());
        data.put("dateCreated", this.getDateCreated());
        data.put("field", this.getField());
        data.put("company", new String[] {this.company.FILENAME,
                this.company.getId()});
        ArrayList<ArrayList> interview_list = new ArrayList<>();
        for(Interview x : this.interviews){
            interview_list.add(new ArrayList(){{ add(FILENAME); add(x.getId()); }});
        }
        data.put("interviews", interview_list);
        ArrayList<ArrayList> schedule = new ArrayList<>();
        for(LocalDate x : this.schedule.keySet()){
            ArrayList dates = this.schedule.get(x);
            schedule.add(new ArrayList(){{ add(x); add(dates); }});
        }
        data.put("schedule", schedule);
        FileSystem.write(FILENAME, getId(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getId(), this);
        HashMap data = FileSystem.read(FILENAME, getId());
        this.setPassword((String)data.get("password"));
        this.setLegalName((String)data.get("legalName"));
        this.setEmail((String)data.get("email"));
        this.setDateCreated(LocalDate.parse((String)data.get("password")));
        this.field = (String)data.get("field");
        if(FileSystem.isLoaded((String)((ArrayList)data.get("company")).get(1),
                (String)((ArrayList)data.get("company")).get(1))){
            this.company = (Company) FileSystem.mapGet((String)((ArrayList)data.get("company")).get(1),
                    (String)((ArrayList)data.get("company")).get(1));
        }
        else{
            this.company = new Company((String)((ArrayList)data.get("company")).get(1));
        }
        ArrayList<Interview> intervies = new ArrayList<>();
        for(Object x : (ArrayList)(data.get("interviews"))){
            if(FileSystem.isLoaded((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1))){
                intervies.add((Interview) FileSystem.mapGet((String)((ArrayList)x).get(0), (String)((ArrayList)x).get(1)));
            }
            else{
                intervies.add(new Interview((String)((ArrayList)x).get(1)));
            }
        }
        HashMap<LocalDate, ArrayList<Integer>> schedule = new HashMap<>();
        for(Object x : (ArrayList)data.get("schedule")){
            schedule.put(LocalDate.parse((String)((ArrayList)x).get(0)), (ArrayList<Integer>)((ArrayList)x).get(1));
        }
        this.schedule = schedule;
    }
}
