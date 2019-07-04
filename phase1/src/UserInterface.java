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

    /**
     * Interface for displaying user types.
     *
     * @return the number of options in the menu.
     */
    private int displayUserTypes() {
        System.out.println("Please select your user type:");
        System.out.println("1 - Job Applicant");
        System.out.println("2 - HR Coordinator");
        System.out.println("3 - Interviewer");
        return 3;
    }

    /**
     * Interface for getting the menu option selected.
     * @param sc            The scanner for user input.
     * @param numOptions    The number of options in the menu.
     * @return the option selected.
     */
    int getMenuOption(Scanner sc, int numOptions) {
        int option = sc.nextInt();
        if (option < 1 || option > numOptions) {
            System.out.println("Invalid input. Please try again.");
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
        System.out.print("Enter your legal name: ");
        String legalName = sc.nextLine();
        sc.nextLine();
        System.out.print("Enter your email address: ");
        String email = sc.next();
        int numOptions = this.displayUserTypes();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                return UserInterface.userManager.createApplicant(username, password, legalName, email,
                        LocalDate.now(), true);
            case 2:
                return this.createNewHRC(sc, username, password, legalName, email);
            case 3:
                return this.createNewInterviewer(sc, username, password, legalName, email);
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
        System.out.println("Enter your company name: ");
        String companyName = sc.nextLine();
        Company company = JobApplicationSystem.getCompany(companyName);
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
        System.out.print("Enter your company name: ");
        String companyName = sc.nextLine();
        while (JobApplicationSystem.getCompany(companyName) == null) {
            System.out.println("Company name not found.");
            System.out.print("Enter your company name: ");
            companyName = sc.nextLine();
        }
        Company company = JobApplicationSystem.getCompany(companyName);
        System.out.println("Enter your field: ");
        String field = sc.nextLine();
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
        System.out.print("Enter your username: ");
        String username = sc.next();
        System.out.print("Enter your password: ");
        String password = sc.next();
        if (userManager.findUserByUsername(username) == null) {
            return signUp(sc, username, password);
        }
        else {
            while (!JobApplicationSystem.getUserManager().passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                System.out.print("Enter your password: ");
                password = sc.next();
            }
            return userManager.findUserByUsername(username);
        }

        /*
        - Prompt user to enter their username
        - Prompt user to enter their password
        If username is not in system:
            - Prompt for user type (job applicant, interviewer, or HR coordinator)
            If user is an applicant:
                - Prompt for legal name
                - Prompt for e-mail
                - userManager.createApplicant(username, password, legalName, email, LocalDate.now(), true);
            Else if user is an interviewer:
                - Prompt for e-mail
                - Prompt for company name
                if (JobApplicationSystem.getCompany(companyName) == null) {
                    - rejecc
                }
                else {
                    - Company company = JobApplicationSystem.getCompany(companyName);
                    - Prompt for field
                    - userManager.createInterviewer(username, password, legalName, email, company, field,
                        LocalDate.now(), true);
                }
            Else: //Job coordinator
                - Prompt for legal name
                - Prompt for e-mail
                - Prompt for company name
                if (JobApplicationSystem.getCompany(companyName) == null) {
                    - Company company = JobApplicationSystem.createCompany(companyName);
                }
                else {
                    - Company company = JobApplicationSystem.getCompany(companyName);
                    - userManager.createHRCoordinator(username, password, legalName, email, company, LocalDate.now(),
                        true);
                }
        Else:
            if (userManager.passwordCorrect(username, password)) {
                - load user data from system
            }
            else {
                - prompt user to re-enter password
            }

        */
    }
}
