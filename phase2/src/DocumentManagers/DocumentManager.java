package DocumentManagers;

import ApplicantStuff.JobApplicationDocument;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class DocumentManager implements Serializable {
    // The actual folder that contains this user's documents
    private File folder;

    // === Constructors ===
    public DocumentManager() {
    }

    public DocumentManager(File folder) {
        this.folder = folder;
    }

    // === Getters ===
    protected File getFolder() {
        return this.folder;
    }

    // === Setters ===
    void setFolder(File folder) {
        this.folder = folder;
        this.folder.mkdir();
    }

    // === Other methods ===

    /**
     * Remove all documents in this list from the document manager's document list.
     *
     * @param documentList The list of documents to be removed.
     */
    protected void removeDocuments(ArrayList<JobApplicationDocument> documentList) {
        for (JobApplicationDocument document : documentList) {
            document.getFile().delete();
        }
    }
}
