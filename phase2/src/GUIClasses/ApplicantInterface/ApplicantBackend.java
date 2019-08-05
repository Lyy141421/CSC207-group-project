package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Interview;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Main.JobApplicationSystem;
import Main.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

class ApplicantBackend {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
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

    Applicant getApplicant() {
        return applicant;
    }

    ArrayList<CompanyJobPosting> getCurrentJobPostings() {
        return applicant.getJobApplicationManager().getCurrentJobAppsPostings();
    }

    /**
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    ArrayList<CompanyJobPosting> findApplicablePostings(ArrayList<Object> inputs) {
        String field = (String) inputs.get(0);
        String companyName = (String) inputs.get(1);
        String id = (String) inputs.get(2);
        String tags = (String) inputs.get(3);
        Boolean byLocation = (Boolean) inputs.get(4);

        ArrayList<CompanyJobPosting> applicableJobPostings = jobAppSystem.getAllOpenCompanyJobPostings();
        if (!id.isEmpty()) {
            int idInteger = Integer.valueOf(id);
            CompanyJobPosting companyJobPosting = jobAppSystem.getCompanyJobPostingWithID(idInteger);
            if (companyJobPosting != null) {
                return new ArrayList<>(Arrays.asList(companyJobPosting));
            } else {
                return new ArrayList<>();
            }
        }
        if (!companyName.isEmpty()) {
            applicableJobPostings.retainAll(jobAppSystem.getOpenCompanyJobPostingsInCompany(companyName));
        }
        if (!field.isEmpty()) {
            applicableJobPostings.retainAll(jobAppSystem.getOpenCompanyJobPostingsInField(field));
        }
        if (!tags.isEmpty()) {
            String[] tagsArray = tags.split(", ");
            applicableJobPostings.retainAll(jobAppSystem.getCompanyJobPostingsWithTags(tagsArray));
        }
        if (byLocation) {
            applicableJobPostings.retainAll(jobAppSystem.getCompanyJobPostingsInCMA(this.applicant));
        }
        return applicableJobPostings;
    }

    ArrayList<BranchJobPosting> getApplicableBranchJobPostings(CompanyJobPosting companyJobPosting) {
        return this.applicant.getApplicableBranchJobPostings(companyJobPosting, jobAppSystem.getToday());
    }

    void addReferences(JobApplication jobApp, ArrayList<String> emails) {
        jobAppSystem.getUserManager().addReferences(jobApp, emails);
    }

    File getApplicantFolder() {
        return this.applicant.getDocumentManager().getFolder();
    }

    /**
     * Returns a list of applicant's applications
     */
    ArrayList<JobApplication> getApps() {
        return applicant.getJobApplicationManager().getJobApplications();
    }

    ArrayList<Interview> getUpcomingInterviews() {
        return applicant.getJobApplicationManager().getUpcomingInterviews(jobAppSystem.getToday());
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

    boolean hasFilesInAccount() {
        return this.applicant.getDocumentManager().getFolder().listFiles().length > 0;
    }

    JobApplication createJobApplication(BranchJobPosting jobPosting) {
        return new JobApplication(this.applicant, jobPosting, this.jobAppSystem.getToday());
    }

    /**
     * Withdraw an application
     */
    boolean withdrawApp(JobApplication application) {
        return applicant.withdrawJobApplication(jobAppSystem.getToday(), application.getJobPosting());
    }

    /**
     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     * @param jobPostings the postings in question
     * //
     */
    String[] getListNames(ArrayList<CompanyJobPosting> jobPostings) {
        int len = jobPostings.size();
        String[] ret = new String[len];

        for (int i = 0; i < len; i++) {
            ret[i] = jobPostings.get(i).getTitle() + " - " + jobPostings.get(i).getId();
        }

        return ret;
    }
}
