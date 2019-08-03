package GUIClasses.ApplicantInterface;

import ApplicantStuff.*;
import CompanyStuff.Branch;
import CompanyStuff.Interview;
import CompanyStuff.JobPostings.*;
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
    /**
     * Check if there are interviews on the date of login
     */
    boolean checkUpcomingInterviews() {
        ArrayList<Interview> upcomingInterviews = applicant.getJobApplicationManager().getUpcomingInterviews(
                jobAppSystem.getToday());
        return (upcomingInterviews.size() > 0);
    }

    /*
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    // TODO fix
//    ArrayList<BranchJobPosting> findApplicablePostings(String field,
//                                                       String companyName, String ID, String Tags) {
//        ArrayList<BranchJobPosting> openJobPostings =
//                applicant.getOpenJobPostingsNotAppliedTo(jobAppSystem); //TODO are you serious
//        ArrayList<BranchJobPosting> ret = new ArrayList<>();
//        for (BranchJobPosting posting : openJobPostings) {
//            String filterField = (field == null) ? posting.getField() : field;
//            String filterCompanyName = (companyName == null) ? posting.getBranch().getName() : companyName;
//            //ID, tags
//            if (filterField.equalsIgnoreCase(posting.getField())
//                    && filterCompanyName.equalsIgnoreCase(posting.getBranch().getName())) {
//                ret.add(posting);
//            }
//        }
//        return ret;
//    }


    /**
     * Returns an arraylist containing all the job postings that apply to the applicant
     */
    ArrayList<CompanyJobPosting> findApplicablePostings(String field, String companyName, String id, String tags, boolean
            byLocation) {
        String[] tagsArray = tags.split(", ");
        int idInteger = Integer.valueOf(id);

        ArrayList<CompanyJobPosting> applicableJobPostings = jobAppSystem.getAllOpenCompanyJobPostings();
        if (!id.isEmpty()) {
            return new ArrayList<>(Arrays.asList(jobAppSystem.getCompanyJobPostingWithID(idInteger)));
        }
        if (!companyName.isEmpty()) {
            this.keepIntersection(applicableJobPostings, jobAppSystem.getOpenCompanyJobPostingsInCompany(companyName));
        }
        if (!field.isEmpty()) {
            this.keepIntersection(applicableJobPostings, jobAppSystem.getOpenCompanyJobPostingsInField(field));
        }
        if (!tags.isEmpty()) {
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
        return applicant.withdrawJobApplication(jobAppSystem.getToday(), application.getJobPosting());
    }

    /**
     * Return status since last job application (none submitted or days since)
     */
    String daysSince() {
        if (applicant.getJobApplicationManager().getJobApplications().isEmpty())
            return ("You have not yet submitted any job applications.");
        else
            return ("It has been " +
                    applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(jobAppSystem.getToday()) +
                    " days since your most recent application closed.");
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
