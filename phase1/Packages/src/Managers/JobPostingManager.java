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

    /**
     * Create a new job posting manager for this company.
     *
     * @param company The company that this manager is for.
     */
    public JobPostingManager(Company company) {
        this.company = company;
    }

    /**
     * Create a new job posting manager for this company;
     *
     * @param jobPostings The list of job postings for this company.
     * @param company     The company that this manager is for.
     */
    public JobPostingManager(ArrayList<JobPosting> jobPostings, Company company) {
        this.jobPostings = jobPostings;
        this.company = company;
    }

    // === Getters ===

    /**
     * Get the job postings for positions in this company.
     *
     * @return the list of job postings for positions in this company.
     */
    public ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    /**
     * Get the company that this job posting manager is for.
     *
     * @return the company that this job posting manager is for.
     */
    Company getCompany() {
        return this.company;
    }

    // === Setters ===

    /**
     * Set the job postings for this job posting manager.
     *
     * @param jobPostings The list of job postings to be set.
     */
    void setJobPostings(ArrayList<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

    /**
     * Set the company for this job posting manager.
     *
     * @param company The company to be set.
     */
    void setCompany(Company company) {
        this.company = company;
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
     * Search the job postings by title within this company.
     *
     * @param jobTitle The job title.
     * @return the list of job postings with this job title.
     */
    ArrayList<JobPosting> searchJobPostingByTitle(String jobTitle) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (jobPosting.getTitle().equalsIgnoreCase(jobTitle)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get the job posting from this company with this ID. Return null if not found.
     *
     * @param ID The id of the job posting in question.
     * @return the job posting with this ID or null if not found.
     */
    public JobPosting getJobPosting(int ID) {
        for (JobPosting jobPosting : this.getJobPostings()) {
            if (jobPosting.getId() == ID) {
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
            if (jobPosting.getCloseDate().isAfter(today)) {
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
            if (!jobPosting.isFilled()) {
                LocalDate closeDate = jobPosting.getCloseDate();
                if (closeDate.isEqual(today) || closeDate.isBefore(today)) {
                    jobPostings.add(jobPosting);
                }
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
    public ArrayList<JobPosting> getClosedJobPostingsNoInterview(LocalDate today) {
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for (JobPosting jobPosting : this.getClosedJobPostingsNotFilled(today)) {
            if (jobPosting.getInterviewManager() == null) {
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
            if (jobPosting.getInterviewManager().getHrTask(today) == InterviewManager.SCHEDULE_INTERVIEWS) {
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
            if (jobPosting.getInterviewManager().getHrTask(today) == InterviewManager.HIRE_APPLICANTS) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

}
