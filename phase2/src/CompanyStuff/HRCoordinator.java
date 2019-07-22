package CompanyStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import Main.User;
import ApplicantStuff.JobApplication;
import Miscellaneous.InterviewTime;

import java.time.LocalDate;
import java.util.ArrayList;

public class HRCoordinator extends User {
    /**
     * An account for an HR Coordinator.
     */

    // === Instance variables ===
    // The branch that this HR Coordinator works for
    private Branch branch;


    // === Public methods ===
    // === Constructors ===
    public HRCoordinator() {
    }

    public HRCoordinator(String username, String password, String legalName, String email, Branch branch,
                         LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.branch = branch;
        branch.addHRCoordinator(this);
    }

    // === Getters ===
    public Branch getBranch() {
        return this.branch;
    }

    // === Setters ===
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // === Other methods ===
    /**
     * Create and add a job posting to the system.
     *
     * @param jobTitle          The job title.
     * @param jobDescription    The job description.
     * @param requiredDocuments The required documents for this job posting.
     * @param tags              The tags for this job posting.
     * @param numPositions      The number of positions for this job.
     * @param postDate          The date this job posting was posted.
     * @param applicationCloseDate  The last day for application submissions.
     * @param referenceCloseDate    The last day for reference letter submissions.
     */
    public BranchJobPosting addJobPosting(String jobTitle, String jobField, String jobDescription,
                                          ArrayList<String> requiredDocuments, ArrayList<String> tags, int numPositions,
                                          LocalDate postDate, LocalDate applicationCloseDate, LocalDate referenceCloseDate) {
        CompanyJobPosting companyJobPosting = new CompanyJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, this.branch.getCompany(), this.branch);
        this.branch.getCompany().addCompanyJobPosting(companyJobPosting);
        BranchJobPosting branchJobPosting = new BranchJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, numPositions, this.branch, postDate, applicationCloseDate, referenceCloseDate);
        this.branch.getJobPostingManager().addJobPosting(branchJobPosting);
        return branchJobPosting;
    }

    public BranchJobPosting implementJobPosting(CompanyJobPosting companyJobPosting, int numPositions, LocalDate postDate,
                                                LocalDate applicationCloseDate, LocalDate referenceCloseDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(companyJobPosting.getTitle(), companyJobPosting.getField(),
                companyJobPosting.getDescription(), companyJobPosting.getRequiredDocuments(), companyJobPosting.getTags(),
                numPositions, this.branch, postDate, applicationCloseDate, referenceCloseDate);
        this.branch.getJobPostingManager().addJobPosting(branchJobPosting);
        return branchJobPosting;
    }

    /**
     * Get a job application with this ID.
     *
     * @param ID The ID in question.
     * @return the job application with this ID or null if cannot be accessed/found.
     */
    public JobApplication findJobAppById(int ID) {
        ArrayList<BranchJobPosting> jobPostings = this.branch.getJobPostingManager().getBranchJobPostings();
        for (BranchJobPosting jobPosting : jobPostings) {
            for (JobApplication jobApplication : jobPosting.getJobApplications()) {
                if (jobApplication.getId() == ID) {
                    return jobApplication;
                }
            }
        }
        return null;
    }

    /**
     * Choose the interview configuration for this job posting.
     *
     * @param jobPosting    The job posting in question.
     * @param configuration The configuration chosen.
     */
    public void chooseInterviewConfiguration(BranchJobPosting jobPosting, ArrayList<Object[]> configuration) {
        jobPosting.getInterviewManager().setInterviewConfiguration(configuration);
    }

    /**
     * Schedule a time for the group interview.
     *
     * @param interview The interview to be scheduled.
     * @param time      The time to be scheduled.
     * @return true iff all interviewers for this interview are available at this time.
     */
    public boolean scheduleGroupInterview(Interview interview, InterviewTime time) {
        for (Interviewer interviewer : interview.getAllInterviewers()) {
            if (!interviewer.isAvailable(time)) {
                return false;
            }
        }
        // TODO notify applicants and interviewers of the scheduled interview time
        interview.setTime(time);
        return true;
    }

    // HR Coordinator class
    /*    */

    /**
     * Get the task that the HR Coordinator must accomplish for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param today      Today's date.
     * @return an integer that represents the task that the HR Coordinator must accomplish for this job posting.
     */
    int getTask(BranchJobPosting jobPosting, LocalDate today) {
        if (this.branch.getJobPostingManager().getClosedJobPostingsNoApplicantsChosen(today).contains(jobPosting)) {
            return InterviewManager.SELECT_APPS_FOR_FIRST_ROUND;
        } else {
            return jobPosting.getInterviewManager().getHrTask();
        }
    }

    @Override
    public String[] getDisplayedProfileCategories() {
        return new String[]{"User Type", "Username", "Legal Name", "Email", "Company Branch", "Account Created"};
    }

    @Override
    public String[] getDisplayedProfileInformation() {
        return new String[]{"HR Coordinator", this.getUsername(), this.getLegalName(), this.getEmail(),
                this.getBranch().getName(), this.getDateCreated().toString()};
    }
}
