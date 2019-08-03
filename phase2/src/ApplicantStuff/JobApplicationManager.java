package ApplicantStuff;

import CompanyStuff.Interview;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Miscellaneous.InterviewTimeComparator;
import Miscellaneous.CloseDateComparator;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class JobApplicationManager implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Number of days before an interview when the interview is considered "upcoming"
    private static final int UPCOMING_DAYS = 7;

    // === Instance variables ===
    // List of job applications submitted sorted chronologically by close date
    private ArrayList<JobApplication> jobApplications = new ArrayList<>();

    // === Public methods ===
    // === Constructors ===
    public JobApplicationManager() {
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
    void addJobApplication(JobApplication application) {
        this.jobApplications.add(application);
        this.jobApplications.sort(new CloseDateComparator());
    }

    /**
     * Remove this job application.
     *
     * @param jobPosting The job posting from which the job application is to be removed.
     */
    void removeJobApplication(BranchJobPosting jobPosting) {
        JobApplication app = this.findJobApplication(jobPosting);
        this.jobApplications.remove(app);
    }

    /**
     * Get the list of interviews considered "upcoming" for this applicant.
     *
     * @return the list of interviews considered "upcoming" for this applicant, based on UPCOMING_DAYS.
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
                        today.plusDays(UPCOMING_DAYS + 1).isAfter(interviewDate))) {
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
     * Above, but returning their postings
     */
    public ArrayList<CompanyJobPosting> getCurrentJobAppsPostings() {
        ArrayList<CompanyJobPosting> currentJobApps = new ArrayList<>();
        for (JobApplication jobApplication : this.getJobApplications()) {
            if (!this.getPreviousJobApplications().contains(jobApplication)) {
                currentJobApps.add(jobApplication.getJobPosting());
            }
        }
        return currentJobApps;
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
    // TODO set as private after testing
    public JobApplication findJobApplication(BranchJobPosting posting) {
        for (JobApplication app : this.jobApplications) {
            if (app.getJobPosting().equals(posting)) {
                return app;
            }
        }
        return null;
    }
}
