package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.InterviewManager;
import NotificationSystem.Notification;
import NotificationSystem.Observable;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPosting extends CompanyJobPosting implements Observable, Serializable {

    // === Instance variables ===
    private int numPositions;
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate applicantCloseDate; // The date on which this job posting is closed for further applications
    private LocalDate referenceCloseDate;  //   The date on which this job posting is closed for reference letter submission
    private boolean filled; // Whether the job posting is filled
    private Branch branch; // The branch that listed this job posting
    private ArrayList<JobApplication> jobApplications; // The list of applications for this job posting
    private InterviewManager interviewManager; // Interview manager for this job posting
    private ArrayList<Observer> observer_list = new ArrayList<>(); // A list of observers for pushing notifications to

    // === Constructor ===
    public BranchJobPosting(String title, String field, String description, ArrayList<String> requiredDocuments,
                            ArrayList<String> tags, int numPositions, Branch branch,
                            LocalDate postDate, LocalDate applicantCloseDate, LocalDate referenceCloseDate) {
        super(title, field, description, requiredDocuments, tags, branch.getCompany(), branch);
        this.numPositions = numPositions;
        this.branch = branch;
        this.postDate = postDate;
        this.applicantCloseDate = applicantCloseDate;
        this.referenceCloseDate = referenceCloseDate;
        this.filled = false;
        this.jobApplications = new ArrayList<>();
        branch.getJobPostingManager().addJobPosting(this);
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

    public LocalDate getApplicantCloseDate() {
        return this.applicantCloseDate;
    }

    public LocalDate getReferenceCloseDate() {
        return this.referenceCloseDate;
    }

    public boolean isFilled() {
        return this.filled;
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
     * Check whether this job postings has had any applications submitted.
     *
     * @return true iff this job posting has at least one application submitted.
     */
    public boolean hasNoApplicationsSubmitted() {
        return this.jobApplications.isEmpty();
    }

    /**
     * Check whether this job posting has closed for further applications and updates the company document manager.
     * @param today Today's date.
     * @return true iff the application submission date has passed.
     */
    public boolean isClosedForApplications(LocalDate today) {
        boolean closed = this.applicantCloseDate.isBefore(today);
        if (closed) {
            setChanged();
            notifyObservers();
        }
        return closed;
    }

    /**
     * Check whether this job posting has closed for reference letter submission.
     *
     * @param today Today's date.
     * @return true iff the reference letter submission date has passed.
     */
    public boolean isClosedForReferences(LocalDate today) {
        return this.referenceCloseDate.isBefore(today);
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
     * Create an interview manager for this job posting.
     */
    public void createInterviewManager() {
        InterviewManager interviewManager = new InterviewManager(this,
                (ArrayList<JobApplication>) this.getJobApplications().clone());
        this.setInterviewManager(interviewManager);
    }

    /**
     * Check whether this job posting has had any interviews.
     *
     * @return true iff this job posting has had an interview.
     */
    public boolean hasInterviews() {
        for (JobApplication jobApp : this.getJobApplications()) {
            if (!jobApp.getInterviews().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advance the round of interviews for this job posting.
     */
    public void advanceInterviewRound() {
        if (interviewManager == null) {
            return;
        }
        InterviewManager interviewManager = this.getInterviewManager();
        if (interviewManager.getCurrentRound() < interviewManager.getMaxNumberOfRounds()) {
            if (interviewManager.isCurrentRoundOver()) {
                interviewManager.advanceRound();
            }
        }
    }

    /**
     * Get a list of emails of applicants rejected.
     *
     * @return a list of emails of applicants rejected.
     */
    public ArrayList<String> getEmailsForRejectList() {
        ArrayList<String> emails = new ArrayList<>();
        for (JobApplication jobApp : this.jobApplications) {
            emails.add(jobApp.getApplicant().getEmail());
        }
        return emails;
    }


    /**
     * Remove this job application for this job posting.
     *
     * @param jobApplication The job application to be removed.
     */
    void removeJobApplication(JobApplication jobApplication) {
        this.jobApplications.remove(jobApplication);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BranchJobPosting)) {
            return false;
        } else {
            BranchJobPosting other = (BranchJobPosting) obj;
            return (branch.getCompany().equals(other.getCompany()) && id == other.id);
        }
    }

    // === Observable Methods ===

    public void attach(Observer observer){
        observer_list.add(observer);
    }

    public void detach(Observer observer){
        observer_list.remove(observer);
    }

    public void notifyAllObservers(Notification notification){
        for (Observer observer : observer_list){
            notifyObserver(observer, notification);
        }
    }

    public void notifyObserver(Observer observer, Notification notification){
        observer.update(notification);
    }
}
