package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

public class JobApplicationDocument implements Storable {

    // === Class variables ===
    // The total number of job application documents
    private static int total;

    // === Instance variables ===
    // The ID of this document
    private String ID;
    // The contents of this job application document
    private String contents;

    // === Constructor ===
    public JobApplicationDocument(String contents) {
        this.ID = String.valueOf(JobApplicationDocument.total);
        this.contents = contents;
        JobApplicationDocument.total++;
    }

    public JobApplicationDocument(String title, String contents) {
        this.ID = title;
        this.contents = contents;
        JobApplicationDocument.total++;
    }

    // === Getters ===
    public String getId() {
        return this.ID;
    }

    public String getContents() {
        return this.contents;
    }

    // === Setters ===
    public void setContents(String contents) {
        this.contents = contents;
    }




}
