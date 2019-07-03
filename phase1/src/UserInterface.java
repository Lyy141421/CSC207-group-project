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

    // === Constructors ===
    UserInterface() {
    }

    UserInterface(User user) {
        this.user = user;
    }

    public static void main (String[] args) {
        UserInterface UI = new UserInterface();
        User user = UI.login();
        user.getUserInterface().run();
    }

    void run() {
    }

    private int displayUserTypes() {
        System.out.println("Please select your user type:");
        System.out.println("1 - Job applicant");
        System.out.println("2 - Interviewer");
        System.out.println("3 - HR coordinator");
        return 3;
    }

    int getMenuOption(Scanner sc, int numOptions) {
        System.out.println();
        System.out.print("Select an option number: ");
        int option = sc.nextInt();
        if (option < 1 || option > numOptions) {
            System.out.println("Invalid input.");
            this.getMenuOption(sc, numOptions);
        }
        return option;
    }

    User signUp(Scanner sc, String username, String password) {
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
        System.out.print("Enter your legal name: ");
        String legalName = sc.nextLine();
        System.out.println("Enter your email address: ");
        String email = sc.next();
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
        System.out.println("Enter your email address: ");
        String email = sc.next();
        System.out.println("Enter your company name: ");
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
        System.out.println("Enter your email address: ");
        String email = sc.next();
        System.out.println("Enter your company name: ");
        String companyName = sc.nextLine();
        Company company = JobApplicationSystem.getCompany(companyName);
        System.out.println("Enter your field: ");
        String field = sc.nextLine();
        return UserInterface.userManager.createInterviewer(username, password, legalName, email, company, field,
                LocalDate.now(), true);
    }

    User login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = sc.next();
        System.out.print("Enter your password: ");
        String password = sc.next();
        if (UserInterface.userManager.findUserByUsername(username) == null) {
            return this.signUp(sc, username, password);
        } else {
            while (!UserInterface.userManager.passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                System.out.print("Enter your password: ");
                password = sc.next();
            }
            return UserInterface.userManager.findUserByUsername(username);

//            if (userManager.findUserByUsername(username) instanceof Applicant) {
//                Applicant applicant = (Applicant)userManager.findUserByUsername(username);
//            }
//            else if (userManager.findUserByUsername(username) instanceof Interviewer) {
//                Interviewer interviewer = (Interviewer)userManager.findUserByUsername(username);
//            }
//            else if (userManager.findUserByUsername(username) instanceof HRCoordinator) {
//                HRCoordinator HRcoordinator = (HRCoordinator) userManager.findUserByUsername(username);
//            }
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
