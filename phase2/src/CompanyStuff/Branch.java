package CompanyStuff;

import ApplicantStuff.JobApplication;
import DocumentManagers.BranchDocumentManager;
import DocumentManagers.DocumentManagerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Branch implements Serializable {

    // === Instance variables ===
    // The name of this branch
    private String name;
    // The company that runs this branch
    private Company company;
    // The HR Coordinators for this company
    private ArrayList<HRCoordinator> hrCoordinators;
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers;
    // The branch job posting manager for this branch
    private BranchJobPostingManager branchJobPostingManager;
    // The document manager for this company
    private BranchDocumentManager documentManager;

    // === Public methods ===
    // === Constructors ===
    public Branch(String name) {
        this.name = name;
        this.hrCoordinators = new ArrayList<>();
        this.fieldToInterviewers = new HashMap<>();
        this.branchJobPostingManager = new BranchJobPostingManager(this);
        this.documentManager = (BranchDocumentManager) new DocumentManagerFactory().createDocumentManager(this);
    }

    // === Getters ==
    public String getId() {
        return getName();
    }

    public String getName() {
        return this.name;
    }

    public Company getCompany() {
        return company;
    }

    public ArrayList<HRCoordinator> getHrCoordinators() {
        return this.hrCoordinators;
    }

    HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    public BranchJobPostingManager getBranchJobPostingManager() {
        return branchJobPostingManager;
    }

    public BranchDocumentManager getDocumentManager() {
        return this.documentManager;
    }

    // === Other methods ===

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
        return this.getFieldToInterviewers().keySet().contains(field);
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    public void addInterviewer(Interviewer interviewer) {
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

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param round The interview round.
     */
    // TODO Interview factory?
    public void setUpInterview(JobApplication jobApplication, int round) {
        BranchJobPosting posting = jobApplication.getBranchJobPosting();
        Interviewer interviewer = this.findInterviewerByField(posting.getField());
        Interview interview = new Interview(jobApplication, posting.getInterviewManager());
        // TODO make this action observable and notify jobApplication and interviewer
        jobApplication.addInterview(interview);
        interviewer.addInterview(interview);
        jobApplication.getStatus().advanceStatus();
    }
}
