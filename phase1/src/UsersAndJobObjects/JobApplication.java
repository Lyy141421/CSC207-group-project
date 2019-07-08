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
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.status = new Status();
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    public JobApplication(Applicant applicant, JobPosting jobPosting, JobApplicationDocument CV,
                          JobApplicationDocument coverletter, Status status, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.status = status;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
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

    public boolean isArchived() {
        return this.status.isArchived();
    }

    boolean isOnPhoneInterview() {
        return this.status.isOnPhoneInterview();
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
        Interviewer interviewer = hrCoordinator.getCompany().findInterviewer(jobField);
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
        return this.interviews.get(this.interviews.size() - 1);
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
     * Get a string of the overview of this job application, including id, applicant name, job posting, status,
     * and application date.
     *
     * @return a string of the overview of this job application.
     */
    public String getOverview() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);

        return s;
    }

    /**
     * Get a string representation of this job application.
     *
     * @return a string representation of this job application.
     */
    @Override
    public String toString() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "\n\nCV: \n" + this.getCV().getContents() + "\n\n";
        s += "Cover letter: \n" + this.getCoverLetter().getContents() + "\n\n";
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);
        return s;
    }

    // ============================================================================================================== //
    // === Package-private methods ===
    // === Constructors ===

    JobApplication(LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    JobApplication(JobPosting jobPosting, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.jobPosting = jobPosting;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    // === Private methods ===

    /**
     * Add an interview for this job application.
     *
     * @param interview The interview to be added.
     */
    private void addInterview(Interview interview) {
        this.interviews.add(interview);
    }

    /**
     * Get the interview IDs for this job application.
     *
     * @return the IDs of the interviews that this job application has undergone.
     */
    private String getInterviewIDs() {
        String s = "";
        for (Interview interview : this.interviews) {
            s += interview.getId() + ", ";
        }
        return s.substring(0, s.length() - 2); // remove last comma and space
    }
}
