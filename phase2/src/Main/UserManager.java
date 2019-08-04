package Main;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.Branch;
import FileLoadingAndStoring.DataLoaderAndStorer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {
    // Class that stores, creates and updates children of the User class

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
    public Applicant createApplicant(String username, String password, String legalName, String email, String postalCode,
                                     LocalDate dateCreated) {
        HashMap<String, String> fsaHashMap = DataLoaderAndStorer.loadFSAHashMap();
        String cma = fsaHashMap.get(postalCode.substring(0, 3).toUpperCase());
        Applicant newApplicant = new Applicant(username, password, legalName, email, cma, dateCreated);
        this.allUsers.add(newApplicant);
        return newApplicant;
    }

    public Interviewer createInterviewer(String username, String password, String legalName, String email, Branch branch,
                                         String field, LocalDate dateCreated) {
        Interviewer newInterviewer = new Interviewer(username, password, legalName, email, branch, field, dateCreated);
        this.allUsers.add(newInterviewer);
        return newInterviewer;
    }

    public HRCoordinator createHRCoordinator(String username, String password, String legalName,
                                             String email, Branch branch, LocalDate dateCreated) {
        HRCoordinator newHRC = new HRCoordinator(username, password, legalName, email, branch, dateCreated);
        this.allUsers.add(newHRC);
        return newHRC;
    }

    public Reference createReference(String email, LocalDate dateCreated) {
        Reference newReference = new Reference(email, dateCreated);
        this.allUsers.add(newReference);
        return newReference;
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
     * Add referees for this job application.
     *
     * @param jobApp The job application that will need reference letters.
     * @param emails The emails of the referees that the applicant submitted.
     * @return a map of new references' emails to their passwords
     */
    public void addReferences(JobApplication jobApp, ArrayList<String> emails) {
        for (String email : emails) {
            Reference reference = (Reference) this.findUserByEmail(email);
            if (reference == null) {
                reference = this.createReference(email, jobApp.getApplicationDate());
            }
            reference.addJobApplication(jobApp);
        }
    }


    /**
     * Find the user with this email.
     *
     * @param email The email being searched for.
     * @return the user with the provided email or null if not found.
     */
    private User findUserByEmail(String email) {
        for (User user : this.allUsers) {
            if (user.getEmail().equals(email)) {
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

    /**
     * Delete all reference accounts that have no applications they need to submit reference letters for.
     */
    public void deleteAllEmptyReferenceAccounts() {
        for (User user : (ArrayList<User>) this.allUsers.clone()) {
            if (user instanceof Reference) {
                if (((Reference) user).getJobAppsForReference().isEmpty()) {
                    this.allUsers.remove(user);
                }
            }
        }
    }
}
