package JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.Company;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyJobPosting implements Serializable {

    // === Class variables ===
    // The total number of CompanyJobPostings created
    private static int totalNumOfCompanyJobPostings;

    // === Instance variables ===
    // The job posting id
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
    // The company that listed this job posting
    private Company company;
    // The branches that list job postings corresponding to this one
    private ArrayList<Branch> branches;

    // === Constructor ===
    CompanyJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                      ArrayList<String> tags, Company company, Branch branch) {
        totalNumOfCompanyJobPostings++;
        this.id = totalNumOfCompanyJobPostings;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requiredDocuments = requiredDocuments;
        this.tags = tags;
        this.company = company;
        this.branches = new ArrayList<>();
        this.branches.add(branch);
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

//    public void updateFields(int skipFieldKey, LocalDate skipDateKey, ArrayList<Object> fields) {
//        for (int i = 0; i < fields.size(); i++) {
//            if (fields.get(i) instanceof String && !(fields.get(i).equals(String.valueOf(skipFieldKey)))) {
//                if (i == 0) {
//                    this.setTitle((String.valueOf(fields.get(i))));
//                } else if (i == 1) {
//                    this.setField((String.valueOf(fields.get(i))));
//                } else if (i == 2) {
//                    this.setDescription((String.valueOf(fields.get(i))));
//                } else if (i == 3) {
////                    this.setTags((String.valueOf(fields.get(i)))); Todo fix
//                }
//            } else if (fields.get(i) instanceof Integer && (Integer) fields.get(i) > skipFieldKey) {
//                this.setNumPositions((Integer) fields.get(i));
//            } else if (fields.get(i) instanceof LocalDate && !((LocalDate) fields.get(i)).isEqual(skipDateKey)) {
//                this.setCloseDate((LocalDate) fields.get(i));
//            }
//        }
//    }

    public String getTagsString(){
        return tags.toString().replace("[", "").replace("]", "");
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

    /**
     * Get a string representation of this job posting for standard input.
     *
     * @return a string representation of this job posting for standard input.
     */
    @Override
    public String toString() {
        String s = "Job ID: " + this.id + "\n";
        s += "Title: " + this.title + "\n";
        s += "Field: " + this.field + "\n";
        s += "Description: " + this.description + "\n";
        //s += "Branches: " + branches + "\n";
        s += "Tags: " + this.getTagsString() + "\n";
        s += "Company: " + this.company.getName() + "\n";
        return s;
    }
}
