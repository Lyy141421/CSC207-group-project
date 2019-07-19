package JobPostings;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CompanyJobPosting implements Serializable {

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
    private Company company; // The company that listed this job posting
    private ArrayList<BranchJobPosting> branchJobPostings;  // The list of branch job postings listed under this posting

    // === Constructor ===
    CompanyJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                      ArrayList<String> tags, Company company) {
        CompanyJobPosting.totalNum++;
        this.id = CompanyJobPosting.totalNum;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requiredDocuments = requiredDocuments;
        this.tags = tags;
        this.company = company;
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

    public ArrayList<BranchJobPosting> getBranchJobPostings() {
        return this.branchJobPostings;
    }

    // === Other methods ===

    public ArrayList<JobApplication> getJobApplications() {
        ArrayList<JobApplication> jobApps = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            jobApps.addAll(branchJobPosting.getJobApplications());
        }
        return jobApps;
    }

    public BranchJobPosting getBranchJobPosting(Branch branch) {
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            if (branchJobPosting.getBranch().equals(branch)) {
                return branchJobPosting;
            }
        }
        return null;
    }

    /**
     * Add a branch job posting.
     *
     * @param branchJobPosting The branch job posting to be added.
     */
    public void addBranchJobPosting(BranchJobPosting branchJobPosting) {
        this.branchJobPostings.add(branchJobPosting);
    }

    /**
     * Check whether this job posting is closed.
     *
     * @param today Today's date.
     * @return true iff this job posting is closed, ie all the branch job postings listed are closed.
     */
    public boolean isClosed(LocalDate today) {
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            if (!branchJobPosting.isClosed(today)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether this job posting is filled.
     *
     * @return true iff this job posting is filled, ie, all the branch job postings listed are filled.
     */
    public boolean isFilled() {
        for (BranchJobPosting branchJobPosting : this.branchJobPostings) {
            if (!branchJobPosting.isFilled()) {
                return false;
            }
        }
        return true;
    }


//    /**
//     * Update the fields for this job posting.
//     *
//     * @param skipFieldKey The key that represents if the field should not be updated.
//     * @param skipDateKey  The key that represents if the date should not be updated.
//     * @param fields       The list of fields to be updated.
//     */
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
        return getTags().toString().replace("[", "").replace("]", "");
    }

    public boolean containsTag(String tag){
        return getTags().contains(tag);
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
            return (this.getId() == ((CompanyJobPosting) obj).getId());
        }
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
        //s += "Branches: " + branches + "\n";
        s += "Tags: " + this.getTagsString() + "\n";
        s += "Company: " + this.company.getName() + "\n";
        return s;
    }
}
