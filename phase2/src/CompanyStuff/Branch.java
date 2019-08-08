package CompanyStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import Main.JobApplicationSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Branch implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 2L;

    // === Instance variables ===
    // The name of this branch
    private String name;
    // The Census Metropolitan Area or Census Agglomeration that this branch is assumed to fall into
    private String cma;
    // The company that runs this branch
    private Company company;
    // The HR Coordinators for this branch
    private ArrayList<HRCoordinator> hrCoordinators;
    // The interviewers in this branch
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers;
    // The branch job posting manager for this branch
    private volatile BranchJobPostingManager jobPostingManager;

    // === Public methods ===
    // === Constructor ===
    public Branch(String name, String cma, Company company) {
        this.name = name;
        this.cma = cma;
        this.company = company;
        this.hrCoordinators = new ArrayList<>();
        this.fieldToInterviewers = new HashMap<>();
    }

    // === Getters ==
    public String getId() {
        return getName();
    }

    public String getName() {
        return this.name;
    }

    public String getCma() {
        return cma;
    }

    public Company getCompany() {
        return company;
    }

    public ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    public HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    public BranchJobPostingManager getJobPostingManager() {
        return this.jobPostingManager;
    }

    public Interviewer getInterviewer(Interviewer interviewer) {
        for (String field : this.fieldToInterviewers.keySet()) {
            for (Interviewer interviewer1 : this.fieldToInterviewers.get(field)) {
                if (interviewer1.equals(interviewer)) {
                    return interviewer1;
                }
            }
        }
        return null;
    }

    // === Setter ===
    public void setJobPostingManager(BranchJobPostingManager jobPostingManager) {
        this.jobPostingManager = jobPostingManager;
    }

    public void addJobPostingManager() {
        jobPostingManager = new BranchJobPostingManager(this);
    }

    public void setFieldToInterviewers(HashMap<String, ArrayList<Interviewer>> fieldToInterviewers) {
        this.fieldToInterviewers = fieldToInterviewers;
    }

    // === Other methods ===

    String formatCase(String s) {
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            String firstChar = words[i].substring(0, 1);
            String rest = "";
            if (words[i].length() > 1) {
                rest = words[i].substring(1);
            }
            words[i] = firstChar.toUpperCase() + rest.toLowerCase();
        }
        String res = "";
        for (String word : words) {
            res += " " + word;
        }
        return res.substring(1);
    }

    /**
     * Adds the HR Coordinator to the company.
     *
     * @param hrCoordinator The HR Coordinator to be added.
     */
    public void addHRCoordinator(HRCoordinator hrCoordinator) {
        this.hrCoordinators.add(hrCoordinator);
    }

    /**
     * Check whether this job field has interviewers.
     *
     * @param field The field in question.
     * @return true iff this company has an interviewer or this job field.
     */
    public boolean hasInterviewerForField(String field) {
        for (String fieldName : this.fieldToInterviewers.keySet()) {
            if (fieldName.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    public void addInterviewer(Interviewer interviewer) {
        String field = interviewer.getField();
        for (String fieldName : this.fieldToInterviewers.keySet()) {
            if (field.equalsIgnoreCase(fieldName)) {
                this.fieldToInterviewers.get(formatCase(fieldName)).add(interviewer);
            }
        }
        this.fieldToInterviewers.put(formatCase(field), new ArrayList<>());
        this.fieldToInterviewers.get(formatCase(field)).add(interviewer);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Branch))
            return false;
        else
            return this.company.equals(((Branch) obj).company) && this.name.equalsIgnoreCase(((Branch) obj).name);
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < name.length(); i++)
            sum += name.toLowerCase().charAt(i);
        return sum;
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Other methods ===

    /**
     * Find the interviewer with the least amount of interviews in this job field. Return null if there are no interviewers
     * in that field.
     *
     * @param jobField The job field of the interviewer to be found.
     * @return the interviewer with the least amount of interviews in this field.
     * Precondition: field exists in interviewer map
     */
    Interviewer findInterviewerByField(String jobField) {
        String fieldInMap = "";
        for (String field : this.fieldToInterviewers.keySet()) {
            if (field.equalsIgnoreCase(jobField)) {
                fieldInMap = field;
            }
        }
        Interviewer interviewerSoFar = this.fieldToInterviewers.get(fieldInMap).get(0);
        int minNumberOfInterviews = interviewerSoFar.getInterviews().size();
        for (Interviewer interviewer : this.fieldToInterviewers.get(fieldInMap)) {
            int numberOfInterviews = interviewer.getInterviews().size();
            if (numberOfInterviews <= minNumberOfInterviews) {
                interviewerSoFar = interviewer;
                minNumberOfInterviews = numberOfInterviews;
            }
        }
        return interviewerSoFar;
    }
}