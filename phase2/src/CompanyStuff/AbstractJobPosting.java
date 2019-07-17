package CompanyStuff;

import java.util.ArrayList;

public class AbstractJobPosting {

    // === Class variables ===
    // The total number of job postings
    private static int totalNum;

    // === Instance variables ===
    // The id of the job posting
    private int id;
    // The job posting title
    private String title;
    // The job posting field
    private String field;
    // The job posting description
    private String description;
    // The required documents
    private ArrayList<String> requiredDocuments;
    // The job posting tags
    private ArrayList<String> tags;

    AbstractJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                       ArrayList<String> tags) {
        AbstractJobPosting.totalNum++;
        this.id = AbstractJobPosting.totalNum;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requiredDocuments = requiredDocuments;
        this.tags = tags;
    }

    // === Getters ===
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getField() {
        return this.field;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<String> getRequiredDocuments() {
        return this.requiredDocuments;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }
}
