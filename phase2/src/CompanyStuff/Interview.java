package CompanyStuff;

import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Interview {

    // === Class variables ===
    // The total number of interviews conducted
    private static int totalNumOfInterviews;
    // The interview types allowed
    static String ONE_ON_ONE = "One-on-One";
    static String GROUP = "Group";

    // === Instance variables ===
    // The unique identifier for this interview
    private int id;
    // A map of job applications associated with this interview to whether or not they passed
    private HashMap<JobApplication, Boolean> jobApplicationsToResult = new HashMap<>();
    // The interview coordinator and their notes
    private HashMap<Interviewer, String> interviewCoordinatorToNotes;
    // The other interviewers and their notes
    private HashMap<Interviewer, String> otherInterviewersToNotes = new HashMap<>();
    // The date and time of this interview
    private InterviewTime time;
    // Interview round
    private int roundNumber;
    // InterviewManager of the job posting this interview is held for
    private InterviewManager interviewManager;

    // === Representation invariants ===
    // ID >= 1

    // === Constructor ===
    Interview(JobApplication jobApplication, Interviewer interviewer, InterviewManager interviewManager) {
        Interview.totalNumOfInterviews++;
        this.id = Interview.totalNumOfInterviews;
        this.jobApplicationsToResult = new HashMap<JobApplication, Boolean>() {{
            put(jobApplication, null);
        }};
        this.interviewCoordinatorToNotes = new HashMap<Interviewer, String>() {{
            put(interviewer, null);
        }};
        this.interviewManager = interviewManager;
        this.roundNumber = this.interviewManager.getCurrentRound();
    }

    Interview(ArrayList<JobApplication> jobApplications, Interviewer interviewCoordinator,
              ArrayList<Interviewer> otherInterviewers, InterviewManager interviewManager) {
        Interview.totalNumOfInterviews++;
        this.id = Interview.totalNumOfInterviews;
        this.setJobApplications(jobApplications);
        this.interviewCoordinatorToNotes = new HashMap<Interviewer, String>() {{
            put(interviewCoordinator, null);
        }};
        this.setOtherInterviewersToNotes(otherInterviewers);
        this.interviewManager = interviewManager;
        this.roundNumber = this.interviewManager.getCurrentRound();
    }

    // === Public methods ===
    // === Getters ===
    public int getId() {
        return this.id;
    }

    public HashMap<Interviewer, String> getInterviewCoordinatorToNotes() {
        return this.interviewCoordinatorToNotes;
    }

    public Interviewer getInterviewCoordinator() {
        Interviewer interviewCoordinator = null;
        for (Interviewer interviewer : this.interviewCoordinatorToNotes.keySet()) {
            interviewCoordinator = interviewer;
        }
        return interviewCoordinator;
    }

    public InterviewTime getTime() {
        return this.time;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    InterviewManager getInterviewManager() {
        return this.interviewManager;
    }

    public HashMap<JobApplication, Boolean> getJobApplicationsToResult() {
        return this.jobApplicationsToResult;
    }

    public ArrayList<JobApplication> getJobApplications() {
        ArrayList<JobApplication> jobApps = new ArrayList<>();
        jobApps.addAll(this.jobApplicationsToResult.keySet());
        return jobApps;
    }

    public ArrayList<Interviewer> getOtherInterviewers() {
        ArrayList<Interviewer> interviewers = new ArrayList<>();
        interviewers.addAll(this.otherInterviewersToNotes.keySet());
        return interviewers;
    }

    public HashMap<Interviewer, String> getOtherInterviewersToNotes() {
        return this.otherInterviewersToNotes;
    }

    // === Setters ===

    public void setTime(InterviewTime time) {
        this.time = time;
    }

    public void setInterviewCoordinatorNotes(String notes) {
        this.interviewCoordinatorToNotes.replace(this.getInterviewCoordinator(), notes);
    }

    public void setOtherInterviewNotes(Interviewer interviewer, String notes) {
        this.otherInterviewersToNotes.replace(interviewer, notes);
    }

    // === Other methods ===
    public void setResults(HashMap<JobApplication, Boolean> jobAppToResult) {
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
     * Get all the interviewers involved in this interview.
     *
     * @return the number of interviewers involved in this interview.
     */
    public ArrayList<Interviewer> getAllInterviewers() {
        ArrayList<Interviewer> interviewerListCopy = new ArrayList<>();
        interviewerListCopy.add(this.getInterviewCoordinator());
        for (Interviewer interviewer : this.getOtherInterviewers()) {
            interviewerListCopy.add(interviewer);
        }
        return interviewerListCopy;
    }

    /**
     * Remove this application from this interview.
     *
     * @param jobApp The job application to be removed.
     */
    public void removeApplication(JobApplication jobApp) {
        this.jobApplicationsToResult.remove(jobApp);
    }

    public JobApplication findJobAppById(int id) {
        for (JobApplication jobApp : this.jobApplicationsToResult.keySet()) {
            if (jobApp.getId() == id) {
                return jobApp;
            }
        }
        return null;
    }

    public static String[] getCategoryNamesForInterviewerUnscheduled() {
        return new String[]{"Interviewee", "Job Posting"};
    }

    public String[] getCategoryValuesForInterviewerUnscheduled() {
        JobApplication jobApp = this.getJobApplications().get(0);
        return new String[]{jobApp.getApplicant().getLegalName(), jobApp.getJobPosting().getTitle()};
    }

    public static String[] getCategoryNamesForInterviewerScheduled() {
        return new String[]{"Job Posting", "Interview Type", "Interview Description", "Interview Coordinator",
                "Interview Time"};
    }

    public String[] getCategoryValuesForInterviewerScheduled() {
        JobApplication jobApp = this.getJobApplications().get(0);
        return new String[]{jobApp.getJobPosting().getTitle(), this.getInterviewManager().getCurrentRoundType(),
                this.getInterviewManager().getCurrentRoundType(), this.getInterviewCoordinator().getLegalName(),
                this.getTime().toString()};
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

    /**
     * Set the map of interviewers to notes from an array list of interviewers.
     *
     * @param otherInterviewers The other interviewers for this interview.
     */
    private void setOtherInterviewersToNotes(ArrayList<Interviewer> otherInterviewers) {
        for (Interviewer interviewer : otherInterviewers) {
            this.otherInterviewersToNotes.put(interviewer, null);
        }
    }
}
