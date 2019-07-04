import java.time.LocalDate;
import java.util.Scanner;

class UserInterface {
    /**
     * The general user interface
     */

    // === Class variables ===
    // The UserManager for this JobApplicationSystem
    private static UserManager userManager = JobApplicationSystem.getUserManager();

    // === Instance variables ===
    // The user who logged in
    User user;

    public static void main(String[] args) {
        UserInterface UI = new UserInterface();
        User user = UI.login();
        user.getUserInterface().run(LocalDate.now());
    }

    // === Constructors ===
    UserInterface() {
    }

    UserInterface(User user) {
        this.user = user;
    }


    // === Inherited method ===
    void run(LocalDate today) {
    }

    // === Other methods ===

    /**
     * Get the input from the user.
     *
     * @param sc The scanner for user input.
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
     * Interface for displaying user types.
     *
     * @return the number of options in the menu.
     */
    private int displayUserTypes() {
        System.out.println();
        System.out.println("Please select your user type:");
        System.out.println("1 - Job applicant");
        System.out.println("2 - Interviewer");
        System.out.println("3 - HR coordinator");
        return 3;
    }

    /**
     * Interface for getting the menu option selected.
     *
     * @param sc         The scanner for user input.
     * @param numOptions The number of options in the menu.
     * @return the option selected.
     */
    int getMenuOption(Scanner sc, int numOptions) {
        String input = sc.next();
        while (true) {
            try {
                int option = Integer.valueOf(input);
                while (option < 1 || option > numOptions) {
                    System.out.println("Invalid input. Please try again.");
                    input = sc.next();
                    option = Integer.valueOf(input);
                }
                return option;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
                input = sc.next();
            }
        }
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
        String legalName = this.getInputLine(sc, "Enter your legal name: ");
        String email = this.getInputToken(sc, "Enter your email address: ");
        sc.nextLine();
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                System.out.println();
                System.out.println("Sign-up successful!");
                return userManager.createApplicant(username, password, legalName, email, LocalDate.now(), true);
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
        sc.nextLine();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        if (company == null) {
            company = JobApplicationSystem.createCompany(companyName);
        }
        System.out.println("Sign-up successful!");
        return userManager.createHRCoordinator(username, password, legalName, email, company,
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
        sc.nextLine();
        String companyName = this.getInputLine(sc, "Enter your company name: ");
        while (JobApplicationSystem.getCompany(companyName) == null) {
            System.out.println("Company name not found.");
            companyName = this.getInputLine(sc, "Enter your company name: ");
        }
        Company company = JobApplicationSystem.getCompany(companyName);
        String field = this.getInputLine(sc, "Enter your field: ");
        System.out.println("Sign-up successful!");
        return JobApplicationSystem.getUserManager().createInterviewer(username, password, legalName, email, company,
                field, LocalDate.now(), true);
    }

    /**
     * Login this user into the system. Sign them up if username does not already exist in the system.
     *
     * @return the user who logged-in.
     */
    private User login() {
        Scanner sc = new Scanner(System.in);
        String username = this.getInputToken(sc, "Enter your username: ");
        sc.nextLine();
        String password = this.getInputLine(sc, "Enter your password: ");
        if (UserInterface.userManager.findUserByUsername(username) == null) {
            return signUp(sc, username, password);
        }
        else {
            while (!JobApplicationSystem.getUserManager().passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                password = this.getInputLine(sc, "Enter your password: ");
            }
            System.out.println("Login successful!");
            return UserInterface.userManager.findUserByUsername(username);
        }
    }
}