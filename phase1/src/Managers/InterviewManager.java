package Managers;

import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;

public class InterviewManager {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Class variables ===
    // Integers that represent the task the HR Coordinator needs to accomplish
    static final int CLOSE_POSTING_NO_HIRE = -2;
    public static int SELECT_APPS_FOR_PHONE_INTERVIEW = -1;
    private static final int DO_NOTHING = 0;
    static final int SCHEDULE_INTERVIEWS = 1;
    static final int HIRE_APPLICANTS = 2;



    // === Instance variables ===
    // The job posting for this interview manager
    private JobPosting jobPosting;
    // Map of job applications still in consideration for the job to their interviews
    private ArrayList<JobApplication> applicationsInConsideration;
    // Map of job applications of applicants that are rejected for the job to their interviews
    private ArrayList<JobApplication> applicationsRejected;
    // The current round of interviews
    private int currentRound;
    // Integer that represents the task the HR Coordinator needs to fulfill
    private int hrTask;

    // === Representation invariants ===
    // The list of interviews for each applicant is sorted by date.
    // 0 <= currentRound <= 4

    // === Public methods ===

    // === Constructor ===

    public InterviewManager(JobPosting jobPosting, ArrayList<JobApplication> applicationsInConsideration) {
        this.jobPosting = jobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = new ArrayList<>();
    }

    public InterviewManager(JobPosting jobPosting, ArrayList<JobApplication> applicationsInConsideration,
                     ArrayList<JobApplication> applicationsRejected) {
        this.jobPosting = jobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = applicationsRejected;
    }

    public InterviewManager(JobPosting jobPosting, ArrayList<JobApplication> applicationsInConsideration,
                     ArrayList<JobApplication> applicationsRejected, int currentRound) {
        this.jobPosting = jobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = applicationsRejected;
        this.currentRound = currentRound;
    }

    // === Getters ===

    public int getCurrentRound() {
        return this.currentRound;
    }

    public ArrayList<JobApplication> getApplicationsInConsideration () {
        return this.applicationsInConsideration;
    }

    public ArrayList<JobApplication> getApplicationsRejected () {
        return this.applicationsRejected;
    }

    // === Other methods ===

    /**
     * Get the number of applications still required.
     *
     * @return the number of applications still required before completely filling all positions.
     */
    public int getNumOpenPositions() {
        return this.jobPosting.getNumPositions() - this.applicationsInConsideration.size();
    }

    /**
     * Checks whether the current round of interviews is over.
     * @return  true iff all interviews in this round have been completed.
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
            InterviewManager IM = applicationToWithdraw.getJobPosting().getInterviewManager();
            if (!interview.isComplete()) {
                interview.getInterviewer().removeInterview(interview);
            }
            if (IM != null) {
                IM.reject(applicationToWithdraw);
            }
        }
    }

    /**
     * Check if the number of applications in consideration is less than or equal to the number of positions available
     * in the posting and is not zero.
     *
     * @return true iff number of applications in consideration is less than or equal to the number of positions
     * available and is not zero.
     */
    public boolean isNumApplicantUnderOrAtThreshold() {
        int jobAppsSize = this.applicationsInConsideration.size();
        return jobAppsSize > 0 && jobAppsSize <= this.jobPosting.getNumPositions();
    }


    /**
     * Get the task required by the HR Coordinator for this job posting at this moment in time.
     *
     * @return the integer representing the task the HR Coordinator must accomplish.
     */
    public int getHrTask() {
        if (this.hasNoJobApplicationsInConsideration()) {
            return InterviewManager.CLOSE_POSTING_NO_HIRE;
        } else if (this.currentRound != 0 && this.isNumApplicantUnderOrAtThreshold()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (!this.jobPosting.getJobApplications().isEmpty() && !this.jobPosting.hasInterviews()) {
            // Applicants for phone interview selected but no interviews scheduled
            return InterviewManager.SCHEDULE_INTERVIEWS;
        } else if (this.isInterviewProcessOver()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (this.isCurrentRoundOver()) {
            return InterviewManager.SCHEDULE_INTERVIEWS;
        } else {
            return InterviewManager.DO_NOTHING;
        }
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
    // === Private methods ===
    // === Other methods ===

    /**
     * Check whether there are any applications in consideration.
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
        return this.isCurrentRoundOver() && this.currentRound == Interview.MAX_NUM_ROUNDS;
    }
}
