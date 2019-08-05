package CompanyStuff.JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import NotificationSystem.Observable;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CompanyJobPosting extends Observable implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // The total number of job postings created
    private static int totalNumOfJobPostings;
    // The list of recommended documents for submission
    public static String[] RECOMMENDED_DOCUMENTS = new String[]{"CV", "Cover letter", "Reference letter"};
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
    }

    // === Public methods ===

    // === Getters ===
    public static int getTotalNumOfJobPostings() {
        return totalNumOfJobPostings;
    }

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

    public static void setTotalNumOfJobPostings(int totalNum) {
        totalNumOfJobPostings = totalNum;
    }

    // === Other methods ===
    /**
     * Add a branch that has decided to use this job posting.
     *
     * @param branch The branch to be added.
     */
    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public void removeBranch(Branch branch) {
        if (this.branches.contains(branch))
            this.branches.remove(branch);
    }

    public ArrayList<BranchJobPosting> getOpenBranchJobPostingList(LocalDate today) {
        ArrayList<BranchJobPosting> branchJobPostings = new ArrayList<>();
        for (Branch branch : this.getBranches()) {
            for (BranchJobPosting branchJobPosting : branch.getJobPostingManager().getOpenJobPostings(today)) {
                if (branchJobPosting.getCompanyPostingId() == this.id) {
                    branchJobPostings.add(branchJobPosting);
                }
            }
        }
        return branchJobPostings;
    }

    public boolean hasBranchJobPostingInCma(String cma, LocalDate today) {
        for (BranchJobPosting branchJobPosting : this.getOpenBranchJobPostingList(today)) {
            if (branchJobPosting.getBranch().getCma().equals(cma)) {
                return true;
            }
        }
        return false;
    }

    private String getListComplementString(ArrayList<String> actualList, String[] recommendedList) {
        ArrayList<String> listComplement = (ArrayList<String>) actualList.clone();
        for (String item : recommendedList) {
            for (String item2 : actualList) {
                if (item2.equalsIgnoreCase(item)) {
                    listComplement.remove(item2);
                    break;
                }
            }
        }
        return listComplement.toString().replace("[", "").replace("]",
                "").replace(", ", ";");
    }

    public String getTagsStringNoRecommended() {
        return this.getListComplementString(this.getTags(), CompanyJobPosting.RECOMMENDED_TAGS);
    }

    public String getDocsStringNoRecommended() {
        return this.getListComplementString(this.getRequiredDocuments(), CompanyJobPosting.RECOMMENDED_DOCUMENTS);
    }

    private ArrayList<String> getListIntersection(ArrayList<String> actualList, String[] recommendedList) {
        ArrayList<String> listIntersection = new ArrayList<>();
        for (String item : recommendedList) {
            for (String item2 : actualList) {
                if (item.equalsIgnoreCase(item2)) {
                    listIntersection.add(item);
                    break;
                }
            }
        }
        return listIntersection;
    }

    public ArrayList<String> getRecommendedTagsUsed() {
        return this.getListIntersection(this.getTags(), CompanyJobPosting.RECOMMENDED_TAGS);
    }

    public ArrayList<String> getRecommendedDocumentsUsed() {
        return this.getListIntersection(this.getRequiredDocuments(), CompanyJobPosting.RECOMMENDED_DOCUMENTS);
    }

    String getStringForList(ArrayList<String> list) {
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
//        s += "Branches: " + branches + "\n";  // TODO this should be displayed for applicant no?
        s += "Required Documents: " + this.getStringForList(this.requiredDocuments) + "\n\n";
        s += "Tags: " + this.getStringForList(this.tags) + "\n\n";
        s += "Company: " + this.company.getName() + "\n\n";
        return s;
    }
}
