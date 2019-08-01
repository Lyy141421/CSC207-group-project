package NotificationSystem;

import java.util.ArrayList;

public interface Observer {

    /**
     * The observer receiving something to observe
     *
     * @param obj - the object to be observed
     */
    void update(Object obj);

    /**
     * Gets an ArrayList of All Notifications of this User
     *
     * @return - ArrayList of Notifications
     */
    ArrayList<Notification> getAllNotifications();

    /**
     * Adds a Notification to this user
     *
     * @param notification The Notification to add
     */
    void addNotification(Notification notification);

    /**
     * Removes a Notification from this user
     *
     * @param notification The Notification to remove
     */
    void removeNotification(Notification notification);

    /**
     * Removes all Notifications from this user
     */
    void removeAllNotifications();

}
