import java.time.LocalDate;

abstract class UserAccount {
    /**
     * An account in the job application system.
     */

    // === Instance variables ===
    // The user's legal name
    private String legalName;
    // The account username
    private String username;
    // The account password
    private String password;
    // The date the account was created
    private LocalDate dateCreated;

    // === Constructors ===

    /**
     * Create a user account.
     */
    UserAccount() {
    }

    ;

    /**
     * Create a user account.
     *
     * @param username    The username of this user.
     * @param dateCreated The date this account was created.
     */
    UserAccount(String username, LocalDate dateCreated) {
        this.username = username;
        this.dateCreated = dateCreated;
    }

    /**
     * Create a user account.
     *
     * @param legalName   The user's legal name.
     * @param username    The account username.
     * @param password    The account password.
     * @param dateCreated The date this account was created.
     */
    UserAccount(String legalName, String username, String password, LocalDate dateCreated) {
        this.legalName = legalName;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
    }

    // === Getters ===

    /**
     * Get the legal name of this user.
     *
     * @return the legal name of this user.
     */
    String getLegalName() {
        return this.legalName;
    }

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
     * Get the date this account was created.
     *
     * @return the date this account was created.
     */
    LocalDate getDateCreated() {
        return this.dateCreated;
    }

    // === Setters ===

    /**
     * Set the legal name of this user.
     *
     * @param legalName The legal name of this user.
     */
    void setLegalName(String legalName) {
        this.legalName = legalName;
    }

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

}
