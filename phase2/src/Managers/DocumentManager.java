package Managers;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobApplicationDocument;
import UsersAndJobObjects.User;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class DocumentManager implements Serializable {
    // The user for which this document manager is for
    private User user;
    // List of filenames uploaded to account
    private ArrayList<JobApplicationDocument> documents = new ArrayList<>();
    // The folder name
    private String folderName = "phase2/userDocuments/" + this.user.getUsername();
    // The actual folder that contains this user's documents
    private File folder;

    // === Constructors ===
    public DocumentManager(User user) {
        this.user = user;
        this.folder = new File(this.folderName);
        this.folder.mkdir();
    }

    public DocumentManager(User user, ArrayList<JobApplicationDocument> documents, String folderName) {
        this.user = user;
        this.documents = documents;
        this.folderName = folderName;
    }

    // === Getters ===
    public ArrayList<JobApplicationDocument> getDocuments() {
        return this.documents;
    }

    // === Other methods ===

    /**
     * Add the documents from a list if they do not yet exist in this document manager.
     *
     * @param documents The documents to be added.
     */
    public void addDocuments(ArrayList<JobApplicationDocument> documents) {
        for (JobApplicationDocument document : documents) {
            if (!this.documents.contains(document)) {
                this.documents.add(document);
            }
        }
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccount(LocalDate today) {
        if (this.user instanceof Applicant) {
            Applicant applicant = (Applicant) user;
            if (applicant.isInactive(today)) {
                JobApplicationManager JAM = applicant.getJobApplicationManager();
                JobApplication lastClosedJobApp = JAM.getLastClosedJobApp();
                ArrayList<JobApplicationDocument> documents = JAM.getFilesSubmittedForApplication(lastClosedJobApp);
                this.removeDocuments(documents);
            }
        }
    }

    /**
     * Remove all documents in this list from the document manager's document list.
     *
     * @param documentList The list of documents to be removed.
     */
    private void removeDocuments(ArrayList<JobApplicationDocument> documentList) {
        this.documents.removeAll(documentList);
    }
}
