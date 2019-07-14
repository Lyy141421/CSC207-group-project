package ApplicantStuff;

import DocumentManagers.UserDocumentManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicantDocumentManager extends UserDocumentManager {

    // === Instance variables ===
    Applicant applicant;

    ApplicantDocumentManager(Applicant applicant) {
        super(applicant);
        this.applicant = applicant;
    }

    ApplicantDocumentManager(Applicant applicant, File folder) {
        super(applicant, folder);
        this.applicant = applicant;
    }

    /**
     * Make subdirectories for this applicant.
     */
    @Override
    public void makeSubDirectories() {
        File CVFolder = new File(this.getFolder().getPath() + File.pathSeparator + "CVs");
        File CoverLetterFolder = new File(this.getFolder().getPath() + File.pathSeparator + "CoverLetters");
        File OtherFileFolder = new File(this.getFolder().getPath() + File.pathSeparator + "Other");
        try {
            CVFolder.createNewFile();
            CoverLetterFolder.createNewFile();
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
    public void removeFilesFromAccount(LocalDate today) {
        if (this.applicant.isInactive(today)) {
            JobApplicationManager JAM = applicant.getJobApplicationManager();
            JobApplication lastClosedJobApp = JAM.getLastClosedJobApp();
            ArrayList<JobApplicationDocument> documents = JAM.getFilesSubmittedForApplication(lastClosedJobApp);
            this.removeDocuments(documents);
        }
    }
}
