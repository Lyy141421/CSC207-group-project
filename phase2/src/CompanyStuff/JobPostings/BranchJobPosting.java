package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.InterviewManager;
import Main.JobApplicationSystem;
import NotificationSystem.NotificationFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPosting extends CompanyJobPosting implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Category labels for Reference
    public static final String[] CATEGORY_LABELS_FOR_REFERENCE = new String[]{"Title", "Field", "Description",
            "Company/Branch", "Reference deadline"};
    // Category labels for HR
    public static final String[] CATEGORY_LABELS_FOR_HR = new String[]{"Title", "Field"};

    // === Instance variables ===
    private int companyPostingId;
    private int numPositions;
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed for further applications
    private boolean filled; // Whether the job posting is filled
    private Branch branch; // The branch that listed this job posting
    private ArrayList<JobApplication> jobApplications; // The list of applications for this job posting
    private InterviewManager interviewManager; // Interview manager for this job posting

    // === Representation invariants ===
    // References can only submit reference letters after the applicant close date and before the reference close date

    // === Constructor ===
    public BranchJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                            ArrayList<String> tags, int numPositions, Branch branch,
                            LocalDate postDate, LocalDate closeDate, int companyJobPostingId) {
        super(title, field, description, requiredDocuments, tags, branch.getCompany());
        this.numPositions = numPositions;
        this.branch = branch;
        this.postDate = postDate;
        this.closeDate = closeDate;
        this.filled = false;
        this.jobApplications = new ArrayList<>();
        this.companyPostingId = companyJobPostingId;
    }

    // === Getters ===
    public int getNumPositions() {
        return this.numPositions;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    public InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public LocalDate getCloseDate() {
        return this.closeDate;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public int getCompanyPostingId() {
        return this.companyPostingId;
    }

    // === Setters ===
    public void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    public void setFilled() {
        this.filled = true;
    }

    // === Other methods ===

    /**
     * Update the fields of this job posting.
     * @param numPositions  The number of positions for this job posting.
     * @param applicationCloseDate  The application close date.
     */
    public void updateFields(int numPositions, LocalDate applicationCloseDate) {
        this.numPositions = numPositions;
        this.closeDate = applicationCloseDate;
    }

    /**
     * Check whether this job posting has had any applications submitted.
     *
     * @return true iff this job posting has at least one application submitted.
     */
    public boolean hasNoApplicationsSubmitted() {
        return this.jobApplications.isEmpty();
    }

    /**
     * Check whether this job posting has closed for further applications.
     * @param today Today's date.
     * @return true iff the application submission date has passed.
     */
    public boolean isClosed(LocalDate today) {
        return this.closeDate.isBefore(today);
    }

    /**
     * Get all the references for this job posting.
     *
     * @return a list of all the references for this job posting.
     */
    ArrayList<Reference> getAllReferences() {
        ArrayList<Reference> references = new ArrayList<>();
        for (JobApplication jobApp : this.jobApplications) {
            references.addAll(jobApp.getReferences());
        }
        return references;
    }

    /**
     * Add this job application for this job posting.
     *
     * @param jobApplication The job application to be added.
     */
    public void addJobApplication(JobApplication jobApplication) {
        this.jobApplications.add(jobApplication);
    }

    /**
     * Find the job application associated with this applicant.
     *
     * @param applicant The applicant whose application is to be searched for.
     * @return the application of this applicant or null if not found.
     */
    public JobApplication findJobApplication(Applicant applicant) {
        for (JobApplication jobApp : this.jobApplications) {
            if (jobApp.getApplicant().equals(applicant)) {
                return jobApp;
            }
        }
        return null;
    }

    /**
     * Review the applications submitted for this job posting.
     */
    public void reviewJobApplications() {
        for (JobApplication jobApp : this.getJobApplications()) {
            jobApp.getStatus().advanceStatus();
        }
    }

    /**
     * Create an interview manager for this job posting after this branch posting has been closed for further applications.
     */
    public void createInterviewManager() {
        InterviewManager interviewManager = new InterviewManager(this,
                (ArrayList<JobApplication>) this.getJobApplications().clone());
        this.interviewManager = interviewManager;
    }

    /**
     * Advance the round of interviews for this job posting.
     */
    public void advanceInterviewRound() {
        if (this.interviewManager == null || !interviewManager.hasChosenApplicantsForFirstRound()) {
            return;
        }
        if (this.interviewManager.interviewProcessHasBegun() && this.interviewManager.getCurrentRound() < this.interviewManager.getFinalRoundNumber()) {
            if (this.interviewManager.currentRoundIsOver()) {
                this.interviewManager.advanceRound();
                this.notifyAllObservers(new NotificationFactory().createNotification(NotificationFactory.ADVANCE_ROUND, this));
            }
        }
    }

    /**
     * Remove this job application for this job posting.
     *
     * @param jobApplication The job application to be removed.
     */
    public void removeJobApplication(JobApplication jobApplication) {
        this.jobApplications.remove(jobApplication);
    }

    /**
     * Close the job posting with no applications in consideration.
     */
    public void closeJobPostingNoApplicationsInConsideration() {
        this.setFilled();
        this.numPositions = 0;
    }

    /**
     * Get the category values for this job posting for a reference.
     *
     * @return a list of category values for this job posting for a reference.
     */
    public String[] getCategoryValuesForHR() {
        return new String[]{this.getTitle(), this.getField()};
    }

    /**
     * Get the category values for this job posting for a reference.
     *
     * @return a list of category values for this job posting for a reference.
     */
    public String[] getCategoryValuesForReference() {
        return new String[]{this.getTitle(), this.getField(), this.getDescription(), this.getBranch().getName(),
                this.getCloseDate().toString()};
    }

    /**
     * Get a string representation of this job posting.
     *
     * @return a string representation of this job posting.
     */
    @Override
    public String toString() {
        String s = "Job ID: " + this.id + "\n\n";
        s += "Title: " + this.getTitle() + "\n\n";
        s += "Field: " + this.getField() + "\n\n";
        s += "Description: " + this.getDescription() + "\n\n";
        s += "Number of positions: " + this.getNumPositions() + "\n\n";
        s += "Filled: " + (this.isFilled()?"Yes":"No") + "\n\n";
        s += "Required Documents: " + this.getStringForList(this.getRequiredDocuments()) + "\n\n";
        s += "Tags: " + this.getStringForList(this.getTags()) + "\n\n";
        s += "Close date: " + this.getCloseDate().toString() + "\n\n";
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BranchJobPosting)) {
            return false;
        } else {
            BranchJobPosting other = (BranchJobPosting) obj;
            return id == other.id;
        }
    }

    // === Observable Methods ===

    @Override
    public void updateObserverList(){
        for (JobApplication job_application : this.interviewManager.getApplicationsInConsideration()){
            this.attach(job_application.getApplicant());
        }
        for (JobApplication job_application : this.interviewManager.getApplicationsRejected()){
            this.detach(job_application.getApplicant());
        }
    }
}
