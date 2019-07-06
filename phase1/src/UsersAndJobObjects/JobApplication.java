package UsersAndJobObjects;

import FileLoadingAndStoring.Storable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class JobApplication implements Storable{
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // Job application statuses as constants
    private static final int ARCHIVED = -3;
    private static final int SUBMITTED = -2;
    private static final int UNDER_REVIEW = -1;
    private static final int PHONE_INTERVIEW = 0;
    private static final int IN_PERSON_1 = 1;
    private static final int IN_PERSON_2 = 2;
    private static final int IN_PERSON_3 = 3;
    private static final int HIRED = 4;
    // Map of statuses and their identifying integers
    private static HashMap<Integer, String> statuses = new HashMap<Integer, String>() {{
        put(JobApplication.ARCHIVED, "Archived");
        put(JobApplication.SUBMITTED, "Submitted");
        put(JobApplication.UNDER_REVIEW, "Under review");
        put(JobApplication.PHONE_INTERVIEW, "Phone interview");
        put(JobApplication.IN_PERSON_1, "In-person interview round 1");
        put(JobApplication.IN_PERSON_2, "In-person interview round 2");
        put(JobApplication.IN_PERSON_3, "In-person interview round 3");
        put(JobApplication.HIRED, "Hired");
    }};
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
    private String CV;
    // The the file name of the cover letter submitted for this application
    private String coverLetter;
    // The status of this application
    private int status = -2;
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

    public JobApplication(Applicant applicant, JobPosting jobPosting, String CV, String coverletter, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    // === Getters ===

    public static HashMap<Integer, String> getStatuses() {
        return statuses;
    }

    public String getId() {
        return Integer.toString(this.ID);
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public JobPosting getJobPosting() {
        return this.jobPosting;
    }

    public String getCV() {
        return this.CV;
    }

    public String getCoverLetter() {
        return this.coverLetter;
    }

    public int getStatus() {
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

    public void setCV(String CV) {
        this.CV = CV;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setStatus(int status) {
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
     * Advance the status of this application.
     */
    public void advanceStatus() {
        this.status++;
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
        this.advanceStatus();
    }

    /**
     * Gets the last interview conducted/scheduled for this job application
     * @return  the last interview conducted/scheduled for this job application.
     */
    public Interview getLastInterview() {
        return this.interviews.get(this.interviews.size() - 1);
    }

    /**
     * Archive the job application.
     */
    public void setArchived() {
        this.setStatus(JobApplication.ARCHIVED);
    }

    /**
     * Set this job application status to hired.
     */
    public void setHired() {
        this.setStatus((JobApplication.HIRED));
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
     * Get a string representation of this job application.
     *
     * @return a string representation of this job application.
     */
    @Override
    public String toString() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + " -- ID: " + this.getJobPosting().getId();
        s += "CV: " + "\n" + this.getCV() + "\n";
        s += "Cover letter: " + this.getCoverLetter() + "\n";
        s += "Status: " + JobApplication.statuses.get(this.getStatus()) + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf) + "\n";
        s += "Interview IDs: " + this.getInterviewIDs();
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

    JobApplication(int ID, Applicant applicant, JobPosting jobPosting, String CV, String coverletter, int status,
                   LocalDate applicationDate) {
        this.ID = ID;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.applicationDate = applicationDate;
        this.status = status;
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
