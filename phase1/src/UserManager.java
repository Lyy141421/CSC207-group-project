import java.io.BufferedReader;
import java.time.LocalDate;

class UserManager {
    // Class that stores, creates and updates children of the User class

    // === Instance Variables ===
    private User[] allUsers = new User[50]; //Array in which all users are stored

    UserManager(BufferedReader appUsers) {
        //TODO: instantiate existing users upon startup by reading appUsers
    }

    // === Creating User accounts and adding them to allUsers ===
    private void createInterviewer(String email, Company company, String field, LocalDate dateCreated) {
        Interviewer newInterviewer = new Interviewer(email, company, field, dateCreated);
        this.addToAllUsers(newInterviewer);
    }

    private void createHRCoordinator(String username, String password, String legalName,
                                     String email, Company company, LocalDate dateCreated) {
        HRCoordinator newHRBoy = new HRCoordinator(username, password, legalName, email, company, dateCreated);
        this.addToAllUsers(newHRBoy);
    }

    private void createApplicant(String username, String password,
                                 String legalName, String email, LocalDate dateCreated) {
        Applicant newApplicant = new Applicant(username, password, legalName, email, dateCreated);
        this.addToAllUsers(newApplicant);
    }

    // === User Operations ===

    /**
     * Checks if the password submitted matches the User's password, returns boolean
     */
    boolean login(User checkedUser, String password) {
        return checkedUser.getPassword().equals(password);
    }

    // === Array operations on allUsers ===
    /**
     * @param username Account being searched for
     * @return Account with the provided username. >RETURNS NULL IF NONE FOUND<
     * TODO: Figure out a better way to deal with non-existent accounts
     */
    User findUserByUsername(String username) {
        User returnUser = null;
        for(int i=0; i < allUsers.length - 1; i++) {
            if(allUsers[i] == null) {
                break;
            } else if(allUsers[i].getUsername().equals(username)) {
                returnUser = allUsers[i];
                break;
            }
        }
        return returnUser;
    }

    /**
     * Adds the passed user to allUsers. Checks and expands allUsers size when required.
     *
     * @param newUser The user object being added to allUsers
     */
    private void addToAllUsers(User newUser) {
        this.allUsersSizeCheck();
        for(int i=0; i < this.allUsers.length - 1; i++) {
            if(this.allUsers[i] == null) {
                this.allUsers[i] = newUser;
                break;
            }
        }
    }

    /**
     * Helper function for any method that adds to allUsers.
     * Checks if allUsers is full with usersIsFull, and expands the size of the array if it is.
     */
    private void allUsersSizeCheck() {
        if(this.usersIsFull()) {
            int oldArrayLen = this.allUsers.length;
            User[] newArray = new User[oldArrayLen + 10];
            System.arraycopy(this.allUsers, 0, newArray, 0, oldArrayLen);
            this.allUsers = newArray;
        }
    }

    /**
     * Helper function for allUsersSizeCheck
     * Checks if the array storing users allUsers is full or not.
     */
    private boolean usersIsFull() {
        for(int i=0; i < this.allUsers.length - 1; i++) {
            if(this.allUsers[i] == null) {
                return false;
            }
        }
        return true;
    }
}
