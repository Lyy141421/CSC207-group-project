package GUIClasses;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import Main.JobApplicationSystem;

import java.time.LocalDate;
import java.util.ArrayList;

public class MethodsTheGUICallsInHR {

    // === Instance variables ===
    private JobApplicationSystem jobAppSystem;
    private HRCoordinator hr;

    public MethodsTheGUICallsInHR(JobApplicationSystem jobAppSystem, HRCoordinator hr) {
        this.jobAppSystem = jobAppSystem;
        this.hr = hr;
    }

    public HRCoordinator getHR() {
        return this.hr;
    }

    /**
     * * Get the task that the HR Coordinator must accomplish at this moment for this job posting.
     */
    int getTaskForJobPosting(BranchJobPosting jobPosting) {
        return jobPosting.getInterviewManager().getHrTask();
    }

    /**
     * Gets an array list of branch job postings that are under review for first round of interviews.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToReview() {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsRecentlyClosedForReferences(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of branch job postings that are ready to schedule for next round of interviews.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToSchedule() {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsThatNeedGroupInterviewsScheduled(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of branch job postings that are in hiring stage.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToHire() {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsForHiring(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of all branch job postings.
     *
     * @return the array list of all branch job postings.
     */
    public ArrayList<BranchJobPosting> getAllJP() {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getBranchJobPostings();
    }


    /**
     * Add a job posting for this company.
     * @param jobPostingFields  The fields that the user inputs.
     */
    public void addJobPosting(Object[] jobPostingFields) {
        String title = (String) jobPostingFields[0];
        String field = (String) jobPostingFields[1];
        String description = (String) jobPostingFields[2];
        ArrayList<String> requirements = (ArrayList<String>) jobPostingFields[3];
        ArrayList<String> tags = (ArrayList<String>) jobPostingFields[4];
        int numPositions = (Integer) jobPostingFields[5];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[6];
        LocalDate referenceCloseDate = (LocalDate) jobPostingFields[7];
        this.hr.addJobPosting(title, field, description, requirements, tags, numPositions, jobAppSystem.getToday(), applicationCloseDate,
                referenceCloseDate);
    }

    /**
     * Get all job applications submitted by this applicant with this username.
     * @param applicantUsername The applicant username inputted.
     * @return a list of job applications submitted by this applicant with this username.
     */

    public ArrayList<JobApplication> getAllJobApplicationsToCompany(String applicantUsername) {
        Applicant applicant = (Applicant) jobAppSystem.getUserManager().findUserByUsername(applicantUsername);
        if (applicant == null) {
            return new ArrayList<>();
        }
        return this.hr.getBranch().getCompany().getAllApplicationsToCompany(applicant);
    }

    /**
     * Hire or reject an application.
     *
     * @param jobApp The job application in question.
     * @param toHire Whether or not the HR Coordinator wants to hire the applicant.
     * */

    public boolean hireOrRejectApplication(JobApplication jobApp, boolean toHire) {
        if (toHire) {
            jobApp.getStatus().setHired();
        } else {
            jobApp.getStatus().setArchived();
        }
        BranchJobPosting jobPosting = jobApp.getJobPosting();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        if (jobPosting.getInterviewManager().getNumOpenPositions() > 0) {
            return false;
        }
        return true;
    }

    public boolean hireApplications(ArrayList<JobApplication> jobAppsToHire) {
        for (JobApplication jobApp : jobAppsToHire) {
            jobApp.getStatus().setHired();
        }
        BranchJobPosting jobPosting = jobAppsToHire.get(0).getJobPosting();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        if (jobPosting.getInterviewManager().getNumOpenPositions() > 0) {
            return false;
        }
        return true;
    }

    /* *
     * Checks whether this job application has been rejected.
     *
     * @param jobApplication The job application in question.
     * @return true iff this job application has been rejected.
     */
    public boolean isRejected(JobApplication jobApplication) {
        return jobApplication.getJobPosting().getInterviewManager().getApplicationsRejected().contains(jobApplication);
    }

    /**
     * Choose whether this application moves on for the first round.
     *
     * @param jobApp   The job application in question.
     * @param selected Whether or not the application is selected to move on.
     */
    public void selectApplicationForFirstRound(JobApplication jobApp, boolean selected) {
        if (!selected) {
            jobApp.getJobPosting().getInterviewManager().reject(jobApp);
        }
    }

    public ArrayList<Interviewer> getInterviewersInField(BranchJobPosting jobPosting) {
        return this.hr.getBranch().getFieldToInterviewers().get(jobPosting.getField());
    }

    /**
     * Set up interviews for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param interviewCoordinator The interview coordinator selected
     * @param otherInterviewers The other interviewers selected
     */
    public void setUpGroupInterviews(BranchJobPosting jobPosting, Interviewer interviewCoordinator,
                                     ArrayList<Interviewer> otherInterviewers, int minNumDaysNotice) {
        jobPosting.getInterviewManager().setUpGroupInterview(interviewCoordinator, otherInterviewers,
                jobAppSystem.getToday(), minNumDaysNotice);
    }

}
