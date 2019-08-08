package GUIClasses.StartInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplicationManager;
import CompanyStuff.*;
import CompanyStuff.JobPostings.BranchJobPosting;
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

    private static final String VALID_BASIC = "Ok";
    private static final String EMAIL_REGEX = "^([A-Za-z0-9]+(?:([._\\-])[A-Za-z0-9]+|[A-Za-z0-9]*))+@([A-Za-z0-9]+\\.)+[A-Za-z]+$";

    private JobApplicationSystem jobAppSystem;

    // === Constructor ===
    LoginBackend(JobApplicationSystem jobAppSystem) {
        this.jobAppSystem = jobAppSystem;
    }

    User findUserByUsername(String username) {
        User user = jobAppSystem.getUserManager().findUserByUsername(username);
        if (user instanceof HRCoordinator) {
//            HRCoordinator hr = (HRCoordinator) user;
//            this.jobAppSystem.getUserManager().getHR(hr).setBranch(this.jobAppSystem.getBranch(hr.getBranch()));
            Branch branch = this.jobAppSystem.getBranch(((HRCoordinator) user).getBranch());
            if (branch != null) {
                ((HRCoordinator) user).setBranch(branch);
            }
        } else if (user instanceof Interviewer) {
            Interviewer interviewer = (Interviewer) user;
//            this.jobAppSystem.getUserManager().getInterviewer(interviewer).setBranch(this.jobAppSystem.getBranch(interviewer.getBranch()));
            Branch branch = this.jobAppSystem.getBranch(interviewer.getBranch());
            if (branch != null) {
                interviewer.setBranch(branch);
                interviewer.setInterviews(branch.getInterviewer(interviewer).getInterviews());
            }
        } else if (user instanceof Applicant) {
            Applicant applicant = (Applicant) user;
            JobApplicationManager jobApplicationManager = applicant.getJobApplicationManager();
            if (jobApplicationManager != null) {
                this.jobAppSystem.getUserManager().getApplicant(applicant).setJobApplicationManager(jobApplicationManager);
            }
        }


//        System.out.println("From login backend");
//        System.out.println("Branch ID from HR : " + ((HRCoordinator) user).getBranch());
//        System.out.println(((HRCoordinator) user).getBranch().getFieldToInterviewers());
//        System.out.println("From job application system: ");
//        for (Company company : jobAppSystem.getCompanies()) {
//            for (Branch branch : company.getBranches()) {
//                System.out.println(branch);
//                System.out.println(branch.getFieldToInterviewers().toString());
//            }
//        }
        return user;
    }

    // === Private methods ===

    /**
     * Check whether email address is valid.
     *
     * @return true iff the email address is valid.
     */
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
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
        return Pattern.matches("^[A-Za-z0-9]+$", username) || Pattern.matches(EMAIL_REGEX, username);
    }

    private boolean isValidLegalName(String legalName) {
        String regex = "^[A-Z][a-z]*(['\\-][A-Z][a-z]*)*( [A-Z][a-z]*(['\\-][A-Z][a-z]*)*)+$";
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
    private String createNewUser(HashMap<String, String> inputs) {
        String password = inputs.get("password");
        String name = inputs.get("name");
        String email = inputs.get("email");
        if (name.equals("") || password.equals("")) {
            return NewUserPanel.BLANK_ENTRY;
        } else if (!isValidLegalName(name)) {
            return NewUserPanel.INVALID_LEGAL_NAME;
        } else if (!isValidEmail(email)) {
            return NewUserPanel.INVALID_EMAIL;
        } else {
            return VALID_BASIC;
        }
    }

    /**
     * Create a new Applicant.
     * @return integer constants depending on the validity of the user input.
     */
    String createNewApplicant(HashMap<String, String> inputs) {
        String prelimStatus = this.createNewUser(inputs);
        if (!prelimStatus.equals(VALID_BASIC)) {
            return prelimStatus;
        }
        String postalCode = inputs.get("postalCode");
        if (!isValidPostalCode(postalCode)) {
            return NewUserPanel.INVALID_POSTAL_CODE;
        } else {
            String username = inputs.get("username");
            String password = inputs.get("password");
            String name = inputs.get("name");
            String email = inputs.get("email");
            jobAppSystem.getUserManager().createApplicant(username, password, name, email, postalCode, jobAppSystem);
            ((Applicant) jobAppSystem.getUserManager().findUserByUsername(username)).resetJobApplicationManager(jobAppSystem);
            return NewUserPanel.SUCCESS;
        }
    }

    /**
     * Create a new HR Coordinator.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    String createNewHRC(HashMap<String, String> inputs) {
        String prelimStatus = this.createNewUser(inputs);
        if (!prelimStatus.equals(VALID_BASIC)) {
            return prelimStatus;
        }
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        if (company == null) {
            company = jobAppSystem.createCompany(inputs.get("company"));
        }
        if (!isValidPostalCode(inputs.get("postalCode"))) {
            return NewUserPanel.INVALID_POSTAL_CODE;
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
                username, password, name, email, branch, jobAppSystem);
        branch.addJobPostingManager();
        jobAppSystem.getBranch(branch).setJobPostingManager(branch.getJobPostingManager());
        return NewUserPanel.SUCCESS;
    }

    /**
     * Create a new interviewer.
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    String createNewInterviewer(HashMap<String, String> inputs) {
        String prelimStatus = this.createNewUser(inputs);
        if (!prelimStatus.equals(VALID_BASIC)) {
            return prelimStatus;
        }
        Company company = jobAppSystem.getCompany(inputs.get("company"));
        String field = inputs.get("field");
        if (company == null) {
            return NewUserPanel.INVALID_COMPANY;
        } else {
            Branch branch = company.getBranch(inputs.get("branch"));
            if (branch == null) {
                return NewUserPanel.INVALID_BRANCH;
            } else {
                String username = inputs.get("username");
                String password = inputs.get("password");
                String name = inputs.get("name");
                String email = inputs.get("email");
                jobAppSystem.getUserManager().createInterviewer(username, password, name, email, field, branch, jobAppSystem);
                return NewUserPanel.SUCCESS;
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
    String login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return NewUserPanel.BLANK_ENTRY;
        } else if (!isValidUsername(username)) {
            return LoginMain.INVALID_USERNAME;
        } else if (jobAppSystem.getUserManager().findUserByUsername(username) == null) {
            return LoginMain.USER_NOT_FOUND;
        } else {
            if (jobAppSystem.getUserManager().passwordCorrect(username, password)) {
                return NewUserPanel.SUCCESS;
            } else {
                return LoginMain.WRONG_PASSWORD;
            }
        }
    }
}