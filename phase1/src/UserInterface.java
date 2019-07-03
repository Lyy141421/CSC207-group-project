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
    String getInput(Scanner sc) {
        String input = sc.nextLine();
        if (input == null) {
            System.out.println("Invalid input. Please input again.");
            this.getInput(sc);
        }
        return input;
    }

    /**
     * Interface for displaying user types.
     *
     * @return the number of options in the menu.
     */
    private int displayUserTypes() {
        System.out.println("Please select your user type");
        System.out.println("1 - Job applicant");
        System.out.println("2 - Interviewer");
        System.out.println("3 - HR coordinator");
        return 3;
    }

    /**
     * Interface for getting the menu option selected.
     * @param sc            The scanner for user input.
     * @param numOptions    The number of options in the menu.
     * @return the option selected.
     */
    int getMenuOption(Scanner sc, int numOptions) {
        System.out.print("\nSelect an option number: ");
        int option = sc.nextInt();
        if (option < 1 || option > numOptions) {
            System.out.println("Invalid input.");
            this.getMenuOption(sc, numOptions);
        }
        return option;
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
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                return this.createNewApplicant(sc, username, password);
            case 2:
                return this.createNewHRC(sc, username, password);
            case 3:
                return this.createNewInterviewer(sc, username, password);
        }
        return null;    // Won't execute because option number is guaranteed to be within bounds.
    }

    String[] getLegalNameAndEmail(Scanner sc) {
        String[] legalNameAndEmail = new String[2];
        System.out.print("Enter your legal name: ");
        legalNameAndEmail[0] = this.getInput(sc);
        sc.nextLine();
        System.out.print("Enter your email address: ");
        legalNameAndEmail[1] = this.getInput(sc);
        sc.nextLine();
        return legalNameAndEmail;
    }

    /**
     * Create a new applicant.
     *
     * @param sc       The scanner for user input.
     * @param username The applicant's username.
     * @param password The applicant's password.
     * @return the new applicant instance created.
     */
    private User createNewApplicant(Scanner sc, String username, String password) {
        System.out.println();
        String[] legalNameAndEmail = this.getLegalNameAndEmail(sc);
        return UserInterface.userManager.createApplicant(username, password, legalNameAndEmail[0],
                legalNameAndEmail[1], LocalDate.now(), true);
    }

    /**
     * Create a new HR Coordinator.
     *
     * @param sc       The scanner for user input.
     * @param username The HR Coordinator's username.
     * @param password The HR Coordinator's password.
     * @return the new HR Coordinator instance created.
     */
    private User createNewHRC(Scanner sc, String username, String password) {
        String[] legalNameAndEmail = this.getLegalNameAndEmail(sc);
        System.out.print("Enter your company name: ");
        String companyName = this.getInput(sc);
        Company company = JobApplicationSystem.getCompany(companyName);
        return UserInterface.userManager.createHRCoordinator(username, password, legalNameAndEmail[0],
                legalNameAndEmail[1], company, LocalDate.now(), true);
    }

    /**
     * Create a new interviewer.
     *
     * @param sc       The scanner for user input.
     * @param username The interviewer's username.
     * @param password The interviewer's password.
     * @return the new interviewer instance created.
     */
    private User createNewInterviewer(Scanner sc, String username, String password) {
        String[] legalNameAndEmail = this.getLegalNameAndEmail(sc);
        System.out.print("Enter your company name: ");
        String companyName = this.getInput(sc);
        Company company = JobApplicationSystem.getCompany(companyName);
        System.out.print("Enter your field: ");
        String field = this.getInput(sc);
        return UserInterface.userManager.createInterviewer(username, password, legalNameAndEmail[0],
                legalNameAndEmail[1], company, field, LocalDate.now(), true);
    }

    /**
     * Login this user into the system. Sign them up if username does not already exist in the system.
     *
     * @return the user who logged-in.
     */
    private User login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = this.getInput(sc);
        System.out.print("Enter your password: ");
        String password = this.getInput(sc);
        User user = UserInterface.userManager.findUserByUsername(username);
        if (user == null) {
            return this.signUp(sc, username, password);
        } else {
            while (!UserInterface.userManager.passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                System.out.print("Enter your password: ");
                password = this.getInput(sc);
            }
            return user;
        }
    }
}
