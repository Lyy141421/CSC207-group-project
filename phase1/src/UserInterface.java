class UserInterface {
    /**
     * The general user interface
     */

    // === Instance variables ===
    private UserManager userManager; // the UserManager for this JobApplicationSystem

    UserInterface() {
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
            Else:
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
