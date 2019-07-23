package CompanyStuff;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import java.io.Serializable;
import java.util.ArrayList;

public class InterviewManager implements Serializable {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Class variables ===
    // Integers that represent the task the HR Coordinator needs to accomplish
    public static final int CLOSE_POSTING_NO_HIRE = -1;
    private static final int DO_NOTHING = 0;
    static final int SELECT_APPS_FOR_FIRST_ROUND = 1;
    static final int SET_INTERVIEW_CONFIGURATION = 2;
    public static final int SCHEDULE_GROUP_INTERVIEWS = 3;
    public static final int HIRE_APPLICANTS = 4;

    // === Instance variables ===
    // The job posting for this interview manager
    private BranchJobPosting branchJobPosting;
    // Map of job applications still in consideration for the job to their interviews
    private ArrayList<JobApplication> applicationsInConsideration;
    // Map of job applications of applicants that are rejected for the job to their interviews
    private ArrayList<JobApplication> applicationsRejected;
    // The configuration of interviews chosen for this job posting
    // (nested array is interview description and interview type
    private ArrayList<String[]> interviewConfiguration;
    // The current round of interviews
    private int currentRound = 0;
    // The maximum number of interview rounds
    private int maxNumberOfRounds;

    // === Representation invariants ===
    // The list of interviews for each applicant is sorted by date.
    // The current round is an index in the interviewConfiguration list
    // The Object[] in interviewConfiguration has 2 elements
    //  - Index 0: Interview description (String)
    //  - Index 1: Interview type (String) -- "One-on-One" or "Group"

    // === Public methods ===

    // === Constructors ===
    public InterviewManager(BranchJobPosting branchJobPosting, ArrayList<JobApplication> applicationsInConsideration) {
        this.branchJobPosting = branchJobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = new ArrayList<>();
        this.interviewConfiguration = new ArrayList<>();
    }

    // === Getters ===
    public int getCurrentRound() {
        return this.currentRound;
    }

    public int getMaxNumberOfRounds() {
        return this.maxNumberOfRounds;
    }

    public String getCurrentRoundDescription() {
        return this.interviewConfiguration.get(this.currentRound)[0];
    }

    public String getCurrentRoundType() {
        return this.interviewConfiguration.get(this.currentRound)[1];
    }

    public ArrayList<String[]> getInterviewConfiguration() {
        return this.interviewConfiguration;
    }

    public ArrayList<JobApplication> getApplicationsInConsideration() {
        return this.applicationsInConsideration;
    }

    public ArrayList<JobApplication> getApplicationsRejected() {
        return this.applicationsRejected;
    }

    // === Setters ===
    public void setInterviewConfiguration(ArrayList<String[]> interviewConfiguration) {
        this.interviewConfiguration = interviewConfiguration;
        this.maxNumberOfRounds = interviewConfiguration.size();
    }

    // === Other methods ===

    /**
     * Get the number of applications still required.
     *
     * @return the number of applications still required before completely filling all positions.
     */
    public int getNumOpenPositions() {
        return this.branchJobPosting.getNumPositions() - this.applicationsInConsideration.size();
    }

