package DocumentManagers;
import ApplicantStuff.JobApplicationDocument;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class DocumentManager implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // The initial path
    static String INITIAL_PATH = "./uploadedDocuments";

    // === Instance variables ===
    // The actual folder that contains the company's/user's documents
    private File folder;

    // === Getters ===
    public File getFolder() {
        return this.folder;
    }

    // === Setters ===
    void setFolder(File folder) {
        this.folder = folder;
        this.folder.mkdirs();
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
