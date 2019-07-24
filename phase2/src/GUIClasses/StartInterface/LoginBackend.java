package GUIClasses.StartInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;

class LoginBackend {
    /**
     * The general user interface
     */

    // === Instance variables ===
    JobApplicationSystem jobAppSystem;

    // === Constructors ===
    LoginBackend(JobApplicationSystem jobAppSystem) {
        this.jobAppSystem = jobAppSystem;
    }

    // === Getter ===

    public JobApplicationSystem getJobAppSystem() {
        return this.jobAppSystem;
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
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    int createNewApplicant(HashMap<String, String> inputs) {
        String username = inputs.get("username");
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        String postalCode = inputs.get("postalCode");   // TODO edit inputs
        if(!this.checkValidEmail(email)) {
            return 1;
        } else if (name.equals("") || password.equals("")) {
            return 0;
        } else {
            jobAppSystem.getUserManager().createApplicant(
                    username, password, name, email, LocalDate.now(), postalCode);
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
        if(!this.checkValidEmail(email)) {
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
        if(!this.checkValidEmail(email)) {
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