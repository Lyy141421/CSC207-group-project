package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.JobApplicationManager;
import Main.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicantDocumentManager extends DocumentManager {

    // === Instance variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/users";
    private Applicant applicant;

    // === Constructor ===
    public ApplicantDocumentManager(Applicant applicant) {
        this.applicant = applicant;
        this.setFolder(new File(FOLDER + "/" + this.applicant.getUsername()));
    }

    /**
     * Add a list of files to this applicant's account.
     * @param filesSubmitted    A list of files to this applicant's account.
     * @return the job application document objects of the files submitted.
     */
    public ArrayList<JobApplicationDocument> addFilesToAccount(ArrayList<File> filesSubmitted) {
        ArrayList<JobApplicationDocument> documentsSubmitted = new ArrayList<>();
        for (File file : filesSubmitted) {
            documentsSubmitted.add(new JobApplicationDocument(file, applicant.getUsername()));
        }
        return documentsSubmitted;
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccountIfInactive(LocalDate today) {
        if (this.applicant.isInactive(today)) {
            JobApplicationManager jobApplicationManager = applicant.getJobApplicationManager();
            JobApplication lastClosedJobApp = jobApplicationManager.getLastClosedJobApp();
            ArrayList<JobApplicationDocument> documents = jobApplicationManager.getFilesSubmittedForApplication(lastClosedJobApp);
            this.removeDocuments(documents);
        }
    }
}
