import java.util.ArrayList;

class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */

    // === Instance variables ===
    // The HR Coordinator who is logged in
    private HRCoordinator HRC;

    // === Constructor ===

    /**
     * Create the interface for this HR Coordinator.
     *
     * @param HRC The HR Coordinator who is logged in.
     */
    HRCoordinatorInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    /**
     * Interface for getting a job posting by this HR Coordinator's company.
     * @return the job posting being searched for.
     */
    JobPosting getJobPosting() {
        /*1. Get the job posting that this HR Coordinator wants to view
        2. Find whether the job posting exists for their company
        Note: HR Coordinators can see any application for any job posting in their company*/
        /*
        ArrayList<JobPosting> jobPostings = this.HRC.getCompany().searchJobPostingByTitle(jobTitle);
        if (jobPostings.size() > 1) {
            // choose job posting from list
        }
        else {
            return jobPostings.get(0);
        }
        */
        return null; // stub
    }

    /**
     * Interface for adding a new job posting to the system.
     */
    void addJobPosting() {
        // 1. Get data for job posting -- Job title, Job field, Description, Requirements, Close date
        // 2. Create job posting
        // this.HRC.addJobPosting(jobTitle, jobField, jobDescription, requirements, numPositions, postDate, closeDate);
    }

    /**
     * Interface for viewing all job postings.
     */
    void viewAllJobPostings() {
        // Interface stuff
        // JobApplicationSystem.getAllJobPostings();
    }

    /**
     * Interface for viewing all applications for a specific job posting.
     */
    void viewAllApplicationsForJobPosting() {
        /*
        1. Get the job posting that this HR Coordinator wants to view
        2. Find whether the job posting exists for their company
        Note: HR Coordinators can see any application for any job posting in their company
        3. Display all the applications for that job posting
             - Display a menu of all the applicants' names and the application date for each
             - Display a menu of stuff that the HR Coordinator can see
                 - Cover letter, CV
         */

        // JobPosting jobPosting = this.getJobPosting();
        // Interface stuff
    }

    /**
     * Interface for searching for a specific job application.
     */
    void searchSpecificJobApplication() {
        /*
        1. Get the job posting title -- Show a drop-down menu of all the job postings for their company
        2. Get the applicant username -- Drop down menu?
        3. Find application, if it exists
         */
        /*
        JobPosting jobPosting = this.getJobPosting();
        Applicant applicant = JobApplicationSystem.getUserManager().findUserByUsername(username);
        jobPosting.findJobApplication(applicant);
        */
    }

    /**
     * Interface for reviewing all job applications after a job posting has closed.
     */
    void reviewApplicationsForJobPosting() {
        /*
        1. Get the job posting title
        2. Show all applications for job posting with a button on the side to advance (default is reject)
        */

        /*JobPosting jobPosting = this.getJobPosting();
        jobPosting.reviewApplications();     // This advances the jobApp status to "under review"
        InterviewManager interviewManager = new InterviewManager(jobPosting, jobPosting.getJobApplications(),
                new ArrayList<>());
        jobPosting.setInterviewManager(interviewManager);
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            if (advance) {
                jobApp.advanceStatus();
            }
            else {
                jobPosting.getInterviewManager().reject(jobApp);
            }
        }*/
    }

    /**
     * Interface for setting up interviews for an entire interview round.
     */
    void setUpInterviewsForRound() {
        /*
        1. Get the job posting by title.
        2. Show list of applications still in the running
        3. Set up an interview for each job posting
         */
        /*JobPosting jobPosting = this.getJobPosting();
        for (JobApplication jobApp : jobPosting.getInterviewManager().getApplicationsInConsideration()) {
            this.setUpInterviewForJobApplication(jobApp);
        }*/
    }

    /**
     * Interface for setting up an interview for a job application.
     * @param jobApplication    The job application for which an interview is to be set up.
     */
    void setUpInterviewForJobApplication(JobApplication jobApplication) {
        /*
        1. Find the interviewer in the specific job field that has the least number of interviews scheduled
        2. Match interviewer with application
         */
        /*
        jobApplication.advanceStatus();
        jobApplication.setUpInterview(this.HRC, jobApp.getStatus());*/
    }

    /**
     * Interface for hiring an applicant.
     */
    void hireApplicant() {
        /*
        1. Get the job posting or automatically display to HR Coordinator when logging in that the interview process
        for this job posting has been completed.
        2. Get a list of final candidates
        3. Choose applicant and hire
        4. Set the posting as filled
         */
        /*
        JobPosting jobPosting = this.getJobPosting();
        jobPosting.setFilled();
        ArrayList<JobApplication> finalCandidates = jobPosting.getInterviewManager().getApplicationsInConsideration();
        if (finalCandidates.size() == 1) {
            JobApplication jobApp = finalCandidates.get(0);
        } else {
            JobApplication jobApp = selectApplicantToHire();
        }
        jobApp.advanceStatus();*/
        // do something with email jobApp.getApplicant().getEmail();
    }

    /**
     * Interface for selecting the application of the applicant who is to be hired.
     *
     * @param finalCandidates The list of final candidates after all interview rounds have been completed.
     * @return the job application selected.
     */
    JobApplication selectApplicationForHiring(ArrayList<JobApplication> finalCandidates) {
        /*
        1. Show list of applications still in consideration
        2. Get choice
        3. Return application
         */
        return null;
    }


}
