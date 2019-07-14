package Main;

import ApplicantStuff.Applicant;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.Company;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserManager {
    // Class that stores, creates and updates children of the UsersAndJobObjects.User class

    // === Instance Variables ===
    private ArrayList<User> allUsers = new ArrayList<>(); //Array in which all users are stored

    // === Getter ===
    public ArrayList<User> getAllUsers() {
        return this.allUsers;
    }

    // === Setter ===
    public void setAllUsers(ArrayList<User> users) {
        this.allUsers = users;
    }

    // === Creating User accounts and adding them to allUsers ===

    /**
     * All of the following methods create new instances of the various child classes of User
     */
    public Applicant createApplicant(String username, String password, String legalName, String email, LocalDate dateCreated)
            throws IOException {
        Applicant newApplicant = new Applicant(username, password, legalName, email, dateCreated);
        this.allUsers.add(newApplicant);
        return newApplicant;
    }

    public Interviewer createInterviewer(String username, String password, String legalName, String email, Company company,
                                         String field, LocalDate dateCreated) throws IOException {
        Interviewer newInterviewer = new Interviewer(username, password, legalName, email, company, field, dateCreated);
        this.allUsers.add(newInterviewer);
        return newInterviewer;
    }

    public HRCoordinator createHRCoordinator(String username, String password, String legalName,
                                             String email, Company company, LocalDate dateCreated) throws IOException {
        HRCoordinator newHRC = new HRCoordinator(username, password, legalName, email, company, dateCreated);
        this.allUsers.add(newHRC);
        return newHRC;
    }

    // === User operations ===

    /**
     * Checks if the password submitted matches the UsersAndJobObjects.User's password, returns boolean
     */
    public boolean passwordCorrect(String checkedUsername, String password) {
        User checkedUser = findUserByUsername(checkedUsername);
        return checkedUser.getPassword().equals(password);
    }

    // === Array operations on allUsers ===

    /**
     * @param username Account being searched for
     * @return Account with the provided username. >RETURNS NULL IF NONE FOUND<
     */
    public User findUserByUsername(String username) {
        for (User user : this.allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * FOR ALL METHODS BELOW THIS JAVADOC
     * Returns all of the relevant type of users in an arraylist.
     *
     * @return list of all existing (UsersAndJobObjects.User type)
     */
    public ArrayList<Applicant> getAllApplicants() {
        ArrayList<Applicant> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof Applicant) {
                ret.add((Applicant) user);
            }
        }
        return ret;
    }

    public ArrayList<Interviewer> getAllInterviewers() {
        ArrayList<Interviewer> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof Interviewer) {
                ret.add((Interviewer) user);
            }
        }
        return ret;
    }

    public ArrayList<HRCoordinator> getAllHRCoordinators() {
        ArrayList<HRCoordinator> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof HRCoordinator) {
                ret.add((HRCoordinator) user);
            }
        }
        return ret;
    }
}
