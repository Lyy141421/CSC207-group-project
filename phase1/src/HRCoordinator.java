import java.lang.reflect.Array;
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
                this.company, postDate, closeDate);
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
     * View all the applications for this job posting.
     *
     * @param jobPosting The job posting to be viewed.
     * @return a list of applications for this job posting.
     */
    ArrayList<JobApplication> viewAllApplications(JobPosting jobPosting) {
        return (ArrayList)(jobPosting.getAppsByApplicant().values());
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
        for (JobApplication jobApp : jobPosting.getAppsByApplicant().values()) {
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
     * Get an array of a list of job applications selected and a list of job applications rejected for a phone
     * interview for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return an array of lists of job applications selected and rejected for a phone interview.
     */
    private ArrayList[] getApplicationsForPhoneInterview(JobPosting jobPosting) {
        ArrayList[] jobApps = new ArrayList[2];
        ArrayList<JobApplication> jobApplicationsInConsideration = new ArrayList<>();
        ArrayList<JobApplication> jobApplicationsRejected = new ArrayList<>();
        jobApps[0] = jobApplicationsInConsideration;
        jobApps[1] = jobApplicationsRejected;
        for (JobApplication jobApplication : jobPosting.getAppsByApplicant().values()) {
            if (jobApplication.getStatus() == 0) {
                jobApplicationsInConsideration.add(jobApplication);
            } else {
                jobApplicationsRejected.add(jobApplication);
            }
        }
        return jobApps;
    }

    /**
     * Create and add an interviewer for jobs in this field.
     *
     * @param email The interviewer's email.
     * @param field The job field that the interviewer specializes in.
     * @param today Today's date.
     */
    void addInterviewer(String email, String field, LocalDate today) {
        this.company.addInterviewer(new Interviewer(email, this.company, field, today));
    }

    /**
     * Create an interview manager for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    void createInterviewManager(JobPosting jobPosting) {
        ArrayList<JobApplication>[] jobApps = this.getApplicationsForPhoneInterview(jobPosting);
        InterviewManager interviewManager = new InterviewManager(jobPosting, jobApps[0], jobApps[1]);
        jobPosting.setInterviewManager(interviewManager);
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
        Interview interview = new Interview(jobApplication, interviewer, this,
                jobPosting.getInterviewManager(), round);
        jobApplication.addInterview(interview);
        interviewer.addInterview(interview);
    }

    /**
     * Advance the round of interviews for this job posting.
     *
     * @param today      Today's date.
     * @param jobPosting The job posting in question.
     */

    void advanceInterviewRound(LocalDate today, JobPosting jobPosting) {
        InterviewManager interviewManager = jobPosting.getInterviewManager();
        if (interviewManager.getApplicationsInConsideration().size() == 1)
            hireApplicant(interviewManager.getApplicationsInConsideration().get(0).getApplicant(), jobPosting);
        else if (interviewManager.getCurrentRound() == Interview.getMaxNumRounds()) {
            chooseFromApplications(interviewManager.getApplicationsInConsideration(), jobPosting);
        }
        else {
            if (interviewManager.currentRoundOver(today)) {
                interviewManager.advanceRound();
            }
        }
    }

    /**
     * Choose one applicant to hire based on their applications.
     *
     * @param applications The applications in consideration.
     * @param jobPosting The job posting to be filled.
     */

    void chooseFromApplications(ArrayList<JobApplication> applications, JobPosting jobPosting) {
        // TODO
        hireApplicant(null, jobPosting);
    }

    /**
     * Hire this applicant for this job position.
     *
     * @param applicant The applicant to be hired.
     * @param jobPosting The job posting to be filled.
     */

    Applicant hireApplicant(Applicant applicant, JobPosting jobPosting) {
        this.updateJobPostingStatus(jobPosting);
        return applicant;
    }


}
