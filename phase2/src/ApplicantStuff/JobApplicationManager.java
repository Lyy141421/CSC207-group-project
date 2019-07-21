package ApplicantStuff;

import CompanyStuff.Interview;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTimeComparator;
import Miscellaneous.CloseDateComparator;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class JobApplicationManager implements Serializable {

    // === Instance variables ===
    // List of job applications submitted sorted chronologically by close date
    private ArrayList<JobApplication> jobApplications = new ArrayList<>();
    // Number of days before an interview when the interview is considered "upcoming"
    private static final int upcomingDays = 7;

    // === Public methods ===

    // === Constructors ===
    public JobApplicationManager() {
    }

    public JobApplicationManager(ArrayList<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    // === Getters ===
    public ArrayList<JobApplication> getJobApplications() {
        return this.jobApplications;
    }

    // === Other methods ===

    /**
     * Add a job application to this applicant's application list.
     *
     * @param application The application being added.
     */
    public void addJobApplication(JobApplication application) {
        this.jobApplications.add(application);
        this.jobApplications.sort(new CloseDateComparator());
    }

    /**
     * Remove this job application.
     *
     * @param jobPosting The job posting from which the job application is to be removed.
     */
    public void removeJobApplication(BranchJobPosting jobPosting) {
        JobApplication app = this.findJobApplication(jobPosting);
        this.jobApplications.remove(app);
    }

    /**
     * Get the list of interviews considered "upcoming" for this applicant.
     *
     * @return the list of interviews considered "upcoming" for this applicant, based on upcomingDays.
     */
    public ArrayList<Interview> getUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = new ArrayList<>();
        for (JobApplication application : jobApplications) {
            for (Interview interview : application.getInterviews()) {
                if (interview.getTime() == null) {
                    break;
                }
                LocalDate interviewDate = interview.getTime().getDate();
                if (interviewDate.isEqual(today) || (interviewDate.isAfter(today) &&
                        today.plusDays(upcomingDays + 1).isAfter(interviewDate))) {
                    upcomingInterviews.add(interview);
                }
            }
        }
        upcomingInterviews.sort(new InterviewTimeComparator());
        return upcomingInterviews;
    }

    /**
     * Get the number of days since the most recent job posting close date.
     *
     * @param today Today's date.
     * @return the number of days since the most recent job posting close date.
     */
    public long getNumDaysSinceMostRecentCloseDate(LocalDate today) {
        if (this.getLastClosedJobApp() == null) {
            return 0;
        } else {
            LocalDate closeDate = this.getLastClosedJobApp().getJobPosting().getApplicantCloseDate();
            return Math.max(0, DAYS.between(closeDate, today));
        }
    }

    /**
     * Get the previous job applications for this applicant.
     *
     * @return a list of previous job applications submitted where the posting is now filled.
     */
    public ArrayList<JobApplication> getPreviousJobApplications() {
        ArrayList<JobApplication> previousJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (jobApplication.isArchived() || jobApplication.isHired()) {
                previousJobApps.add(jobApplication);
            }
        }
        return previousJobApps;
    }

    /**
     * Get the current job applications for this applicant.
     *
     * @return a list of current job applications submitted (posting is not yet filled).
     */
    public ArrayList<JobApplication> getCurrentJobApplications() {
        ArrayList<JobApplication> currentJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (!this.getPreviousJobApplications().contains(jobApplication)) {
                currentJobApps.add(jobApplication);
            }
        }
        return currentJobApps;
    }

    /**
     * Get a list of files submitted for this job application.
     *
     * @param jobApplication The job application in question.
     * @return a list of files submitted for this job application
     */
    public ArrayList<JobApplicationDocument> getFilesSubmittedForApplication(JobApplication jobApplication) {
        return jobApplication.getFilesSubmitted();
    }

    /**
     * Find the application with the last closing date of all submitted applications by this applicant.
     *
     * @return the application with the last close date, or null if no applications exist.
     */
    public JobApplication getLastClosedJobApp() {
        if (this.jobApplications.isEmpty()) {
            return null;
        } else {
            return this.jobApplications.get(this.jobApplications.size() - 1);
        }
    }

    // ============================================================================================================== //
    // === Private methods ===

    /**
     * Find the job application associated with this job posting.
     *
     * @param posting The job posting in question.
     * @return the job application associated with this job posting.
     */
    private JobApplication findJobApplication(BranchJobPosting posting) {
        for (JobApplication app : this.jobApplications) {
            if (app.getJobPosting().equals(posting)) {
                return app;
            }
        }
        return null;
    }
}
