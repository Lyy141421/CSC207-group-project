package ApplicantStuff;

import NotificationSystem.Notification;
import NotificationSystem.Observable;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class Status implements Serializable, Observable {

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
    // The integer that represents a "Hired" status
    private int hired;
    private int lastRound;

    // === Instance variable ===
    private int value = Status.SUBMITTED;
    private ArrayList<Observer> observer_list = new ArrayList<>(); //A list of observers to this status
    private JobApplication jobApplication;

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
    public void setDescriptions(ArrayList<Object[]> interviewConfiguration) {
        for (int i = 0; i < interviewConfiguration.size(); i++) {
            this.descriptions.put(i, (String) interviewConfiguration.get(i)[0]);
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
        notifyAllObservers(new Notification("Hired!!",
                "You have been hired at "+ this.jobApplication.getJobPosting().getBranch().getName() +", make sure to check your email"));
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
    boolean isHired() {
        return this.value == this.hired;
    }

    /**
     * Checks whether this status is set to archived.
     *
     * @return true iff this status is set to archived.
     */
    boolean isArchived() {
        return this.value == Status.ARCHIVED;
    }

    // Status class
    boolean isUnderReview() {
        return this.value == Status.UNDER_REVIEW;
    }

    boolean isOnFirstRound() {
        return this.value == Status.FIRST_ROUND;
    }

    boolean isOnLastRound() {
        return this.value == this.lastRound;
    }

    // Observable Classes

    /**
     * Adding an observer to the notification recipient list
     */
    public void attach(Observer observer){
        if (!observer_list.contains(observer)) {
            observer_list.add(observer);
        }
    }

    /**
     * Removing an observer from the notification recipient list
     */
    public void detach(Observer observer){
        observer_list.remove(observer);
    }

    /**
     * Sending a notification to all observers
     *
     * @param notification - The notification to be sent
     */
    public void notifyAllObservers(Notification notification){updateObserverList();
        for (Observer observer : observer_list) {
            notifyObserver(observer, notification);
        }
    }

    /**
     *Sending a notification to a particular observer
     *
     * @param observer - The observer receiving the notification
     * @param notification - The notification to be sent
     */
    public void notifyObserver(Observer observer, Notification notification){
        observer.update(notification);
    }

    /**
     * A method to internally change the structure of the observer list
     */
    public void updateObserverList(){ }

}
