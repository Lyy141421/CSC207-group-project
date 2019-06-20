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
     * Get the current round of interviews.
     *
     * @return the current round of interviews.
     */
    int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Get the interviews of given round for job applications still in consideration.
     *
     * @return the current round of interviews.
     */
    private ArrayList<Interview> getInterviewsByRound (int round) {
        ArrayList<Interview> interviews= new ArrayList<>();
        for (JobApplication application : this.applicationsInConsideration) {
            for (Interview interview : application.getInterviews()) {
                if (interview.getRoundNumber() == round) {
                    interviews.add(interview);
                }
            }
        }
        return interviews;
    }

    ArrayList<JobApplication> getApplicationsInConsideration () {
        return this.applicationsInConsideration;
    }


    // === Other methods ===

    /**
     * Gets a list of all interviews for this current round of interviews sorted by date.
     *
     * @return a list of all interviews for this current round.
     */

    //Check the method above: getInterviewsByRound. Sorting will be merged into getLastInterviewForCurrentRound().
    /*private ArrayList<Interview> getInterviewsForCurrentRoundSorted() {
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
    }*/

    Interview getLastInterviewOfCurrentRound() {
        ArrayList<Interview> interviews = this.getInterviewsByRound(this.currentRound);
        interviews.sort(new DateComparator());
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
        JobApplication jobApplication = interview.getJobApplication();
        jobApplication.addInterview(interview);
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

    boolean isCurrentRoundOver(LocalDate today) {
        Interview lastInterview = this.getLastInterviewOfCurrentRound();
        return today.isAfter(lastInterview.getDate());
    }

    /**
     * Update the applications still in consideration for this job.
     */
    /*void updateApplicationsInConsideration() {
        for (Interview interview : this.getInterviewsForCurrentRoundSorted()) {
            if (!interview.isPassed()) {
                JobApplication jobApplication = interview.getJobApplication();
                this.reject(jobApplication);
            }
        }
    }*/
}
