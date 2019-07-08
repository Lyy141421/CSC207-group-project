package GUIClasses;

import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicantInterfaceTest extends UserInterface {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private Applicant applicant = (Applicant) this.user; // The applicant currently logged in

    // === Constructor ===
    ApplicantInterfaceTest(User user) {
        super(user);
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
                System.out.println("Job ID: " + posting.getId());
                System.out.println("Title: " + posting.getTitle());
                System.out.println("Field: " + posting.getField());
                System.out.println("Description: " + posting.getDescription());
                System.out.println("Requirements: " + posting.getRequirements());
                System.out.println("Number of positions: " + posting.getNumPositions());
                System.out.println("Company: " + posting.getCompany().getName());
                System.out.println("Post date: " + posting.getPostDate());
                System.out.println("Close date: " + posting.getCloseDate());
                System.out.println();
            }
        }
        if (noPostingsFound) {
            System.out.println("No postings were found matching the specified criteria.");
        }
    }

    /**
     * Allow the applicant to select files from their account, and use these files to assemble a JobApplication.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     * @param posting The job posting that this applicant wishes to apply to.
     * @return a job application containing this applicant's chosen files.
     */
    private JobApplication createJobApplicationThroughFiles(Scanner sc, LocalDate today, JobPosting posting) {
        System.out.println("Here are your files: ");
        int CVFileNumber = 0;
        for (JobApplicationDocument document : applicant.getDocumentManager().getDocuments()) {
            CVFileNumber++;
            System.out.println(CVFileNumber + ". " + document.getContents());
            System.out.println();
        }
        System.out.println("Please enter the file number of the CV you would like to submit.");
        int CVOption = getMenuOption(sc, CVFileNumber);
        JobApplicationDocument CV = applicant.getDocumentManager().getDocuments().get(CVOption-1);
        int coverLetterFileNumber = 0;
        for (JobApplicationDocument document : applicant.getDocumentManager().getDocuments()) {
            coverLetterFileNumber++;
            System.out.println(coverLetterFileNumber + ". " + document.getContents());
        }
        System.out.println("Please enter the file number of the cover letter you would like to submit.");
        int coverLetterOption = getMenuOption(sc, coverLetterFileNumber);
        JobApplicationDocument coverLetter = applicant.getDocumentManager().getDocuments().get(coverLetterOption-1);
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
        String CVContents = getInputLinesUntilDone(sc, "Enter the contents of your CV as a series of plain text lines " +
                "(program will stop reading after the first empty line): ");
        String coverLetterContents = getInputLinesUntilDone(sc, "Enter the contents of your cover letter as a series of" +
                " plain text lines (program will stop reading after the first empty line): ");
        JobApplicationDocument CV = new JobApplicationDocument(CVContents);
        JobApplicationDocument coverLetter = new JobApplicationDocument(coverLetterContents);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
    }

    /**
     * Submit a job application on behalf of the applicant.
     *
     * @param sc The Scanner for user input.
     * @param today Today's date.
     */
    private void submitApplication(Scanner sc, LocalDate today) {
        System.out.println();
        String companyName = getInputLine(sc, "Enter the name of the company you wish to apply to: ");
        Company company = JobApplicationSystem.getCompany(companyName);
        while (company == null) {
            System.out.println("No company was found matching name \"" + companyName + "\".");
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
        int numOptions = 2;
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
                    System.out.println("Application successfully submitted.");
                    return;
                }
            case 2:
                JobApplication application = this.createJobApplicationThroughTextEntry(sc, today, posting);
                posting.addJobApplication(application);
                applicant.registerJobApplication(application);
                System.out.println("Application successfully submitted.");
        }
    }

    /**
     * Display all of the applications that the applicant has submitted.
     *
     * @param sc The Scanner for user input.
     * @param withdrawal Whether or not the applicant wishes to withdraw an application.
     */
    private void displayApplications(Scanner sc, boolean withdrawal) {
        System.out.println();
        ArrayList<JobApplication> applications = applicant.getJobApplicationManager().getJobApplications();
        if (applications.isEmpty()) {
            System.out.println("You have not yet submitted any applications.");
        }
        else {
            int applicationNumber = 0;
            for (JobApplication application : applications) {
                applicationNumber++;
                if (withdrawal) {
                    System.out.print(applicationNumber + ". ");
                }
                System.out.println("Title: " + application.getJobPosting().getTitle());
                System.out.println("Company: " + application.getJobPosting().getCompany().getName());
                System.out.println("Status: " + application.getStatus().getDescription());
                System.out.println();
            }
            if (withdrawal) {
                int applicationOption = getMenuOption(sc, applicationNumber);
                JobApplication applicationToWithdraw = applications.get(applicationOption-1);
                boolean appWithdrawn = applicant.withdrawJobApplication(applicationToWithdraw.getJobPosting());
                if (appWithdrawn)
                    System.out.println("Application successfully withdrawn.");
                else {
                    System.out.println("As the job posting corresponding to this application has already been filled, " +
                            "it is no longer possible to withdraw the application.");
                }
            }
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
        for (JobApplication application : applicant.getJobApplicationManager().getPreviousJobApplications()) {
            JobPosting posting = application.getJobPosting();
            System.out.println("Title: " + posting.getTitle());
            System.out.println("Field: " + posting.getField());
            System.out.println("Description: " + posting.getDescription());
            System.out.println("Company: " + posting.getCompany().getName());
            System.out.println("Close date: " + posting.getCloseDate());
            System.out.println("Status: " + application.getStatus().getDescription());
            System.out.println();
        }
        System.out.println("Current job applications:");
        for (JobApplication application : applicant.getJobApplicationManager().getCurrentJobApplications()) {
            JobPosting posting = application.getJobPosting();
            System.out.println("Title: " + posting.getTitle());
            System.out.println("Field: " + posting.getField());
            System.out.println("Description: " + posting.getDescription());
            System.out.println("Company: " + posting.getCompany().getName());
            System.out.println("Close date: " + posting.getCloseDate());
            System.out.println("Status: " + application.getStatus().getDescription());
            System.out.println();
        }
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
