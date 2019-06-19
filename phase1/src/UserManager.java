import java.io.BufferedReader;
import java.time.LocalDate;
import java.util.ArrayList;

class UserManager {
    // Class that stores, creates and updates children of the User class

    // === Instance Variables ===
    private ArrayList<User> allUsers = new ArrayList<>(); //Array in which all users are stored

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
     */
    User findUserByUsername(String username) {
        for (User user : this.allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;    // You can throw a NullPointerException and then handle it in the login-loop in the interface
    }

    /**
     * Adds the passed user to allUsers.
     *
     * @param newUser The user object being added to allUsers
     */
    private void addToAllUsers(User newUser) {
        this.allUsers.add(newUser);
    }
}
