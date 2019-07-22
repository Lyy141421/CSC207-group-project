package NotificationSystem;

public interface Observable {

    /**
     * Adding an observer to the notification recipient list
     */
    void attach(Observer observer);

    /**
     * Removing an observer from the notification recipient list
     */
    void detach(Observer observer);

    /**
     * Sending a notification to all observers
     *
     * @param notification - The notification to be sent
     */
    void notifyAllObservers(Notification notification);

    /**
     *Sending a notification to a particular observer
     *
     * @param observer - The observer receiving the notification
     * @param notification - The notification to be sent
     */
    void notifyObserver(Observer observer, Notification notification);

}
