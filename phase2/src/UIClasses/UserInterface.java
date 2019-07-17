package UIClasses;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Interview;
import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import Main.User;

import java.io.IOException;
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
    // The scanner for user input
    Scanner sc = new Scanner(System.in);
    // The Job application being used
    JobApplicationSystem JAS;
    // Today's date
    LocalDate today;

    // ============================================================================================================== //
    // === Public methods ===
    // === Constructor ===
    public UserInterface(JobApplicationSystem JAS) {
        this.JAS = JAS;
        this.today = JAS.getToday();
    }

    // === Method to be overridden ===

    /**
     * Run this user interface.
     *
     */
    public void run() throws ExitException, IOException {
        System.out.println("\nWelcome to GET A JOB!");
        User user = this.login();
        UserInterface userInterface = new InterfaceFactory().createInterface(JAS, user);
        userInterface.run();
        System.out.println("\nThank you for using GET A JOB. Have a wonderful day!");
        this.closeProgram();
    }

    // === Other methods ===

    /**
     * Get and set today's date as inputted by the user.
     *
     */
    public void getTodaysDateValid() {
        LocalDate previousLoginDate = this.JAS.getPreviousLoginDate();
        LocalDate date;
        if (previousLoginDate == null) {    // First start up with no PreviousLoginDate.txt
            date = this.getDate("Please enter today's date (yyyy-mm-dd): ");
        } else if (this.today == null) {    // First start up with date in data.ser
            date = this.getDateIncludingToday(previousLoginDate,
                    "Please enter today's date (yyyy-mm-dd): ");
        } else {    // While the application runs
            date = this.getDateIncludingToday(
                    "Please enter today's date (yyyy-mm-dd): ");
        }
        this.JAS.setPreviousLoginDate(date);
        this.JAS.setToday(date);
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Constructor ===
    UserInterface(JobApplicationSystem JAS, User user) {
        this.JAS = JAS;
        this.today = JAS.getToday();
        this.user = user;
    }

    UserInterface() {
    }

    // === Methods to be inherited ===

    /**
     * Prints a list of objects.
     *
     * @param objects The objects to be printed.
     */
    void printList(ArrayList objects) {
        if (objects.isEmpty()) {
            System.out.println("\nNo items to view.");
        }
        for (int i = 0; i < objects.size(); i++)
            System.out.println("\n" + objects.get(i));
    }

    /**
     * Prints a numbered list of objects.
     *
     * @param objects The objects to be printed.
     */
    void printListToSelectFrom(ArrayList objects) {
        if (objects.isEmpty()) {
            System.out.println("\nNo items to view.");
        }
        for (int i = 1; i <= objects.size(); i++) {
            System.out.println("\n" + i + ".");
            System.out.println(objects.get(i - 1));
        }
    }

    /**
     * Get the input from the user (with spaces)
     *
     * @param message The prompt that is displayed.
     * @return the input from the user.
     */
    String getInputLine(String message) {
        System.out.print(message);
        String input = this.sc.nextLine();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            this.getInputLine(message);
        }
        return input;
    }

    /**
     * Get input from the user (with spaces) until an empty line is entered.
     *
     * @param message The prompt that is displayed.
     * @return the input from the user (minus the last empty line).
     */
    String getInputLinesUntilDone(String message) {
        System.out.print(message);
        String input = "";
        String inputLine = sc.nextLine();
        while (inputLine.length() > 0) {
            input += inputLine;
            input += '\n';
            inputLine = this.sc.nextLine();
        }
        return input.trim();
    }

    /**
     * Get the input from the user (no spaces)
     *
     * @param message The prompt that is displayed.
     * @return the input from the user.
     */
    String getInputToken(String message) {
        System.out.print(message);
        String input = this.sc.next();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            this.getInputToken(message);
        }
        return input;
    }

    /**
     * Get the natural number inputted by the user.
     *
     * @param message The prompt that is displayed.
     * @return the integer inputted by the user.
     */
    int getNaturalNumber(String message) {
        System.out.print(message);
        String input = this.sc.nextLine();
        try {
            int intInput = Integer.parseInt(input);
            if (intInput < 0) {
                throw new NumberFormatException();
            }
            return intInput;
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            return this.getNaturalNumber(message);
        }
    }

    /**
     * Get the positive integer inputted by the user.
     *
     * @param message The prompt that is displayed.
     * @return the integer inputted by the user.
     */
    int getPositiveInteger(String message) {
        int input = this.getNaturalNumber(message);
        if (input < 1) {
            System.out.println("Invalid input. Please enter a number > 0.");
            this.getPositiveInteger(message);
        }
        return input;
    }

    /**
     * Get the date inputted by the user.
     *
     * @param message The prompt that is displayed.
     * @return the date inputted by the user.
     */
    LocalDate getDateIncludingToday(String message) {
        System.out.print(message);
        String input = this.sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, dtf);
            if (date.isBefore(this.today)) {
                System.out.println("Invalid date. Please enter again.");
                System.out.println();
                return this.getDateIncludingToday(message);
            }
            return date;
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDateIncludingToday(message);
        }
    }

    /**
     * Get the date inputted by the user.
     *
     * @param today   Today's date.
     * @param message The prompt that is displayed.
     * @return the date inputted by the user.
     */
    private LocalDate getDateIncludingToday(LocalDate today, String message) {
        System.out.print(message);
        String input = this.sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, dtf);
            if (date.isBefore(today)) {
                System.out.println("Invalid date. Please enter again.");
                System.out.println();
                return this.getDateIncludingToday(today, message);
            }
            return date;
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDateIncludingToday(today, message);
        }
    }

    /**
     * Get the date inputted by the user not including today's date.
     *
     * @param message The prompt that is displayed.
     * @return the date inputted by the user.
     */
    LocalDate getDateAfterToday(String message) {
        System.out.print(message);
        String input = this.sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, dtf);
            if (date.isBefore(this.today) || date.isEqual(this.today)) {
                System.out.println("Invalid date. Please enter again.");
                System.out.println();
                return this.getDateAfterToday(message);
            }
            return date;
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDateAfterToday(message);
        }
    }

    /**
     * Interface for getting the menu option selected.
     *
     * @param numOptions The number of options in the menu.
     * @return the option selected.
     */
    int getMenuOption(int numOptions) {
        int option = this.getPositiveInteger("Enter value: ");
        while (option > numOptions) {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            option = this.getPositiveInteger("Enter value: ");
        }
        return option;
    }

    /**
     * Get the job application with the id that the user inputs.
     *
     * @return the job application with the id that the user inputs or null if not found.
     */
    JobApplication getJobApplication() {
        System.out.println();
        int id = this.getPositiveInteger("Enter the job application ID: ");
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
     */
    void viewAllInterviewsForJobApp() {
        JobApplication jobApp = this.getJobApplication();
        if (jobApp != null) {
            System.out.println();
            System.out.println("Previous interviews:");
            ArrayList<Interview> interviews = jobApp.getInterviews();
            this.printList(interviews);
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
     * @param username             The user's username.
     * @param password             The user's password.
     * @return the new user instance created.
     */
    private User signUp(String username, String password) throws IOException {
        System.out.println();
        String legalName = this.getOnlyLetters("Enter your legal name: ");
        String email = this.getValidEmail("Enter your email address: ");
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(numOptions);
        switch (option) {
            case 1:
                System.out.println();
                System.out.println("Sign-up successful!");
                return this.JAS.getUserManager().createApplicant
                        (username, password, legalName, email, this.today);
            case 2:
                return this.createNewInterviewer(username, password, legalName, email);
            case 3:
                return this.createNewHRC(username, password, legalName, email);
        }
        return null;    // Won't execute because option number is guaranteed to be within bounds.
    }

    /**
     * Create a new HR Coordinator.
     *
     * @param username             The HR Coordinator's username.
     * @param password             The HR Coordinator's password.
     * @param legalName            The HR Coordinator's legalName
     * @param email                The HR Coordinator's email.
     * @return the new HR Coordinator instance created.
     */
    private User createNewHRC(String username, String password, String legalName, String email) throws IOException {
        System.out.println();
        String companyName = this.getInputLine("Enter your branch name: ");
        Branch branch = this.JAS.getCompany(companyName);
        if (branch == null) {
            branch = this.JAS.createCompany(companyName);
        }
        System.out.println("Sign-up successful!");
        HRCoordinator HRC = this.JAS.getUserManager().createHRCoordinator(username, password, legalName, email, branch,
                this.today);
        branch.addHRCoordinator(HRC);
        return HRC;
    }

    /**
     * Create a new interviewer.
     *
     * @param username             The interviewer's username.
     * @param password             The interviewer's password.
     * @param legalName            The interviewer's legal name.
     * @param email                The interviewer's email.
     * @return the new interviewer instance created.
     */
    private User createNewInterviewer(String username, String password, String legalName, String email) throws IOException {
        System.out.println();
        String companyName = this.getInputLine("Enter your branch name: ");
        Branch branch = this.JAS.getCompany(companyName);
        while (branch == null) {
            System.out.println("Branch name not found.");
            companyName = this.getInputLine("Enter your branch name: ");
            branch = this.JAS.getCompany(companyName);
        }
        String field = this.getInputLine("Enter your field: ");
        System.out.println("Sign-up successful!");
        Interviewer interviewer = this.JAS.getUserManager().createInterviewer(username, password,
                legalName, email, branch, field, this.today);
        branch.addInterviewer(interviewer);
        return interviewer;
    }

    /**
     * Login this user into the system. Sign them up if username does not already exist in the system.
     *
     * @return the user who logged-in.
     */
    private User login() throws IOException {
        String username = this.getInputToken("\nEnter your username: ");
        sc.nextLine();
        String password = this.getInputLine("Enter your password: ");
        if (this.JAS.getUserManager().findUserByUsername(username) == null) {
            return signUp(username, password);
        } else {
            while (!this.JAS.getUserManager().passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                password = this.getInputLine("Enter your password: ");
            }
            System.out.println("Login successful!");
            System.out.println();
            return this.JAS.getUserManager().findUserByUsername(username);
        }
    }

    /**
     * Gets a string of letters inputted by the user.
     *
     * @param message The prompt that is displayed.
     * @return the string of letters inputted by the user.
     */
    private String getOnlyLetters(String message) {
        String input = this.getInputLine(message);
        boolean onlyLetters = true;
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isLetter(input.charAt(i)) && !Character.isSpaceChar(input.charAt(i))) {
                onlyLetters = false;
            }
        }
        if (onlyLetters) {
            return input;
        } else {
            System.out.println("Invalid input. Please enter again.");
            return this.getOnlyLetters(message);
        }
    }

    /**
     * Get the date without comparing to previous date.
     *
     * @param message The prompt displayed.
     * @return the date the user inputs.
     */
    private LocalDate getDate(String message) {
        System.out.print(message);
        String input = sc.next();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(input, dtf);
        } catch (DateTimeParseException dtpe) {
            System.out.println("Cannot read date. Please enter again.");
            System.out.println();
            return this.getDate(message);
        }
    }

    /**
     * Get an e-mail address as input from the user. E-mail address must be in valid format.
     *
     * @param message The prompt that is displayed.
     * @return the e-mail address inputted by the user.
     */
    private String getValidEmail(String message) {
        String input = this.getInputLine(message);
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
            return this.getValidEmail(message);
        }
    }

    /**
     * Close the program upon user input.
     *
     */
    private void closeProgram() throws ExitException {
        String input = this.getInputToken("\nEnter '-1' if you would like to stop running the system or " +
                "any other key (except 'Enter') to keep running: ");
        this.sc.nextLine();
        if (input.equals("-1")) {
            throw new ExitException();
        }
        System.out.println();
    }
}