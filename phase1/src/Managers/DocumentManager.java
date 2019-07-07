package Managers;

import UsersAndJobObjects.JobApplicationDocument;

import java.util.ArrayList;

public class DocumentManager {
    // List of filenames uploaded to account
    private ArrayList<JobApplicationDocument> documents;

    // === Constructor ===
    public DocumentManager() {}

    public DocumentManager(ArrayList<JobApplicationDocument> documents) {
        this.documents = documents;
    }

    // === Getters ===

    ArrayList<JobApplicationDocument> getDocuments() {
        return this.documents;
    }

    // === Setters ===

    void setDocuments(ArrayList<JobApplicationDocument> documents) {
        this.documents = documents;
    }

    // === Other methods ===

    /**
     * Add a document to this account.
     *
     * @param document The document to be added.
     */
    void addDocument(JobApplicationDocument document) {
        this.documents.add(document);
    }

    /**
     * Add the documents from a list if they do not yet exist in this document manager.
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
     * @param documentList  The list of documents to be removed.
     */
    public void removeDocuments(ArrayList<JobApplicationDocument> documentList) {
        this.documents.removeAll(documentList);
    }
}
