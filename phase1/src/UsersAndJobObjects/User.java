package UsersAndJobObjects;

import GUIClasses.UserInterface;
import java.time.LocalDate;

public abstract class User {
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
    // The interface for this user
    private UserInterface userInterface = new UserInterface(this);


    // === Public methods ===
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

    public UserInterface getUserInterface() {
        return this.userInterface;
    }

    // === Setters ===

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // === Other methods ===
    /**
     * Report whether this user is the same as obj.
     *
     * @param obj The other object being compared to.
     * @return true iff obj is an user and has the same username as this account.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        else {
            return this.username.equals(((User)obj).username);
        }
    }

    /**
     * Return a hashcode for this user.
     * @return an int; the same int should be returned for all users equal to this user.
     */
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

    User(String username, String password, String legalName, String email, LocalDate dateCreated) {
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    // === Other methods ===

    /**
     * Report whether this account has the same username as otherUsername.
     *
     * @param otherUsername The other username to be compared to.
     * @return true iff this username and otherUsername are identical.
     */
    boolean hasSameUsername(String otherUsername) {
        return this.username.equals(otherUsername);
    }
}
