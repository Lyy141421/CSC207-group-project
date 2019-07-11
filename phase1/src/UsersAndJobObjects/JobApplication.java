package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JobApplication implements Storable{
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "JobApplications";

    // === Instance variables ===
    // Unique identifier for a submitted job application
    private int ID;
    // The applicant for a job
    private Applicant applicant;
    // The JobPosting that was applied for
    private JobPosting jobPosting;
    // The file name of the CV submitted for this application
    private JobApplicationDocument CV;
    // The the file name of the cover letter submitted for this application
    private JobApplicationDocument coverLetter;
    // The status of this application
    private Status status;
    // The date this application was submitted
    private LocalDate applicationDate;
    // The interviews conducted for this application
    private ArrayList<Interview> interviews = new ArrayList<>();

    // === Representation invariants ===
    // - interviews are ordered in terms of round number

    // === Public methods ===

    // === Constructors ===
    public JobApplication(String id) {
        this.ID = Integer.parseInt(id);
        JobApplication.totalNumOfApplications = Integer.max(this.ID, JobApplication.totalNumOfApplications);
    }

    public JobApplication(Applicant applicant, JobPosting jobPosting, JobApplicationDocument CV,
                          JobApplicationDocument coverletter, LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.status = new Status();
        this.applicationDate = applicationDate;
    }

    public JobApplication(Applicant applicant, JobPosting jobPosting, JobApplicationDocument CV,
                          JobApplicationDocument coverletter, Status status, LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    // === Getters ===
    public String getId() {
        return Integer.toString(this.ID);
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public JobPosting getJobPosting() {
        return this.jobPosting;
    }

    public JobApplicationDocument getCV() {
        return this.CV;
    }

    public JobApplicationDocument getCoverLetter() {
        return this.coverLetter;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Setters ===
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public void setCV(JobApplicationDocument CV) {
        this.CV = CV;
    }

    public void setCoverLetter(JobApplicationDocument coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    // === Other methods ===

    /**
     * Checks whether this application has been archived.
     *
     * @return true iff this application is set to archived.
     */
    public boolean isArchived() {
        return this.status.isArchived();
    }

    /**
     * Checks whether the applicant of this application has been hired.
     * @return true iff this application is set to hired.
     */
    public boolean isHired() {
        return this.status.isHired();
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param hrCoordinator The HR Coordinator who set up this interview
     * @param round         The interview round.
     */
    public void setUpInterview(HRCoordinator hrCoordinator, int round) {
        JobPosting jobPosting = this.getJobPosting();
        String jobField = jobPosting.getField();
        Interviewer interviewer = hrCoordinator.getCompany().findInterviewerByField(jobField);
        Interview interview = new Interview(this, interviewer, hrCoordinator,
                jobPosting.getInterviewManager(), round);
        this.addInterview(interview);
        interviewer.addInterview(interview);
        this.status.advanceStatus();
    }

    /**
     * Gets the last interview conducted/scheduled for this job application
     * @return  the last interview conducted/scheduled for this job application.
     */
    public Interview getLastInterview() {
        if (this.interviews.size() == 0) {
            return null;
        } else {
            return this.interviews.get(this.interviews.size() - 1);
        }
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString() {
        return Integer.toString(this.ID);
    }

    /**
     * Get a string of what an applicant should see when viewing their applications.
     *
     * @return a string of what an applicant should see when viewing their applications.
     */
    public String toStringForApplicant() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId() + "\n";
        s += "Status: " + this.getStatus().getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);
        return s;
    }

    @Override
    public String toString() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "\n\nCV: \n" + this.getCV() + "\n\n";
        s += "Cover letter: \n" + this.getCoverLetter() + "\n\n";
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Constructors ===
    JobApplication(LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.ID = JobApplication.totalNumOfApplications;
        this.applicationDate = applicationDate;
    }

    JobApplication(JobPosting jobPosting, LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.ID = JobApplication.totalNumOfApplications;
        this.jobPosting = jobPosting;
        this.applicationDate = applicationDate;
    }

    // ============================================================================================================== //
    // === Private methods ===
    /**
     * Add an interview for this job application.
     *
     * @param interview The interview to be added.
     */
    private void addInterview(Interview interview) {
        this.interviews.add(interview);
    }
}
