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

    // === Constructor ===
    HRBackend(JobApplicationSystem jobAppSystem, HRCoordinator hr) {
        this.jobAppSystem = jobAppSystem;
        this.hr = hr;
    }

    // === Getters ===
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

    // === Other methods ===

    /**
     * Gets an array list of branch job postings that are under review for first round of interviews.
     * @return the array list of branch job postings.
     */
    ArrayList<BranchJobPosting> getJPToReview() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getJobPostingsRecentlyClosedForApplications(this.jobAppSystem.getToday());
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
     * Gets an array list of all the job postings in this branch.
     *
     * @return a list of all job postings in this branch.
     */
    ArrayList<BranchJobPosting> getAllJP() {
        return this.hr.getBranch().getJobPostingManager().getBranchJobPostings();
    }

    /**
     * Gets an array list of all branch job postings.
     *
     * @return the array list of all branch job postings.
     */
    ArrayList<BranchJobPosting> getAllFilledJP() {
        BranchJobPostingManager jpManager = this.hr.getBranch().getJobPostingManager();
        return jpManager.getAllFilledJobPostings();
    }

    /**
     * Gets an array list of all the applications in consideration for this job posting.
     *
     * @param jobPosting The job posting selected by the HR coordinator.
     * @return the list of applications in consideration for this job posting.
     */
    ArrayList<JobApplication> getApplicationsInConsiderationForJobPosting(BranchJobPosting jobPosting) {
        if (jobPosting.getInterviewManager() != null) {
            return jobPosting.getInterviewManager().getApplicationsInConsideration();
        }
        return new ArrayList<>();
    }

    /**
     * Gets an array list of all the applicants rejected for this job posting.
     *
     * @param jobPosting The job posting selected by the HR coordinator.
     * @return the list of applicants rejected for this job posting.
     */
    ArrayList<Applicant> getRejectedApplicantsForJobPosting(BranchJobPosting jobPosting) {
        ArrayList<Applicant> rejectedApplicants = new ArrayList<>();
        if (jobPosting.getInterviewManager() != null) {
            for (JobApplication jobApplication : jobPosting.getInterviewManager().getApplicationsRejected()) {
                rejectedApplicants.add(jobApplication.getApplicant());
            }
        }
        return rejectedApplicants;
    }

    String formatCase(String s) {
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            String firstChar = words[i].substring(0, 1);
            String rest = "";
            if (words[i].length() > 1) {
                rest = words[i].substring(1);
            }
            words[i] = firstChar.toUpperCase() + rest.toLowerCase();
        }
        String res = "";
        for (String word : words) {
            res += " " + word;
        }
        return res.substring(1);
    }

    /**
     * Add a job posting to the branch.
     * @param mandatoryFields  The fields that must be entered regardless of method of adding JP.
     * @param defaultFields The fields that are set by default in company posting mode.
     */
    void addJobPosting(Object[] mandatoryFields, String[] defaultFields) {
        String title = defaultFields[0];
        String field = this.formatCase(defaultFields[1]);
        String description = defaultFields[2];
        ArrayList<String> requirements = new ArrayList<>(Arrays.asList(defaultFields[3].split(";")));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(defaultFields[4].split(";")));
        int numPositions = (Integer) mandatoryFields[0];
        LocalDate applicationCloseDate = (LocalDate) mandatoryFields[1];
        this.hr.addJobPosting(title, field, description, requirements, tags, numPositions, jobAppSystem.getToday(), applicationCloseDate);
    }

    /**
     * Checks whether this branch has an interviewer in this field.
     * @param field The field in question.
     * @return true iff this branch has an interviewer in this field.
     */
    boolean hasInterviewerOfField(String field) {
        return this.hr.getBranch().hasInterviewerForField(field);
    }

    /**
     * Implement an existing company job posting.
     * @param cjp   The company job posting to be implemented.
     * @param jobPostingFields  The job posting fields inputted by hr.
     */
    void implementJobPosting(CompanyJobPosting cjp, Object[] jobPostingFields) {
        int numPositions = (int) jobPostingFields[0];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[1];
        this.hr.implementJobPosting(cjp, numPositions, jobAppSystem.getToday(), applicationCloseDate);
    }

    /**
     * Update the job posting.
     * @param jobPosting    The job posting to be updated.
     * @param jobPostingFields  The fields that were updated.
     */
    void updateJobPosting(BranchJobPosting jobPosting, Object[] jobPostingFields) {
        int numPositions = (int) jobPostingFields[0];
        LocalDate applicationCloseDate = (LocalDate) jobPostingFields[1];
        this.hr.updateJobPosting(jobPosting, numPositions, applicationCloseDate);
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

    /**
     * Get all the applicants who have applied to this company.
     * @return a list of all applicants who have applied to this company.
     */
    private ArrayList<Applicant> getAllApplicantsWhoHaveAppliedToCompany() {
        return this.hr.getBranch().getCompany().getAllApplicantsWhoHaveAppliedToCompany(jobAppSystem.getToday());
    }

    /**
     * Get a hash map of applicant names to the applicant object.
     * @return a hash map of names to applicants
     */
    HashMap<String, Applicant> getApplicantHashMap() {
        HashMap<String, Applicant> titleToApplicant = new HashMap<>();
        for (Applicant applicant : this.getAllApplicantsWhoHaveAppliedToCompany()) {
            String title = applicant.getLegalName() + " (" + applicant.getUsername() + ")";
            titleToApplicant.put(title, applicant);
        }
        return titleToApplicant;
    }

    /**
     * Get the job applications that a single applicant has submitted to this company.
     * @param applicant The applicant who is being reviewed.
     * @return the job applications that a single applicant has submitted to this company.
     */
    ArrayList<JobApplication> getJobAppsByApplicantForCompany(Applicant applicant) {
        return this.hr.getBranch().getCompany().getAllApplicationsToCompany(applicant);
    }

    /**
     * Get a list of job applications sorted in non-decreasing order based on the number of occurrences of key words and phrases.
     *
     * @param jobPosting         The job posting that has recently closed.
     * @param keyWordsAndPhrases The key words and phrases that the HR Coordinator has inputted.
     * @return a list of job applications sorted in non-decreasing order based for this job posting.
     */
    ArrayList<JobApplication> getJobApplicationInNonDecreasingOrder(BranchJobPosting jobPosting, String[] keyWordsAndPhrases) {
        ArrayList<String> keyWordsArrayList = new ArrayList<>();
        if (keyWordsAndPhrases.length > 0) {
            for (String item : keyWordsAndPhrases) {
                if (!item.isEmpty()) {
                    keyWordsArrayList.add(item);
                }
            }
        }
        JobApplicationGrader jobAppGrader = new JobApplicationGrader(jobPosting, keyWordsArrayList);
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

    /**
     * Get the interviewers who are eligible to interview for this job posting.
     * @param jobPosting    The job posting in question.
     * @return the list of interviewers who are eligible to interview for this job posting.
     */
    ArrayList<Interviewer> getInterviewersInField(BranchJobPosting jobPosting) {
        return this.hr.getBranch().getFieldToInterviewers().get(jobPosting.getField());
    }

    /**
     * Set the interview configuration for this job posting.
     * @param jobPosting    The job posting that is being set.
     * @param isInterviewRoundOneOnOne  A list of whether or not each round configured is one-on-one
     * @param descriptions  The descriptions for each round
     * Precondition: isInterviewRoundOneOnOne.size() == descriptions.size()
     */
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

    /**
     * Select applicants to hire.
     *
     * @param branchJobPosting The job posting for which a hiring decision is being made.
     * @param jobAppsToHire    The list of applicants to hire.
     */
    void selectApplicantsForHire(BranchJobPosting branchJobPosting, ArrayList<JobApplication> jobAppsToHire) {
        branchJobPosting.getInterviewManager().hireApplicants(jobAppsToHire);
    }

    /**
     * Get all interviewer notes for this job application.
     * @param jobApp    The job application being viewed.
     * @return a hash map of the interviewer notes for each round and for each interviewer
     */
    HashMap<String, HashMap<Interviewer, String>> getAllInterviewNotesForApplication(JobApplication jobApp) {
        return jobApp.getAllInterviewNotesForApplication();
    }

    /**
     * Get a list of company job postings that can be implemented.
     * @return a list of company job postigns that can be implemented.
     */
    ArrayList<CompanyJobPosting> getCompanyJobPostingsThatCanBeExtended() {
        return this.hr.getBranch().getJobPostingManager().getExtendableCompanyJobPostings();
    }

    /**
     * Get a list of job postings that can be updated.
     * @return a list of job postings that can be updated.
     */
    ArrayList<BranchJobPosting> getJPThatCanBeUpdated() {
        return this.hr.getBranch().getJobPostingManager().getUpdatableJobPostings(this.jobAppSystem.getToday());
    }

}
