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

    /**
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    ArrayList<CompanyJobPosting> findApplicablePostings(String field, String companyName, String id, String tags, boolean
            byLocation) {

        ArrayList<CompanyJobPosting> applicableJobPostings = jobAppSystem.getAllOpenCompanyJobPostings();
        if (!id.isEmpty()) {
            int idInteger = Integer.valueOf(id);
            return new ArrayList<>(Arrays.asList(jobAppSystem.getCompanyJobPostingWithID(idInteger)));
        }
        if (!companyName.isEmpty()) {
            this.keepIntersection(applicableJobPostings, jobAppSystem.getOpenCompanyJobPostingsInCompany(companyName));
        }
        if (!field.isEmpty()) {
            this.keepIntersection(applicableJobPostings, jobAppSystem.getOpenCompanyJobPostingsInField(field));
        }
        if (!tags.isEmpty()) {
            String[] tagsArray = tags.split(", ");
            this.keepIntersection(applicableJobPostings, jobAppSystem.getCompanyJobPostingsWithTags(tagsArray));
        }
        if (byLocation) {
            this.keepIntersection(applicableJobPostings, jobAppSystem.getCompanyJobPostingsInCMA(this.applicant));
        }
        return applicableJobPostings;
    }

    ArrayList<BranchJobPosting> getApplicableBranchJobPostings(CompanyJobPosting companyJobPosting) {
        return this.applicant.getApplicableBranchJobPostings(companyJobPosting, jobAppSystem.getToday());
    }

    private void keepIntersection(ArrayList<CompanyJobPosting> listToBeUpdated, ArrayList<CompanyJobPosting> otherList) {
        ArrayList<CompanyJobPosting> listToBeUpdatedClone = (ArrayList<CompanyJobPosting>) listToBeUpdated.clone();
        for (CompanyJobPosting jobPosting : listToBeUpdatedClone) {
            if (!otherList.contains(jobPosting)) {
                listToBeUpdated.remove(jobPosting);
            }
        }
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
     * //     * Takes a list of postings and converts them to Name - ID form for card/navigation purposes
     * //     * @param jobPostings the postings in question
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
