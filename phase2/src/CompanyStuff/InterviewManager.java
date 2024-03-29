package CompanyStuff;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import NotificationSystem.NotificationFactory;
import NotificationSystem.Observable;
import NotificationSystem.Observer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewManager extends Observable implements Serializable {
    /**
     * A class that manages interviews for a job posting.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Integers that represent the task the HR Coordinator needs to accomplish
    private static final int DO_NOTHING = 0;
    public static final int EXTEND_APPLICATION_DEADLINE = 1;
    public static final int SELECT_APPS_FOR_FIRST_ROUND = 2;
    public static final int SCHEDULE_GROUP_INTERVIEWS = 3;
    public static final int SELECT_APPS_TO_HIRE = 4;

    // === Instance variables ===
    // The job posting for this interview manager
    private BranchJobPosting branchJobPosting;
    // Map of job applications still in consideration for the job to their interviews
    private ArrayList<JobApplication> applicationsInConsideration;
    // Map of job applications of applicants that are rejected for the job to their interviews
    private ArrayList<JobApplication> applicationsRejected;
    // The configuration of interviews chosen for this job posting
    private ArrayList<String[]> interviewConfiguration;
    // The current round of interviews
    private int currentRound = -1;
    // The maximum number of interview rounds
    private int maxNumberOfRounds;
    // Whether or not the applicants have been weeded out before the first round of interviews
    private boolean chosenApplicantsForFirstRound = false;

    // === Representation invariants ===
    // The list of interviews for each applicant is sorted by date.
    // The current round is an index in the interviewConfiguration list
    // The Object[] in interviewConfiguration has 2 elements
    //  - Index 0: Interview type (String) -- "One-on-One" or "Group"
    //  - Index 1: Interview description (String)

    // === Public methods ===

    // === Constructors ===
    public InterviewManager(BranchJobPosting branchJobPosting, ArrayList<JobApplication> applicationsInConsideration) {
        this.branchJobPosting = branchJobPosting;
        this.applicationsInConsideration = applicationsInConsideration;
        this.applicationsRejected = new ArrayList<>();
        this.interviewConfiguration = new ArrayList<>();
        this.updateObserverList();
    }

    // === Getters ===
    public BranchJobPosting getBranchJobPosting() {
        return this.branchJobPosting;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public int getFinalRoundNumber() {
        return this.maxNumberOfRounds - 1;
    }

    String[] getCurrentRoundTypeAndDescription() {
        return this.interviewConfiguration.get(this.currentRound);
    }

    public boolean hasChosenApplicantsForFirstRound() {
        return this.chosenApplicantsForFirstRound;
    }

    ArrayList<String[]> getInterviewConfiguration() {
        return this.interviewConfiguration;
    }

    public ArrayList<JobApplication> getApplicationsInConsideration() {
        return this.applicationsInConsideration;
    }

    public ArrayList<JobApplication> getApplicationsRejected() {
        return this.applicationsRejected;
    }

    // === Setters ===
    public void setInterviewConfiguration(ArrayList<String[]> interviewConfiguration) {
        this.interviewConfiguration = interviewConfiguration;
        this.maxNumberOfRounds = interviewConfiguration.size();
        for (JobApplication jobApp : this.applicationsInConsideration) {
            jobApp.getStatus().setDescriptions(interviewConfiguration);
        }
        this.advanceRound();
    }

    // === Other methods ===

    /**
     * Checks whether the current round of interviews is over.
     *
     * @return true iff all interviews in this round have been completed.
     */
    public boolean currentRoundIsOver() {
        for (JobApplication jobApp : this.getApplicationsInConsideration()) {
            Interview lastInterview = jobApp.getLastInterview();
            if (lastInterview != null && lastInterview.getRoundNumber() == this.currentRound && !lastInterview.isIncomplete()) {
                return true;
            }
        }
        return false;
    }

    public boolean interviewProcessHasBegun() {
        return this.getCurrentRound() > -1;
    }

    /**
     * Advance the round of interviews.
     */
    public void advanceRound() {
        this.currentRound++;
        if (!this.interviewConfiguration.isEmpty()) {
            if (this.getCurrentRoundTypeAndDescription()[0].equals(Interview.ONE_ON_ONE)) {
                this.setUpOneOnOneInterviews();
            }
        }
    }

    /**
     * Reject an application for this job.
     *
     * @param jobApplication The application to be rejected.
     */
    private void reject(JobApplication jobApplication) {
        this.applicationsInConsideration.remove(jobApplication);
        this.applicationsRejected.add(jobApplication);
    }

    /**
     * Reject the applications in this list for this job. Called when HR first selects applications to advance to first
     * round.
     *
     * @param jobApplications The job applications to be rejected.
     */
    public void rejectApplicationsForFirstRound(ArrayList<JobApplication> jobApplications) {
        this.branchJobPosting.reviewJobApplications();
        for (JobApplication jobApp : jobApplications) {
            this.reject(jobApp);
        }
        this.chosenApplicantsForFirstRound = true;
    }

    /**
     * Withdraw this application from contention for this job posting.
     *
     * @param applicationToWithdraw the application to withdraw.
     */
    public void updateForApplicationWithdrawal(JobApplication applicationToWithdraw) {
        if (!applicationToWithdraw.getInterviews().isEmpty()) {
            Interview interview = applicationToWithdraw.getLastInterview();
            interview.removeApplication(applicationToWithdraw);
            // If it is a group interview and there are still people left, do not withdraw
            if (interview.getJobApplications().isEmpty()) {
                this.updateInterviewersOfInterviewCompletionOrCancellation(interview);
            }
        }
        this.reject(applicationToWithdraw);
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
    // === Package-private methods ===

    /**
     * Get the task required by the HR Coordinator for this job posting at this moment in time.
     *
     * @return the integer representing the task the HR Coordinator must accomplish.
     */
    public int getHrTask() {
        if (!this.hasChosenApplicantsForFirstRound()) {
            if (this.hasNoJobApplicationsInConsideration()) {
                return InterviewManager.EXTEND_APPLICATION_DEADLINE;
            }
            return InterviewManager.SELECT_APPS_FOR_FIRST_ROUND;
        } else if (!this.isInterviewProcessOver() && this.isCurrentRoundGroupInterviewUnscheduled()) {
            if (!this.branchJobPosting.getBranch().hasInterviewerForField(this.branchJobPosting.getField())) {
                this.notifyAllObservers(new NotificationFactory().createNotification(NotificationFactory.NO_INTERVIEWERS_IN_FIELD, this));
            } else {
                return InterviewManager.SCHEDULE_GROUP_INTERVIEWS;
            }
        } else if (this.isInterviewProcessOver() && !this.isNumApplicationsUnderOrAtThreshold()) {
            return InterviewManager.SELECT_APPS_TO_HIRE;
        }
        return InterviewManager.DO_NOTHING;
    }

    /**
     * Update the status of this job posting.
     * Note: this will be called every time someone logs in
     *
     */
    public void updateJobPostingFilledStatus() {
        if (this.hasNoJobApplicationsInConsideration()) {
            this.updateObserverList();
            this.notifyAllObservers(new NotificationFactory().createNotification(
                    NotificationFactory.NO_APPS_IN_CONSIDERATION, this.getBranchJobPosting()));
            this.getBranchJobPosting().closeJobPostingNoApplicationsInConsideration();
        } else if (this.isInterviewProcessOver() && this.isNumApplicationsUnderOrAtThreshold()) {
            // The check for current round ensures that applicants get at least 1 interview
            this.hireAllApplicants();
        }
    }

    /**
     * Cancel all incomplete interviews before remaining applicants are automatically hired.
     */
    private void cancelAllIncompleteInterviews() {
        for (JobApplication jobApp : this.applicationsInConsideration) {
            Interview lastInterview = jobApp.getLastInterview();
            if (lastInterview.isIncomplete()) {
                this.updateInterviewersOfInterviewCompletionOrCancellation(lastInterview);
            }
        }
    }

    /**
     * Set up one-on-one interviews for all applications in consideration.
     * Note: Interviewer would set the date/time
     */
    public void setUpOneOnOneInterviews() {
        String field = this.branchJobPosting.getField();
        if (!this.branchJobPosting.getBranch().hasInterviewerForField(field)) {
            this.notifyAllObservers(new NotificationFactory().createNotification(NotificationFactory.NO_INTERVIEWERS_IN_FIELD, this));
        }
        else {
            for (JobApplication jobApp : this.applicationsInConsideration) {
                Interviewer interviewer = this.branchJobPosting.getBranch().findInterviewerByField(field);
                Interview interview = new Interview(jobApp, interviewer, this);
                this.addInterviewForInterviewers(interview);
            }
        }
    }

    /**
     * Set up a group interview for all applications in consideration.
     *
     * @param interviewCoordinator The interview coordinator selected.
     * @param otherInterviewers    The other interviewers selected.
     * @param today                Today's date.
     * @param minNumDaysNotice     The minimum number of days after which the interview can be held.
     */
    public void setUpGroupInterview(Interviewer interviewCoordinator, ArrayList<Interviewer> otherInterviewers,
                                    LocalDate today, int minNumDaysNotice) {
        Interview interview = new Interview(this.applicationsInConsideration, interviewCoordinator,
                otherInterviewers, this);
        ArrayList<Interviewer> allInterviewers = interview.getAllInterviewers();
        InterviewTime interviewTime = this.getEarliestTimeAvailableForAllInterviewers(allInterviewers, today.plusDays(minNumDaysNotice));
        interview.setTime(interviewTime);
        this.updateParticipantsOfNewInterview(interview);
    }

    /**
     * Update the interviewers for this interview of completion or cancellation.
     *
     * @param interview The interview that has been completed or cancelled.
     */
    void updateInterviewersOfInterviewCompletionOrCancellation(Interview interview) {
        for (Interviewer interviewer : interview.getAllInterviewers()) {
            interviewer.removeInterview(interview);
        }
    }

    /**
     * Update the list of applications in consideration when an interview is completed.
     *
     * @param interview The interview that has been completed.
     */
    void updateApplicationsInConsideration(Interview interview) {
        for (JobApplication jobApp : interview.getJobApplications()) {
            if (!interview.isPassed(jobApp)) {
                this.reject(jobApp);
            }
        }
    }

    // ============================================================================================================== //
    // === Private methods ===
    // === Other methods ===

    /**
     * Get the earliest interviewTime available for all these interviewers from this date forward.
     *
     * @param allInterviewers The interviewers participating in a group interview.
     * @param earliestDate    The earliest date that this group interview can be scheduled.
     * @return the earliest InterviewTime when all these interviewers are available.
     */
    private InterviewTime getEarliestTimeAvailableForAllInterviewers(ArrayList<Interviewer> allInterviewers,
                                                                     LocalDate earliestDate) {
        Interviewer interviewer1 = allInterviewers.get(0);
        LocalDate dateAvailable = interviewer1.getFirstDateAvailableOnOrAfterDate(earliestDate);
        ArrayList<String> timeSlotsAvailableForInterviewer1 = interviewer1.getTimeSlotsAvailableOnDate(dateAvailable);
        int i = 1;
        while (!timeSlotsAvailableForInterviewer1.isEmpty() && i < allInterviewers.size()) {
            ArrayList<String> timeSlotsFilledForInterviewer = allInterviewers.get(i).getTimeSlotsFilledOnDate(dateAvailable);
            timeSlotsAvailableForInterviewer1.removeAll(timeSlotsFilledForInterviewer);
            i++;
        }
        if (timeSlotsAvailableForInterviewer1.isEmpty()) {
            return this.getEarliestTimeAvailableForAllInterviewers(allInterviewers, earliestDate.plusDays(1));
        } else {
            String timeSlot = timeSlotsAvailableForInterviewer1.get(0);
            return new InterviewTime(dateAvailable, timeSlot);
        }
    }

    /**
     * Add an interview for all interviewers involved.
     *
     * @param interview The interview that was set up.
     */
    private void addInterviewForInterviewers(Interview interview) {
        for (Interviewer interviewer : interview.getAllInterviewers()) {
            interviewer.addInterview(interview);
        }
    }

    /**
     * Add an interview for all job applications involved.
     *
     * @param interview The interview that was set up.
     */
    public void addInterviewForJobApplications(Interview interview) {
        for (JobApplication jobApp : interview.getJobApplications()) {
            jobApp.addInterview(interview);
            jobApp.getStatus().advanceStatus();
        }
    }

    /**
     * Update all the participants selected of the new interview that was created.
     *
     * @param interview The interview that was set up.
     */
    private void updateParticipantsOfNewInterview(Interview interview) {
        this.addInterviewForInterviewers(interview);
        if (interview.getType().equals(Interview.GROUP)) {
            this.addInterviewForJobApplications(interview);
        }
    }

    /**
     * Checks whether the next round of interviews is a group interview.
     *
     * @return true iff the next interview round is a group interview.
     */
    private boolean isCurrentRoundGroupInterviewUnscheduled() {
        Interview lastInterview;
        if (this.applicationsInConsideration.isEmpty()) {
            lastInterview = this.applicationsRejected.get(applicationsRejected.size() - 1).getLastInterview();
        } else {
            lastInterview = this.applicationsInConsideration.get(0).getLastInterview();
        }
        return this.currentRound > -1 && this.interviewConfiguration.get(this.currentRound)[0].equals(Interview.GROUP) &&
                (lastInterview == null || lastInterview.getRoundNumber() < this.currentRound);
    }


    /**
     * Check whether there are any applications in consideration.
     *
     * @return true iff the number of applications in consideration is equal to 0.
     */
    private boolean hasNoJobApplicationsInConsideration() {
        return this.applicationsInConsideration.isEmpty();
    }

    /**
     * Check if interview process is over, ie, the number of applications left is under or at the threshold
     * and/or all the interview rounds have been completed.
     *
     * @return true iff the number of applications left is under or at the threshold
     * and/or all the interview rounds have been completed.
     */
    private boolean isInterviewProcessOver() {
        return this.currentRoundIsOver() && (this.isNumApplicationsUnderOrAtThreshold() |
                this.currentRound == this.getFinalRoundNumber());
    }

    /**
     * Check if the number of applications in consideration is less than or equal to the number of positions available
     * in the posting and is not zero.
     *
     * @return true iff number of applications in consideration is less than or equal to the number of positions
     * available and is not zero.
     */
    private boolean isNumApplicationsUnderOrAtThreshold() {
        int jobAppsSize = this.applicationsInConsideration.size();
        return jobAppsSize > 0 && jobAppsSize <= this.branchJobPosting.getNumPositions();
    }

    /**
     * Hire the applicants in this list.
     *
     * @param jobAppsToHire The job applications of those to be hired.
     */
    public void hireApplicants(ArrayList<JobApplication> jobAppsToHire) {
        ArrayList<JobApplication> applications = (ArrayList<JobApplication>)this.applicationsInConsideration.clone();
        for (JobApplication jobApp : applications) {
            if (jobAppsToHire.contains(jobApp)) {
                jobApp.getStatus().setHired();
            } else {
                this.reject(jobApp);
            }
        }
        this.branchJobPosting.setFilled();
        this.archiveRejected();
    }

    private void hireAllApplicants() {
        this.cancelAllIncompleteInterviews();
        this.hireApplicants(this.applicationsInConsideration);
        if (this.applicationsInConsideration.size() < this.branchJobPosting.getNumPositions()) {
            this.notifyAllObservers(new NotificationFactory().createNotification(NotificationFactory.AUTO_HIRING_LESS,
                    this.getBranchJobPosting()));
        } else {
            this.notifyAllObservers(new NotificationFactory().createNotification(NotificationFactory.AUTO_HIRING_EXACT,
                    this.getBranchJobPosting()));
        }
    }

    @Override
    public void updateObserverList() {
        for (Observer observer : this.getBranchJobPosting().getBranch().getHrCoordinators()) {
            if (!this.containsObserver(observer)){
                this.attach(observer);
            }
        }
    }
}