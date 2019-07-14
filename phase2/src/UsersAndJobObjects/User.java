package UsersAndJobObjects;

import Managers.DocumentManager;

import java.io.IOException;
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
    // The applicant's document manager
    private DocumentManager documentManager;

    // === Public methods ===

    // === Getters ===
    public String getId() {
        return getUsername();
    }

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

    public DocumentManager getDocumentManager() {
        return this.documentManager;
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

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Constructors ===
    User() {
    }

    User(String username, String password, String legalName, String email, LocalDate dateCreated) throws IOException {
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.documentManager = new DocumentManager(this);
    }

    User(String username, String password, String legalName, String email, LocalDate dateCreated,
         DocumentManager documentManager) {
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.documentManager = documentManager;
    }
}