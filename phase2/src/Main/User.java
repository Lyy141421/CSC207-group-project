package Main;

import NotificationSystem.Notification;
import NotificationSystem.NotificationManager;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

public abstract class User implements Serializable, Observer {
    /**
     * An account in the job application system.
     */

    // === Class variables ===
    // The password length of a random generated password
    private static int PASSWORD_LENGTH = 8;
    // The characters allowed in the random password
    private static String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

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
    private NotificationManager notification_manager = new NotificationManager();

    // === Public methods ===

    // === Constructors ===
    public User() {
    }

    public User(String email, LocalDate dateCreated) {
        this.username = email;
        this.password = this.generateRandomPassword();
        this.email = email;
        this.dateCreated = dateCreated;
    }

    public User(String username, String password, String legalName, String email, LocalDate dateCreated) {
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
        return this.notification_manager;
    }

    // === Setters ===
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // === Other methods ===

    /**
     * Generates a random password of 8 characters.
     *
     * @return a random password of 8 characters.
     * @author https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
     */
    private String generateRandomPassword() {
        StringBuilder passwordSB = new StringBuilder();
        Random random = new Random();
        while (passwordSB.length() < PASSWORD_LENGTH) { // length of the random string.
            int index = (int) (random.nextFloat() * PASSWORD_CHARS.length());
            passwordSB.append(PASSWORD_CHARS.charAt(index));
        }
        return passwordSB.toString();
    }

    public abstract String[] getDisplayedInformation();

    public void Update(Object obj){
        if(obj instanceof Notification){
            this.getNotificationManager().add((Notification)obj);
        }

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