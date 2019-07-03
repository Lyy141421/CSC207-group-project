import java.util.Scanner;

class UserInterface {
    /**
     * The general user interface
     */

    // === Instance variables ===
    private static UserManager userManager = JobApplicationSystem.getUserManager(); // the UserManager for this JobApplicationSystem

    UserInterface() {
    }

    public static void main (String[] args) {
        UserInterface UI = new UserInterface();
        UI.login();
    }

    void run() {
        // login -- get user type
        // create specific user interface
        // run the specific user interface
    }

    int displayUserTypes() {
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
    void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = sc.next();
        System.out.print("Enter your password: ");
        String password = sc.next();
        if (userManager.findUserByUsername(username) == null) {
            int numOptions = this.displayUserTypes();
            int option = this.getMenuOption(sc, numOptions);
            //TODO not done
        }
        else {
            while (!userManager.passwordCorrect(username, password)) {
                System.out.println("Incorrect password.");
                System.out.print("Enter your password: ");
                password = sc.next();
            }
            if (userManager.findUserByUsername(username) instanceof Applicant) {
                Applicant applicant = (Applicant)userManager.findUserByUsername(username);
            }
            else if (userManager.findUserByUsername(username) instanceof Interviewer) {
                Interviewer interviewer = (Interviewer)userManager.findUserByUsername(username);
            }
            else if (userManager.findUserByUsername(username) instanceof HRCoordinator) {
                HRCoordinator HRcoordinator = (HRCoordinator) userManager.findUserByUsername(username);
            }
            else {
                System.out.println("Error");
            }
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

    /**
     * Interface for getting the menu option selected.
     *
     * @param sc         The scanner for user input.
     * @param numOptions The number of options in this menu.
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
}
