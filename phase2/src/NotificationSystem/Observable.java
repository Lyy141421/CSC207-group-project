package NotificationSystem;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public abstract class Observable {

    // A list of observers for pushing notifications
    private ArrayList<Observer> observer_list = new ArrayList<>();

    /**
     * Adding an observer to the notification recipient list
     */
    protected void attach(Observer observer){
        if (!observer_list.contains(observer)) {
            observer_list.add(observer);
        }
    }

    /**
     * Removing an observer from the notification recipient list
     */
    protected void detach(Observer observer){
        observer_list.remove(observer);
    }

    /**
     * Sending a notification to all observers
     *
     * @param notification - The notification to be sent
     */
    protected void notifyAllObservers(Notification notification){
        updateObserverList();
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
    protected void notifyObserver(Observer observer, Notification notification){
        observer.update(notification);
    }

    /**
     * A method to internally change the structure of the observer list
     */
    protected void updateObserverList() { }

}
