import java.time.LocalDate;

abstract class JobPosting {

    // === Instance variables ===
    // Unique identifier for this job posting
    private int ID;
    // The job title
    private String title;
    // The job field
    private String field;
    // The job description
    private String description;
    // The company that listed this job posting
    private Company company;
    // The date on which this job posting was listed
    private LocalDate postDate;
    // The date on which this job posting is closed
    private LocalDate closeDate;

}
