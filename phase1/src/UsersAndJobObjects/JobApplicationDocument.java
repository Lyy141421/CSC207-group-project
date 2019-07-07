package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

public class JobApplicationDocument implements Storable {

    // === Class variables ===
    // The total number of documents
    static int totalNum;

    // === Instance variables ===
    // The ID of this document
    private int ID;
    // The contents of this job application document
    private String contents;

    // === Constructor ===
    public JobApplicationDocument(String contents) {
        this.ID = JobApplicationDocument.totalNum;
        this.contents = contents;
        JobApplicationDocument.totalNum ++;
    }

    // === Getters ===
    public String getId() {
        return String.valueOf(this.ID);
    }

    public String getContents() {
        return this.contents;
    }

    // === Setters ===
    public void setContents(String contents) {
        this.contents = contents;
    }




}
