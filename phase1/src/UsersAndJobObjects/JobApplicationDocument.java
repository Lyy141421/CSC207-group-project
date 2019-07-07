package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

public class JobApplicationDocument implements Storable {

    // === Instance variables ===
    // The ID of this document
    private String ID;
    // The contents of this job application document
    private String contents;

    // === Constructor ===
    public JobApplicationDocument(String title, String contents) {
        this.ID = title;
        this.contents = contents;
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
