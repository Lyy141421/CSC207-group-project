package CompanyStuff;

import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Interview implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // The total number of interviews conducted
    private static int totalNumOfInterviews;
    // The interview types allowed
    public static String ONE_ON_ONE = "One-on-One Interview";
    public static String GROUP = "Group Interview";
    // The category names for interviewer for unscheduled/incomplete interviews
    public static final String[] CATEGORY_NAMES_FOR_INTERVIEWER_UNSCHEDULED_OR_INCOMPLETE = new String[]{"Interviewee", "Job Posting"};
    // The category names for interviewer for scheduled interviews
    public static final String[] CATEGORY_NAMES_FOR_INTERVIEWER_SCHEDULED = new String[]{"Job Posting", "Type", "Description", "Coordinator",
            "Time"};

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
    // The interview manager for this interview
    private InterviewManager interviewManager;
    // The interview type
    private String[] typeAndDescription;
    // The interview round number
    private int roundNumber;

    // === Representation invariants ===
    // ID >= 1

    // === Constructor ===
    public Interview(JobApplication jobApplication, Interviewer interviewer, InterviewManager interviewManager) {
        Interview.totalNumOfInterviews++;
        this.id = Interview.totalNumOfInterviews;
        this.jobApplicationsToResult = new HashMap<JobApplication, Boolean>() {{
            put(jobApplication, null);
        }};
        this.interviewCoordinatorToNotes = new HashMap<Interviewer, String>() {{
            put(interviewer, null);
        }};
        this.interviewManager = interviewManager;
        this.typeAndDescription = this.interviewManager.getCurrentRoundTypeAndDescription();
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
        this.typeAndDescription = this.interviewManager.getCurrentRoundTypeAndDescription();
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

    public int getRoundNumber() {
        return roundNumber;
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

    /**
     * Set the results for all the job applications in this interview at once.
     *
     * @param jobAppToResult The hash map of job applications to results.
     */
    public void setResults(HashMap<JobApplication, Boolean> jobAppToResult) {
        for (JobApplication jobApp : jobAppToResult.keySet()) {
            boolean result = jobAppToResult.get(jobApp);
            this.jobApplicationsToResult.replace(jobApp, result);
        }
        this.interviewManager.updateInterviewersOfInterviewCompletionOrCancellation(this);
        this.interviewManager.updateApplicationsInConsideration(this);
    }

    /**
     * Set the result for this one application.
     *
     * @param jobApp The job application in question.
     * @param result The result (pass/fail)
     */
    public void setResult(JobApplication jobApp, Boolean result) {
        this.jobApplicationsToResult.replace(jobApp, result);
        this.interviewManager.updateInterviewersOfInterviewCompletionOrCancellation(this);
        this.interviewManager.updateApplicationsInConsideration(this);
    }

    public HashMap<Interviewer, String> getAllInterviewersToNotes() {
        HashMap<Interviewer, String> allInterviewersToNotes = new HashMap<>();
        allInterviewersToNotes.putAll((HashMap<Interviewer, String>) this.interviewCoordinatorToNotes.clone());
        allInterviewersToNotes.putAll((HashMap<Interviewer, String>) this.getOtherInterviewersToNotes().clone());
        return allInterviewersToNotes;
    }

    /**
     * Gets all the notes for this interview.
     *
     * @return a list of all the notes for this interview.
     */
    public ArrayList<String> getAllNotes() {
        ArrayList<String> allNotes = new ArrayList<>();
        allNotes.add(this.interviewCoordinatorToNotes.get(this.getInterviewCoordinator()));
        allNotes.addAll(this.otherInterviewersToNotes.values());
        return allNotes;
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
    public boolean isIncomplete() {
        for (JobApplication jobApp : this.jobApplicationsToResult.keySet()) {
            if (this.jobApplicationsToResult.get(jobApp) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether this interview is scheduled.
     *
     * @return true iff this interview has been scheduled
     */
    public boolean isScheduled() {
        return this.time != null;
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

    /**
     * Check whether this interviewer has already written notes for this interview.
     *
     * @param interviewer The interviewer in question.
     * @return true iff this interviewer has already written notes for this interview.
     */
    public boolean hasAlreadyWrittenNotesForInterview(Interviewer interviewer) {
        if (this.getInterviewCoordinator().equals(interviewer)) {
            return this.getInterviewCoordinatorToNotes().get(interviewer) != null;
        } else {
            return this.getOtherInterviewersToNotes().get(interviewer) != null;
        }
    }

    /**
     * Get the names of the interviewees for this interview.
     *
     * @return a string of names of interviewees for this interview.
     */
    public String getIntervieweeNames() {
        String applicantNames = "";
        for (JobApplication jobApp : this.getJobApplications()) {
            applicantNames += jobApp.getApplicant().getLegalName() + ", ";
        }
        applicantNames = applicantNames.substring(0, applicantNames.length() - 2);
        return applicantNames;
    }

    public String[] getCategoryValuesForInterviewerUnscheduledOrIncomplete() {
        JobApplication jobApp = this.getJobApplications().get(0);
        return new String[]{this.getIntervieweeNames(), jobApp.getJobPosting().getTitle()};
    }

    public String[] getCategoryValuesForInterviewerScheduled() {
        JobApplication jobApp = this.getJobApplications().get(0);
        return new String[]{jobApp.getJobPosting().getTitle(), typeAndDescription[0],
                typeAndDescription[1], this.getInterviewCoordinator().getLegalName(),
                this.getTime().toString()};
    }

    public String getMiniDescriptionForHR() {
        return "Job posting: " + this.getJobApplications().get(0).getJobPosting().getTitle() + " -- " +
                this.typeAndDescription[0] + " (" + this.typeAndDescription[1] + ")";
    }

    private String getOtherInterviewersNames() {
        String s = "";
        for (Interviewer interviewer : this.getOtherInterviewers()) {
            s += interviewer.getLegalName() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        return s;
    }

    @Override
    public String toString() {
        String s = "Interview ID: " + this.getId() + "\n";
        s += "Job Posting: " + this.getJobApplications().get(0).getJobPosting().getTitle() + "\n";
        s += "Interview Type: " + this.typeAndDescription[0] + "\n";
        s += "Interview Description: " + this.typeAndDescription[1] + "\n";
        s += "Interview Coordinator: " + this.getInterviewCoordinator().getLegalName() + "\n";
        if (!this.getOtherInterviewers().isEmpty()) {
            s += "Secondary Interviewers: " + this.getOtherInterviewersNames() + "\n";
        }
        s += "Interviewees: " + this.getIntervieweeNames() + "\n";
        s += "Interview Time: ";
        if (this.getTime() != null) {
            s += this.getTime();
        } else {
            s += "TBD";
        }
        return s;
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
