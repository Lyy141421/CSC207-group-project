package CompanyStuff;

import ApplicantStuff.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPosting extends JobPosting{

    private int numPositions;
    private Branch branch; // The company that listed this job posting
    private LocalDate postDate; // The date on which this job posting was listed
    private LocalDate closeDate; // The date on which this job posting is closed
    private boolean filled; // Whether the job posting is filled
    private ArrayList<JobApplication> jobApplications; // The list of applications for this job posting
    private InterviewManager interviewManager; // UsersAndJobObjects.Interview manager for this job posting

    BranchJobPosting(JobPosting posting, int numPositions, Branch branch, LocalDate postDate, LocalDate closeDate) {
        super(posting.getTitle(), posting.getField(), posting.getDescription(), posting.getRequirements(),
                posting.getBranches());
        this.numPositions = numPositions;
        this.branch = branch;
        this.postDate = postDate;
        this.closeDate = closeDate;
        this.filled = false;
        this.jobApplications = new ArrayList<>();
        this.interviewManager = new InterviewManager();
    }

    private int id;

    public int getId() {
        return id;
    }

    public ArrayList<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public InterviewManager getInterviewManager() {
        return interviewManager;
    }
}
