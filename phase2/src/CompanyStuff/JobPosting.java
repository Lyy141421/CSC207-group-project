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
    private ArrayList<String> tags; // The tags for this job posting
    private int numPositions;
    private Company company; // The company that listed this job posting
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private boolean filled; // Whether the job posting is filled
    private ArrayList<JobApplication> jobApplications = new ArrayList<>(); // The list of job applications for this job posting
    private InterviewManager interviewManager; // UsersAndJobObjects.Interview manager for this job posting


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

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public String getTagsString(){
        return getTags().toString().replace("[", "").replace("]", "");
    }

    public int getNumPositions() {
        return this.numPositions;
    }

    public Company getCompany() {
        return this.company;
    }

    public LocalDate getPostDate() {
        return this.postDate;
    }

    public LocalDate getCloseDate() {
        return this.closeDate;
    }

    public ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public InterviewManager getInterviewManager() {
        return this.interviewManager;
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

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public void setJobApplications(ArrayList<JobApplication> jobApps) {
        this.jobApplications = jobApps;
    }

    public void setFilled() {
        this.filled = true;
    }

    public void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
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
//                    this.setTags((String.valueOf(fields.get(i)))); Todo fix
                }
            } else if (fields.get(i) instanceof Integer && (Integer) fields.get(i) > skipFieldKey) {
                this.setNumPositions((Integer) fields.get(i));
            } else if (fields.get(i) instanceof LocalDate && !((LocalDate) fields.get(i)).isEqual(skipDateKey)) {
                this.setCloseDate((LocalDate) fields.get(i));
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
        s += "Number of positions: " + this.getNumPositions() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Post date: " + this.getPostDate().format(dtf) + "\n";
        s += "Close date: " + this.getCloseDate().format(dtf) + "\n";
        s += "Description: " + this.getDescription() + "\n";
        s += "Tags: " + this.getTagsString() + "\n";
        s += "Company: " + this.getCompany().getName() + "\n";
        s += "Filled: " + (this.isFilled() ? "Yes" : "No"); // "Yes" if posting is filled; "No" otherwise
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Constructor ===
    JobPosting(String title, String field, String description, ArrayList<String> tags, int numPositions, Company company,
               LocalDate postDate, LocalDate closeDate) {
        totalNumOfPostings++;
        this.id = totalNumOfPostings;
        this.title = title;
        this.field = field;
        this.description = description;
        this.tags = tags;
        this.numPositions = numPositions;
        this.company = company;
        this.filled = false;
        this.postDate = postDate;
        this.closeDate = closeDate;
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
