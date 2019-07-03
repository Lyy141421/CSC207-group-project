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
        System.out.print("Enter your legal name: ");
        String legalName = sc.nextLine();
        sc.nextLine();
        System.out.print("Enter your email address: ");
        String email = sc.nextLine();
        return UserInterface.userManager.createApplicant(username, password, legalName, email, LocalDate.now(),
                true);
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
        System.out.print("Enter your legal name: ");
        String legalName = sc.nextLine();
        System.out.print("Enter your email address: ");
        String email = sc.nextLine();
        System.out.print("Enter your company name: ");
        String companyName = sc.nextLine();
        Company company = JobApplicationSystem.getCompany(companyName);
        return UserInterface.userManager.createHRCoordinator(username, password, legalName, email, company,
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
    private User createNewInterviewer(Scanner sc, String username, String password) {
        System.out.print("Enter your legal name: ");
        String legalName = sc.nextLine();
        System.out.print("Enter your email address: ");
        String email = sc.nextLine();
        System.out.print("Enter your company name: ");
        String companyName = sc.nextLine();
        Company company = JobApplicationSystem.getCompany(companyName);
        System.out.print("Enter your field: ");
        String field = sc.nextLine();
        return UserInterface.userManager.createInterviewer(username, password, legalName, email, company, field,
                LocalDate.now(), true);
    }

    /**
     * Login this user into the system. Sign them up if username does not already exist in the system.
     *
     * @return the user who logged-in.
     */
    private User login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        try {
            User user = UserInterface.userManager.findUserByUsername(username);
            while (!UserInterface.userManager.passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                System.out.print("Enter your password: ");
                password = sc.nextLine();
            }
            return user;
        } catch (NullPointerException npe) {
            return this.signUp(sc, username, password);
        }
    }
}
