package UsersAndJobObjects;

import FileLoadingAndStoring.FileSystem;
import GUIClasses.HRCoordinatorInterface;
import Managers.InterviewManager;

import java.time.LocalDate;
import java.util.HashMap;

public class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "HRCoordinators";

    // === Instance variables ===
    // The company that this HR Coordinator works for
    private Company company;


    // === Public methods ===
    // === Constructors ===

    public HRCoordinator(){}

    public HRCoordinator(String id) {
        this.setUsername(id);
    }

    public HRCoordinator(String username, String password, String legalName, String email, Company company,
                  LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.company = company;
    }

    // === Getters ===

    public Company getCompany() {
        return this.company;
    }

    // === Setters ===

    public void setCompany(Company company) {
        this.company = company;
    }

    // === Other methods ===
    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle       The job title.
     * @param jobDescription The job description.
     * @param requirements   The requirements for this job.
     * @param numPositions   The number of positions for this job.
     * @param postDate       The date this job posting was posted.
     * @param closeDate      The date this job posting is closed.
     */
    public void addJobPosting(String jobTitle, String jobField, String jobDescription, String requirements,
                              int numPositions, LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new JobPosting(jobTitle, jobField, jobDescription, requirements,
                numPositions, this.company, postDate, closeDate);
        this.company.getJobPostingManager().addJobPosting(jobPosting);
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    /**
     * Get the task that the HR Coordinator must accomplish for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param today      Today's date.
     * @return an integer that represents the task that the HR Coordinator must accomplish for this job posting.
     */
    int getTask(JobPosting jobPosting, LocalDate today) {
        if (this.company.getJobPostingManager().getClosedJobPostingsNoInterview(today).contains(jobPosting)) {
            return InterviewManager.SELECT_APPS_FOR_PHONE_INTERVIEW;
        } else {
            return jobPosting.getInterviewManager().getHrTask(today);
        }
    }
}
