package GUIClasses;

import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicantInterface extends UserInterface {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private Applicant applicant = (Applicant) this.user; // The applicant currently logged in

    // === Constructor ===
    ApplicantInterface(User user) {
        super(user);
    }

    // === Other methods ===

    /**
     * Run the applicant interface.
     *
     * @param today Today's date.
     */
    void run(LocalDate today) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome, " + applicant.getLegalName() + ".");
        this.displayUpcomingInterviews(today);
        while (true) {
            try {
                this.runMainMenu(sc, today);
            }
            catch (ExitException ee) {
                return;
            }
        }
    }

    // === Private methods ===
    /**
     * Run the main menu of the applicant interface.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     */
    private void runMainMenu(Scanner sc, LocalDate today) throws ExitException{
        int numOptions = displayMainMenuOptions();
        int option = getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                this.setJobPostingSearchFilters(sc, today); // Browse open job postings not applied to
                return;
            case 2:
                this.displayDocuments(); // View uploaded documents
                return;
            case 3:
                this.submitApplication(sc, today); // Apply for a job
                return;
            case 4:
                this.displayApplications(sc, today, false); // View applications
                return;
            case 5:
                this.displayApplications(sc, today, true); // Withdraw an application
                return;
            case 6:
                this.displayAccountHistory(today); // View account history
                return;
            case 7:
                throw new ExitException(); // Exit

        }

    }

    /**
     * Display the upcoming interviews for this applicant.
     *
     * @param today Today's date.
     */
    private void displayUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(today);
        if (upcomingInterviews.size() == 0) {
            System.out.println("You have no upcoming interviews.");
        }
        else {
            System.out.println("Here are your upcoming interviews:");
            for (Interview interview : upcomingInterviews) {
                System.out.println("Title: " + interview.getJobApplication().getJobPosting().getTitle());
                System.out.println("Company: " + interview.getJobApplication().getJobPosting().getCompany().getName());
                System.out.println(interview.getRoundNumberDescription(interview.getRoundNumber()));
                System.out.println(interview.getTime());
                System.out.println();
            }
        }
    }

    /**
     * Display the applicant interface main menu options.
     *
     * @return the number of options in the menu.
     */
    private int displayMainMenuOptions() {
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - Browse open job postings not applied to");
        System.out.println("2 - View uploaded documents");
        System.out.println("3 - Apply for a job");
        System.out.println("4 - View applications");
        System.out.println("5 - Withdraw an application");
        System.out.println("6 - View account history");
        System.out.println("7 - Exit");
        return 7;
    }


    /**
     * Display the types of filters that the applicant can select from when browsing job postings.
     *
     * @return the number of options in the menu.
     */
    private int displayFilterOptions() {
        System.out.println("Select the search filter you would like to use:");
        System.out.println("1 - Filter by field");
        System.out.println("2 - Filter by company");
        System.out.println("3 - Filter by field and company");
        System.out.println("4 - No filter");
        return 4;
    }

    /**
     * Display the options for job application creation.
     *
     * @return the number of options in the menu.
     */
    private int displaySubmitMenuOptions() {
        System.out.println();
        System.out.println("Select an application option:");
        System.out.println("1 - Apply using a CV and cover letter from your account files");
        System.out.println("2 - Enter a CV and cover letter manually");
        return 2;
    }

    /**
     * Display the open job postings that this applicant has not applied to.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     */
    private void setJobPostingSearchFilters(Scanner sc, LocalDate today) {
        System.out.println();
        int numOptions = this.displayFilterOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                String field = getInputLine(sc, "Enter your field: ");
                this.displayOpenJobPostingsNotYetAppliedTo(today, field, null);
                return;
            case 2:
                String companyName = getInputLine(sc, "Enter your company name: ");
                this.displayOpenJobPostingsNotYetAppliedTo(today, null, companyName);
                return;
            case 3:
                field = getInputLine(sc, "Enter your field: ");
                companyName = getInputLine(sc, "Enter your company name: ");
                this.displayOpenJobPostingsNotYetAppliedTo(today, field, companyName);
                return;
            case 4:
                this.displayOpenJobPostingsNotYetAppliedTo(today, null, null);
        }
    }

    private void displayOpenJobPostingsNotYetAppliedTo(LocalDate today, String field, String companyName) {
        ArrayList<JobPosting> openJobPostings = applicant.getOpenJobPostingsNotAppliedTo(today);
        boolean noPostingsFound = true;
        for (JobPosting posting : openJobPostings) {
            // if field is null, assign value of posting.getField() to filterField so that all postings pass the filter;
            // otherwise, assign value of field to filterField
            String filterField = (field == null)? posting.getField() : field;
            String filterCompanyName = (companyName == null)? posting.getCompany().getName() : companyName;
            if (filterField.equalsIgnoreCase(posting.getField())
                    && filterCompanyName.equalsIgnoreCase(posting.getCompany().getName())) {
                noPostingsFound = false;
                System.out.println("\n" + posting.toStringStandardInput());
            }
        }
        if (noPostingsFound) {
            System.out.println("\nNo postings were found matching the specified criteria.");
        }
    }

    /**
     * Allow the applicant to select a document from their account files.
     * @param sc The Scanner for user input.
     * @param documentType The type of document being selected.
     * @return the document selected.
     */
    private JobApplicationDocument selectDocumentFromFiles(Scanner sc, String documentType) {
        System.out.println("\nHere are your files: ");
        int fileNumber = 0;
        for (JobApplicationDocument document : applicant.getDocumentManager().getDocuments()) {
            fileNumber++;
            System.out.println("\n" + fileNumber + ". " + document.getContents());
        }
        System.out.println("\nPlease enter the file number of the " + documentType + " you would like to submit.");
        int option = getMenuOption(sc, fileNumber);
        return applicant.getDocumentManager().getDocuments().get(option-1);
    }
    /**
     * Allow the applicant to select documents from their account, and use these documents to assemble a JobApplication.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     * @param posting The job posting that this applicant wishes to apply to.
     * @return a job application containing this applicant's chosen documents.
     */
    private JobApplication createJobApplicationThroughFiles(Scanner sc, LocalDate today, JobPosting posting) {
        JobApplicationDocument CV = this.selectDocumentFromFiles(sc, "CV");
        JobApplicationDocument coverLetter = this.selectDocumentFromFiles(sc, "cover letter");
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    /**
     * Allow the applicant to enter files as plaintext, and use these files to assemble a JobApplication.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     * @param posting The job posting that this applicant wishes to apply to.
     * @return a job application containing this applicant's entered files.
     */
    private JobApplication createJobApplicationThroughTextEntry(Scanner sc, LocalDate today, JobPosting posting) {
        String CVContents = getInputLinesUntilDone(sc, "\nEnter the contents of your CV as a series of plain " +
                "text lines (program will stop reading after the first empty line): ");
        String coverLetterContents = getInputLinesUntilDone(sc, "\nEnter the contents of your cover letter " +
                "as a series of plain text lines (program will stop reading after the first empty line): ");
        JobApplicationDocument CV = new JobApplicationDocument(CVContents);
        JobApplicationDocument coverLetter = new JobApplicationDocument(coverLetterContents);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    /**
     * Get the posting that the applicant wishes to apply to.
     * @param sc The Scanner for user input.
     * @param today Today's date.
     * @return the posting that the applicant wishes to apply to.
     */
    private JobPosting getPostingForJobApplication(Scanner sc, LocalDate today) {
        String companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        while (company == null) {
            System.out.println("No company was found matching name \"" + companyName + "\".");
            System.out.println();
            companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
            company = JobApplicationSystem.getCompany(companyName);
        }
        int postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
        JobPosting posting = company.getJobPostingManager().getJobPosting(postingId);
        while (posting == null || posting.getCloseDate().isBefore(today)) {
            System.out.println("No open posting was found matching id " + postingId + ".");
            System.out.println();
            postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
            posting = company.getJobPostingManager().getJobPosting(postingId);
        }
        return posting;
    }

    /**
     * Submit a job application on behalf of the applicant.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     */
    private void submitApplication(Scanner sc, LocalDate today) {
        JobPosting posting = this.getPostingForJobApplication(sc, today);
        System.out.println();
        if (applicant.hasAppliedTo(posting)) {
            System.out.println("You have already submitted an application for this job posting.");
            return;
        }
        int numOptions = this.displaySubmitMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                if (applicant.getDocumentManager().getDocuments().isEmpty()) {
                    System.out.println("You have not yet uploaded any files.");
                    this.submitApplication(sc, today);
                    return;
                }
                else {
                    JobApplication application = this.createJobApplicationThroughFiles(sc, today, posting);
                    posting.addJobApplication(application);
                    applicant.registerJobApplication(application);
                    System.out.println("\nApplication successfully submitted.");
                    return;
                }
            case 2:
                JobApplication application = this.createJobApplicationThroughTextEntry(sc, today, posting);
                posting.addJobApplication(application);
                applicant.registerJobApplication(application);
                System.out.println("\nApplication successfully submitted.");
        }
    }

    /**
     * Display all of the applications that the applicant has submitted.
     *
     * @param sc The Scanner for user input.
     * @param withdrawal Whether or not the applicant wishes to withdraw an application.
     */
    private void displayApplications(Scanner sc, LocalDate today, boolean withdrawal) {
        System.out.println();
        ArrayList<JobApplication> applications = applicant.getJobApplicationManager().getJobApplications();
        if (applications.isEmpty())
            System.out.println("You have not yet submitted any applications.");
        else {
            int applicationNumber = 0;
            for (JobApplication application : applications) {
                applicationNumber++;
                if (withdrawal)
                    System.out.print(applicationNumber + ". ");
                System.out.println(application.toStringForApplicant());
                System.out.println();
            }
            if (withdrawal) {
                if (this.withdrawApplication(sc, today, applications, applicationNumber))
                    System.out.println("Application successfully withdrawn.");
                else
                    System.out.println("As the job posting corresponding to this application has already been filled, " +
                            "it is no longer possible to withdraw the application.");
            }
        }
    }

    /**
     * Interface for withdrawing an application.
     *
     * @param sc                The scanner for user input.
     * @param today             Today's date.
     * @param applications      All applications this applicant has submitted.
     * @param applicationNumber The application number selected.
     * @return true iff the application was successfully withdrawn.
     */
    private boolean withdrawApplication(Scanner sc, LocalDate today, ArrayList<JobApplication> applications,
                                        int applicationNumber) {
        int applicationOption = getMenuOption(sc, applicationNumber);
        JobApplication applicationToWithdraw = applications.get(applicationOption - 1);
        boolean appWithdrawn = applicant.withdrawJobApplication(today, applicationToWithdraw.getJobPosting());
        applicationToWithdraw.getJobPosting().getInterviewManager().withdrawApplication(applicationToWithdraw);
        return appWithdrawn;
    }

    /**
     * Display either the previous or current applications this applicant has submitted, depending on whether
     * previous is true.
     * @param previous whether or not the applications shown should be previous applications.
     */
    private void displayApplicationHistory(boolean previous) {
        ArrayList<JobApplication> applications = previous ?
                applicant.getJobApplicationManager().getPreviousJobApplications()
                : applicant.getJobApplicationManager().getCurrentJobApplications();
        for (JobApplication application : applications) {
            JobPosting posting = application.getJobPosting();
            System.out.println(posting.toStringStandardInput());
            System.out.println("Your status: " + application.getStatus());
            System.out.println();
        }
    }

    /**
     * Display the applicant's account history.
     *
     * @param today Today's date.
     */
    private void displayAccountHistory(LocalDate today) {
        System.out.println();
        System.out.println("Account created: " + applicant.getDateCreated());
        System.out.println("Previous job applications:");
        this.displayApplicationHistory(true);
        System.out.println("Current job applications:");
        this.displayApplicationHistory(false);
        if (applicant.getJobApplicationManager().getJobApplications().isEmpty())
            System.out.println("You have not yet submitted any job applications.");
        else
            System.out.println("It has been " +
                    applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(today) +
                    " days since your most recent application closed.");
    }

    /**
     * Display the applicant's submitted documents.
     */
    private void displayDocuments() {
        System.out.println();
        ArrayList<JobApplicationDocument> documents = applicant.getDocumentManager().getDocuments();
        if (documents.isEmpty()) {
            System.out.println("You have not yet uploaded any documents.");
        }
        else {
            for (JobApplicationDocument document : documents) {
                System.out.println(document);
                System.out.println();
            }
        }
    }
}
