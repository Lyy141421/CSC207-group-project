import java.util.ArrayList;
import java.util.HashMap;

class InterviewManager {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Instance variables ===
    // The job posting for this interview manager
    private JobPosting jobPosting;
    // List of applicants still in consideration for the job
    private HashMap<Applicant, ArrayList<Interview>> applicantsInConsideration;
    // List of applicants rejected for the job
    private HashMap<Applicant, ArrayList<Interview>> applicantsRejected;
    // The current round of interviews
    private int currentRound;

    // === Representation invariants ===
    // The list of interviews for each applicant is sorted by date.

    // === Constructor ===

    /**
     * Create an interview manager -- to be used after a job posting has closed.
     *
     * @param jobPosting                The job posting for this interview manager.
     * @param applicantsInConsideration The applicants who are still in the running for this job.
     * @param applicantsRejected        The applicants who have already been rejected for this job.
     */
    InterviewManager(JobPosting jobPosting, ArrayList<Applicant> applicantsInConsideration,
                     ArrayList<Applicant> applicantsRejected) {
        this.jobPosting = jobPosting;
        this.applicantsInConsideration = new HashMap<>();
        for (Applicant applicant : applicantsInConsideration) {
            this.applicantsInConsideration.put(applicant, new ArrayList<>());
        }

        this.applicantsRejected = new HashMap<>();
        for (Applicant applicant : applicantsRejected) {
            this.applicantsInConsideration.put(applicant, new ArrayList<>());
        }
    }

    /**
     * Create an interview manager.
     *
     * @param jobPosting                The job posting for this interview manager.
     * @param applicantsInConsideration The applicants who are still in the running for this job and the interviews
     *                                  that they have completed and are scheduled.
     * @param applicantsRejected        The applicants who have already been rejected for this job and the
     *                                  interviews they have been involved in.
     * @param currentRound              The current round of interviews.
     */
    InterviewManager(JobPosting jobPosting, HashMap<Applicant, ArrayList<Interview>> applicantsInConsideration,
                     HashMap<Applicant, ArrayList<Interview>> applicantsRejected, int currentRound) {
        this.jobPosting = jobPosting;
        this.applicantsInConsideration = applicantsInConsideration;
        this.applicantsRejected = applicantsRejected;
        this.currentRound = currentRound;
    }

    // === Getters ===

    /**
     * Get the current round of interviews.
     *
     * @return the current round of interviews.
     */
    int getCurrentRound() {
        return this.currentRound;
    }


    // === Other methods ===

    /**
     * Gets a list of all interviews for this current round of interviews sorted by date.
     *
     * @return a list of all interviews for this current round.
     */
    ArrayList<Interview> getInterviewsForCurrentRoundSorted() {
        ArrayList<Interview> currentInterviews = new ArrayList<>();
        for (Applicant applicant : this.applicantsInConsideration.keySet()) {
            for (Interview interview : this.applicantsInConsideration.get(applicant)) {
                if (interview.getRoundNumber() == currentRound) {
                    currentInterviews.add(interview);
                }
            }
        }
        return currentInterviews.sort(DateComparator);
    }

    Interview getLastInterviewForCurrentRound() {
        ArrayList<Interview> interviews = this.getInterviewsForCurrentRoundSorted();
        return interviews.get(interviews.size() - 1);
    }

    /**
     * Advance the round of interviews.
     */
    void advanceRound() {
        this.currentRound++;
    }

    /**
     * Add an interview.
     *
     * @param interview The interview to be added.
     */
    void addInterview(Interview interview) {
        Applicant applicant = interview.getApplicant();
        if (!this.applicantsInConsideration.containsKey(applicant)) {
            this.applicantsInConsideration.put(applicant, new ArrayList<>());
        }
        this.applicantsInConsideration.get(applicant).add(interview);
    }

    /**
     * Reject an applicant for this job.
     *
     * @param applicant The applicant to be rejected.
     */
    void reject(Applicant applicant) {
        ArrayList<Interview> interviews = this.applicantsInConsideration.get(applicant);
        applicantsInConsideration.remove(applicant);
        applicantsRejected.put(applicant, interviews);
    }

    /**
     * Get a list of interviews involving this applicant for this job.
     *
     * @param applicant The applicant in question.
     * @return a list of interviews involving this applicant for this job.
     */
    ArrayList<Interview> findInterviewsByApplicant(Applicant applicant) {
        if (this.applicantsInConsideration.containsKey(applicant)) {
            return this.applicantsInConsideration.get(applicant);
        } else if (this.applicantsRejected.containsKey(applicant)) {
            return this.applicantsRejected.get(applicant);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Update the applicants still in consideration for this job.
     */
    void updateApplicantsInConsideration() {
        for (Interview interview : this.getInterviewsForCurrentRoundSorted()) {
            if (!interview.isPassed()) {
                Applicant applicant = interview.getApplicant();
                this.reject(applicant);
            }
        }
    }
}
