package CompanyStuff;

import ApplicantStuff.JobApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class OneOnOneInterview extends Interview {

    // === Instance variables ===
    private JobApplication jobApplication;
    private Boolean result;

    // === Constructor ===
    OneOnOneInterview() {
    }

    OneOnOneInterview(JobApplication jobApplication, Interviewer interviewer, InterviewManager interviewManager) {
        super(interviewer, interviewManager);
        this.jobApplication = jobApplication;
    }


    // === Other methods ===

    /**
     * Set the result for this interview.
     *
     * @param jobAppToResult The result of this interview.
     */
    void setResults(HashMap<JobApplication, Boolean> jobAppToResult) {
        this.result = jobAppToResult.get(jobApplication);
    }

    /**
     * Check whether this interview was passed.
     *
     * @return true iff this interview has been passed.
     */
    boolean isPassed() {
        return this.result;
    }

    /**
     * Check whether this interview is complete.
     *
     * @return true iff result is not null.
     */
    boolean isComplete() {
        return this.result != null;
    }

    /**
     * Get the number of applications involved in this interview.
     *
     * @return the number of applications involved in this interview.
     */
    int getNumApplications() {
        return 1;
    }

    /**
     * Get a list of all interviewers involved in this interview.
     *
     * @return a list of all interviewers involved in this interview.
     */
    ArrayList<Interviewer> getAllInterviewers() {
        return new ArrayList<>(Arrays.asList(this.getInterviewCoordinator()));
    }

    /**
     * Get a collection of all job applications involved in this interview.
     *
     * @return a collection of all job applications involved in this interview.
     */
    Collection<JobApplication> getJobApplications() {
        return new ArrayList<>(Arrays.asList(this.jobApplication));
    }

    /**
     * Check whether the application id matches the one in question.
     *
     * @param id The id of the application in question.
     * @return the job application if the id matches or null otherwise.
     */
    JobApplication findJobAppById(int id) {
        if (this.jobApplication.getId() == id) {
            return this.jobApplication;
        }
        return null;
    }

}
