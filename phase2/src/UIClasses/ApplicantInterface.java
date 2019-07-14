package UIClasses;

import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.util.ArrayList;

public class ApplicantInterface extends UserInterface {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private Applicant applicant = (Applicant) this.user; // The applicant currently logged in

    // === Constructor ===
    ApplicantInterface(JobApplicationSystem JAS, User user) {
        super(JAS, user);
    }

    // === Other methods ===

    /**
     * Run the applicant interface.
     *
     */
    @Override
    public void run() {
        System.out.println("Welcome, " + this.user.getLegalName() + ".\n");
        this.displayUpcomingInterviews();
        while (true) {
            try {
                this.runMainMenu();
            } catch (ExitException ee) {
                return;
            }
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Run the main menu of the applicant interface.
     *
     */
    private void runMainMenu()
            throws ExitException {
        int numOptions = displayMainMenuOptions();
        int option = getMenuOption(numOptions);
        switch (option) {
            case 1:
                this.setJobPostingSearchFilters(); // Browse open job postings not applied to
                return;
            case 2:
                this.displayDocuments(); // View uploaded documents
                return;
            case 3:
                this.submitApplication(); // Apply for a job
                return;
            case 4:
                this.displayApplications(false); // View applications
                return;
            case 5:
                this.displayApplications(true); // Withdraw an application
                return;
            case 6:
                this.displayAccountHistory(); // View account history
                return;
            case 7:
                throw new ExitException(); // Exit

        }

    }

    /**
     * Display the upcoming interviews for this applicant.
     *
     */
    private void displayUpcomingInterviews() {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(this.today);
        if (upcomingInterviews.size() == 0) {
            System.out.println("You have no upcoming interviews.");
        } else {
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
        System.out.println("\nPlease select an option below:");
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
        System.out.println("\nSelect the search filter you would like to use:");
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
        System.out.println("\nSelect an application method:");
        System.out.println("1 - Apply using a CV and cover letter from your account files");
        System.out.println("2 - Enter a CV and cover letter manually");
        return 2;
    }

    /**
     * Display the open job postings that this applicant has not applied to.
     *
     */
    private void setJobPostingSearchFilters() {
        System.out.println();
        int numOptions = this.displayFilterOptions();
        int option = this.getMenuOption(numOptions);
        switch (option) {
            case 1:
                String field = getInputLine("\nEnter your field: ");
                this.displayOpenJobPostingsNotYetAppliedTo(field, null);
                return;
            case 2:
                String companyName = getInputLine("\nEnter your company name: ");
                this.displayOpenJobPostingsNotYetAppliedTo(null, companyName);
                return;
            case 3:
                field = getInputLine("\nEnter your field: ");
                companyName = getInputLine("Enter your company name: ");
                this.displayOpenJobPostingsNotYetAppliedTo(field, companyName);
                return;
            case 4:
                this.displayOpenJobPostingsNotYetAppliedTo(null, null);
        }
    }

    private void displayOpenJobPostingsNotYetAppliedTo(String field, String companyName) {
        ArrayList<JobPosting> openJobPostings = this.applicant.getOpenJobPostingsNotAppliedTo(this.JAS);
        boolean noPostingsFound = true;
        for (JobPosting posting : openJobPostings) {
            // if field is null, assign value of posting.getField() to filterField so that all postings pass the filter;
            // otherwise, assign value of field to filterField
            String filterField = (field == null) ? posting.getField() : field;
            String filterCompanyName = (companyName == null) ? posting.getCompany().getName() : companyName;
            if (filterField.equalsIgnoreCase(posting.getField())
                    && filterCompanyName.equalsIgnoreCase(posting.getCompany().getName())) {
                noPostingsFound = false;
                System.out.println("\n" + posting.toString());
            }
        }
        if (noPostingsFound) {
            System.out.println("\nNo postings were found matching the specified criteria.");
        }
    }

    /**
     * Allow the applicant to select a document from their account files.
     *
     * @param documentType The type of document being selected.
     * @return the document selected.
     */
    private JobApplicationDocument selectDocumentFromFiles(String documentType) {
        ArrayList<JobApplicationDocument> documents = this.applicant.getDocumentManager().getDocuments();
        System.out.println("\nHere are your files: ");
        this.printListToSelectFrom(documents);
        System.out.println("\nPlease enter the file number of the " + documentType + " you would like to submit.");
        int option = getMenuOption(documents.size());
        return this.applicant.getDocumentManager().getDocuments().get(option - 1);
    }

    /**
     * Allow the applicant to select documents from their account, and use these documents to assemble a JobApplication.
     *
     * @param posting The job posting that this applicant wishes to apply to.
     * @return a job application containing this applicant's chosen documents.
     */
    private JobApplication createJobApplicationThroughFiles(JobPosting posting) {
        if (applicant.getDocumentManager().getDocuments().isEmpty()) {
            System.out.println("You have not yet uploaded any documents.");
            return null;
        } else {
            JobApplicationDocument CV = this.selectDocumentFromFiles("CV");
            JobApplicationDocument coverLetter = this.selectDocumentFromFiles("cover letter");
            return new JobApplication(applicant, posting, CV, coverLetter, today);
        }
    }

    /**
     * Allow the applicant to enter files as plaintext, and use these files to assemble a JobApplication.
     *
     * @param posting The job posting that this applicant wishes to apply to.
     * @return a job application containing this applicant's entered files.
     */
    private JobApplication createJobApplicationThroughTextEntry(JobPosting posting) {
        String CVContents = getInputLinesUntilDone("\nEnter the contents of your CV as a series of plain " +
                "text lines (program will stop reading after the first empty line): ");
        String coverLetterContents = getInputLinesUntilDone("\nEnter the contents of your cover letter " +
                "as a series of plain text lines (program will stop reading after the first empty line): ");
        JobApplicationDocument CV = new JobApplicationDocument(CVContents);
        JobApplicationDocument coverLetter = new JobApplicationDocument(coverLetterContents);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    /**
     * Get the posting that the applicant wishes to apply to.
     *
     * @return the posting that the applicant wishes to apply to.
     */
    private JobPosting getPostingForJobApplication() {
        String companyName = getInputLine("\nEnter the name of the company you wish to apply to: ");
        Company company = this.JAS.getCompany(companyName);
        if (company == null) {
            System.out.println("\nNo company was found matching name \"" + companyName + ".");
            return null;
        }
        int postingId = getPositiveInteger("Enter the ID of the posting you wish to apply for: ");
        JobPosting posting = company.getJobPostingManager().getJobPosting(postingId);
        if (posting == null || posting.getCloseDate().isBefore(this.today)) {
            System.out.println("\nNo open posting was found matching ID " + postingId + ".");
        }
        return posting;
    }

    /**
     * Submit a job application on behalf of the applicant.
     *
     */
    private void submitApplication() {
        JobPosting posting = this.getPostingForJobApplication();
        if (posting == null) {
            return;
        }
        if (this.applicant.hasAppliedTo(posting)) {
            System.out.println("\nYou have already submitted an application for this job posting.");
            return;
        }
        int numOptions = this.displaySubmitMenuOptions();
        int option = this.getMenuOption(numOptions);
        switch (option) {
            case 1:
                if (applicant.getDocumentManager().getDocuments().isEmpty()) {
                    System.out.println("\nYou have not uploaded any files.");
                    System.out.println();
                } else {
                    JobApplication application = this.createJobApplicationThroughFiles(
                            posting);
                    posting.addJobApplication(application);
                    applicant.registerJobApplication(application);
                    System.out.println("\nApplication successfully submitted.");
                }
                break;
            case 2:
                JobApplication application = this.createJobApplicationThroughTextEntry(posting);
                posting.addJobApplication(application);
                this.applicant.registerJobApplication(application);
                System.out.println("\nApplication successfully submitted.");
        }
    }

    /**
     * Display all of the applications that the applicant has submitted.
     *
     * @param withdrawal Whether or not the applicant wishes to withdraw an application.
     */
    private void displayApplications(boolean withdrawal) {
        System.out.println();
        ArrayList<JobApplication> applications = this.applicant.getJobApplicationManager().getJobApplications();
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
                if (this.withdrawApplication(applications, applicationNumber))
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
     * @param applications      All applications this applicant has submitted.
     * @param applicationNumber The application number selected.
     * @return true iff the application was successfully withdrawn.
     */
    private boolean withdrawApplication(ArrayList<JobApplication> applications,
                                        int applicationNumber) {
        int applicationOption = getMenuOption(applicationNumber);
        JobApplication applicationToWithdraw = applications.get(applicationOption - 1);
        boolean appWithdrawn = this.applicant.withdrawJobApplication(this.today, applicationToWithdraw.getJobPosting());
        applicationToWithdraw.getJobPosting().getInterviewManager().withdrawApplication(applicationToWithdraw);
        return appWithdrawn;
    }

    /**
     * Display either the previous or current applications this applicant has submitted, depending on whether
     * previous is true.
     *
     * @param previous whether or not the applications shown should be previous applications.
     */
    private void displayApplicationHistory(boolean previous) {
        ArrayList<JobApplication> applications = previous ?
                applicant.getJobApplicationManager().getPreviousJobApplications()
                : applicant.getJobApplicationManager().getCurrentJobApplications();
        if (applications.isEmpty()) {
            System.out.println("N/A");
        }
        for (JobApplication application : applications) {
            JobPosting posting = application.getJobPosting();
            System.out.println("\n" + posting.toString());
            System.out.println("Your status: " + application.getStatus());
        }
    }

    /**
     * Display the applicant's account history.
     *
     */
    private void displayAccountHistory() {
        System.out.println();
        System.out.println("Account created: " + this.applicant.getDateCreated());
        System.out.println("\nPrevious job applications:");
        this.displayApplicationHistory(true);
        System.out.println("\nCurrent job applications:");
        this.displayApplicationHistory(false);
        long numDaysSinceClose = this.applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(today);
        if (this.applicant.getJobApplicationManager().getJobApplications().isEmpty())
            System.out.println("You have not yet submitted any job applications.");
        else if (numDaysSinceClose == 0)
            System.out.println("Your most recent posting closed today.");
        else if (numDaysSinceClose == 1)
            System.out.println("It has been 1 day since your most recent posting closed.");
        else
            System.out.println("It has been " + numDaysSinceClose + " days since your most recent posting closed.");
    }

    /**
     * Display the applicant's submitted documents.
     */
    private void displayDocuments() {
        ArrayList<JobApplicationDocument> documents = this.applicant.getDocumentManager().getDocuments();
        this.printListToSelectFrom(documents);
    }
}
