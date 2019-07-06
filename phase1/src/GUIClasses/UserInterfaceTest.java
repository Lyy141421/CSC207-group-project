package GUIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Company;
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
     * Get the input from the user (with spaces)
     *
     * @param sc The scanner for user input.
     * @param message   The prompt that is displayed.
     * @return the input from the user.
     */
    String getInputLine(Scanner sc, String message) {
        System.out.print(message);
        String input = sc.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            this.getInputLine(sc, message);
        }
        return input;
    }

    /**
     * Get the input from the user (no spaces)
     *
     * @param sc      The scanner for user input.
     * @param message The prompt that is displayed.
     * @return the input from the user.
     */
    String getInputToken(Scanner sc, String message) {
        System.out.print(message);
        String input = sc.next();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            this.getInputToken(sc, message);
        }
        return input;
    }

    /**
     * Gets a string of letters inputted by the user.
     * @param sc    The scanner for user input.
     * @param message   The prompt that is displayed.
     * @return  the string of letters inputted by the user.
     */
    String getOnlyLetters(Scanner sc, String message) {
        String input = this.getInputLine(sc, message);
        boolean onlyLetters = true;
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isLetter(input.charAt(i)) && !Character.isSpaceChar(input.charAt(i))) {
                onlyLetters = false;
            }
        }
        if (onlyLetters) {
            return input;
        }
        else {
            System.out.println("Invalid input. Please enter again.");
            return this.getOnlyLetters(sc, message);
        }
    }

    /**
     * Get the integer inputted by the user.
     *
     * @param sc      The scanner for user input.
     * @param message The prompt that is displayed.
     * @return the integer inputted by the user.
     */
    int getInteger(Scanner sc, String message) {
        System.out.print(message);
        String input = sc.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            return this.getInteger(sc, message);
        }
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

    /**
     * Interface for getting the menu option selected.
     *
     * @param sc         The scanner for user input.
     * @param numOptions The number of options in the menu.
     * @return the option selected.
     */
    int getMenuOption(Scanner sc, int numOptions) {
        int option = this.getInteger(sc, "Enter value: ");
        while (option < 1 || option > numOptions) {
            System.out.println("Invalid input. Please try again.");
            option = this.getInteger(sc, "Enter value: ");
        }
        return option;
    }


    // === Private methods ===

    /**
     * Get an e-mail address as input from the user. E-mail address must be in valid format.
     *
     * @param sc The scanner for user input.
     * @param message   The prompt that is displayed.
     * @return the e-mail address inputted by the user.
     */

    private String getValidEmail(Scanner sc, String message) {
        String input = this.getInputLine(sc, message);
        boolean validEmail = true;
        if (!input.contains("@") || input.charAt(0) == '@')
            validEmail = false;
        else {
            String[] splitInput = input.split("@", 2);
            if (!splitInput[1].contains(".") || splitInput[1].charAt(0) == '.'
                    || splitInput[1].charAt(splitInput[1].length()-1) == '.')
                validEmail = false;
        }
        if (validEmail)
            return input;
        else {
            System.out.println("Invalid input. Please enter again.");
            return this.getValidEmail(sc, message);
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
     * Sign up this user based on the user type selected.
     *
     * @param sc       The scanner for user input.
     * @param username The user's username.
     * @param password The user's password.
     * @return the new user instance created.
     */
    private User signUp(Scanner sc, String username, String password) {
        System.out.println();
        String legalName = this.getOnlyLetters(sc, "Enter your legal name: ");
        String email = this.getValidEmail(sc, "Enter your email address: ");
        sc.nextLine();
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                System.out.println();
                System.out.println("Sign-up successful!");
                return JobApplicationSystem.getUserManager().createApplicant
                        (username, password, legalName, email, LocalDate.now(), true);
            case 2:
                return this.createNewInterviewer(sc, username, password, legalName, email);
            case 3:
                return this.createNewHRC(sc, username, password, legalName, email);
        }
        return null;    // Won't execute because option number is guaranteed to be within bounds.
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
        return JobApplicationSystem.getUserManager().createInterviewer(username, password, legalName, email, company,
                field, LocalDate.now(), true);
    }

    /**
     * Determine status of the login
     * @return 0 - no user input, 1 - no user exists, 2 - successful login, 3 - wrong pass
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