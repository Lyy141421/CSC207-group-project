package Managers;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.User;
import UsersAndJobObjects.Company;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.security.Key;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserManager {
    // Class that stores, creates and updates children of the UsersAndJobObjects.User class

    // === Instance Variables ===
    private ArrayList<User> allUsers = new ArrayList<>(); //Array in which all users are stored
    private String key = "CSC207Summer2019"; //Key used for password AES Encryption

    // === Creating User accounts and adding them to allUsers ===
    /**
     * All of the following methods create new instances of the various child classes of User
     */
    public Interviewer createInterviewer(String username, String password, String legalName, String email, Company company,
                                         String field, LocalDate dateCreated) {
        Interviewer newInterviewer = new Interviewer(username, password, legalName, email, company, field, dateCreated);
        this.allUsers.add(newInterviewer);
        return newInterviewer;
    }

    public HRCoordinator createHRCoordinator(String username, String password, String legalName,
                                             String email, Company company, LocalDate dateCreated) {
        HRCoordinator newHRC = new HRCoordinator(username, password, legalName, email, company, dateCreated);
        this.allUsers.add(newHRC);
        return newHRC;
    }

    public Applicant createApplicant(String username, String password,
                                     String legalName, String email, LocalDate dateCreated) {
        Applicant newApplicant = new Applicant(username, password, legalName, email, dateCreated);
        this.allUsers.add(newApplicant);
        return newApplicant;
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
    public ArrayList getAllApplicants() {
        ArrayList<User> ret = new ArrayList<>();
        for(User user: this.allUsers) {
            if(user instanceof Applicant) {
                ret.add(user);
            }
        }
        return ret;
    }

    public ArrayList getAllHRCoordinator() {
        ArrayList<User> ret = new ArrayList<>();
        for(User user: this.allUsers) {
            if(user instanceof HRCoordinator) {
                ret.add(user);
            }
        }
        return ret;
    }

    public ArrayList getAllInterviewers() {
        ArrayList<User> ret = new ArrayList<>();
        for(User user: this.allUsers) {
            if(user instanceof Interviewer) {
                ret.add(user);
            }
        }
        return ret;
    }

    public void addUserList(ArrayList list){
        allUsers.addAll(list);
    }
}
