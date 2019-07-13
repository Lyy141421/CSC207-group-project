package Managers;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobApplicationDocument;

import java.time.LocalDate;
import java.util.ArrayList;

public class DocumentManager {
    // List of filenames uploaded to account
    private ArrayList<JobApplicationDocument> documents = new ArrayList<>();
    // The user for which this document manager is for
    private Applicant applicant;

    // === Constructors ===
    public DocumentManager(Applicant applicant) {
        this.applicant = applicant;
    }

    public DocumentManager(Applicant applicant, ArrayList<JobApplicationDocument> documents) {
        this.applicant = applicant;
        this.documents = documents;
    }

    // === Getters ===
    public ArrayList<JobApplicationDocument> getDocuments() {
        return this.documents;
    }

    public JobApplicationDocument getDocumentById(String Id) {
        for (JobApplicationDocument x : getDocuments()) {
            if (x.getId().equals(Id)) {
                return x;
            }
        }
        return null;
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
     * Remove all documents in this list from the document manager's document list.
     *
     * @param documentList The list of documents to be removed.
     */
    public void removeDocuments(ArrayList<JobApplicationDocument> documentList) {
        this.documents.removeAll(documentList);
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccount(LocalDate today) {
        if (this.applicant.isInactive(today)) {
            JobApplicationManager JAM = this.applicant.getJobApplicationManager();
            JobApplication lastClosedJobApp = JAM.getLastClosedJobApp();
            ArrayList<JobApplicationDocument> documents = JAM.getFilesSubmittedForApplication(lastClosedJobApp);
            this.removeDocuments(documents);
        }
    }
}
