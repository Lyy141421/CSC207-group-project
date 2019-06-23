class HRCoordinatorInterface extends UserInterface {
    /**
     * The general HR Coordinator interface
     */

    // === Instance variables ===
    private HRCoordinator HRC;

    // === Constructor ===
    HRCoordinatorInterface(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    JobPosting getJobPosting() {
        /*1. Get the job posting that this HR Coordinator wants to view
        2. Find whether the job posting exists for their company
        Note: HR Coordinators can see any application for any job posting in their company*/
//        return this.HRC.getCompany().searchJobPostingByTitle(jobTitle);
        return null; // stub
    }

    void addJobPosting() {
        // 1. Get data for job posting -- Job title, Job field, Description, Requirements, Close date
        // 2. Create job posting
        // this.HRC.addJobPosting(jobTitle, jobField, jobDescription, requirements, numPositions, postDate, closeDate);
    }

    void viewAllJobPostings() {
        // Interface stuff
        // JobApplicationSystem.getAllJobPostings();
    }

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

    void setUpInterviewForJobApplication() {
        /*
        1. Get the job posting by title.
        2. Get the specific application by applicant username.
        2. Find the interviewer in the specific job field that has the least number of interviews scheduled
        3. Match interviewer with application
         */
        /*
        JobPosting jobPosting = this.getJobPosting();
        Applicant applicant = JobApplicationSystem.getUserManager().findUserByUsername(username);
        JobApplication jobApp = jobPosting.findJobApplication(applicant);
        jobApp.advanceStatus();
        jobApp.setUpInterview(this.HRC, jobApp.getStatus());*/
    }


    void hireApplicant() {
        /*
        1. Get the job posting
        2. Set the posting as filled
        3. Get a list of final candidates
        4. Choose applicant and hire
         */
        /*
        JobPosting jobPosting = this.getJobPosting();
        jobPosting.setFilled();
        ArrayList<JobApplication> finalCandidates = jobPosting.getInterviewManager().getApplicationsInConsideration();
        if (finalCandidates.size() == 1) {
            // get email of finalCandidates.get(0).getApplicant();
        } else {
            // choose an applicant from list
            // get email of applicant
        }*/
    }


}
