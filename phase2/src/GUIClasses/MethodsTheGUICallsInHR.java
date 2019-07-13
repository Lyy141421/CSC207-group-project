package GUIClasses;

public class MethodsTheGUICallsInHR {
    // === Methods that GUI will call ===

    // HRCoordinator Interface class
    /*    *//**
     * Get the task that the HR Coordinator must accomplish at this moment for this job posting.
     *//*
    int getTaskForJobPosting(JobPosting jobPosting) {
        return jobPosting.getInterviewManager().getHrTask();
    }

    *//**
     * Gets a list of lists of job postings that include high priority and all postings for the company.
     * @param today Today's date.
     * @return the list of lists of job postings required.
     *//*
    ArrayList<ArrayList<JobPosting>> getHighPriorityAndAllJobPostings(LocalDate today) {
        JobPostingManager JPM = this.HRC.getCompany().getJobPostingManager();
        ArrayList<ArrayList<JobPosting>> jobPostingsList = new ArrayList<>();
        jobPostingsList.add(JPM.getClosedJobPostingsNoApplicantsChosen(today));
        jobPostingsList.add(JPM.getJobPostingsWithRoundCompletedNotForHire(today));
        jobPostingsList.add(JPM.getJobPostingsForHiring(today));
        jobPostingsList.add(JPM.getJobPostings());
        return jobPostingsList;
    }

    *//**
     * Add a job posting for this company.
     * @param today  Today's date.
     * @param jobPostingFields  The fields that the user inputs.
     *//*
    void addJobPosting(LocalDate today, Object[] jobPostingFields) {
        String title = (String) jobPostingFields[0];
        String field = (String) jobPostingFields[1];
        String description = (String) jobPostingFields[2];
        String requirements = (String) jobPostingFields[3];
        int numPositions = (Integer) jobPostingFields[4];
        LocalDate closeDate = (LocalDate) jobPostingFields[5];
        this.HRC.addJobPosting(title, field, description, requirements, numPositions, today, closeDate);
    }

    *//**
     * Get all job applications submitted by this applicant with this username.
     * @param applicantUsername The applicant username inputted.
     * @return a list of job applications submitted by this applicant with this username.
     *//*
    ArrayList<JobApplication> getAllJobApplicationsToCompany(String applicantUsername) {
        Applicant applicant = (Applicant) JobApplicationSystem.getUserManager().findUserByUsername(applicantUsername);
        if (applicant == null) {
            return new ArrayList<>();
        }
        return this.HRC.getCompany().getAllApplicationsToCompany(applicant);
    }

    *//**
     * Hire or reject an application.
     *
     * @param jobApp The job application in question.
     * @param toHire Whether or not the HR Coordinator wants to hire the applicant.
     *//*
    boolean hireOrRejectApplication(JobApplication jobApp, boolean toHire) {
        if (toHire) {
            jobApp.getStatus().setHired();
        } else {
            jobApp.getStatus().setArchived();
        }
        JobPosting jobPosting = jobApp.getJobPosting();
        jobPosting.setFilled();
        jobPosting.getInterviewManager().archiveRejected();
        if (jobPosting.getInterviewManager().getNumOpenPositions() > 0) {
            return false;
        }
        return true;
    }

    *//**
     * Checks whether this job application has been rejected.
     *
     * @param jobApplication The job application in question.
     * @return true iff this job application has been rejected.
     *//*
    boolean isRejected(JobApplication jobApplication) {
        return jobApplication.getJobPosting().getInterviewManager().getApplicationsRejected().contains(jobApplication);
    }

    *//**
     * Choose whether this application moves on for phone interviews.
     *
     * @param jobApp   The job application in question.
     * @param selected Whether or not the application is selected to move on.
     *//*
    void selectApplicationForPhoneInterview(JobApplication jobApp, boolean selected) {
        if (!selected) {
            jobApp.getJobPosting().getInterviewManager().reject(jobApp);
        }
    }

    *//**
     * Set up interviews for this job posting.
     *
     * @param jobPosting The job posting in question.
     *//*
    boolean setUpInterviews(JobPosting jobPosting) {
        Company company = jobPosting.getCompany();
        String field = jobPosting.getField();
        if (!company.hasInterviewerForField(field)) {
            return false;
        } else {
            ArrayList<JobApplication> jobApps = jobPosting.getInterviewManager().getApplicationsInConsideration();
            if (jobApps.isEmpty()) {
                return false;
            }
            for (JobApplication jobApp : jobApps) {
                jobApp.setUpInterview(this.HRC, jobApp.getStatus().getValue() + 1);
            }
            return true;
        }
    }*/

    // Job Application class
/*    public boolean isUnderReview() {
        return this.status.isUnderReview();
    }

    public boolean isOnPhoneInterview() {
        return this.status.isOnPhoneInterview();
    }

    public boolean isInPerson3() {
        return this.status.isInPerson3();
    }*/


    // Status class
    /*boolean isUnderReview () {
        return this.value == Status.UNDER_REVIEW;
    }

    boolean isOnPhoneInterview() {
        return this.value == Status.PHONE_INTERVIEW;
    }

    boolean isInPerson3() {
        return this.value == Status.IN_PERSON_3;
    }*/


    // HR Coordinator class
    /*    *//**
     * Get the task that the HR Coordinator must accomplish for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @param today      Today's date.
     * @return an integer that represents the task that the HR Coordinator must accomplish for this job posting.
     *//*
    int getTask(JobPosting jobPosting, LocalDate today) {
        if (this.company.getJobPostingManager().getClosedJobPostingsNoApplicantsChosen(today).contains(jobPosting)) {
            return InterviewManager.SELECT_APPS_FOR_PHONE_INTERVIEW;
        } else {
            return jobPosting.getInterviewManager().getHrTask();
        }
    }*/

}
