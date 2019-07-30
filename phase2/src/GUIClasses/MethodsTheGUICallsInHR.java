package GUIClasses;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobApplicationGrader;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Main.JobApplicationSystem;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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

    public LocalDate getToday() {
        return this.jobAppSystem.getToday();
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
        return JPManager.getJobPostingsThatNeedHRSelectionForHiring(this.jobAppSystem.getToday());
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
     * @param mandatoryFields  The fields that must be entered regardless of method of adding JP.
     * @param defaultFields The fields that are set by default in company posting mode.
     */
    public void addJobPosting(Object[] mandatoryFields, String[] defaultFields) {
        String title = defaultFields[0];
        String field = defaultFields[1];
        String description = defaultFields[2];
        ArrayList<String> requirements = new ArrayList<>(Arrays.asList(defaultFields[3].split(";")));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(defaultFields[4].split(";")));
        int numPositions = (Integer) mandatoryFields[0];
        LocalDate applicationCloseDate = (LocalDate) mandatoryFields[1];
        //TODO: number of reference is at index 2 of mandatoryFields
        LocalDate referenceCloseDate = (LocalDate) mandatoryFields[3];
        this.hr.addJobPosting(title, field, description, requirements, tags, numPositions, jobAppSystem.getToday(), applicationCloseDate,
                referenceCloseDate);
    }

    public void implementJobPosting(CompanyJobPosting cjp, Object[] jobPostingFields) {
        int numPositions = (int) jobPostingFields[0];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[1];
        //TODO: num of reference is at index 2.
        LocalDate referenceCloseDate = (LocalDate) jobPostingFields[3];
        this.hr.implementJobPosting(cjp, numPositions, jobAppSystem.getToday(), applicationCloseDate, referenceCloseDate);
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

    /**
     * Get a list of job applications sorted in non-decreasing order based on the number of occurrences of key words and phrases.
     *
     * @param jobPosting         The job posting that has recently closed.
     * @param keyWordsAndPhrases The key words and phrases that the HR Coordinator has inputted.
     * @return
     */
    public ArrayList<JobApplication> getJobApplicationInNonDecreasingOrder(BranchJobPosting jobPosting, ArrayList<String> keyWordsAndPhrases) {
        JobApplicationGrader jobAppGrader = new JobApplicationGrader(jobPosting, keyWordsAndPhrases);
        return jobAppGrader.getSortedJobApps();
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
     * Reject the list of applications for first round.
     *
     * @param jobApps   The job applications NOT getting interviews.
     */
    public void rejectApplicationForFirstRound(ArrayList<JobApplication> jobApps) {
        for (JobApplication app : jobApps) {
            app.getJobPosting().getInterviewManager().reject(app);
        }
    }


    public ArrayList<Interviewer> getInterviewersInField(BranchJobPosting jobPosting) {
        return this.hr.getBranch().getFieldToInterviewers().get(jobPosting.getField());
    }

    public void setInterviewConfiguration(BranchJobPosting jobPosting, ArrayList<Boolean> isInterviewRoundOneOnOne,
                                          ArrayList<String> descriptions) {
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        int i = 0;
        while (i < isInterviewRoundOneOnOne.size()) {
            if (isInterviewRoundOneOnOne.get(i)) {
                interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, descriptions.get(i)});
            } else {
                interviewConfiguration.add(new String[]{Interview.GROUP, descriptions.get(i)});
            }
            i++;
        }
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
    }

    /**
     * Gets the folder for this job application in the company "server".
     *
     * @param jobApp The job application selected by the interviewer to view.
     * @return the file object that holds the files submitted for this job application.
     */
    public File getFolderForJobApplication(JobApplication jobApp) {
        return this.hr.getBranch().getCompany().getDocumentManager().getFolderForJobApplication(jobApp);
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

    public void selectApplicantsForHire(ArrayList<JobApplication> jobAppsToHire) {
        BranchJobPosting jobPosting = jobAppsToHire.get(0).getJobPosting();
        jobPosting.getInterviewManager().hireApplicants(jobAppsToHire);
    }

}
