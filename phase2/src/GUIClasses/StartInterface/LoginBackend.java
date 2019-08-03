package GUIClasses.StartInterface;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import Main.JobApplicationSystem;
import Main.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Pattern;

class LoginBackend {
    /*
     * The general user interface
     */

    private JobApplicationSystem jobAppSystem;

    // === Constructor ===
    LoginBackend(JobApplicationSystem jobAppSystem) {
        this.jobAppSystem = jobAppSystem;
    }


    User findUserByUsername(String username) {
        return jobAppSystem.getUserManager().findUserByUsername(username);
    }

    // === Private methods ===

    /**
     * Check whether email address is valid.
     *
     * @return true iff the email address is valid.
     */
    private boolean isValidEmail(String email) {
        String regex = "^([A-Za-z0-9]+(?:([._-])[A-Za-z0-9]+|[A-Za-z0-9]*))+@([A-Za-z0-9]+\\.)+[a-z]+$$";
        return Pattern.matches(regex, email);
    }

    /**
     * Check whether this postal code is valid.
     *
     * @param postalCode The postal code inputted by the user.
     * @return true iff the postal code is a valid Canadian postal code.
     */
    private boolean isValidPostalCode(String postalCode) {
        return Pattern.matches("(^[A-Z][0-9]){3}$", postalCode);
    }

    private boolean isValidUsername(String username) {
        return Pattern.matches("^[A-Za-z0-9]+$", username);
    }

    private boolean isValidLegalName(String legalName) {
        String regex = "^[A-Z][a-z]+(?:([- ])[A-Z][a-z]+|[a-z]*)+$";
        return Pattern.matches(regex, legalName);
    }

    /**
     * Get and set today's date as inputted by the user.
     */
    private boolean isValidDate(LocalDate inputtedDate) {
        if (this.jobAppSystem.getPreviousLoginDate() == null || !inputtedDate.isBefore(this.jobAppSystem.getPreviousLoginDate())) {
            this.jobAppSystem.setPreviousLoginDate(inputtedDate);
            this.jobAppSystem.setToday(inputtedDate);
            return true;
        }
        return false;
    }

    private boolean isEverythingValid(String username, String legalName, String email, String postalCode) {
        return this.isValidUsername(username) && this.isValidLegalName(legalName) && this.isValidEmail(email) &&
                this.isValidPostalCode(postalCode);
    }

    /**
     * Create a new Applicant.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewApplicant(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        String postalCode = inputs.get("postalCode");   // TODO edit inputs
        if (!isEverythingValid(username, name, email, postalCode)) {
            return 1;
        } else if (name.equals("") || password.equals("")) {
            return 0;
        } else {
            jobAppSystem.getUserManager().createApplicant(
                    username, password, name, email, jobAppSystem.getToday(), postalCode);
            return 3;
        }
    }

    /**
     * Create a new HR Coordinator.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewHRC(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        Branch branch = company.getBranch(inputs.get("branch"));
        if (!this.isValidEmail(email)) {
            return 1;
        } else if(company == null) {
            return 2;
        } else if(name.equals("") || password.equals("")) {
            return 0;
        } else {
            jobAppSystem.getUserManager().createHRCoordinator(
                    username, password, name, email, branch, LocalDate.now());
            return 3;
        }
    }

    /**
     * Create a new interviewer.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewInterviewer(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        Branch branch = company.getBranch(inputs.get("branch"));
        String field = "what the fuck"; //TODO: figure out what to do here
        if (!this.isValidEmail(email)) {
            return 1;
        } else if(name.equals("") || password.equals("")) {
            return 0;
        } else if(company == null) {
            return 2;
        } else {
            jobAppSystem.getUserManager().createInterviewer(
                    username, password, name, email, branch, field, LocalDate.now());
            return 3;
        }
    }

    /**
     * Collects user input data from the different forms, so that a new account may be created
     */
    HashMap<String, String> getInputs(Component[] items, String newUsername) {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("username", newUsername);
        for(Component c : items) {
            if(c.getName() != null) {
                switch (c.getName()) {
                    case "password":
                        ret.put("password", new String(((JPasswordField)c).getPassword()));
                        break;
                    case "name":
                        ret.put("name", ((JTextField)c).getText());
                        break;
                    case "email":
                        ret.put("email", ((JTextField)c).getText());
                        break;
                    case "company":
                        ret.put("company", ((JTextField)c).getText());
                        break;
                    case "field":
                        ret.put("field", ((JTextField)c).getText());
                        break;
                }
            }
        }
        return ret;
    }

    /**
     * Determine status of the login
     * @return 0 - blank field, 1 - no user exists, 2 - successful login, 3 - wrong pass
     */
    int login(String username, String password) {
        if (jobAppSystem.getUserManager().findUserByUsername(username) == null) {
            return 1;
        } else if (username.equals("") || password.equals("")) {
            return 0;
        }
        else {
            if (jobAppSystem.getUserManager().passwordCorrect(username, password)) {
                return 2;
            } else {
                return 3;
            }
        }
    }
}