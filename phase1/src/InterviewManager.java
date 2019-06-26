import java.time.LocalDate;
import java.util.ArrayList;

class InterviewManager {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Class variables ===
    // The maximum number of in-person interview rounds
    private static int MAX_NUM_INTERVIEW_ROUNDS = 3;

    // === Instance variables ===
    // The job posting for this interview manager
    private JobPosting jobPosting;
    // Map of job applications still in consideration for the job to their interviews
    private ArrayList<JobApplication> applicationsInConsideration;
    // Map of job applications of applicants that are rejected for the job to their interviews
    private ArrayList<JobApplication> applicationsRejected;
    // The current round of interviews
    private int currentRound;

    // === Representation invariants ===
    // The list of interviews for each applicant is sorted by date.

    // === Constructor ===

    /**
     * Create an interview manager -- to be used after a job posting has closed.
     *
     * @param jobPosting                The job posting for this interview manager.
     * @param applicationsInConsideration The applications of applicants who are still in the running for this job.
     * @param applicationsRejected        The applications of applicants who have already been rejected for this job.
     */
    InterviewManager(JobPosting jobPosting, ArrayList<JobApplication> applicationsInConsideration,
                     ArrayList<JobApplication> applicationsRejected) {
        this.jobPosting = jobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = applicationsRejected;
    }

    /**
     * Create an interview manager.
     *
     * @param jobPosting                The job posting for this interview manager.
     * @param applicationsInConsideration The applications of applicants who are still in the running for this job.
     * @param applicationsRejected        The applications of applicants who have already been rejected for this job.
     * @param currentRound              The current round of interviews.
     */
    InterviewManager(JobPosting jobPosting, ArrayList<JobApplication> applicationsInConsideration,
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
    int getCurrentRound() {
        return this.currentRound;
    }

    ArrayList<JobApplication> getApplicationsInConsideration () {
        return this.applicationsInConsideration;
    }

    ArrayList<JobApplication> getApplicationsRejected () {
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
    boolean currentRoundOver(LocalDate today) {
        Interview lastInterview = this.getLastInterviewOfCurrentRound();
        return today.isAfter(lastInterview.getTime().getDate());
    }

    /**
     * Advance the round of interviews.
     */
    void advanceRound() {
        this.currentRound++;
    }

    /**
     * Reject an application for this job.
     *
     * @param jobApplication The application to be rejected.
     */
    void reject(JobApplication jobApplication) {
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
        return this.currentRoundOver(today) && this.currentRound == InterviewManager.MAX_NUM_INTERVIEW_ROUNDS;
    }

    /**
     * Check if this job posting is ready for hiring.
     *
     * @param today Today's date.
     * @return true iff this job posting is ready for hiring.
     */
    boolean isReadyForHiring(LocalDate today) {
        return this.isNumApplicantUnderThreshold() || this.isInterviewProcessOver(today);
    }
}
