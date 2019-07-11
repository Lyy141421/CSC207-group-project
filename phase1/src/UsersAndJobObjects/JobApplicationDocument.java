package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

public class JobApplicationDocument implements Storable {

    // === Class variables ===
    // The total number of job application documents
    private static int totalNumOfDocuments;

    // === Instance variables ===
    // The ID of this document
    private String ID;
    // The contents of this job application document
    private String contents;

    // === Constructor ===
    public JobApplicationDocument(String contents) {
        JobApplicationDocument.totalNumOfDocuments++;
        this.ID = String.valueOf(JobApplicationDocument.totalNumOfDocuments);
        this.contents = contents;
    }

    public JobApplicationDocument(String title, String contents) {
        JobApplicationDocument.totalNumOfDocuments++;
        this.ID = title;
        this.contents = contents;
    }

    // === Getters ===
    public String getId() {
        return this.ID;
    }

    public String getContents() {
        return this.contents;
    }

    // === Other methods ===
    @Override
    public String toString() {
        return this.getContents();
    }
}
