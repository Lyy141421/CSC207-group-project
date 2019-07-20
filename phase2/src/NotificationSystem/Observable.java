package NotificationSystem;

public interface Observable {

    public void attach(Observer observer);

    public void detach(Observer observer);

    public void notifyAllObservers(Notification notification);

    public void notifyObserver(Observer observer, Notification notification);

}
