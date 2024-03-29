package ApplicantStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class Reference extends User {

    // === Class variables ===
    static final long serialVersionUID = 1L;

    // === Instance variables ===
    // The job applications that need references from this referee
    private ArrayList<JobApplication> jobAppsForReference;

    // === Constructor ===
    public Reference(String email, LocalDate dateCreated) {
        super(email, dateCreated);
        this.jobAppsForReference = new ArrayList<>();
    }

    // === Getter ===
    public ArrayList<JobApplication> getJobAppsForReference() {
        return this.jobAppsForReference;
    }

    // === Other methods ===

    /**
     * Add a job application to this reference's list of job applications that they need to submit a reference for.
     *
     * @param jobApp The job application to be added.
     */
    public void addJobApplication(JobApplication jobApp) {
        this.jobAppsForReference.add(jobApp);
    }

    /**
     * Remove a job application from this reference's list of job applications that they need to submit a reference for.
     *
     * @param jobApp The job application to be removed.
     */
    public void removeJobApplication(JobApplication jobApp) {
        this.jobAppsForReference.remove(jobApp);
    }

    /**
     * Remove the job applications for this job posting form this reference's list of job applications that still need
     * reference letters.
     *
     * @param jobPosting The job posting to be removed.
     */
    public void removeJobPosting(BranchJobPosting jobPosting) {
        for (JobApplication jobApp : (ArrayList<JobApplication>) this.jobAppsForReference.clone()) {
            if (jobApp.getJobPosting().equals(jobPosting)) {
                this.removeJobApplication(jobApp);
            }
        }
    }

    /**
     * Get a list of job postings that this reference's referees are applying to.
     *
     * @return a list of job postings that this reference's referees are applying to.
     */
    public ArrayList<BranchJobPosting> getJobPostings() {
        ArrayList<BranchJobPosting> jobPostings = new ArrayList<>();
        ArrayList<JobApplication> jobApps = this.jobAppsForReference;
        for (JobApplication jobApp : jobApps) {
            BranchJobPosting jobPosting = jobApp.getJobPosting();
            if (!jobPostings.contains(jobPosting)) {
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    /**
     * Get an array of job posting names and ids.
     *
     * @return an array of job posting names and ids that this person's referees have applied to.
     */
    public String[] getJobPostingNameList() {
        ArrayList<String> jobPostingNamesAndIds = new ArrayList<>();
        for (BranchJobPosting branchJobPosting : this.getJobPostings()) {
            jobPostingNamesAndIds.add("ID: " + branchJobPosting.getId() + " - " + branchJobPosting.getTitle());
        }
        return jobPostingNamesAndIds.toArray(new String[jobPostingNamesAndIds.size()]);
    }

    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Email", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"Reference", this.getEmail(), this.getDateCreated().toString()};
    }
}
