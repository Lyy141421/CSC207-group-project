package Managers;

import Miscellaneous.InterviewTimeComparator;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.time.LocalDate;
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

    /**
     * Get the job posting associated with this interview manager.
     *
     * @return the job posting associated with this interview manager.
     */
    JobPosting getJobPosting() {
        return this.jobPosting;
    }

    /**
     * Get the current round of interviews.
     *
     * @return the current round of interviews.
     */
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
     * Get the interviews of given round for job applications still in consideration.
     *
     * @return the current round of interviews.
     */
    private ArrayList<Interview> getInterviewsByRound(int round) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (JobApplication application : this.applicationsInConsideration) {
            for (Interview interview : application.getInterviews()) {
                if (interview.getRoundNumber() == round) {
                    interviews.add(interview);
                }
            }
        }
        return interviews;
    }

    /**
     * Get the last interview of this current round.
     *
     * @return the last interview of this current round.
     */
    private Interview getLastInterviewOfCurrentRound() {
        ArrayList<Interview> interviews = this.getInterviewsByRound(this.currentRound);
        interviews.sort(new InterviewTimeComparator());
        return interviews.get(interviews.size() - 1);
    }

    /**
     * Reports whether the current round of interviews is over.
     *
     * @param today Today's date.
     * @return true iff the current round of interviews is over.
     */
    public boolean isCurrentRoundOver(LocalDate today) {
        Interview lastInterview = this.getLastInterviewOfCurrentRound();
        return today.isAfter(lastInterview.getTime().getDate());
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
     * Check if number of applications in consideration is no more than number of positions available in the job
     * posting.
     * @return true iff number of applications in consideration is less than or equal to number of position in this
     * job posting.
     */
    boolean isNumApplicantUnderThreshold () {
        return this.applicationsInConsideration.size() <= this.jobPosting.getNumPositions();
    }

    /**
     * Check if interview process has finished the last interview round.
     *
     * @param today Today's date
     * @return true iff the maximum number of interview rounds has been completed.
     */
    private boolean isInterviewProcessOver(LocalDate today) {
        return this.isCurrentRoundOver(today) && this.currentRound == InterviewManager.MAX_NUM_INTERVIEW_ROUNDS;
    }

    /**
     * Get the task required by the HR Coordinator for this job posting at this moment in time.
     *
     * @param today Today's date.
     * @return the integer representing the task the HR Coordinator must accomplish.
     */
    public int getHrTask(LocalDate today) {
        if (this.isNumApplicantUnderThreshold()) {
            return InterviewManager.HIRE_APPLICANTS;
        } else if (this.isInterviewProcessOver(today)) {
            return InterviewManager.CHOOSE_APPLICANTS_FOR_HIRE;
        } else if (this.isCurrentRoundOver(today)) {
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
            jobApp.setArchived();
        }
    }

}
