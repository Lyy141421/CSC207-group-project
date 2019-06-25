import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class JobPosting {

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
    private ArrayList<JobApplication> jobApplications; // The list of job applications for this job posting
    private InterviewManager interviewManager; // Interview manager for this job posting

    // === Constructors ===
    JobPosting() {
    }

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

    // === Getters ===
    int getId() {
        return this.id;
    }

    String getTitle() {
        return this.title;
    }

    String getField() {
        return this.field;
    }

    int getNumPositions() {
        return this.numPositions;
    }

    Company getCompany() {
        return this.company;
    }

    LocalDate getCloseDate() {
        return this.closeDate;
    }

    ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    boolean isFilled() {
        return this.filled;
    }

    InterviewManager getInterviewManager () {
        return this.interviewManager;
    }


    // == Setters ===
    void setFilled() {
        this.filled = true;
    }

    void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }

    void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    // === Other methods ===

    /**
     * Add this job application for this job posting.
     *
     * @param jobApplication The job application to be added.
     */
    void addJobApplication(JobApplication jobApplication) {
        this.jobApplications.add(jobApplication);
    }

    /**
     * Remove this job application for this job posting.
     *
     * @param jobApplication The job application to be removed.
     */
    void removeJobApplication(JobApplication jobApplication) {
        this.jobApplications.remove(jobApplication);
    }

    /**
     * Find the job application associated with this applicant.
     * @param applicant The applicant whose application is to be searched for.
     * @return the application of this applicant or null if not found.
     */
    JobApplication findJobApplication(Applicant applicant) {
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
    void reviewApplications() {
        for (JobApplication jobApp : this.getJobApplications()) {
            jobApp.advanceStatus();
        }
    }

    /**
     * Advance the round of interviews for this job posting.
     *
     * @param today Today's date.
     */
    void advanceInterviewRound(LocalDate today) {
        InterviewManager interviewManager = this.getInterviewManager();
        if (interviewManager.getCurrentRound() < Interview.getMaxNumRounds()) {
            if (interviewManager.currentRoundOver(today)) {
                interviewManager.advanceRound();
            }
        }
    }

    /**
     * Get an array of a list of job applications selected and a list of job applications rejected for a phone
     * interview for this job posting.
     *
     * @return an array of lists of job applications selected and rejected for a phone interview.
     */
    private ArrayList<JobApplication>[] getApplicationsForPhoneInterview() {
        ArrayList<JobApplication>[] jobApps = new ArrayList[2];
        ArrayList<JobApplication> jobApplicationsInConsideration = new ArrayList<>();
        ArrayList<JobApplication> jobApplicationsRejected = new ArrayList<>();
        jobApps[0] = jobApplicationsInConsideration;
        jobApps[1] = jobApplicationsRejected;
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (jobApplication.getStatus() == 0) {
                jobApplicationsInConsideration.add(jobApplication);
            } else {
                jobApplicationsRejected.add(jobApplication);
            }
        }
        return jobApps;
    }

    /**
     * Create an interview manager for this job posting.
     */
    void createInterviewManager() {
        ArrayList<JobApplication>[] jobApps = this.getApplicationsForPhoneInterview();
        InterviewManager interviewManager = new InterviewManager(this, jobApps[0], jobApps[1]);
        this.setInterviewManager(interviewManager);
    }

    /**
     * Get the current round of interviews for this job posting.
     *
     * @return the current round of interviews for this job posting.
     */
    int getCurrentInterviewRound() {
        return this.interviewManager.getCurrentRound();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobPosting)) {
            return false;
        }
        else {
            return this.id == ((JobPosting) obj).getId();
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
