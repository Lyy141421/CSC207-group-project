package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPosting extends JobPosting{

    private int numPositions;
    private Branch branch; // The branch that listed this job posting
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private boolean filled; // Whether the job posting is filled
    private ArrayList<JobApplication> jobApplications; // The list of applications for this job posting
    private InterviewManager interviewManager; // UsersAndJobObjects.Interview manager for this job posting

    BranchJobPosting(JobPosting posting, int numPositions, Branch branch, LocalDate postDate, LocalDate closeDate) {
        super(posting.getTitle(), posting.getField(), posting.getDescription(), posting.getTags(),
                branch);
        this.numPositions = numPositions;
        this.branch = branch;
        this.postDate = postDate;
        this.closeDate = closeDate;
        this.filled = false;
        this.jobApplications = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public Branch getBranch() {
        return branch;
    }

    public int getNumPositions() {
        return numPositions;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public boolean isFilled() {
        return filled;
    }

    public ArrayList<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public InterviewManager getInterviewManager() {
        return interviewManager;
    }

    /**
     * Update the fields for this job posting.
     *
     * @param skipFieldKey The key that represents if the field should not be updated.
     * @param skipDateKey  The key that represents if the date should not be updated.
     * @param fields       The list of fields to be updated.
     */
    public void updateFields(int skipFieldKey, LocalDate skipDateKey, ArrayList<Object> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i) instanceof String && !(fields.get(i).equals(String.valueOf(skipFieldKey)))) {
                if (i == 0) {
                    this.setTitle((String.valueOf(fields.get(i))));
                } else if (i == 1) {
                    this.setField((String.valueOf(fields.get(i))));
                } else if (i == 2) {
                    this.setDescription((String.valueOf(fields.get(i))));
                } else if (i == 3) {
//                    this.setTags((String.valueOf(fields.get(i)))); Todo fix
                }
            } else if (fields.get(i) instanceof Integer && (Integer) fields.get(i) > skipFieldKey) {
                this.numPositions = ((Integer) fields.get(i));
            } else if (fields.get(i) instanceof LocalDate && !((LocalDate) fields.get(i)).isEqual(skipDateKey)) {
                this.closeDate = ((LocalDate) fields.get(i));
            }
        }
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

    /**
     * Check whether this job postings has had any applications submitted.
     *
     * @return true iff this job posting has at least one application submitted.
     */
    public boolean hasNoApplicationsSubmitted() {
        return this.jobApplications.isEmpty();
    }

    /**
     * Check whether this job posting has closed.
     */
    public boolean isClosed(LocalDate today) {
        return this.closeDate.isBefore(today);
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
        this.interviewManager = new InterviewManager(this, jobApplications);
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
        if (interviewManager.getCurrentRound() < Interview.MAX_NUM_ROUNDS) {
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

}
