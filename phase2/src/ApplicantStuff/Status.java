package ApplicantStuff;

import NotificationSystem.Notification;
import NotificationSystem.Observable;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class Status extends Observable implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Job application statuses as constants
    private static final int ARCHIVED = -3;
    private static final int SUBMITTED = -2;
    private static final int UNDER_REVIEW = -1;
    private static final int FIRST_ROUND = 0;
    // Map of statuses and their identifying integers
    private TreeMap<Integer, String> descriptions = new TreeMap<Integer, String>() {{
        put(Status.ARCHIVED, "Archived");
        put(Status.SUBMITTED, "Submitted");
        put(Status.UNDER_REVIEW, "Under review");
    }};

    // === Instance variable ===
    private int value = Status.SUBMITTED;   // The integer representing the status
    private JobApplication jobApplication;  // The job application that this status is for
    private int hired;  // The integer that represents a "Hired" status
    private int lastRound;  // The integer that represents the last round of interviews

    // === Constructors ===
    Status(Observer observer, JobApplication jobApplication) {
        this.attach(observer);
        this.jobApplication = jobApplication;
    }

    // === Getters ===
    public int getValue() {
        return this.value;
    }

    String getDescription() {
        return this.descriptions.get(this.value);
    }

    // === Setters ===
    public void setDescriptions(ArrayList<String[]> interviewConfiguration) {
        for (int i = 0; i < interviewConfiguration.size(); i++) {
            this.descriptions.put(i, interviewConfiguration.get(i)[0]);
        }
        this.descriptions.put(interviewConfiguration.size(), "Hired");
        this.lastRound = interviewConfiguration.size() - 1;
        this.hired = this.lastRound + 1;
    }

    private void setValue(int value) {
        this.value = value;
    }

    // === Other methods ===

    /**
     * Set this status as "Archived".
     */
    public void setArchived() {
        this.setValue(Status.ARCHIVED);
    }

    /**
     * Set this status as "Hired".
     */
    public void setHired() {
        this.setValue(this.hired);
        notifyAllObservers(new Notification("You've Been Hired!",
                "You have been hired at " + this.jobApplication.getJobPosting().getBranch().getName() + "," +
                        " make sure to check your email."));
    }

    /**
     * Advance this status.
     */
    public void advanceStatus() {
        this.value++;
    }

    @Override
    public String toString() {
        return this.descriptions.get(this.value);
    }

    /**
     * Checks whether this status is set to hired.
     *
     * @return true iff this status is set to hired.
     */
    public boolean isHired() {
        return this.value == this.hired;
    }

    /**
     * Checks whether this status is set to archived.
     *
     * @return true iff this status is set to archived.
     */
    public boolean isArchived() {
        return this.value == Status.ARCHIVED;
    }
}
