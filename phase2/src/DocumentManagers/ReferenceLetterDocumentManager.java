package DocumentManagers;

import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;

import java.io.File;
import java.util.ArrayList;

public class ReferenceLetterDocumentManager extends DocumentManager {

    // === Instance variables ===
    public static String FOLDER_PATH = DocumentManager.INITIAL_PATH + "/referenceLetters";
    private JobApplication jobApp;

    // === Constructor ===
    public ReferenceLetterDocumentManager(JobApplication jobApp) {
        this.jobApp = jobApp;
        this.setFolder(new File(FOLDER_PATH + "/" + this.jobApp.getApplicant().getUsername() + "_" +
                this.jobApp.getJobPosting().getId()));
    }

    /**
     * Add a list of files to this applicant's account.
     *
     * @param referenceLetters A list of reference letters for this job application.
     * @return the job application document objects of the files submitted.
     */
    public ArrayList<JobApplicationDocument> addFileToFolder(ArrayList<File> referenceLetters) {
        ArrayList<JobApplicationDocument> documentsSubmitted = new ArrayList<>();
        for (File file : referenceLetters) {
            documentsSubmitted.add(new JobApplicationDocument(file, this.getFolder()));
        }
        return documentsSubmitted;
    }

}
