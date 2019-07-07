package GUIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInterfaceTest {
    /**
     * The general user interface
     */

    // === Instance variables ===
    // The user who logged in
    User user;

    // === Constructors ===
    UserInterfaceTest() {
    }

    public UserInterfaceTest(User user) {
        this.user = user;
    }

    // === Inherited methods ===
    void run(LocalDate today) {
    }

    /**
     * Get the date inputted by the user.
     * @param sc        The scanner for user input.
     * @param today     Today's date.
     * @param message   The prompt that is displayed.
     * @return  the date inputted by the user.
     */
    LocalDate getDate(Scanner sc, LocalDate today, String message) {
        System.out.print(message);
        String input = sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, dtf);
            if (date.isBefore(today)) {
                System.out.println("Invalid date. Please enter again.");
                System.out.println();
                return this.getDate(sc, today, message);
            }
            return date;
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDate(sc, today, message);
        }
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
     * Interface for displaying user types.
     *
     * @return the number of options in the menu.
     */
    private int displayUserTypes() {
        System.out.println();
        System.out.println("Select your user type:");
        System.out.println("1 - Job applicant");
        System.out.println("2 - Interviewer");
        System.out.println("3 - HR coordinator");
        return 3;
    }

    /**
     * Create a new HR Coordinator.
     *
     * @param sc       The scanner for user input.
     * @param username The HR Coordinator's username.
     * @param password The HR Coordinator's password.
     * @return the new HR Coordinator instance created.
     */
    private User createNewHRC(Scanner sc, String username, String password, String legalName, String email) {
        System.out.println();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        if (company == null) {
            company = JobApplicationSystem.createCompany(companyName);
        }
        System.out.println("Sign-up successful!");
        return JobApplicationSystem.getUserManager().createHRCoordinator(username, password, legalName, email, company,
                LocalDate.now(), true);
    }

    /**
     * Create a new interviewer.
     *
     * @param sc       The scanner for user input.
     * @param username The interviewer's username.
     * @param password The interviewer's password.
     * @return the new interviewer instance created.
     */
    private User createNewInterviewer(Scanner sc, String username, String password, String legalName, String email) {
        System.out.println();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        while (JobApplicationSystem.getCompany(companyName) == null) {
            System.out.println("Company name not found.");
            companyName = this.getInputLine(sc, "Enter your company name: ");
        }
        Company company = JobApplicationSystem.getCompany(companyName);
        String field = this.getOnlyLetters(sc, "Enter your field: ");
        System.out.println("Sign-up successful!");
        Interviewer interviewer = JobApplicationSystem.getUserManager().createInterviewer(username, password,
                legalName, email, company, field, LocalDate.now(), true);
        company.addInterviewer(interviewer);
        return interviewer;
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