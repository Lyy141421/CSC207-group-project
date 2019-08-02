package GUIClasses.ApplicantInterface;

import ApplicantStuff.*;
import CompanyStuff.Interview;
import CompanyStuff.JobPostings.*;
import Main.JobApplicationSystem;
import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

class ApplicantBackend {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private LocalDate today;
    private Applicant applicant; // The applicant currently logged in
    private JobApplicationSystem jobAppSystem;

    // === Constructor ===
    ApplicantBackend(User user) {
        this.applicant = (Applicant) user;
    }

    ApplicantBackend(User user, JobApplicationSystem jobAppSystem) {
        this.applicant = (Applicant) user;
        this.jobAppSystem = jobAppSystem;
    }
    /**
     * Check if there are interviews on the date of login
     */
    boolean checkUpcomingInterviews(LocalDate today) {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(today);
        return (upcomingInterviews.size() > 0);
    }

    /**
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    ArrayList<BranchJobPosting> findApplicablePostings(String field,
                                                       String companyName, String ID, String Tags) {
        ArrayList<BranchJobPosting> openJobPostings =
                applicant.getOpenJobPostingsNotAppliedTo(new JobApplicationSystem()); //TODO are you serious
        ArrayList<BranchJobPosting> ret = new ArrayList<>();
        for (BranchJobPosting posting : openJobPostings) {
            String filterField = (field == null) ? posting.getField() : field;
            String filterCompanyName = (companyName == null) ? posting.getBranch().getName() : companyName;
            //ID, tags
            if (filterField.equalsIgnoreCase(posting.getField())
                    && filterCompanyName.equalsIgnoreCase(posting.getBranch().getName())) {
                ret.add(posting);
            }
        }
        return ret;
    }

    /**
     * Returns a list of applicant's applications
     */
    ArrayList<JobApplication> getApps() {
        return applicant.getJobApplicationManager().getJobApplications();
    }

    /**
     * Returns a list of applicant's application's jobpostings
     */
    ArrayList<BranchJobPosting> helperPostings() {
        ArrayList<BranchJobPosting> ret = new ArrayList<>();
        for (JobApplication app : this.getApps()) {
            ret.add(app.getJobPosting());
        }
        return ret;
    }

    /**
     * Withdraw an application
     */
    boolean withdrawApp(JobApplication application) {
        return applicant.withdrawJobApplication(today, application.getJobPosting());
    }

    /**
     * Return status since last job application (none submitted or days since)
     */
    String daysSince() {
        if (applicant.getJobApplicationManager().getJobApplications().isEmpty())
            return ("You have not yet submitted any job applications.");
        else
            return ("It has been " +
                    applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(today) +
                    " days since your most recent application closed.");
    }

    /**
     * //     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     * //     * @param jobPostings the postings in question
     * //
     */
    String[] getListNames(ArrayList<BranchJobPosting> jobPostings) {
        int len = jobPostings.size();
        String[] ret = new String[len];

        for (int i = 0; i < len; i++) {
            ret[i] = jobPostings.get(i).getTitle() + " - " + jobPostings.get(i).getId();
        }

        return ret;
    }
}
