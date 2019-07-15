package Main;

import ApplicantStuff.JobApplication;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class User implements Serializable {
    /**
     * An account in the job application system.
     */

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

    // === Public methods ===

    // === Constructors ===
    public User() {
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

    // === Setters ===
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // === Other methods ===

    /**
     * Get the job application with this ID.
     *
     * @param ID The ID of the job application.
     * @return the job application with this ID or null if not found.
     */
    public JobApplication findJobAppById(int ID) {
        return null;
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