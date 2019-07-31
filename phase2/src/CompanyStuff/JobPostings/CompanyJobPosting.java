package CompanyStuff.JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import NotificationSystem.Observable;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyJobPosting extends Observable implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // The total number of job postings created
    private static int totalNumOfJobPostings;
    // The list of recommended documents for submission
    public static String[] RECOMMENDED_DOCUMENTS = new String[]{"CV/Resume", "Cover letter", "Reference letter"};
    // The list of recommended tags
    public static String[] RECOMMENDED_TAGS = new String[]{"Computer science", "Bachelor's degree", "Prior work experience",
            "Team-oriented"};

    // === Instance variables ===
    // The job posting id
    protected int id;
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
    // The company that listed this job posting
    private Company company;
    // The branches that list job postings corresponding to this one
    private ArrayList<Branch> branches;

    // === Constructor ===
    public CompanyJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                      ArrayList<String> tags, Company company, Branch branch) {
        totalNumOfJobPostings++;
        this.id = totalNumOfJobPostings;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requiredDocuments = requiredDocuments;
        this.tags = tags;
        this.company = company;
        this.branches = new ArrayList<>();
        this.branches.add(branch);
        this.company.addCompanyJobPosting(this);
    }

    // === Public methods ===

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

    public Company getCompany() {
        return this.company;
    }

    public ArrayList<Branch> getBranches() {return this.branches;}

    // === Other methods ===
    /**
     * Add a branch that has decided to use this job posting.
     *
     * @param branch The branch to be added.
     */
    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public String getTagsString(){
        return tags.toString().replace("[", "").replace("]", "").replace(", ", ";");
    }

    public String getDocsString(){
        return requiredDocuments.toString().replace("[", "").replace("]", "").replace(", ", ";");
    }

    private String getStringForList(ArrayList<String> list) {
        String s = "";
        for (String item : list) {
            s += item + ", ";
        }
        if (!list.isEmpty()) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    public boolean containsTag(String tag){
        return tags.contains(tag);
    }

    public boolean containsTag(ArrayList<String> tags){
        for(String t : tags){
            if(!containsTag(t)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CompanyJobPosting)) {
            return false;
        } else {
            return (this.id == ((CompanyJobPosting) obj).id);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Get a string representation of this job posting.
     *
     * @return a string representation of this job posting.
     */
    @Override
    public String toString() {
        String s = "Job ID: " + this.id + "\n\n";
        s += "Title: " + this.title + "\n\n";
        s += "Field: " + this.field + "\n\n";
        s += "Description: " + this.description + "\n\n";
//        s += "Branches: " + branches + "\n";
        s += "Required Documents: " + this.getStringForList(this.requiredDocuments) + "\n\n";
        s += "Tags: " + this.getStringForList(this.tags) + "\n\n";
        s += "Company: " + this.company.getName() + "\n\n";
        return s;
    }
}
