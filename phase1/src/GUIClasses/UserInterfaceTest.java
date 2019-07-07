package GUIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Scanner;

class UserInterfaceTest {
    /**
     * The general user interface
     */

    // === Constructors ===
    UserInterfaceTest() {
    }

    // === Inherited methods ===
    void run(LocalDate today) {
    }

    // === Private methods ===

    /**
     * Get an e-mail address as input from the user. E-mail address must be in valid format.
     *
     * @return the e-mail address inputted by the user.
     */

    private boolean checkValidEmail(String email) {
        if (!email.contains("@") || email.charAt(0) == '@')
            return false;
        else {
            String[] splitInput = email.split("@", 2);
            return !(!splitInput[1].contains(".") || splitInput[1].charAt(0) == '.'
                    || splitInput[1].charAt(splitInput[1].length()-1) == '.');
        }
    }

    /**
     * Create a new Applicant.
     * @return 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewApplicant(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        if(!this.checkValidEmail(email)) {
            return 1;
        } else {
            JobApplicationSystem.getUserManager().createApplicant(
                    username, password, name, email, LocalDate.now(), true);
            return 3;
        }
    }

    /**
     * Create a new HR Coordinator.
     * @return 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewHRC(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        Company company = JobApplicationSystem.getCompany(inputs.get("company"));
        if(!this.checkValidEmail(email)) {
            return 1;
        } else if(company == null) {
            return 2;
        } else {
            JobApplicationSystem.getUserManager().createHRCoordinator(
                    username, password, name, email, company, LocalDate.now(), true);
            return 3;
        }
    }

    /**
     * Create a new interviewer.
     * @return 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewInterviewer(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("passwords");
        String name = inputs.get("name");
        String email = inputs.get("email");
        Company company = JobApplicationSystem.getCompany(inputs.get("company"));
        String field = "what the fuck"; //TODO: figure out what to do here
        if(!this.checkValidEmail(email)) {
            return 1;
        } else if(company == null) {
            return 2;
        } else {
            JobApplicationSystem.getUserManager().createInterviewer(
                    username, password, name, email, company, field, LocalDate.now(), true);
            return 3;
        }
    }

    /**
     * Determine status of the login
     * @return 1 - no user exists, 2 - successful login, 3 - wrong pass
     */
    int login(String username, String password) {
        if (username.equals("")) {
            return 3;
        }
        if (JobApplicationSystem.getUserManager().findUserByUsername(username) == null) {
            return 1;
        }
        else {
            if(JobApplicationSystem.getUserManager().passwordCorrect(username, password)) {
                return 2;
            } else {
                return 3;
            }
        }
    }
}