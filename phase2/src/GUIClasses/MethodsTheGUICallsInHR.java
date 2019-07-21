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
import java.util.HashMap;

public class MethodsTheGUICallsInHR {

    // === Instance variables ===
    private JobApplicationSystem jobAppSystem;
    private HRCoordinator hr;

    MethodsTheGUICallsInHR(JobApplicationSystem jobAppSystem, HRCoordinator hr) {
        this.jobAppSystem = jobAppSystem;
        this.hr = hr;
    }

    /**
     * * Get the task that the HR Coordinator must accomplish at this moment for this job posting.
     */
    int getTaskForJobPosting(BranchJobPosting jobPosting) {
        return jobPosting.getInterviewManager().getHrTask();
    }

    /**
     * Gets a hash map of titles to branch job postings that are under review for first round of interviews.
     * @param today Today's date.
     * @return the hash map of titles to branch job postings.
     */
    public HashMap<String, BranchJobPosting> getJPToReview(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        ArrayList<BranchJobPosting> JPToReview = JPManager.getClosedJobPostingsNoApplicantsChosen(today);

        return this.getTitleToJPMap(JPToReview);
    }

    /**
     * Gets a hash map of titles to branch job postings that are ready to schedule for next round of interviews.
     * @param today Today's date.
     * @return the hash map of titles to branch job postings.
     */
    public HashMap<String, BranchJobPosting> getJPToSchedule(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        ArrayList<BranchJobPosting> JPToSchedule = JPManager.getJobPostingsWithRoundCompletedNotForHire(today);

        return this.getTitleToJPMap(JPToSchedule);
    }

    /**
     * Gets a hash map of titles to branch job postings that are in hiring stage.
     * @param today Today's date.
     * @return the hash map of titles to branch job postings.
     */
    public HashMap<String, BranchJobPosting> getJPToHire(LocalDate today) {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        ArrayList<BranchJobPosting> JPToHire = JPManager.getJobPostingsForHiring(today);

        return this.getTitleToJPMap(JPToHire);
    }

    /**
     * Gets a hash map of titles to all branch job postings.
     *
     * @return the hash map of titles to branch job postings.
     */
    public HashMap<String, BranchJobPosting> getAllJP() {
        BranchJobPostingManager JPManager = this.hr.getBranch().getJobPostingManager();
        ArrayList<BranchJobPosting> allJP = JPManager.getBranchJobPostings();

        return this.getTitleToJPMap(allJP);
    }

    /**
     * Gets a hash map of titles to branch job postings from a list of job postings.
     * @param JPList a list of job postings.
     * @return the hash map of titles to branch job postings.
     */
    private HashMap<String, BranchJobPosting> getTitleToJPMap(ArrayList<BranchJobPosting> JPList) {
        HashMap<String, BranchJobPosting> titleToJPMap = new HashMap<>();
        for (BranchJobPosting JP : JPList) {
            titleToJPMap.put(this.toJPTitle(JP), JP);
        }

        return titleToJPMap;
    }

    /**
     * Gets a string representation of the title of this branch job posting.
     * @param branchJobPosting a branch job posting.
     * @return the title to be displayed of this branch job posting.
     */
    private String toJPTitle(BranchJobPosting branchJobPosting) {
        return branchJobPosting.getId() + "-" + branchJobPosting.getTitle();
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
