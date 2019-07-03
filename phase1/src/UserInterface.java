import java.util.Scanner;

class UserInterface {
    /**
     * The general user interface
     */

    // === Instance variables ===
    private UserManager userManager; // the UserManager for this JobApplicationSystem

    UserInterface() {
    }

    void run() {
        // login -- get user type
        // create specific user interface
        // run the specific user interface
    }

    void login() {
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
