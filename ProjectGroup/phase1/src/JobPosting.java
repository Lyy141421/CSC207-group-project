import java.time.LocalDate;
import java.util.ArrayList;

abstract class JobPosting {

    // Elaine: I'm adding this so that my code doesn't have red underlines
    // === Instance variables ===
    // Unique identifier for this job posting
    private int ID;
    // The job title
    private String title;
    // The job field
    private String field;
    // The job description
    private String description;
    // The requirements for this job posting
    private ArrayList<String> requirements;
    // The company that listed this job posting
    private Company company;
    // Whether the job posting is filled
    private boolean filled = false;
    // The date on which this job posting was listed
    private LocalDate postDate;
    // The date on which this job posting is closed
    private LocalDate closeDate;
    // The list of job applications
    private ArrayList<JobApplication> jobApplications = new ArrayList<>();

    // === Constructors ===
    JobPosting() {
    }

    JobPosting(String title, String field, String description, ArrayList<String> requirements, LocalDate postDate,
               LocalDate closeDate) {
        this.title = title;
        this.field = field;
        this.description = description;
        this.requirements = requirements;
        this.postDate = postDate;
        this.closeDate = closeDate;
    }

    // === Getters ===
    String getField() {
        return this.field;
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


    // == Setters ===
    void setFilled() {
        this.filled = true;
    }

    // === Other methods ===
    JobApplication findJobApplication(Applicant applicant) {
        return null;
    }

    void addApplication(JobApplication jobApplication) {

    }

    void removeApplication(JobApplication jobApplication) {

    }


}
