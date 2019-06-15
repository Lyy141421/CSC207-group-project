import java.time.LocalDate;
import java.util.ArrayList;

class HRCoordinatorAccount extends UserAccount {
    /**
     * An account for an HR Coordinator.
     */

    // === Instance variables ===
    // The company that this HR Coordinator works for
    private Company company;

    // === Constructors ===

    /**
     * Create an HR Coordinator account.
     */
    HRCoordinatorAccount() {
    }

    /**
     * Create an HR Coordinator account.
     *
     * @param legalName   The HR Coordinator's legal name.
     * @param username    The HR Coordinator's account username.
     * @param password    The HR Coordinator's account password.
     * @param company     The company that this HR Coordinator works for.
     * @param dateCreated The date this account was created.
     */
    HRCoordinatorAccount(String legalName, String username, String password, Company company, LocalDate dateCreated) {
        super(legalName, username, password, dateCreated);
        this.company = company;
    }

    // === Getters ===

    /**
     * Get the company that this HR Coordinator works for.
     *
     * @return the company that this HR Coordinator works for.
     */
    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    /**
     * Set the company that this HR Coordinator works for.
     */
    void setCompany(Company company) {
        this.company = company;
    }

    // === Other methods ===

    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle       The job title.
     * @param jobDescription The job description.
     * @param requirements   The list of requirements for this job.
     * @param postDate       The date this job posting was posted.
     * @param closeDate      The date this job posting is closed.
     */
    void addJobPosting(String jobTitle, String jobDescription, ArrayList<Requirement> requirements,
                       LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new JobPosting(jobTitle, jobDescription, requirements, postDate, closeDate);
        this.company.addJobPosting(jobPosting);
    }

    /**
     * Update the status of this job posting.
     *
     * @param jobPosting The job posting to be updated.
     * @param status     The new status of this posting.
     */
    void updateJobPostingStatus(JobPosting jobPosting, String status) {
        jobPosting.setStatus(status);
    }

    /**
     * View the applications for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applications for this job posting.
     */
    ArrayList<JobApplication> viewJobApplications(JobPosting jobPosting) {
        return jobPosting.getJobApplications();
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param jobApplication The job application of the applicant.
     */
    void setUpInterview(JobApplication jobApplication, int round) {
        String jobField = jobApplication.getJobPosting().getField();
        InterviewerAccount interviewer = this.company.findInterviewer(jobField);
        LocalDate interviewTime = interviewer.getAvailableTime();
        Interview interview = new Interview(jobApplication, interviewer, this, interviewTime, round);
        this.company.getInterviewManager().addInterview(interview);
    }

}
