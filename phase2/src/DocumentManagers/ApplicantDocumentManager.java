package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.JobApplicationManager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantDocumentManager extends DocumentManager {

    // === Instance variables ===
    public static String FOLDER_PATH = DocumentManager.INITIAL_PATH + "/users";
    private Applicant applicant;

    // === Constructor ===
    public ApplicantDocumentManager(Applicant applicant) {
        this.applicant = applicant;
        this.setFolder(new File(FOLDER_PATH + "/" + this.applicant.getUsername()));
    }

    /**
     * Check whether this account contains this file.
     *
     * @param file The file in question.
     * @return true iff this user document folder contains this file.
     */
    public boolean containsFile(File file) {
        for (File fileInAccount : this.getFolder().listFiles()) {
            if (file.equals(fileInAccount)) {
                return true;
            }
        }
        return false;
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
     * Add a list of files to this applicant's account.
     *
     * @param fileTypeToTextEntries A hashmap of file type to text entry to be added to this applicant's account.
     * @return the job application document objects of the files submitted.
     */
    public ArrayList<JobApplicationDocument> addTextEntriesToAccount(HashMap<String, String> fileTypeToTextEntries) {
        ArrayList<JobApplicationDocument> documentsSubmitted = new ArrayList<>();
        for (String fileType : fileTypeToTextEntries.keySet()) {
            documentsSubmitted.add(new JobApplicationDocument(fileTypeToTextEntries.get(fileType),
                    fileType, applicant.getUsername()));
        }
        return documentsSubmitted;
    }

    /**
     * Remove the files submitted to all applications when the last closed application was at least 30 days ago.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccountIfInactive(LocalDate today) {
        if (this.applicant.isInactive(today)) {
            for (File file : this.getFolder().listFiles()) {
                file.delete();
            }
        }
    }
}
