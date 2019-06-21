import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

abstract class JobPosting {

    // === Instance variables ===
    private static int postingsCreated; // Total number of postings created
    private int id; // Unique identifier for this job posting
    private String title; // The job title
    private String field; // The job field
    private String description; // The job description
    private ArrayList<String> requirements; // The requirements for this job posting
    private Company company; // The company that listed this job posting
    private boolean filled; // Whether the job posting is filled
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private HashMap<Applicant,JobApplication> appsByApplicant = new HashMap<>(); // Map of applicants to applications
    private InterviewManager interviewManager; // Interview manager for this job posting

    // === Constructors ===
    JobPosting() {
    }

    JobPosting(String title, String field, String description, ArrayList<String> requirements, Company company,
               LocalDate postDate, LocalDate closeDate) {
        postingsCreated++;
        this.id = postingsCreated;
        this.title = title;
        this.field = field;
        this.description = description;
        this.requirements = requirements;
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

    Company getCompany() {
        return this.company;
    }

    LocalDate getCloseDate() {
        return this.closeDate;
    }

    HashMap<Applicant, JobApplication> getAppsByApplicant() {
        return this.appsByApplicant;
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

    void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    void setInterviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
    }

    // === Other methods ===
    void addJobApplication(Applicant applicant, JobApplication jobApplication) {
    }

    void removeJobApplication(Applicant applicant) {
    }

    abstract JobApplication findJobApplication(Applicant applicant);

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