    /**
     * Checks whether the current round of interviews is over.
     *
     * @return true iff all interviews in this round have been completed.
     */
    public boolean isCurrentRoundOver() {
        for (JobApplication jobApp : this.getApplicationsInConsideration()) {
            Interview lastInterview = jobApp.getLastInterview();
            if (lastInterview != null && lastInterview.isComplete()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the next round of interviews is a group interview.
     *
     * @return true iff the next interview round is a group interview.
     */
    public boolean isNextRoundGroupInterview() {
        return this.interviewConfiguration.get(this.currentRound + 1)[1].equals(Interview.GROUP);
    }

    /**
     * Advance the round of interviews.
     */
    public void advanceRound() {
        this.currentRound++;
    }

    /**
     * Reject an application for this job.
     *
     * @param jobApplication The application to be rejected.
     */
    public void reject(JobApplication jobApplication) {
        this.applicationsInConsideration.remove(jobApplication);
        this.applicationsRejected.add(jobApplication);
    }

    /**
     * Withdraw this application from contention for this job posting.
     *
     * @param applicationToWithdraw the application to withdraw.
     */
    public void withdrawApplication(JobApplication applicationToWithdraw) {
        if (!applicationToWithdraw.getInterviews().isEmpty()) {
            Interview interview = applicationToWithdraw.getLastInterview();
            interview.removeApplication(applicationToWithdraw);
            // If it is a group interview and there are still people left, do not withdraw
            if (interview.getJobApplications().isEmpty()) {
                for (Interviewer interviewer : interview.getAllInterviewers()) {
                    interviewer.removeInterview(interview);
                }
            }
        }
        this.reject(applicationToWithdraw);
    }

    /**
     * Check if the number of applications in consideration is less than or equal to the number of positions available
     * in the posting and is not zero.
     *
     * @return true iff number of applications in consideration is less than or equal to the number of positions
     * available and is not zero.
     */
    public boolean isNumApplicationsUnderOrAtThreshold() {
        int jobAppsSize = this.applicationsInConsideration.size();
        return jobAppsSize > 0 && jobAppsSize <= this.branchJobPosting.getNumPositions();
    }

    /**
     * Archive all the rejected applications for this job posting.
     */
    public void archiveRejected() {
        for (JobApplication jobApp : this.getApplicationsRejected()) {
            jobApp.getStatus().setArchived();
        }
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    /**
     * Get the task required by the HR Coordinator for this job posting at this moment in time.
     *
     * @return the integer representing the task the HR Coordinator must accomplish.
     */
    public int getHrTask() {
        if (this.hasNoJobApplicationsInConsideration()) {
            return InterviewManager.CLOSE_POSTING_NO_HIRE;
        } else if (this.currentRound != 0 && this.isNumApplicationsUnderOrAtThreshold()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (!this.branchJobPosting.getJobApplications().isEmpty() && this.interviewConfiguration.isEmpty()) {
            // Applicants for first round of interviews selected but no interview configuration decided
            return InterviewManager.SET_INTERVIEW_CONFIGURATION;
        } else if (this.isInterviewProcessOver()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (this.isCurrentRoundOver() && this.isNextRoundGroupInterview()) {
            return InterviewManager.SCHEDULE_GROUP_INTERVIEWS;
        } else {
            return InterviewManager.DO_NOTHING;
        }
    }

    /**
     * Set up one-on-one interviews for all applications in consideration.
     */
    public void setUpOneOnOneInterviews() {
        for (JobApplication jobApp : this.applicationsInConsideration) {
            String field = this.branchJobPosting.getField();
            Interviewer interviewer = this.branchJobPosting.getBranch().findInterviewerByField(field);
            Interview interview = new Interview(jobApp, interviewer, this);
            jobApp.addInterview(interview);
            jobApp.getStatus().advanceStatus();
        }
    }

    /**
     * Set up a group interview for all applications in consideration.
     *
     * @param interviewCoordinator The interview coordinator selected.
     * @param otherInterviewers    The other interviewers selected.
     */
    public void setUpGroupInterview(Interviewer interviewCoordinator, ArrayList<Interviewer> otherInterviewers) {
        Interview interview = new Interview(this.applicationsInConsideration, interviewCoordinator,
                otherInterviewers, this);
        for (JobApplication jobApp : this.applicationsInConsideration) {
            jobApp.addInterview(interview);
            jobApp.getStatus().advanceStatus();
        }
        for (Interviewer interviewer : interview.getAllInterviewers()) {
            interviewer.addInterview(interview);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===
    // === Other methods ===

    /**
     * Check whether there are any applications in consideration.
     *
     * @return true iff the number of applications in consideration is equal to 0.
     */
    private boolean hasNoJobApplicationsInConsideration() {
        return this.applicationsInConsideration.isEmpty();
    }

    /**
     * Check if interview process has finished the last interview round.
     *
     * @return true iff the maximum number of interview rounds has been completed.
     */
    private boolean isInterviewProcessOver() {
        return this.isCurrentRoundOver() && this.currentRound == this.interviewConfiguration.size();
    }
}
