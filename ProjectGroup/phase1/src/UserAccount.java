import java.time.LocalDate;

abstract class UserAccount {
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

    // === Constructors ===

    /**
     * Create a user account.
     */
    UserAccount() {
        this.dateCreated = LocalDate.now();
    }

    ;

    /**
     * Create a user account.
     *
     * @param username    The username of this user.
     * @param dateCreated The date on which this account was created.
     */
    UserAccount(String username, LocalDate dateCreated) {
        this.username = username;
        this.dateCreated = LocalDate.now();
    }

    /**
     * Create a user account.
     *
     * @param username    The user's unique ID.
     * @param password    The account password.
     * @param legalName   The user's legal name.
     * @param email       The user's email address.
     * @param dateCreated The date on which this account was created.
     */
    UserAccount(String username, String password, String legalName, String email, LocalDate dateCreated) {
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    // === Getters ===

    /**
     * Get the username associated with this account.
     *
     * @return the username associated with this account.
     */
    String getUsername() {
        return this.username;
    }

    /**
     * Get the password associated with this account.
     *
     * @return the password associated with this account.
     */
    String getPassword() {
        return this.password;
    }

    /**
     * Get the legal name of this user.
     *
     * @return the legal name of this user.
     */
    String getLegalName() {
        return this.legalName;
    }

    /**
     * Get the user's email address.
     *
     * @return the email address of this user.
     */
    String getEmail() {
        return this.email;
    }

    /**
     * Get the date this account was created.
     *
     * @return the date this account was created.
     */
    LocalDate getDateCreated() {
        return this.dateCreated;
    }

    // === Setters ===

    /**
     * Set the username associated with this account.
     *
     * @param username The username associated with this account.
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password associated with this account.
     *
     * @param password The password associated with this account.
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the legal name of this user.
     *
     * @param legalName The legal name of this user.
     */
    void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * Set the email of this user.
     *
     * @param email The user's email address.
     */
    void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the date this account was created.
     *
     * @param dateCreated The date this account was created.
     */
    void setDateCreated(LocalDate dateCreated) {
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

    /**
     * Report whether this account is the same as other.
     *
     * @param other The other account being compared to.
     * @return true iff this account has the same username as the other account.
     */
    boolean equals(UserAccount other) {
        return this.username.equals(other.username);
    }

}
