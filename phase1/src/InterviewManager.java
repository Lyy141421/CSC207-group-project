import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class InterviewManager {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Instance variables ===
    // The job posting for this interview manager
    private JobPosting jobPosting;
    // Map of job applications still in consideration for the job to their interviews
    private HashMap<JobApplication, ArrayList<Interview>> applicationsInConsideration;
    // Map of job applications of applicants that are rejected for the job to their interviews
    private HashMap<JobApplication, ArrayList<Interview>> applicationsRejected;
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
        this.applicationsInConsideration = new HashMap<>();
        for (JobApplication jobApp : applicationsInConsideration) {
            this.applicationsInConsideration.put(jobApp, new ArrayList<>());
        }

        this.applicationsRejected = new HashMap<>();
        for (JobApplication jobApp : applicationsRejected) {
            this.applicationsRejected.put(jobApp, new ArrayList<>());
        }
    }

    /**
     * Create an interview manager.
     *
     * @param jobPosting                The job posting for this interview manager.
     * @param applicationsInConsideration The applications of applicants who are still in the running for this job.
     * @param applicationsRejected        The applications of applicants who have already been rejected for this job.
     * @param currentRound              The current round of interviews.
     */
    InterviewManager(JobPosting jobPosting, HashMap<JobApplication, ArrayList<Interview>> applicationsInConsideration,
                     HashMap<JobApplication, ArrayList<Interview>> applicationsRejected, int currentRound) {
        this.jobPosting = jobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = applicationsRejected;
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
        for (JobApplication jobApplication : this.applicationsInConsideration.keySet()) {
            for (Interview interview : this.applicationsInConsideration.get(jobApplication)) {
                if (interview.getRoundNumber() == currentRound) {
                    currentInterviews.add(interview);
                }
            }
        }
        currentInterviews.sort(new DateComparator());
        return currentInterviews;
    }

    /**
     * Get the last interview scheduled for this current round.
     *
     * @return the last interview scheduled for this current round.
     */
    Interview getLastInterviewForCurrentRound() {
        ArrayList<Interview> interviews = this.getInterviewsForCurrentRoundSorted();
        return interviews.get(interviews.size() - 1);
    }

    /**
     * Report whether the current round of interviews is over.
     *
     * @param today Today's date.
     * @return true iff the current round of interviews is over.
     */
    boolean isCurrentRoundOver(LocalDate today) {
        return today.isAfter(this.getLastInterviewForCurrentRound().getDate());
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
        JobApplication jobApplication = interview.getJobApplication();
        if (!this.applicationsInConsideration.containsKey(jobApplication)) {
            this.applicationsInConsideration.put(jobApplication, new ArrayList<>());
        }
        this.applicationsInConsideration.get(jobApplication).add(interview);
    }

    /**
     * Reject an application for this job.
     *
     * @param jobApplication The application to be rejected.
     */
    private void reject(JobApplication jobApplication) {
        ArrayList<Interview> interviews = this.applicationsInConsideration.get(jobApplication);
        this.applicationsInConsideration.remove(jobApplication);
        this.applicationsRejected.put(jobApplication, interviews);
    }

    /**
     * Get a list of interviews involving this application for this job.
     *
     * @param jobApplication The application in question.
     * @return a list of interviews involving this application for this job.
     */
    ArrayList<Interview> findInterviewsByApplication(JobApplication jobApplication) {
        if (this.applicationsInConsideration.containsKey(jobApplication)) {
            return this.applicationsInConsideration.get(jobApplication);
        } else if (this.applicationsRejected.containsKey(jobApplication)) {
            return this.applicationsRejected.get(jobApplication);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Update the applications still in consideration for this job.
     */
    void updateApplicationsInConsideration() {
        for (Interview interview : this.getInterviewsForCurrentRoundSorted()) {
            if (!interview.isPassed()) {
                JobApplication jobApplication = interview.getJobApplication();
                this.reject(jobApplication);
            }
        }
    }
}
