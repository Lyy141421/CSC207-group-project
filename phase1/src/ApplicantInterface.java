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
                applyForJob(sc, today);
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
                    System.out.println("No postings were found matching company name \"" + companyName + "\".");
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
                    System.out.println("No postings were found matching field \"" + field +
                            "\" and company name \"" + companyName + "\".");
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
                    System.out.println(posting.getCompany().getName());
                    System.out.println(posting.getPostDate());
                    System.out.println(posting.getCloseDate());
                    System.out.println();
                }
                if (noPostingsFound) {
                    System.out.println("The system does not currently contain any postings.");
                }
        }
    }

    JobApplication createJobApplicationThroughFiles(Scanner sc, LocalDate today, JobPosting posting) {
        System.out.println("Here are your files: ");
        int CVFileNumber = 0;
        for (String file : applicant.getFilesSubmitted()) {
            CVFileNumber++;
            System.out.println(CVFileNumber + ". " + file);
        }
        System.out.println("Please enter the file number of the CV you would like to submit.");
        int CVOption = getMenuOption(sc, CVFileNumber);
        String CV = applicant.getFilesSubmitted().get(CVOption-1);
        int coverLetterFileNumber = 0;
        for (String file : applicant.getFilesSubmitted()) {
            coverLetterFileNumber++;
            System.out.println(coverLetterFileNumber + ". " + file);
        }
        System.out.println("Please enter the file number of the cover letter you would like to submit.");
        int coverLetterOption = getMenuOption(sc, coverLetterFileNumber);
        String coverLetter = applicant.getFilesSubmitted().get(coverLetterOption-1);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    JobApplication createJobApplicationThroughTextEntry(Scanner sc, LocalDate today, JobPosting posting) {
        System.out.println("Enter the contents of your CV as plain text (type DONE and hit space when done): ");
        String CV = sc.next("DONE");
        System.out.println("Enter the contents of your cover letter as plain text (type DONE and hit space when done): ");
        String coverLetter = sc.next("DONE");
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    int displaySubmitMenuOptions() {
        System.out.println();
        System.out.println("Select an application option:");
        System.out.println("1 - Apply using a CV and cover letter from your account files");
        System.out.println("2 - Enter a CV and cover letter manually");
        return 2;
    }

    void applyForJob(Scanner sc, LocalDate today) {
        String companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        while (company == null) {
            System.out.println("No company was found matching name \"" + companyName + "\".");
            companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
            company = JobApplicationSystem.getCompany(companyName);
        }
        int postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
        JobPosting posting = company.getJobPostingManager().getJobPosting(postingId);
        while (posting == null) {
            System.out.println("No posting was found matching id " + postingId + ".");
            postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
            posting = company.getJobPostingManager().getJobPosting(postingId);
        }
        int numOptions = this.displaySubmitMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                JobApplication application = this.createJobApplicationThroughFiles(sc, today, posting);
                posting.addJobApplication(application);
            case 2:
                application = this.createJobApplicationThroughTextEntry(sc, today, posting);
                posting.addJobApplication(application);
        }
    }

    void viewApplications() {
        //TODO
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
        //TODO
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
