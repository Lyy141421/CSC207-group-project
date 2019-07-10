package Managers;

import UsersAndJobObjects.JobPosting;
import UsersAndJobObjects.Company;
import java.time.LocalDate;
import java.util.ArrayList;

public class JobPostingManager {
    /**
     * A class that manages the job postings for each company
     */

    // === Instance variables ===
    // A list of job postings for positions in this company.
    private ArrayList<JobPosting> jobPostings = new ArrayList<>();
    // The company that this job posting manager is for
    private Company company;


    // === Public methods ===

    // === Constructors ===
    public JobPostingManager(Company company) {
        this.company = company;
    }

    public JobPostingManager(ArrayList<JobPosting> jobPostings, Company company) {
        this.jobPostings = jobPostings;
        this.company = company;
    }

    // === Getters ===
    public ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    // === Other methods ===
    /**
     * Add a job posting to this company.
     *
     * @param jobPosting The job posting to be added.
     */
    public void addJobPosting(JobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }

    /**
     * Remove a job posting from this company.
     *
     * @param jobPosting The job posting to be removed.
     */
    public void removeJobPosting(JobPosting jobPosting) {
        this.jobPostings.remove(jobPosting);
    }

    /**
     * Get the job posting from this company with this ID. Return null if not found.
     *
     * @param ID The id of the job posting in question.
     * @return the job posting with this ID or null if not found.
     */
    public JobPosting getJobPosting(int ID) {
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (Integer.valueOf(jobPosting.getId()) == ID) {
                return jobPosting;
            }
        }
        return null;

    }

    /**
     * Get a list of open job postings for this company.
     *
     * @param today Today's date.
     * @return a list of open job postings for this company.
     */
    public ArrayList<JobPosting> getOpenJobPostings(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (!jobPosting.isClosed(today)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of closed job postings for this company that have not yet been filled.
     *
     * @param today Today's date.
     * @return a list of closed job postings for this company that have not yet been filled.
     */
    public ArrayList<JobPosting> getClosedJobPostingsNotFilled(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (!jobPosting.isFilled() && jobPosting.isClosed(today)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings for this company that have recently closed and have not yet been reviewed for interviews.
     *
     * @param today Today's date.
     * @return a list of closed job postings that have not yet started the interview process.
     */
    public ArrayList<JobPosting> getClosedJobPostingsNoApplicantsChosen(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() == null) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings with no applications in consideration.
     *
     * @param today Today's date.
     * @return a list of job postings with no applications in consideration.
     */
    public ArrayList<JobPosting> getClosedJobPostingsNoApplicationsInConsideration(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() != null &&
                    jobPosting.getInterviewManager().getHrTask() == InterviewManager.CLOSE_POSTING_NO_HIRE) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that have closed with no applications submitted.
     *
     * @param today Today's date
     * @return a list of job postings with no applications.
     */
    public ArrayList<JobPosting> getClosedJobPostingsNoApplicationsSubmitted(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.hasNoApplicationsSubmitted()) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of all filled job postings at this company.
     *
     * @return a list of filled job postings at this company.
     */
    public ArrayList<JobPosting> getFilledJobPostings() {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.jobPostings) {
            if (jobPosting.isFilled()) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings where a current interview round is over.
     *
     * @param today Today's date/
     * @return a list of job postings where a current interview round is over.
     */
    public ArrayList<JobPosting> getJobPostingsWithRoundCompletedNotForHire(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.SCHEDULE_INTERVIEWS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get a list of job postings that are ready for the final hiring step.
     *
     * @param today Today's date.
     * @return a list of job postings that are ready for the final hiring step.
     */
    public ArrayList<JobPosting> getJobPostingsForHiring(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager().getHrTask() == InterviewManager.HIRE_APPLICANTS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Getters ===
    Company getCompany() {
        return this.company;
    }

    // === Setters ===
    void setCompany(Company company) {
        this.company = company;
    }
}
