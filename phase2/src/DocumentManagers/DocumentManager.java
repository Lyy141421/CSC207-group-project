package DocumentManagers;

import ApplicantStuff.JobApplicationDocument;
import Main.User;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class DocumentManager<T> implements Serializable {
    // The initial path
    static String INITIAL_PATH = "phase2/uploadedDocuments";

    // The company or user that this document manager is for
    private T object;
    // The actual folder that contains the company's/user's documents
    private File folder;

    // === Constructors ===
    public DocumentManager(T object) {
        this.object = object;
    }

    public DocumentManager(T object, File folder) {
        this.object = object;
        this.folder = folder;
    }

    // === Getters ===
    protected T getObject() {
        return this.object;
    }

    File getFolder() {
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
    void removeDocuments(ArrayList<JobApplicationDocument> documentList) {
        for (JobApplicationDocument document : documentList) {
            document.getFile().delete();
        }
    }
}
