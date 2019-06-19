import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

class HRCoordinator extends User {
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
    HRCoordinator(String username, String password, String legalName, String email, Company company,
                  LocalDate dateCreated) {
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
     * Create a new job posting.
     *
     * @param jobTitle       The job title.
     * @param jobDescription The job description.
     * @param requirements   The list of requirements for this job.
     * @param postDate       The date this job posting was posted.
     * @param closeDate      The date this job posting is closed.
     * @return the job posting created.
     */
    private JobPosting createJobPosting(String jobTitle, String jobField, String jobDescription,
                                        ArrayList<String> requirements, LocalDate postDate, LocalDate closeDate) {
        return new SinglePositionJobPosting(jobTitle, jobField, jobDescription, requirements,
                this.company, postDate, closeDate);
    }

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
        JobPosting jobPosting = this.createJobPosting(jobTitle, jobField, jobDescription, requirements, postDate,
                closeDate);
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
     * View the complete application for this applicant.
     *
     * @param jobPosting The job posting that is being reviewed.
     * @param applicant  The applicant that is being reviewed.
     * @return the application from this applicant for this job posting.
     */
    JobApplication viewApplication(JobPosting jobPosting, Applicant applicant) {
        return jobPosting.findJobApplication(applicant);
    }

    /**
     * View the cover letter from this applicant for this job posting.
     *
     * @param jobPosting The job posting that is being reviewed.
     * @param applicant  The applicant that is being reviewed.
     * @return the cover letter from this applicant for this job posting.
     */
    File viewCoverLetter(JobPosting jobPosting, Applicant applicant) {
        return this.viewApplication(jobPosting, applicant).getCoverLetter();
    }

    /**
     * View the CV submitted by this applicant for this job posting.
     *
     * @param jobPosting The job posting that is being reviewed.
     * @param applicant  The applicant that is being reviewed.
     * @return the applicant's CV for this job posting.
     */
    File viewCV(JobPosting jobPosting, Applicant applicant) {
        return this.viewApplication(jobPosting, applicant).getCV();
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
     * View all the applications for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applications for this job posting.
     */
    ArrayList<JobApplication> viewAllApplications(JobPosting jobPosting) {
        return jobPosting.getApplications();
    }

    /**
     * View all the applicants for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applicants for this job posting.
     */
    ArrayList<Applicant> viewAllApplicants(JobPosting jobPosting) {
        ArrayList<Applicant> applicants = new ArrayList<>();
        for (JobApplication jobApp : this.viewAllApplications(jobPosting)) {
            applicants.add(jobApp.getApplicant());
        }
        return applicants;
    }

    /**
     * View all applications this applicant has submitted for job postings in this company.
     *
     * @param applicant The applicant in question.
     * @return a list of job applications that this applicant has previously submitted to this company.
     */
    ArrayList<JobApplication> viewAllApplicationsToCompany(Applicant applicant) {
        ArrayList<JobApplication> apps = new ArrayList<>();
        for (JobApplication jobApp : applicant.getAllJobApplications()) {
            if (jobApp.getJobPosting().getCompany().equals(this.company)) {
                apps.add(jobApp);
            }
        }
        return apps;
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
     * Advance this application to the next stage of interviews / be hired.
     *
     * @param jobApplication The job application of the applicant to be advanced.
     */
    void advanceApplication(JobApplication jobApplication) {
        jobApplication.advanceStatus();
    }

    /**
     * Create an interviewer for jobs in this field.
     *
     * @param email The interviewer's email.
     * @param field The job field that the interviewer specializes in.
     * @param today Today's date.
     * @return the interviewer created.
     */
    Interviewer createInterviewer(String email, String field, LocalDate today) {
        return new Interviewer(email, this.company, field, today);
    }

    /**
     * Add an interviewer for jobs in this field.
     *
     * @param email The interviewer's email.
     * @param field The job field that the interviewer specializes in.
     * @param today Today's date.
     */
    void addInterviewer(String email, String field, LocalDate today) {
        this.company.addInterviewer(this.createInterviewer(email, field, today));
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
        // Modified the initializer to include the InterviewManager of the posting
        Interview interview = new Interview(jobApplication, interviewer, this, jobPosting.getInterviewManager(), round);
        jobPosting.addInterview(interview);
    }

    /**
     * Hire this applicant for this job position.
     *
     * @param jobPosting The job posting to be filled.
     */
    Applicant hireApplicant(JobPosting jobPosting) {
        this.updateJobPostingStatus(jobPosting);
        return jobPosting.getFinalCandidate();
    }


}
