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
    final String FILENAME = "Interviewers";

    // === Representation invariants ===
    // interviews is sorted in terms of date.

    // === Constructors ===

    Interviewer(String id){
        this.setUsername(id);
        loadSelf();
    }

    Interviewer(String username, String password, String legalName, String email, Company company, String field,
                LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
        this.field = field;
    }

    // === Getters ===
    Company getCompany() {
        return this.company;
    }

    String getField() {
        return this.field;
    }

    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    HashMap<LocalDate, ArrayList<Integer>> getSchedule() {
        return this.schedule;
    }

    // === Setters ===
    void setCompany(Company company) {
        this.company = company;
    }

    void setField(String field) {
        this.field = field;
    }

    void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    void setSchedule(HashMap<LocalDate, ArrayList<Integer>> schedule) {
        this.schedule = schedule;
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
    public String getIdString() {
        return this.getUsername();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", this.getPassword());
        data.put("legalName", this.getLegalName());
        data.put("email", this.getEmail());
        data.put("dateCreated", this.getDateCreated());
        data.put("field", this.getField());
        data.put("company", new String[] {this.company.FILENAME,
                this.company.getIdString()});
        ArrayList<ArrayList> interview_list = new ArrayList<>();
        for(Interview x : this.interviews){
            interview_list.add(new ArrayList<Object>() {{
                add(FILENAME);
                add(x.getIdString());
            }});
        }
        data.put("interviews", interview_list);
        ArrayList<ArrayList> schedule = new ArrayList<>();
        for(LocalDate x : this.schedule.keySet()){
            ArrayList dates = this.schedule.get(x);
            schedule.add(new ArrayList<Object>() {{
                add(x);
                add(dates);
            }});
        }
        data.put("schedule", schedule);
        FileSystem.write(FILENAME, getIdString(), data);
    }

    /**
     *  Loads the interviewer.
     */
    public void loadSelf(){
        FileSystem.mapPut(FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(FILENAME, getIdString());
        this.loadPrelimData(data);
        this.loadCompany(data);
        this.loadInterviews(data);
        this.loadSchedule(data);
    }

    /**
     * Load the preliminary data for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadPrelimData(HashMap data) {
        this.setPassword((String) data.get("password"));
        this.setLegalName((String) data.get("legalName"));
        this.setEmail((String) data.get("email"));
        this.setDateCreated(LocalDate.parse((String) data.get("password")));
        this.setField((String) data.get("field"));
    }

    /**
     * Load the company for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadCompany(HashMap data) {
        this.setCompany((Company)FileSystem.subLoader(Company.class, (String) ((ArrayList) data.get("company")).get(0),
                (String) ((ArrayList) data.get("company")).get(1)));
    }

    /**
     * Load the interviews for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadInterviews(HashMap data) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Object x : (ArrayList) (data.get("interviews"))) {
            interviews.add((Interview) FileSystem.subLoader(Interview.class, (String) ((ArrayList) x).get(0), (String)
                    ((ArrayList) x).get(1)));
        }
        this.setInterviews(interviews);
    }

    /**
     * Load the schedule for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadSchedule(HashMap data) {
        HashMap<LocalDate, ArrayList<Integer>> schedule = new HashMap<>();
        for (Object x : (ArrayList) data.get("schedule")) {
            schedule.put(LocalDate.parse((String) ((ArrayList) x).get(0)), (ArrayList<Integer>) ((ArrayList) x).get(1));
        }
        this.setSchedule(schedule);
    }
}
