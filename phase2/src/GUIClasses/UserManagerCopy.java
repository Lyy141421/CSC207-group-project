/*
package GUIClasses;

import UsersAndJobObjects.*;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserManagerCopy {
    // Class that stores, creates and updates children of the UsersAndJobObjects.User class

    // === Instance Variables ===
    private ArrayList<User> allUsers = new ArrayList<>(); //Array in which all users are stored
    private String key = "CSC207Summer2019"; //Key used for password AES Encryption

    public UserManagerCopy() {
        //TODO: delete after testing
    }

    UserManagerCopy(BufferedReader appUsers) {
        //TODO: instantiate existing users upon startup by reading appUsers
        // note that these stored passwords will ALREADY BE ENCRYPTED
    }

    // === Creating UsersAndJobObjects.User accounts and adding them to allUsers ===

    */
/**
 * All of the following methods create new instances of the various child classes of UsersAndJobObjects.User
 *
 * @param newUser Checks if the user being created is new/whether encryption is required
 *//*

    public Interviewer createInterviewer(String username, String password, String legalName, String email, Company company,
                                         String field, LocalDate dateCreated, boolean newUser) {
        if (newUser) {
            password = this.encrypt(password);
            //TODO: Write to storage
        }
        Interviewer newInterviewer = new Interviewer(username, password, legalName, email, company, field, dateCreated);
        this.allUsers.add(newInterviewer);
        return newInterviewer;
    }

    public HRCoordinator createHRCoordinator(String username, String password, String legalName,
                                             String email, Company company, LocalDate dateCreated, boolean newUser) {
        if (newUser) {
            password = this.encrypt(password);
            //TODO: Write to storage
        }
        HRCoordinator newHRC = new HRCoordinator(username, password, legalName, email, company, dateCreated);
        this.allUsers.add(newHRC);
        return newHRC;
    }

    public Applicant createApplicant(String username, String password,
                                     String legalName, String email, LocalDate dateCreated, boolean newUser) {
        if (newUser) {
            password = this.encrypt(password);
            //TODO: Write to storage
        }
        Applicant newApplicant = new Applicant(username, password, legalName, email, dateCreated);
        this.allUsers.add(newApplicant);
        return newApplicant;
    }

    // === UsersAndJobObjects.User Operations and password encryption ===

    */
/**
 * Checks if the password submitted matches the UsersAndJobObjects.User's password, returns boolean
 *//*

    public boolean passwordCorrect(String checkedUsername, String password) {
        User checkedUser = findUserByUsername(checkedUsername);
        return this.decrypt(checkedUser.getPassword()).equals(password);
    }

    */
/**
 * Encrypts user passwords using a predefined Key and AES encryption.
 *
 * @param password the password being encrypted
 * @return the password after encryption
 *//*

    private String encrypt(String password) {
//        try
//        {
//            Key aesKey = new SecretKeySpec(this.key.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
//
//            byte[] encrypted = cipher.doFinal(password.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b: encrypted) {
//                sb.append((char)b);
//            }
//
//            return sb.toString();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return "Encryption Error";
//        }
        return password;
    }

    */
/**
 * Decrypts stored passwords using a predefined Key and AES decryption.
 *
 * @param encrypted the password, stored in encrypted form
 * @return the password decrypted
 *//*

    private String decrypt(String encrypted) {
//        try
//        {
//            Key aesKey = new SecretKeySpec(this.key.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES");
//
//            byte[] bb = new byte[encrypted.length()];
//            for (int i=0; i<encrypted.length(); i++) {
//                bb[i] = (byte) encrypted.charAt(i);
//            }
//
//            cipher.init(Cipher.DECRYPT_MODE, aesKey);
//            return new String(cipher.doFinal(bb));
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            return "Decryption Error";
//        }
        return encrypted;
    }

    // === Array operations on allUsers ===

    */
/**
 * @param username Account being searched for
 * @return Account with the provided username. >RETURNS NULL IF NONE FOUND<
 *//*

    public User findUserByUsername(String username) {
        for (User user : this.allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    */
/**
 * Deletes a user from the system based on the username
 *
 * @param username the username of the user being deleted
 *//*

    void deleteByUsername(String username) {
        for (User user : this.allUsers) {
            if (user.getUsername().equals(username)) {
                this.allUsers.remove(user);
                break;
            }
        }
        //TODO: Delete from file system
    }

    */
/**
 * FOR ALL METHODS BELOW THIS JAVADOC
 * Returns all of the relevant type of users in an arraylist.
 *
 * @return list of all existing (UsersAndJobObjects.User type)
 *//*

    public ArrayList getAllApplicants() {
        ArrayList<User> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof Applicant) {
                ret.add(user);
            }
        }
        return ret;
    }

    public ArrayList getAllHRCoordinator() {
        ArrayList<User> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof HRCoordinator) {
                ret.add(user);
            }
        }
        return ret;
    }

    public ArrayList getAllInterviewers() {
        ArrayList<User> ret = new ArrayList<>();
        for (User user : this.allUsers) {
            if (user instanceof Interviewer) {
                ret.add(user);
            }
        }
        return ret;
    }

    public void addUserList(ArrayList list) {
        allUsers.addAll(list);
    }
}
*/
