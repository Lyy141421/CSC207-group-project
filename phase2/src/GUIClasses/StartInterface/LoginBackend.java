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

    static final int INVALID_USERNAME = 0;
    static final int BLANK_ENTRY = 1;
    static final int INVALID_LEGAL_NAME = 2;
    static final int INVALID_EMAIL = 3;
    static final int INVALID_POSTAL_CODE = 4;
    static final int INVALID_COMPANY = 5;
    static final int INVALID_BRANCH = 6;
    static final int VALID_BASIC = 7;
    static final int USER_NONEXISTENT = 8;
    static final int WRONG_PASSWORD = 9;
    static final int SUCCESS = 10;

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
        return Pattern.matches("^([A-Z][0-9]){3}$", postalCode);
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
    boolean isValidDate(LocalDate inputtedDate) {
        return this.jobAppSystem.getPreviousLoginDate() == null || !inputtedDate.isBefore(this.jobAppSystem.getPreviousLoginDate());
    }

    /**
     * Create a new user.
     *
     * @param inputs The input fields
     * @return a constant representing the status of the login
     */
    int createNewUser(HashMap<String, String> inputs) {
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        if (name.equals("") || password.equals("")) {
            return BLANK_ENTRY;
        } else if (!isValidLegalName(name)) {
            return INVALID_LEGAL_NAME;
        } else if (!isValidEmail(email)) {
            return INVALID_EMAIL;
        } else {
            return VALID_BASIC;
        }
    }

    /**
     * Create a new Applicant.
     * @return integer constants depending on the validity of the user input.
     */
    int createNewApplicant(HashMap<String, String> inputs) {
        int prelimStatus = this.createNewUser(inputs);
        if (prelimStatus != VALID_BASIC) {
            return prelimStatus;
        }
        String postalCode = inputs.get("postalCode");
        if (!isValidPostalCode(postalCode)) {
            return INVALID_POSTAL_CODE;
        } else {
            String username = inputs.get("username");
            String password = inputs.get("password");
            String name = inputs.get("name");
            String email = inputs.get("email");
            jobAppSystem.getUserManager().createApplicant(
                    username, password, name, email, postalCode, jobAppSystem.getToday());
            return SUCCESS;
        }
    }

    /**
     * Create a new HR Coordinator.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewHRC(HashMap<String, String> inputs) {
        int prelimStatus = this.createNewUser(inputs);
        if (prelimStatus != VALID_BASIC) {
            return prelimStatus;
        }
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        if (company == null) {
            company = jobAppSystem.createCompany(inputs.get("company"));
        }
        Branch branch = company.getBranch(inputs.get("branch"));
        if (branch == null) {
            branch = company.createBranch(inputs.get("branch"), inputs.get("postalCode"));
        }
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        jobAppSystem.getUserManager().createHRCoordinator(
                username, password, name, email, branch, jobAppSystem.getToday());
        return SUCCESS;
    }

    /**
     * Create a new interviewer.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewInterviewer(HashMap<String, String> inputs) {
        int prelimStatus = this.createNewUser(inputs);
        if (prelimStatus != VALID_BASIC) {
            return prelimStatus;
        }
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        String field = inputs.get("field");
        if (company == null) {
            return INVALID_COMPANY;
        } else {
            Branch branch = company.getBranch(inputs.get("branch"));
            if (branch == null) {
                return INVALID_BRANCH;
            } else {
                String username = inputs.get("username");
                String password = inputs.get("password");
                String name = inputs.get("name");
                String email = inputs.get("email");
                jobAppSystem.getUserManager().createInterviewer(
                        username, password, name, email, branch, field, jobAppSystem.getToday());
                return SUCCESS;
            }
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
                    case "postalCode":
                        ret.put("postalCode", ((JTextField) c).getText());
                        break;
                    case "branch":
                        ret.put("branch", ((JTextField) c).getText());
                        break;
                }
            }
        }
        return ret;
    }

    /**
     * Determine status of the login
     * @return constants based on the status of the login.
     */
    int login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return BLANK_ENTRY;
        } else if (!isValidUsername(username)) {
            return INVALID_USERNAME;
        } else if (jobAppSystem.getUserManager().findUserByUsername(username) == null) {
            return USER_NONEXISTENT;
        } else {
            if (jobAppSystem.getUserManager().passwordCorrect(username, password)) {
                return SUCCESS;
            } else {
                return WRONG_PASSWORD;
            }
        }
    }
}