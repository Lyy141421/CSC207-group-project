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
    private Applicant applicant; // The applicant currently logged in

    // === Constructor ===
    public ApplicantInterface(Applicant applicant) {
        this.applicant = applicant;
    }

    // === Other methods ===

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

    private void runMainMenu(Scanner sc, LocalDate today) throws ExitException{
        int numOptions = displayMainMenuOptions();
        int option = getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                this.browseOpenJobPostingsNotAppliedTo(sc, today); // Browse open job postings not applied to
            case 2:
                this.viewDocuments(); // View uploaded documents
            case 3:
                this.submitApplication(sc, today); // Apply for a job
            case 4:
                this.viewApplications(sc, false); // View applications
            case 5:
                this.viewApplications(sc, true); // Withdraw an application
            case 6:
                this.viewAccountHistory(today); // View account history
        }

    }

    private void viewUpcomingInterviews(LocalDate today) {
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
    private int displayMainMenuOptions() {
        System.out.println();
        System.out.println("Please select an option below:");
        System.out.println("1 - Browse open job postings not applied to");
        System.out.println("2 - View uploaded documents");
        System.out.println("3 - Apply for a job");
        System.out.println("4 - View applications");
        System.out.println("5 - Withdraw an application");
        System.out.println("6 - View account history");
        return 6;
    }

    private int displayFilterOptions() {
        System.out.println();
        System.out.println("Select the search filter you would like to use:");
        System.out.println("1 - Filter by field");
        System.out.println("2 - Filter by company");
        System.out.println("3 - Filter by field and company");
        System.out.println("4 - No filter");
        return 4;
    }

    private void browseOpenJobPostingsNotAppliedTo(Scanner sc, LocalDate today) {
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
            System.out.println();
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
        applicant.addFile(CV);
        System.out.println("Enter the contents of your cover letter as plain text (type DONE and hit space when done): ");
        String coverLetter = sc.next("DONE");
        applicant.addFile(coverLetter);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    int displaySubmitMenuOptions() {
        System.out.println();
        System.out.println("Select an application option:");
        System.out.println("1 - Apply using a CV and cover letter from your account files");
        System.out.println("2 - Enter a CV and cover letter manually");
        return 2;
    }

    void submitApplication(Scanner sc, LocalDate today) {
        String companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        while (company == null) {
            System.out.println("No company was found matching name \"" + companyName + "\".");
            companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
            company = JobApplicationSystem.getCompany(companyName);
        }
        int postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
        JobPosting posting = company.getJobPostingManager().getJobPosting(postingId);
        while (posting == null || posting.getCloseDate().isEqual(today) || posting.getCloseDate().isAfter(today)) {
            System.out.println("No open posting was found matching id " + postingId + ".");
            postingId = getInteger(sc, "Enter the id of the posting you wish to apply for: ");
            posting = company.getJobPostingManager().getJobPosting(postingId);
        }
        int numOptions = this.displaySubmitMenuOptions();
        int option = this.getMenuOption(sc, numOptions);
        switch (option) {
            case 1:
                JobApplication application = this.createJobApplicationThroughFiles(sc, today, posting);
                posting.addJobApplication(application);
                applicant.getJobApplicationManager().addJobApplication(application);

            case 2:
                application = this.createJobApplicationThroughTextEntry(sc, today, posting);
                posting.addJobApplication(application);
                applicant.getJobApplicationManager().addJobApplication(application);
        }
    }

    void viewApplications(Scanner sc, boolean withdrawal) {
        ArrayList<JobApplication> applications = applicant.getJobApplicationManager().getJobApplications();
        int applicationNumber = 0;
        for (JobApplication application : applications) {
            applicationNumber++;
            if (withdrawal) {
                System.out.print(applicationNumber + ". ");
            }
            System.out.println(application.getJobPosting().getTitle());
            System.out.println(application.getJobPosting().getCompany().getName());
            System.out.println(JobApplication.getStatuses().get(application.getStatus()));
            System.out.println();
        }
        if (withdrawal) {
            int applicationOption = getMenuOption(sc, applicationNumber);
            JobApplication applicationToWithdraw = applications.get(applicationOption-1);
            boolean appWithdrawn = applicant.withdrawApplication(applicationToWithdraw.getJobPosting());
            if (appWithdrawn) {
                System.out.println("Application successfully withdrawn.");
            }
            else {
                System.out.println("As the job posting corresponding to this application has already been filled, " +
                        "it is no longer possible to withdraw the application.");
            }
        }
    }

    void viewAccountHistory(LocalDate today) {
        System.out.println("Account created: " + applicant.getDateCreated());
        System.out.println("Previous job applications:");
        for (JobApplication application : applicant.getJobApplicationManager().getPreviousJobApplications()) {
            System.out.println(application.getJobPosting().getTitle());
            System.out.println(application.getJobPosting().getCompany().getName());
            System.out.println(JobApplication.getStatuses().get(application.getStatus()));
            System.out.println();
        }
        System.out.println("Current job applications:");
        for (JobApplication application : applicant.getJobApplicationManager().getCurrentJobApplications()) {
            System.out.println(application.getJobPosting().getTitle());
            System.out.println(application.getJobPosting().getCompany().getName());
            System.out.println(JobApplication.getStatuses().get(application.getStatus()));
            System.out.println();
        }
        System.out.println("It has been " +
                applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(today) + " days since your most " +
                "recent application closed.");
    }

    void viewDocuments() {
        for (String file : applicant.getFilesSubmitted()) {
            System.out.println(file);
            System.out.println();
        }
    }
}
