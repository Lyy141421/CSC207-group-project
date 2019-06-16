import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

class HRCoordinator extends UserAccount {
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
    HRCoordinator() {
    }

    /**
     * Create an HR Coordinator account.
     *
     * @param username    The HR Coordinator's account username.
     * @param password    The HR Coordinator's account password.
     * @param legalName   The HR Coordinator's legal name.
     * @param email       The HR Coordinator's email.
     * @param company     The company that this HR Coordinator works for.
     * @param dateCreated The date this account was created.
     */
    HRCoordinator(String username, String password, String legalName, String email, Company company, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
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
    void addJobPosting(String jobTitle, String jobField, String jobDescription, ArrayList<String> requirements,
                       LocalDate postDate, LocalDate closeDate) {
        JobPosting jobPosting = new SinglePositionJobPosting(jobTitle, jobField, jobDescription, requirements,
                postDate, closeDate);
        this.company.addJobPosting(jobPosting);
    }

    /**
     * Update the status of this job posting to filled.
     *
     * @param jobPosting The job posting to be updated.
     */
    void updateJobPostingStatus(JobPosting jobPosting) {
        jobPosting.setFilled();
    }

    /**
     * View all the applications for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applications for this job posting.
     */
    ArrayList<JobApplication> viewAllJobApplications(JobPosting jobPosting) {
        return jobPosting.getApplications();
    }

    /**
     * View the cover letter from this applicant for this job posting.
     *
     * @param jobPosting The job posting that is being reviewed.
     * @param applicant  The applicant that is being reviewed.
     * @return the cover letter from this applicant for this job posting.
     */
    File viewCoverLetter(JobPosting jobPosting, Applicant applicant) {
        return jobPosting.findApplication(applicant).getCoverLetter();
    }

    /**
     * View the CV submitted by this applicant for this job posting.
     *
     * @param jobPosting The job posting that is being reviewed.
     * @param applicant  The applicant that is being reviewed.
     * @return the applicant's CV for this job posting.
     */
    File viewCV(JobPosting jobPosting, Applicant applicant) {
        return jobPosting.findApplication(applicant).getCV();
    }

//    /**
//     * View the applicant's reference letters for this job posting.
//     * @param jobPosting    The job posting that is being reviewed.
//     * @param applicant     The applicant that is being reviewed.
//     * @return  a list of the applicant's reference letters for this job posting.
//     */
//    ArrayList<File> viewReferenceLetters(JobPosting jobPosting, Applicant applicant) {
//        return jobPosting.findApplication(applicant).getReferenceLetters();
//    }

    /**
     * View all the applicants for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applicants for this job posting.
     */
    ArrayList<Applicant> viewAllApplicants(JobPosting jobPosting) {
        ArrayList<Applicant> applicants = new ArrayList<>();
        for (JobApplication jobApp : this.viewAllJobApplications(jobPosting)) {
            applicants.add(jobApp.getApplicant());
        }
        return applicants;
    }

    /**
     * View all previous applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    ArrayList<JobApplication> viewPreviousApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> previousApps = new ArrayList<>();
        for (JobApplication jobApp : applicant.getJobApplications()) {
            if (jobApp.getJobPosting().getCompany().equals(this.company)) {
                previousApps.add(jobApp);
            }
        }
        return previousApps;
    }

    /**
     * Review the applications submitted for this job posting.
     *
     * @param jobPosting The job posting to be reviewed.
     */
    void reviewApplications(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getApplications()) {
            jobApp.advanceStatus();
        }
    }

    /**
     * Advance this applicant to receive a phone interview / get hired.
     *
     * @param jobApplication The job application of the applicant to be advanced.
     * @return the applicant that will advance to the phone interview stage or be hired.
     */
    Applicant advanceApplicant(JobApplication jobApplication) {
        jobApplication.advanceStatus();
        return jobApplication.getApplicant();
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param jobApplication The job application of the applicant.
     * @param round          The interview round.
     */
    void setUpInterview(JobApplication jobApplication, int round) {
        JobPosting jobPosting = jobApplication.getJobPosting();
        String jobField = jobPosting.getField();
        Interviewer interviewer = this.company.findInterviewer(jobField);
        Interview interview = new Interview(jobApplication, interviewer, this, round);
        this.company.addInterview(jobPosting, interview);
    }

    /**
     * Hire this applicant for this job position.
     *
     * @param jobPosting The job posting to be filled.
     */
    Applicant hireApplicant(JobPosting jobPosting) {
        this.updateJobPostingStatus(jobPosting);
        return this.company.getFinalCandidate(jobPosting);
    }


}
