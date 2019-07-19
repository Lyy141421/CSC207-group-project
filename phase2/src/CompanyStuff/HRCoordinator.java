package CompanyStuff;

import JobPostings.BranchJobPosting;
import JobPostings.CompanyJobPosting;
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
     * @param closeDate         The date this job posting is closed.
     */
    public BranchJobPosting addJobPosting(String jobTitle, String jobField, String jobDescription,
                                          ArrayList<String> requiredDocuments, ArrayList<String> tags, int numPositions,
                                          LocalDate postDate, LocalDate closeDate) {
        BranchJobPosting branchJobPosting = new BranchJobPosting(jobTitle, jobField, jobDescription, requiredDocuments,
                tags, numPositions, this.branch, postDate, closeDate);
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
        jobPosting.createInterviewManager();
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

    @Override
    public String toString() {
        return null;    //TODO
    }
}
