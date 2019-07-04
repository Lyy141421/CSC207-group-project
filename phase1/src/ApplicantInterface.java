import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

class ApplicantInterface extends UserInterface {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private Applicant applicant; // The applicant currently logged in

    // === Constructor ===

    /**
     * Create the interface for this Applicant.
     *
     * @param applicant The applicant currently logged in
     */
    ApplicantInterface(Applicant applicant) {
        this.applicant = applicant;
    }

    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        this.viewUpcomingInterviews(today);
        while (true) {
            try {
                this.runMainMenu(sc, today);
            }
            catch (ExitException ee) {
                return;
            }
        }
    }

    void runMainMenu(Scanner sc, LocalDate today) throws ExitException{
        int numOptions = displayMainMenuOptions();
        int option = getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                browseOpenJobPostingsNotAppliedTo(sc, today);
            case 2:
                ; //TODO: ????
            case 3:
                applyForJob();
            case 4:
                viewApplications();
            case 5:
                viewAccountHistory();
            case 6:
                throw new ExitException();
        }

    }

    void viewUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(today);
        if (upcomingInterviews.size() == 0) {
            System.out.println("You have no upcoming interviews.");
        }
        else {
            System.out.println("Here are your upcoming interviews:");
            for (Interview interview : upcomingInterviews) {
                System.out.println(interview.getJobApplication().getJobPosting().getTitle());
                System.out.println(interview.getJobApplication().getJobPosting().getCompany().getName());
                System.out.println(interview.getRoundNumberDescription(interview.getRoundNumber()));
                System.out.println(interview.getTime());
                System.out.println();
            }
        }
    }
    int displayMainMenuOptions() {
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - Browse open job postings not applied to");
        System.out.println("2 - View uploaded documents");
        System.out.println("3 - Apply for a job");
        System.out.println("4 - View applications");
        System.out.println("5 - View account history");
        System.out.println("6 - Exit");
        return 6;
    }

    int displayFilterOptions() {
        System.out.println();
        System.out.println("Select the search filter you would like to use:");
        System.out.println("1 - Filter by field");
        System.out.println("2 - Filter by company");
        System.out.println("3 - Filter by field and company");
        System.out.println("4 - No filter");
        return 4;

    }
    void browseOpenJobPostingsNotAppliedTo(Scanner sc, LocalDate today) {
        ArrayList<JobPosting> openJobPostings = applicant.getOpenJobPostingsNotAppliedTo(today);
        int numOptions = displayFilterOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                String field = getInputLine(sc, "Enter your field: ");
                boolean noPostingsFound = true;
                for (JobPosting posting : openJobPostings) {
                    if (posting.getField().equalsIgnoreCase(field)) {
                        noPostingsFound = false;
                        System.out.println(posting.getTitle());
                        System.out.println(posting.getId());
                        System.out.println(posting.getDescription());
                        System.out.println(posting.getRequirements());
                        System.out.println(posting.getNumPositions());
                        System.out.println(posting.getCompany().getName());
                        System.out.println(posting.getPostDate());
                        System.out.println(posting.getCloseDate());
                        System.out.println();
                    }
                }
                if (noPostingsFound) {
                    System.out.println("No postings were found matching field \"" + field + "\".");
                }
            case 2:
                String companyName = getInputLine(sc, "Enter your company name: ");
                noPostingsFound = true;
                for (JobPosting posting : openJobPostings) {
                    if (posting.getCompany().getName().equalsIgnoreCase(companyName)) {
                        noPostingsFound = false;
                        System.out.println(posting.getTitle());
                        System.out.println(posting.getId());
                        System.out.println(posting.getField());
                        System.out.println(posting.getDescription());
                        System.out.println(posting.getRequirements());
                        System.out.println(posting.getNumPositions());
                        System.out.println(posting.getPostDate());
                        System.out.println(posting.getCloseDate());
                        System.out.println();
                    }
                }
                if (noPostingsFound) {
                    System.out.println("The system ");
                }
            case 3:
                field = getInputLine(sc, "Enter your field: ");
                companyName = getInputLine(sc, "Enter your company name: ");
                noPostingsFound = true;
                for (JobPosting posting : openJobPostings) {
                    if (posting.getField().equalsIgnoreCase(field)
                            && posting.getCompany().getName().equalsIgnoreCase(companyName)) {
                        noPostingsFound = false;
                        System.out.println(posting.getTitle());
                        System.out.println(posting.getId());
                        System.out.println(posting.getDescription());
                        System.out.println(posting.getRequirements());
                        System.out.println(posting.getNumPositions());
                        System.out.println(posting.getPostDate());
                        System.out.println(posting.getCloseDate());
                        System.out.println();
                    }
                }
                if (noPostingsFound) {
                    System.out.println("No postings were found matching companyName \"" + companyName + "\".");
                }
            case 4:
                noPostingsFound = true;
                for (JobPosting posting : openJobPostings) {
                    noPostingsFound = false;
                    System.out.println(posting.getTitle());
                    System.out.println(posting.getId());
                    System.out.println(posting.getField());
                    System.out.println(posting.getDescription());
                    System.out.println(posting.getRequirements());
                    System.out.println(posting.getNumPositions());
                    System.out.println(posting.getPostDate());
                    System.out.println(posting.getCloseDate());
                    System.out.println();
                }
                if (noPostingsFound) {
                    System.out.println("The system does not currently contain any postings.");
                }
        }

        /*
        ArrayList<JobPosting> openJobPostings = applicant.getOpenJobPostingsNotAppliedTo(LocalDate.now());
        - Prompt user if they would like to filter postings by field, company, both, or neither (checkboxes?)
        If user wants to filter by field:
            - Prompt user to enter name of field
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getField().equalsIgnoreCase(field)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getCompany().getName()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching field "field"
            }
        Else if user wants to filter by company:
            - Prompt user to enter name of company
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getCompany().getName().equalsIgnoreCase(companyName)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getField()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching company "companyName"
            }
        Else if user wants to filter by field and company:
            - Prompt user to enter name of field
            - Prompt user to enter name of company
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getField().equalsIgnoreCase(field)
                        && posting.getCompany.getName().equalsIgnoreCase(companyName)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching field "field" and company "companyName"
            }
        Else:
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                noPostingsFound = false;
                - Show posting.getTitle()
                - Show posting.getId()
                - Show posting.getField()
                - Show posting.getDescription()
                - Show posting.getRequirements()
                - Show posting.getNumPositions()
                - Show posting.getCompany.getName()
                - Show posting.getPostDate()
                - Show posting.getCloseDate()
            }
            If (noPostingsFound) {
                - Tell user that the system does not currently have any postings
            }
         */
    }

    void applyForJob() {
        /*
        - Prompt for name of company applicant wishes to apply to
        if (JobApplicationSystem.getCompany(companyName) == null) {
            - Tell user that no company "companyName" was found
        }
        else {
            Company company = JobApplicationSystem.getCompany(companyName);
            - Prompt for id of posting applicant wishes to apply for (keep id in string format)
            if (company.getJobPostingManager().getJobPosting(id) == null) {
                - Tell user that no job posting was found with the given id
            }
            else {
                //TODO finish once clarification is obtained
            }

        }

        */
    }

    void viewApplications() {
        /*
        for (JobApplication application : applicant.getJobApplicationManager().getJobApplications()) {
            - Display application.getJobPosting().getTitle()
            - Display application.getJobPosting().getCompany().getName()
            - Display JobApplication.getStatuses().get(application.getStatus())
            - Give option to withdraw application
            If applicant chooses to withdraw application:
                boolean appWithdrawn = applicant.withdrawApplication(application.getJobPosting());
                if (appWithdrawn) {
                    - Tell user that their application has successfully been withdrawn
                }
                else {
                    - Tell user that since the job posting has been filled, they can no longer withdraw their
                    application
                }
        }
        */
    }

    void viewAccountHistory() {
        /*
        - Display applicant.getDateCreated()
        - Display each application in applicant.getJobApplicationManager().getPreviousJobApplications()
        - Display each application in applicant.getJobApplicationManager().getCurrentJobApplications()
        - Display applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(LocalDate.now())
        * */
    }

    void viewDocuments() {
        // TODO: finish once clarification is obtained
    }



}
