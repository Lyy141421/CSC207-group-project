package UsersAndJobObjects;

import Miscellaneous.InterviewTime;
import Miscellaneous.InterviewTimeComparator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Interviewer extends User {

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "Interviewers";

    // === Instance variables ===
    // The company that this interviewer works for
    private Company company;
    // The field that this interviewer works in
    private String field;
    // The list of interviews that this interviewer must undergo in chronological order
    private ArrayList<Interview> interviews = new ArrayList<>();

    // === Representation invariants ===
    // interviews is sorted in terms of date.


    // === Public methods ===
    // === Constructors ===

    public Interviewer(){}

    public Interviewer(String id){
        this.setUsername(id);
    }

    public Interviewer(String username, String password, String legalName, String email, Company company, String field,
                LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
        this.field = field;
    }

    // === Getters ===
    public Company getCompany() {
        return this.company;
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

    public void setCompany(Company company) {
        this.company = company;
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
    public JobApplication findJobAppById(int id) {
        for (Interview interview : this.interviews) {
            if (Integer.parseInt(interview.getJobApplication().getId()) == id) {
                return interview.getJobApplication();
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
            if (Integer.valueOf(interview.getId()) == id) {
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
        for (Interview interview : this.interviews) {
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
    public void removeInterview(Interview interview) {
        this.interviews.remove(interview);
    }

    /**
     * Get a list of interviews scheduled for this date.
     *
     * @param date The date in question.
     * @return a list of interviews scheduled for this date.
     */
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
    }

    /**
     * Get a list of unscheduled interviews for this interviewer.
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
     * Get a list of job applications of this interviewer's interviewees.
     *
     * @return a list of job applications of the applicants that are being interviewed by this interviewer.
     */
    public ArrayList<JobApplication> getListOfIntervieweeJobApplications() {
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for (Interview interview : this.interviews) {
            jobApplications.add(interview.getJobApplication());
        }
        return jobApplications;
    }

    /**
     * Get a string representation of this interviewer's up-coming schedule.
     *
     * @return a string representation of this interviewer's schedule
     */
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
    }

    /**
     * Record that this interview has been failed.
     *
     * @param interview The interview that has been conducted.
     */
    public void passInterview(Interview interview) {
        interview.setPass(true);
        interview.getInterviewManager().reject(interview.getJobApplication());
    }

    /**
     * Record that this interview has been failed.
     *
     * @param interview The interview that has been conducted.
     */
    public void failInterview(Interview interview) {
        interview.setPass(false);
        interview.getInterviewManager().reject(interview.getJobApplication());
    }

    // ============================================================================================================== //
    // === Package-private methods ===
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
}
