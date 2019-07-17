package CompanyStuff;

import ApplicantStuff.JobApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class GroupInterview extends Interview {

    // === Instance variables ===
    // A map of job applications associated with this interview to whether or not they passed
    private HashMap<JobApplication, Boolean> jobApplicationsToResult;
    // The other interviewers
    private ArrayList<Interviewer> otherInterviewers;

    // === Constructor ===
    GroupInterview() {
    }

    GroupInterview(ArrayList<JobApplication> jobApplications, Interviewer interviewCoordinator,
                   ArrayList<Interviewer> otherInterviewers, InterviewManager interviewManager) {
        super(interviewCoordinator, interviewManager);
        this.setJobApplications(jobApplications);
        this.otherInterviewers = otherInterviewers;
    }

    // === Getters ===
    public HashMap<JobApplication, Boolean> getJobApplicationsToResult() {
        return this.jobApplicationsToResult;
    }

    public Collection<JobApplication> getJobApplications() {
        return this.jobApplicationsToResult.keySet();
    }

    public ArrayList<Interviewer> getOtherInterviewers() {
        return this.otherInterviewers;
    }

    // === Other methods ===
    void setResults(HashMap<JobApplication, Boolean> jobAppToResult) {
        for (JobApplication jobApp : jobAppToResult.keySet()) {
            boolean result = jobAppToResult.get(jobApp);
            this.jobApplicationsToResult.replace(jobApp, result);
        }
    }

    /**
     * Check whether this job application has passed this interview round.
     *
     * @param jobApp The job application in question.
     * @return true iff this job application has passed this interview round.
     */
    public boolean isPassed(JobApplication jobApp) {
        return this.jobApplicationsToResult.get(jobApp);
    }

    /**
     * Check whether interview is completed.
     *
     * @return true iff the interview is completed, ie, every job application has been assigned a pass/fail.
     */
    public boolean isComplete() {
        for (JobApplication jobApp : this.jobApplicationsToResult.keySet()) {
            if (this.jobApplicationsToResult.get(jobApp) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the number of applications involved in this interview.
     *
     * @return the number of applications involved in this interview.
     */
    public int getNumApplications() {
        return this.jobApplicationsToResult.size();
    }

    /**
     * Get all the interviewers involved in this interview.
     *
     * @return the number of interviewers involved in this interview.
     */
    public ArrayList<Interviewer> getAllInterviewers() {
        ArrayList<Interviewer> interviewerListCopy = (ArrayList<Interviewer>) this.otherInterviewers.clone();
        interviewerListCopy.add(this.getInterviewCoordinator());
        return interviewerListCopy;
    }

    public JobApplication findJobAppById(int id) {
        for (JobApplication jobApp : this.jobApplicationsToResult.keySet()) {
            if (jobApp.getId() == id) {
                return jobApp;
            }
        }
        return null;
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Set the map of job applications to result from an array list of job applications.
     *
     * @param jobApplications The job applications involved in this interview.
     */
    private void setJobApplications(ArrayList<JobApplication> jobApplications) {
        for (JobApplication jobApp : jobApplications) {
            this.jobApplicationsToResult.put(jobApp, null);
        }
    }
}
