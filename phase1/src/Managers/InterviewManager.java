package Managers;

import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;

public class InterviewManager {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Class variables ===
    // The maximum number of in-person interview rounds
    private static int MAX_NUM_INTERVIEW_ROUNDS = 3;
    // Integers that represent the task the HR Coordinator needs to accomplish
    public static int SELECT_APPS_FOR_PHONE_INTERVIEW = -1;
    private static final int DO_NOTHING = 0;
    static final int SCHEDULE_INTERVIEWS = 1;
    private static final int CHOOSE_APPLICANTS_FOR_HIRE = 2;
    static final int HIRE_APPLICANTS = 3;



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

    // === Public methods ===

    // === Constructor ===

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
     * Checks whether the current round of interviews is over.
     * @return  true iff all interviews in this round have been completed.
     */
    public boolean isCurrentRoundOver() {
        for (JobApplication jobApp : this.getApplicationsInConsideration()) {
            if (!jobApp.getLastInterview().isComplete()) {
                return false;
            }
        }
        return true;
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
     * Get the task required by the HR Coordinator for this job posting at this moment in time.
     *
     * @return the integer representing the task the HR Coordinator must accomplish.
     */
    public int getHrTask() {
        if (this.isNumApplicantUnderThreshold()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (this.isInterviewProcessOver()) {
            return InterviewManager.CHOOSE_APPLICANTS_FOR_HIRE;
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
     * Check if number of applications in consideration is no more than number of positions available in the job
     * posting.
     * @return true iff number of applications in consideration is less than or equal to number of position in this
     * job posting.
     */
    private boolean isNumApplicantUnderThreshold () {
        return this.applicationsInConsideration.size() <= this.jobPosting.getNumPositions();
    }

    /**
     * Check if interview process has finished the last interview round.
     *
     * @return true iff the maximum number of interview rounds has been completed.
     */
    private boolean isInterviewProcessOver() {
        return this.isCurrentRoundOver() && this.currentRound == InterviewManager.MAX_NUM_INTERVIEW_ROUNDS;
    }

}
