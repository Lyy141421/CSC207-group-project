package GUIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    /**
     * The general user interface
     */

    // === Instance variable ===
    // The user who logged in
    User user;

    // === Public methods ===
    // === Constructor ===
    public UserInterface(User user) {
        this.user = user;
    }

    // ============================================================================================================== //
    // === Public methods ===

    // === Constructor ===
    public UserInterface() {
    }

    // === Method to be overridden ===

    /**
     * Run this user interface.
     *
     * @param sc                   The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     */
    public void run(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        System.out.println("\nWelcome to GET A JOB!\n");
        jobApplicationSystem.applicant30Day();
        jobApplicationSystem.updateAllInterviewRounds();
        User user = this.login(sc, jobApplicationSystem);
        UserInterface userInterface = new InterfaceFactory().create(user);
        userInterface.run(sc, jobApplicationSystem);
        System.out.println("\nThank you for using GET A JOB. Have a wonderful day!");
        this.closeProgram(sc, jobApplicationSystem);
    }

    // === Other method ===

    /**
     * Get and set today's date as inputted by the user.
     * @param sc    The scanner for user input
     * @param jobApplicationSystem The job application system being used.
     */
    public void getTodaysDateValid(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        LocalDate previousLoginDate = jobApplicationSystem.getPreviousLoginDate();
        LocalDate date;
        if (previousLoginDate == null) {
            date = this.getDate(sc, "Please enter today's date (yyyy-mm-dd): ");
        } else {
            date = this.getDate(sc, jobApplicationSystem.getPreviousLoginDate(),
                    "Please enter today's date (yyyy-mm-dd): ");
        }
        jobApplicationSystem.setPreviousLoginDate(date);
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Methods to be inherited ===
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
     * Get input from the user (with spaces) until an empty line is entered.
     *
     * @param sc The scanner for user input.
     * @param message   The prompt that is displayed.
     * @return the input from the user (minus the last empty line).
     */
    String getInputLinesUntilDone(Scanner sc, String message) {
        System.out.print(message);
        String input = "";
        String inputLine = sc.nextLine();
        while (inputLine.length() > 0) {
            input += inputLine;
            input += '\n';
            inputLine = sc.nextLine();
        }
        return input.trim();
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
    private String getOnlyLetters(Scanner sc, String message) {
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
            System.out.println();
            option = this.getInteger(sc, "Enter value: ");
        }
        return option;
    }

    /**
     * Get the job application with the id that the user inputs.
     *
     * @param sc The scanner for user input.
     * @return the job application with the id that the user inputs or null if not found.
     */
    JobApplication getJobApplication(Scanner sc) {
        System.out.println();
        int id = this.getInteger(sc, "Enter the job application ID: ");
        JobApplication jobApplication = this.user.findJobAppById(id);
        if (jobApplication == null) {
            System.out.println("This job application cannot be found.");
        } else {
            System.out.println(jobApplication);
        }
        return jobApplication;
    }

    /**
     * Interface for viewing the previous interviews for this job application (all except the one that is currently scheduled)
     *
     * @param sc The scanner for user input
     */
    void viewAllInterviewsForJobApp(Scanner sc) {
        JobApplication jobApp = this.getJobApplication(sc);
        if (jobApp != null) {
            System.out.println();
            System.out.println("Previous interviews:");
            ArrayList<Interview> interviews = jobApp.getInterviews();
            new PrintItems<Interview>().printList(interviews);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

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
     * @param jobApplicationSystem  The job application system being used.
     * @param username The user's username.
     * @param password The user's password.
     * @return the new user instance created.
     */
    private User signUp(Scanner sc, JobApplicationSystem jobApplicationSystem, String username, String password) {
        System.out.println();
        String legalName = this.getOnlyLetters(sc, "Enter your legal name: ");
        String email = this.getValidEmail(sc, "Enter your email address: ");
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                System.out.println();
                System.out.println("Sign-up successful!");
                return jobApplicationSystem.getUserManager().createApplicant
                        (username, password, legalName, email, LocalDate.now(), true);
            case 2:
                return this.createNewInterviewer(sc, jobApplicationSystem, username, password, legalName, email);
            case 3:
                return this.createNewHRC(sc, jobApplicationSystem, username, password, legalName, email);
        }
        return null;    // Won't execute because option number is guaranteed to be within bounds.
    }

    /**
     * Create a new HR Coordinator.
     *
     * @param sc       The scanner for user input.
     * @param jobApplicationSystem The job application system being used
     * @param username The HR Coordinator's username.
     * @param password The HR Coordinator's password.
     * @param legalName The HR Coordinator's legalName
     * @param email     The HR Coordinator's email.
     * @return the new HR Coordinator instance created.
     */
    private User createNewHRC(Scanner sc, JobApplicationSystem jobApplicationSystem, String username, String password,
                              String legalName, String email) {
        System.out.println();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        Company company = jobApplicationSystem.getCompany(companyName);
        if (company == null) {
            company = jobApplicationSystem.createCompany(companyName);
        }
        System.out.println("Sign-up successful!");
        return jobApplicationSystem.getUserManager().createHRCoordinator(username, password, legalName, email, company,
                LocalDate.now(), true);
    }

    /**
     * Create a new interviewer.
     *
     * @param sc       The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     * @param username The interviewer's username.
     * @param password The interviewer's password.
     * @param legalName The interviewer's legal name.
     * @param email     The interviewer's email.
     * @return the new interviewer instance created.
     */
    private User createNewInterviewer(Scanner sc, JobApplicationSystem jobApplicationSystem, String username,
                                      String password, String legalName, String email) {
        System.out.println();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        Company company = jobApplicationSystem.getCompany(companyName);
        while (company == null) {
            System.out.println("Company name not found.");
            companyName = this.getInputLine(sc, "Enter your company name: ");
            company = jobApplicationSystem.getCompany(companyName);
        }
        String field = this.getOnlyLetters(sc, "Enter your field: ");
        System.out.println("Sign-up successful!");
        Interviewer interviewer = jobApplicationSystem.getUserManager().createInterviewer(username, password,
                legalName, email, company, field, LocalDate.now(), true);
        company.addInterviewer(interviewer);
        return interviewer;
    }

    /**
     * Login this user into the system. Sign them up if username does not already exist in the system.
     *
     * @param sc The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     * @return the user who logged-in.
     */
    private User login(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        String username = this.getInputToken(sc, "\nEnter your username: ");
        sc.nextLine();
        String password = this.getInputLine(sc, "Enter your password: ");
        if (jobApplicationSystem.getUserManager().findUserByUsername(username) == null) {
            return signUp(sc, jobApplicationSystem, username, password);
        } else {
            while (!jobApplicationSystem.getUserManager().passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                password = this.getInputLine(sc, "Enter your password: ");
            }
            System.out.println("Login successful!");
            System.out.println();
            return jobApplicationSystem.getUserManager().findUserByUsername(username);
        }
    }

    /**
     * Get the date without comparing to previous date.
     *
     * @param sc      The scanner for user input.
     * @param message The prompt displayed.
     * @return the date the user inputs.
     */
    private LocalDate getDate(Scanner sc, String message) {
        System.out.print(message);
        String input = sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(input, dtf);
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDate(sc, message);
        }
    }

    /**
     * Get an e-mail address as input from the user. E-mail address must be in valid format.
     *
     * @param sc      The scanner for user input.
     * @param message The prompt that is displayed.
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
                    || splitInput[1].charAt(splitInput[1].length() - 1) == '.')
                validEmail = false;
        }
        if (validEmail)
            return input;
        else {
            System.out.println("Invalid input. Please enter again.");
            System.out.println();
            return this.getValidEmail(sc, message);
        }
    }

    /**
     * Close the program upon user input.
     *
     * @param sc                   The scanner for user input.
     * @param jobApplicationSystem The job application system being used.
     */
    private void closeProgram(Scanner sc, JobApplicationSystem jobApplicationSystem) {
        String input = this.getInputToken(sc, "\nEnter '-1' if you would like to stop running the system or " +
                "any other key (except 'Enter') to keep running: ");
        sc.nextLine();
        if (input.equals("-1")) {
            System.exit(0);
        }
    }
}