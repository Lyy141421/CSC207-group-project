package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.JobApplicationManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicantDocumentManager extends UserDocumentManager {

    // === Instance variables ===
    private Applicant applicant;

    // === Constructor ===
    ApplicantDocumentManager(Applicant applicant) {
        super(applicant);
        this.applicant = applicant;
        this.makeSubDirectories();
    }

    /**
     * Make subdirectories for this applicant.
     */
    private void makeSubDirectories() {
        File CVFolder = new File(this.getFolder().getPath() + "/CVs");
        File CoverLetterFolder = new File(this.getFolder().getPath() + "/coverLetters");
        File OtherFileFolder = new File(this.getFolder().getPath() + "/other");
        try {
            CVFolder.mkdirs();
            CVFolder.createNewFile();
            CoverLetterFolder.mkdirs();
            CoverLetterFolder.createNewFile();
            OtherFileFolder.mkdirs();
            OtherFileFolder.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
