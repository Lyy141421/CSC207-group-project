package Main;

import NotificationSystem.Notification;
import NotificationSystem.NotificationFactory;
import NotificationSystem.NotificationManager;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public abstract class User implements Serializable, Observer {
    /**
     * An account in the job application system.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // The password length of a random generated password
    private static String DEFAULT_PASSWORD = "password";

    // === Instance variables ===
    // The unique account username
    private String username;
    // The account password
    private String password;
    // The user's legal name
    private String legalName;
    // The user's email address
    private String email;
    // The date the account was created
    private LocalDate dateCreated;
    // The Notification Manager
    private NotificationManager notificationManager = new NotificationManager();

    // === Public methods ===

    // === Constructors ===
    public User() {
    }

    public User(String email, LocalDate dateCreated) {
        this.addNotification(new NotificationFactory().createNotification("Welcome", null));
        this.username = email;
        this.password = DEFAULT_PASSWORD;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    public User(String username, String password, String legalName, String email, LocalDate dateCreated) {
        this.addNotification(new NotificationFactory().createNotification("Welcome", null));
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    // === Getters ===

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getLegalName() {
        return this.legalName;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public NotificationManager getNotificationManager(){
        return this.notificationManager;
    }

    // === Setters ===
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // === Other methods ===

    public abstract String[] getDisplayedProfileCategories();

    public abstract String[] getDisplayedProfileInformation();

    /**
     * The observer receiving something to observe
     *
     * @param obj - the object to be observed
     */
    public void update(Object obj) {
        if(obj instanceof Notification){
            this.addNotification((Notification)obj);
        }
    }

    /**
     * Gets an ArrayList of All Notifications of this User
     *
     * @return - ArrayList of Notifications
     */
    public ArrayList<Notification> getAllNotifications(){
        return this.notificationManager.getNotifications();
    }

    /**
     * Adds a Notification to this user
     *
     * @param notification The Notification to add
     */
    public void addNotification(Notification notification){
        this.getNotificationManager().add(notification);
    }

    /**
     * Removes a Notification from this user
     *
     * @param notification The Notification to remove
     */
    public void removeNotification(Notification notification){
        this.getNotificationManager().remove(notification);
    }
    /**
     * Removes all Notifications from this user
     */
    public void removeAllNotifications(){
        this.getNotificationManager().getNotifications().clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        } else {
            return this.username.equals(((User) obj).username);
        }
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < username.length(); i++) {
            sum += username.charAt(i);
        }
        return sum;
    }

}