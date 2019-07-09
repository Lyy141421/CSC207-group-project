package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;
import Managers.InterviewManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class JobPosting implements Storable {

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "JobPostings";


    // === Instance variables ===
    private static int postingsCreated; // Total number of postings created
    private int id; // Unique identifier for this job posting
    private String title; // The job title
    private String field; // The job field
    private String description; // The job description
    private String requirements; // The requirements for this job posting
    private int numPositions;
    private Company company; // The company that listed this job posting
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private boolean filled; // Whether the job posting is filled
    private ArrayList<JobApplication> jobApplications = new ArrayList<>(); // The list of job applications for this job posting
    private InterviewManager interviewManager; // UsersAndJobObjects.Interview manager for this job posting


    // === Public methods ===
    // === Constructor ===

    /**
     * Constructor from memory
     *
     * @param id The id of this object which it is saved under
     */
    public JobPosting(String id){
        this.id = Integer.parseInt(id);
        JobPosting.postingsCreated = Integer.max(this.id, JobPosting.postingsCreated);
    }

    // === Getters ===

    public String getId() {
        return Integer.toString(this.id);
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

    public InterviewManager getInterviewManager () {
        return this.interviewManager;
    }

    // == Setters ===
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
                    this.setRequirements((String.valueOf(fields.get(i))));
                }
            } else if (fields.get(i) instanceof Integer && !fields.get(i).equals(skipFieldKey)) {
                this.setNumPositions((Integer) fields.get(i));
            } else if (fields.get(i) instanceof LocalDate && !((LocalDate) fields.get(i)).isEqual(skipDateKey)) {
                this.setCloseDate((LocalDate) fields.get(i));
            }
        }
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
    public void reviewApplications() {
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
     * @return  true iff this job posting has had an interview.
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
     *
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobPosting)) {
            return false;
        } else {
            return (this.getId()).equals(((JobPosting) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Get a string representation of this job posting.
     *
     * @return a string representation of this job posting.
     */
    @Override
    public String toString() {
        String s = "Number of positions: " + this.getNumPositions() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Post date: " + this.getPostDate().format(dtf) + "\n";
        s += "Close date: " + this.getCloseDate().format(dtf) + "\n";
        s += "Description: " + this.getDescription() + "\n";
        s += "Requirements: " + this.getRequirements() + "\n";
        return s;
    }

    /**
     * Get a string representation of this job posting for standard input.
     *
     * @return a string representation of this job posting for standard input.
     */
    public String toStringStandardInput() {
        String s = "Job ID: " + this.getId() + "\n";
        s += "Title: " + this.getTitle() + "\n";
        s += "Field: " + this.getField() + "\n";
        s += "Number of positions: " + this.getNumPositions() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Post date: " + this.getPostDate().format(dtf) + "\n";
        s += "Close date: " + this.getCloseDate().format(dtf) + "\n";
        s += "Description: " + this.getDescription() + "\n";
        s += "Requirements: " + this.getRequirements() + "\n";
        s += "Company: " + this.getCompany().getName() + "\n";
        s += "Filled: " + this.booleanToString(this.isFilled()) + "\n";
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Constructors ===
    JobPosting(String title, String field, String description, String requirements, int numPositions, Company company,
               LocalDate postDate, LocalDate closeDate) {
        postingsCreated++;
        this.id = postingsCreated;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requirements = requirements;
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

    // === Private methods ===

    /**
     * Returns 'yes' or 'no' based on if bool is True/False
     *
     * @param bool The boolean being converted to a user-friendly string.
     * @return 'yes' or 'no' based on value of bool.
     */
    private String booleanToString(boolean bool) {
        if (bool) {
            return "Yes";
        } else {
            return "No";
        }
    }
}
