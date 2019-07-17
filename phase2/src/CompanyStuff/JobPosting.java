package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JobPosting implements Serializable {

    // === Instance variables ===
    private static int totalNumOfPostings; // Total number of postings created
    private int id; // Unique identifier for this job posting
    private String title; // The job title
    private String field; // The job field
    private String description; // The job description
    private String requirements; // The requirements for this job posting
    private ArrayList<Branch> branches; // The branches that have branch job postings matching this one


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

    public String getRequirements() {
        return this.requirements;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    // === Setters ===
    public void setTitle(String title) {
        this.title = title;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    // === Other methods ===

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
                    this.setRequirements((String.valueOf(fields.get(i))));
                }
            } else if (fields.get(i) instanceof Integer && (Integer) fields.get(i) > skipFieldKey) {
                this.setNumPositions((Integer) fields.get(i));
            } else if (fields.get(i) instanceof LocalDate && !((LocalDate) fields.get(i)).isEqual(skipDateKey)) {
                this.setCloseDate((LocalDate) fields.get(i));
            }
        }
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobPosting)) {
            return false;
        } else {
            return (this.getId() == ((JobPosting) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        return this.id;
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
        s += "Requirements: " + this.getRequirements() + "\n";
        s += "Branches: " + branches + "\n";
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Constructor ===
    JobPosting(String title, String field, String description, String requirements, ArrayList<Branch> branches) {
        totalNumOfPostings++;
        this.id = totalNumOfPostings;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requirements = requirements;
        this.branches = branches;
    }

    // === Other methods ===

    /**
     * Remove this job application for this job posting.
     *
     * @param jobApplication The job application to be removed.
     */
    void removeJobApplication(JobApplication jobApplication) {
        this.jobApplications.remove(jobApplication);
    }
}
