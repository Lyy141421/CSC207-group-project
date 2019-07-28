package CompanyStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Branch implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;

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
    private BranchJobPostingManager jobPostingManager;

    // === Public methods ===
    // === Constructor ===
    public Branch(String name, String cma, Company company) {
        this.name = name;
        this.cma = cma;
        this.company = company;
        this.hrCoordinators = new ArrayList<>();
        this.fieldToInterviewers = new HashMap<>();
        this.jobPostingManager = new BranchJobPostingManager(this);
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

    // === Other methods ===
    public BranchJobPosting getBranchJobPosting(int id) {
        for (BranchJobPosting posting : this.jobPostingManager.getBranchJobPostings()) {
            if (posting.getId() == id)
                return posting;
        }
        return null;
    }

    /**
     * Adds the HR Coordinator to the company.
     *
     * @param hrCoordinator The HR Coordinator to be added.
     */
    void addHRCoordinator(HRCoordinator hrCoordinator) {
        this.hrCoordinators.add(hrCoordinator);
    }

    /**
     * Check whether this job field has interviewers.
     *
     * @param field The field in question.
     * @return true iff this company has an interviewer or this job field.
     */
    public boolean hasInterviewerForField(String field) {
        return this.getFieldToInterviewers().keySet().contains(field);
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    void addInterviewer(Interviewer interviewer) {
        String field = interviewer.getField();
        if (!this.fieldToInterviewers.containsKey(field)) {
            this.fieldToInterviewers.put(field, new ArrayList<>());
        }
        this.fieldToInterviewers.get(field).add(interviewer);
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
            sum += name.charAt(i);
        return sum;
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Other methods ===

    /**
     * Find the interviewer with the least amount of interviews in this job field.
     *
     * @param jobField The job field of the interviewer to be found.
     * @return the interviewer with the least amount of interviews in this field.
     */
    Interviewer findInterviewerByField(String jobField) {
        Interviewer interviewerSoFar = this.fieldToInterviewers.get(jobField).get(0);
        int minNumberOfInterviews = interviewerSoFar.getInterviews().size();
        for (Interviewer interviewer : this.fieldToInterviewers.get(jobField)) {
            int numberOfInterviews = interviewer.getInterviews().size();
            if (numberOfInterviews <= minNumberOfInterviews) {
                interviewerSoFar = interviewer;
                minNumberOfInterviews = numberOfInterviews;
            }
        }
        return interviewerSoFar;
    }
}
