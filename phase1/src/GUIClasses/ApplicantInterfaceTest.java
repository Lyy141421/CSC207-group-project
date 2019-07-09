package GUIClasses;

import Main.JobApplicationSystem;
import Miscellaneous.ExitException;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static Main.JobApplicationSystem.today;

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
     * Check if there are interviews on the date of login
     */
    boolean checkUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(today);
        return (upcomingInterviews.size() > 0);
    }

    ArrayList<JobPosting> findAppliablePostings(LocalDate today, String field, String companyName) {
        ArrayList<JobPosting> openJobPostings = applicant.getOpenJobPostingsNotAppliedTo(today);
        ArrayList<JobPosting> ret = new ArrayList<>();
        for (JobPosting posting : openJobPostings) {
            String filterField = (field == null)? posting.getField() : field;
            String filterCompanyName = (companyName == null)? posting.getCompany().getName() : companyName;
            if (filterField.equalsIgnoreCase(posting.getField())
                    && filterCompanyName.equalsIgnoreCase(posting.getCompany().getName())) {
                ret.add(posting);
            }
        }
        return ret;
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
     * Creates job app through input data
     */
    JobApplication createAppThroughInput(String CVContents, String coverLetterContents, JobPosting posting) {
        JobApplicationDocument CV = new JobApplicationDocument(CVContents);
        JobApplicationDocument coverLetter = new JobApplicationDocument(coverLetterContents);
        return new JobApplication(applicant, posting, CV, coverLetter, today);
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
                boolean appWithdrawn = applicant.withdrawJobApplication(today, applicationToWithdraw.getJobPosting());
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
