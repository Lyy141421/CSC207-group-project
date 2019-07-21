package NotificationSystem;

public interface Observable {

    void attach(Observer observer);

    void detach(Observer observer);

    void notifyAllObservers(Notification notification);

    void notifyObserver(Observer observer, Notification notification);

}
