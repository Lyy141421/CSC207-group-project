package GUIClasses.HRInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.*;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Main.JobApplicationSystem;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class HRBackend {

    // === Instance variables ===
    private JobApplicationSystem jobAppSystem;
    private HRCoordinator hr;

    HRBackend(JobApplicationSystem jobAppSystem, HRCoordinator hr) {
        this.jobAppSystem = jobAppSystem;
        this.hr = hr;
    }

    HRCoordinator getHR() {
        return this.hr;
    }

    LocalDate getToday() {
        return this.jobAppSystem.getToday();
    }

    int[] getTomorrowComponents() {
        LocalDate tomorrow = this.getToday().plusDays(1);
        return new int[]{tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth()};
    }

    /**
     * Gets an array list of branch job postings that are under review for first round of interviews.
     * @return the array list of branch job postings.
     */
    ArrayList<BranchJobPosting> getJPToReview() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getJobPostingsRecentlyClosedForReferences(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of branch job postings that are ready to schedule for next round of interviews.
     * @return the array list of branch job postings.
     */
    ArrayList<BranchJobPosting> getJPToSchedule() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getJobPostingsThatNeedGroupInterviewsScheduled(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of branch job postings that are in hiring stage.
     * @return the array list of branch job postings.
     */
    ArrayList<BranchJobPosting> getJPToHire() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getJobPostingsThatNeedHRSelectionForHiring(this.jobAppSystem.getToday());
    }

    /**
     * Gets an array list of all branch job postings.
     *
     * @return the array list of all branch job postings.
     */
    ArrayList<BranchJobPosting> getAllUnfilledJP() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getAllUnfilledJobPostings();
    }


    /**
     * Add a job posting to the branch and check if there is an interviewer for that field.
     * @param mandatoryFields  The fields that must be entered regardless of method of adding JP.
     * @param defaultFields The fields that are set by default in company posting mode.
     * @param isForced Whether or not the hr is forcing the add, that is, they already know there is no
     *                 interviewer for that field
     * @return true iff there is an interviewer for the field that this job posting is being added to.
     */
    boolean addJobPosting(Object[] mandatoryFields, String[] defaultFields, boolean isForced) {
        String title = defaultFields[0];
        String field = defaultFields[1];
        if (!isForced) {
            if (!this.hr.getBranch().hasInterviewerForField(field)) {
                return false;
            }
        }
        String description = defaultFields[2];
        ArrayList<String> requirements = new ArrayList<>(Arrays.asList(defaultFields[3].split(";")));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(defaultFields[4].split(";")));
        int numPositions = (Integer) mandatoryFields[0];
        LocalDate applicationCloseDate = (LocalDate) mandatoryFields[1];
        LocalDate referenceCloseDate = (LocalDate) mandatoryFields[2];
        this.hr.addJobPosting(title, field, description, requirements, tags, numPositions, jobAppSystem.getToday(), applicationCloseDate,
                referenceCloseDate);
        return true;
    }

    boolean implementJobPosting(CompanyJobPosting cjp, Object[] jobPostingFields, boolean isForced) {
        if (!isForced) {
            if (!this.hr.getBranch().hasInterviewerForField(cjp.getField())) {
                return false;
            }
        }
        int numPositions = (int) jobPostingFields[0];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[1];
        LocalDate referenceCloseDate = (LocalDate) jobPostingFields[2];
        this.hr.implementJobPosting(cjp, numPositions, jobAppSystem.getToday(), applicationCloseDate, referenceCloseDate);
        return true;
    }

    void updateJobPosting(BranchJobPosting jobPosting, Object[] jobPostingFields) {
        int numPositions = (int) jobPostingFields[0];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[1];
        LocalDate referenceCloseDate = (LocalDate) jobPostingFields[2];
        this.hr.updateJobPosting(jobPosting, numPositions, applicationCloseDate, referenceCloseDate);
    }

    /**
     * Get all job applications submitted by this applicant with this username.
     * @param applicantUsername The applicant username inputted.
     * @return a list of job applications submitted by this applicant with this username.
     */

    ArrayList<JobApplication> getAllJobApplicationsToCompany(String applicantUsername) {
        Applicant applicant = (Applicant) jobAppSystem.getUserManager().findUserByUsername(applicantUsername);
        if (applicant == null) {
            return new ArrayList<>();
        }
        return this.getJobAppsByApplicantForCompany(applicant);
    }

    private ArrayList<Applicant> getAllApplicantsWhoHaveAppliedToCompany() {
        return this.hr.getBranch().getCompany().getAllApplicantsWhoHaveAppliedToCompany(jobAppSystem.getToday());
    }

    HashMap<String, Applicant> getApplicantHashMap() {
        HashMap<String, Applicant> titleToApplicant = new HashMap<>();
        for (Applicant applicant : this.getAllApplicantsWhoHaveAppliedToCompany()) {
            String title = applicant.getLegalName() + " (" + applicant.getUsername() + ")";
            titleToApplicant.put(title, applicant);
        }
        return titleToApplicant;
    }

    ArrayList<JobApplication> getJobAppsByApplicantForCompany(Applicant applicant) {
        return this.hr.getBranch().getCompany().getAllApplicationsToCompany(applicant);
    }

    /**
     * Hire or reject an application.
     *
     * @param jobApp The job application in question.
     * @param toHire Whether or not the HR Coordinator wants to hire the applicant.
     * */

    boolean hireOrRejectApplication(JobApplication jobApp, boolean toHire) {
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
    ArrayList<JobApplication> getJobApplicationInNonDecreasingOrder(BranchJobPosting jobPosting, ArrayList<String> keyWordsAndPhrases) {
        JobApplicationGrader jobAppGrader = new JobApplicationGrader(jobPosting, keyWordsAndPhrases);
        return jobAppGrader.getSortedJobApps();
    }

    /**
     * Reject the list of applications for first round.
     *
     * @param jobApps   The job applications NOT getting interviews.
     */
    void rejectApplicationForFirstRound(BranchJobPosting branchJobPosting, ArrayList<JobApplication> jobApps) {
        branchJobPosting.getInterviewManager().rejectApplicationsForFirstRound(jobApps);
    }


    ArrayList<Interviewer> getInterviewersInField(BranchJobPosting jobPosting) {
        return this.hr.getBranch().getFieldToInterviewers().get(jobPosting.getField());
    }

    void setInterviewConfiguration(BranchJobPosting jobPosting, ArrayList<Boolean> isInterviewRoundOneOnOne,
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
    File getFolderForJobApplication(JobApplication jobApp) {
        return this.hr.getBranch().getCompany().getDocumentManager().getFolderForJobApplication(jobApp);
    }

    /**
     * Set up interviews for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param interviewCoordinator The interview coordinator selected
     * @param otherInterviewers The other interviewers selected
     */
    void setUpGroupInterviews(BranchJobPosting jobPosting, Interviewer interviewCoordinator,
                              ArrayList<Interviewer> otherInterviewers, int minNumDaysNotice) {
        jobPosting.getInterviewManager().setUpGroupInterview(interviewCoordinator, otherInterviewers,
                jobAppSystem.getToday(), minNumDaysNotice);
    }

    void selectApplicantsForHire(ArrayList<JobApplication> jobAppsToHire) {
        BranchJobPosting jobPosting = jobAppsToHire.get(0).getJobPosting();
        jobPosting.getInterviewManager().hireApplicants(jobAppsToHire);
    }

    HashMap<String, HashMap<Interviewer, String>> getAllInterviewNotesForApplication(JobApplication jobApp) {
        return jobApp.getAllInterviewNotesForApplication();
    }

    ArrayList<CompanyJobPosting> getCompanyJobPostingsThatCanBeExtended() {
        return this.hr.getBranch().getJobPostingManager().getExtendableCompanyJobPostings();
    }

    ArrayList<BranchJobPosting> getJPThatCanBeUpdated() {
        return this.hr.getBranch().getJobPostingManager().getUpdatableJobPostings(this.jobAppSystem.getToday());
    }

}
