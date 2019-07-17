package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JobPosting implements Serializable {

    // === Instance variables ===
    private static int totalNumOfPostings; // Total number of postings created
    private int id; // Unique identifier for this job posting
    private String title; // The job title
    private String field; // The job field
    private String description; // The job description
    private ArrayList<String> tags; // The tags for this job posting
    private Company company; // The company that listed this job posting
    private ArrayList<Branch> branches; // The branches that have branch job postings matching this one


    // === Public methods ===
    // === Constructor ===
    public JobPosting(String title, String field, String description, ArrayList<String> tags, Branch branch) {
        totalNumOfPostings++;
        this.id = totalNumOfPostings;
        this.title = title;
        this.field = field;
        this.description = description;
        this.tags = tags;
        this.company = branch.getCompany();
        this.branches = new ArrayList<>();
        this.branches.add(branch);
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

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public String getTagsString() {
        return getTags().toString().replace("[", "").replace("]", "");
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    // === Setters ===
    public void setTitle(String title) {
        this.title = title;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    // === Other methods ===

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobPosting)) {
            return false;
        } else {
            return (this.getId() == ((JobPosting) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Get a string representation of this job posting for standard input.
     *
     * @return a string representation of this job posting for standard input.
     */
    @Override
    public String toString() {
        String s = "Job ID: " + this.getId() + "\n";
        s += "Title: " + this.getTitle() + "\n";
        s += "Field: " + this.getField() + "\n";
        s += "Description: " + this.getDescription() + "\n";
        s += "Branches: " + branches + "\n";
        s += "Tags: " + this.getTagsString() + "\n";
        s += "Company: " + this.company.getName() + "\n";
        return s;
    }
}
