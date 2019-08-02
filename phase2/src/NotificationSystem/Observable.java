package NotificationSystem;

import java.util.ArrayList;

public abstract class Observable {

    // A list of observers for pushing notifications
    private ArrayList<Observer> observerList = new ArrayList<>();

    /**
     * Adding an observer to the notification recipient list
     */
    protected void attach(Observer observer){
        if (!observerList.contains(observer)) {
            observerList.add(observer);
        }
    }

    /**
     * Removing an observer from the notification recipient list
     */
    protected void detach(Observer observer){
        observerList.remove(observer);
    }

    /**
     * Sending a notification to all observers
     *
     * @param notification - The notification to be sent
     */
    protected void notifyAllObservers(Notification notification){
        updateObserverList();
        for (Observer observer : observerList) {
            notifyObserver(observer, notification);
        }
    }

    /**
     * Checks if an observer is attached to this observable
     *
     * @param observer - The observer to check
     * @return - Weather or not the observer is in this observable
     */
    protected boolean containsObserver(Observer observer) {
        return observerList.contains(observer);
    }

    /**
     *Sending a notification to a particular observer
     *
     * @param observer - The observer receiving the notification
     * @param notification - The notification to be sent
     */
    protected void notifyObserver(Observer observer, Notification notification){
        observer.update(notification);
    }

    /**
     * A method to internally change the structure of the observer list
     */
    protected void updateObserverList() { }

}
