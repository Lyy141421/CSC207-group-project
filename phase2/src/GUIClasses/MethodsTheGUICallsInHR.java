package GUIClasses;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.HRCoordinator;
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
     * @param today Today's date.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToReview(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsRecentlyClosedForReferences(today);
    }

    /**
     * Gets an array list of branch job postings that are ready to schedule for next round of interviews.
     * @param today Today's date.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToSchedule(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsThatNeedGroupInterviewsScheduled(today);
    }

    /**
     * Gets an array list of branch job postings that are in hiring stage.
     * @param today Today's date.
     * @return the array list of branch job postings.
     */
    public ArrayList<BranchJobPosting> getJPToHire(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        return JPManager.getJobPostingsForHiring(today);
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
     * @param today  Today's date.
     * @param jobPostingFields  The fields that the user inputs.
     */
    public void addJobPosting(LocalDate today, Object[] jobPostingFields) {
        String title = (String) jobPostingFields[0];
        String field = (String) jobPostingFields[1];
        String description = (String) jobPostingFields[2];
        ArrayList<String> requirements = (ArrayList<String>) jobPostingFields[3];
        ArrayList<String> tags = (ArrayList<String>) jobPostingFields[4];
        int numPositions = (Integer) jobPostingFields[5];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[6];
        LocalDate referenceCloseDate = (LocalDate) jobPostingFields[7];
        this.hr.addJobPosting(title, field, description, requirements, tags, numPositions, today, applicationCloseDate,
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
     * Choose whether this application moves on for phone interviews.
     *
     * @param jobApp   The job application in question.
     * @param selected Whether or not the application is selected to move on.
     */
    public void selectApplicationForPhoneInterview(JobApplication jobApp, boolean selected) {
        if (!selected) {
            jobApp.getJobPosting().getInterviewManager().reject(jobApp);
        }
    }

    /**
     * Set up interviews for this job posting.
     *
     * @param jobPosting The job posting in question.
     */
    public boolean setUpInterviews(BranchJobPosting jobPosting) {
        Branch branch = jobPosting.getBranch();
        String field = jobPosting.getField();
        if (!branch.hasInterviewerForField(field)) {
            return false;
        } else {
            jobPosting.getInterviewManager().setUpOneOnOneInterviews();
            return true;
        }
    }

}
