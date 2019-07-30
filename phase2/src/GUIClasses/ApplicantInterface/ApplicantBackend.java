package GUIClasses.ApplicantInterface;

import ApplicantStuff.*;
import CompanyStuff.Interview;
import CompanyStuff.JobPostings.*;
import Main.JobApplicationSystem;
import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicantBackend {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private LocalDate today;
    private Applicant applicant; // The applicant currently logged in

    // === Constructor ===
    ApplicantBackend(User user) {
        this.applicant = (Applicant)user;
    }

    /**
     * Check if there are interviews on the date of login
     */
    boolean checkUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(today);
        return (upcomingInterviews.size() > 0);
    }

    /**
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    ArrayList<BranchJobPosting> findApplicablePostings(String field,
                                                       String companyName, String ID, String Tags) {
        ArrayList<BranchJobPosting> openJobPostings =
                applicant.getOpenJobPostingsNotAppliedTo(new JobApplicationSystem()); //TODO are you serious
        ArrayList<BranchJobPosting> ret = new ArrayList<>();
        for (BranchJobPosting posting : openJobPostings) {
            String filterField = (field == null)? posting.getField() : field;
            String filterCompanyName = (companyName == null)? posting.getBranch().getName() : companyName;
            //ID, tags
            if (filterField.equalsIgnoreCase(posting.getField())
                    && filterCompanyName.equalsIgnoreCase(posting.getBranch().getName())) {
                ret.add(posting);
            }
        }
        return ret;
    }

//    /**
//     * Allow the applicant to select files from their account, and use these files to assemble a JobApplication.
//     *
//     * @param sc The Scanner for user input.
//     * @param today Today's date.
//     * @param posting The job posting that this applicant wishes to apply to.
//     * @return a job application containing this applicant's chosen files.
//     */
//    private JobApplication createJobApplicationThroughFiles(Scanner sc, LocalDate today, JobPosting posting) {
//        System.out.println("Here are your files: ");
//        int CVFileNumber = 0;
//        for (JobApplicationDocument document : applicant.getDocumentManager().getDocuments()) {
//            CVFileNumber++;
//            System.out.println(CVFileNumber + ". " + document.getContents());
//            System.out.println();
//        }
//        System.out.println("Please enter the file number of the CV you would like to submit.");
//        int CVOption = getMenuOption(sc, CVFileNumber);
//        JobApplicationDocument CV = applicant.getDocumentManager().getDocuments().get(CVOption-1);
//        int coverLetterFileNumber = 0;
//        for (JobApplicationDocument document : applicant.getDocumentManager().getDocuments()) {
//            coverLetterFileNumber++;
//            System.out.println(coverLetterFileNumber + ". " + document.getContents());
//        }
//        System.out.println("Please enter the file number of the cover letter you would like to submit.");
//        int coverLetterOption = getMenuOption(sc, coverLetterFileNumber);
//        JobApplicationDocument coverLetter = applicant.getDocumentManager().getDocuments().get(coverLetterOption-1);
//        return new JobApplication(applicant, posting, CV, coverLetter, today);
//    }

//    /**
//     * Creates job app through input data
//     */
//    JobApplication createAppThroughInput(String CVContents, String coverLetterContents, BranchJobPosting posting) {
//        JobApplicationDocument CV = new JobApplicationDocument(CVContents);
//        JobApplicationDocument coverLetter = new JobApplicationDocument(coverLetterContents);
//        return new JobApplication(applicant, posting, CV, coverLetter, today);
//    }

    /**
     * Returns a list of applicant's applications
     */
    ArrayList<JobApplication> getApps() {
        return applicant.getJobApplicationManager().getJobApplications();
    }

    /**
     * Returns a list of applicant's application's jobpostings
     */
    ArrayList<BranchJobPosting> helperPostings() {
        ArrayList<BranchJobPosting>  ret = new ArrayList<>();
        for(JobApplication app : this.getApps()) {
            ret.add(app.getJobPosting());
        }
        return ret;
    }

    /**
     * Withdraw an application
     */
    boolean withdrawApp(JobApplication application) {
        return applicant.withdrawJobApplication(today, application.getJobPosting());
    }

    /**
     * Return status since last job application (none submitted or days since)
     */
    String daysSince() {
        if (applicant.getJobApplicationManager().getJobApplications().isEmpty())
            return("You have not yet submitted any job applications.");
        else
            return("It has been " +
                    applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(today) +
                    " days since your most recent application closed.");
    }

    /**
     * Builds and returns an html string containing info regarding the applicant's previous applications
     */
    String oldApps() {
        String ret = "<html>Previous applications:";
        ArrayList<JobApplication> applications = applicant.getJobApplicationManager().getPreviousJobApplications();
        for (JobApplication application : applications) {
            BranchJobPosting posting = application.getJobPosting();
            ret += "<br>Posting: " + posting.getTitle();
            ret += " (Status: " + application.getStatus() + ")";
        }
        ret += "</html>";
        return ret;
    }

    /**
     //     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     //     * @param jobPostings the postings in question
     //     */
    String[] getListNames(ArrayList<BranchJobPosting> jobPostings) {
        int len = jobPostings.size();
        String[] ret = new String[len];

        for(int i=0; i < len; i++) {
            ret[i] = jobPostings.get(i).getTitle() + " - " + jobPostings.get(i).getId();
        }

        return ret;
    }

    /**
     * Display the applicant's submitted documents.
     */
    private void displayDocuments() { //TODO Phase 2
        System.out.println();
//        ArrayList<JobApplicationDocument> documents = applicant.getDocumentManager().getDocuments();
//        if (documents.isEmpty()) {
//            System.out.println("You have not yet uploaded any documents.");
//        }
//        else {
//            for (JobApplicationDocument document : documents) {
//                System.out.println(document);
//                System.out.println();
//            }
//        }
    }
}
