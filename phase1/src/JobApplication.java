import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class JobApplication {
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // Map of status number to description
    private static HashMap<Integer, String> statuses = new HashMap<Integer, String>() {{
        put(-3, "Archived");
        put(-2, "Submitted");
        put(-1, "Under review");
        put(0, "Phone interview");
        put(1, "In-person interview 1");
        put(2, "In-person interview 2");
        put(3, "In-person interview 3");
        put(4, "Hired");
    }};

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

    // === Constructors ===

    /**
     * Create a new job application.
     * @param applicationDate   The date this application was created.
     */
    JobApplication(LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    /**
     * Create a new job application.
     *
     * @param jobPosting The job posting that is being applied to.
     */
    JobApplication(JobPosting jobPosting, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.jobPosting = jobPosting;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    /**
     * Create a new job application.
     *
     * @param applicant         The Applicant associated with this application.
     * @param jobPosting        The JobPosting associated with this application.
     * @param CV                The filename of the applicant's CV.
     * @param coverletter       The filename of the applicant's cover letter.
     * @param applicationDate   The date this application was created.
     */
    JobApplication(Applicant applicant, JobPosting jobPosting, String CV, String coverletter, LocalDate applicationDate) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        this.applicationDate = applicationDate;
        JobApplication.totalNumOfApplications++;
    }

    /**
     * Create a new job application.
     *
     * @param ID            The application ID.
     * @param applicant     The applicant associated with this application.
     * @param jobPosting    The JobPosting associated with this application.
     * @param CV            The filename of the applicant's CV.
     * @param coverletter   The filename of the applicant's cover letter.
     * @param status            The status of the application.
     * @param applicationDate   The date this application was created.
     */
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

    // === Getters ===

    /**
     * Get the application ID.
     *
     * @return the application ID.
     */
    int getID() {
        return this.ID;
    }

    /**
     * Get the applicant account.
     *
     * @return the applicant account.
     */
    Applicant getApplicant() {
        return this.applicant;
    }

    /**
     * Get the job posting.
     *
     * @return the job posting.
     */
    JobPosting getJobPosting() {
        return this.jobPosting;
    }

    /**
     * Get the filename of the applicant's CV.
     *
     * @return the filename of the applicant's CV.
     */
    String getCV() {
        return this.CV;
    }

    /**
     * Get the filename of the applicant's cover letter.
     *
     * @return the filename of the applicant's cover letter.
     */
    String getCoverLetter() {
        return this.coverLetter;
    }

//    /**
//     * Get the list of reference letters for the applicant.
//     *
//     * @return the list of reference letters for the applicant.
//     */
//    ArrayList<ReferenceLetter> getReferenceLetters() {
//        return this.referenceLetters;
//    }

    /**
     * Get the current status of this application.
     *
     * @return the current status of this application.
     */
    int getStatus() {
        return this.status;
    }

    /**
     * Get the date this application was submitted.
     *
     * @return the date this application was submitted.
     */
    LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    /**
     * Get the interviews of this application.
     *
     * @return the arraylist of interviews.
     */
    ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    // === Setters ===

    /**
     * Set the applicant for this application.
     *
     * @param applicant The applicant who is applying for a job.
     */
    void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Change the CV for this application.
     *
     * @param CV The filename of the applicant's CV.
     */
    void setCV(String CV) {
        this.CV = CV;
    }

    /**
     * Change the cover letter for this application.
     *
     * @param coverLetter The filename of the applicant's cover letter.
     */
    void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    /**
     * Change the status of this application.
     *
     * @param status The status of this application.
     */
    void setStatus(int status) {
        this.status = status;
    }

    // === Other methods ===
    void advanceStatus() {
        this.status++;
    }

    /**
     * Add an interview for this job application.
     *
     * @param interview The interview to be added.
     */
    void addInterview(Interview interview) {
        this.interviews.add(interview);
    }

    /**
     * Set up an interview for the applicant with this job application.
     *
     * @param round The interview round.
     */
    void setUpInterview(HRCoordinator hrCoordinator, int round) {
        JobPosting jobPosting = this.getJobPosting();
        String jobField = jobPosting.getField();
        Interviewer interviewer = hrCoordinator.getCompany().findInterviewer(jobField);
        Interview interview = new Interview(this, interviewer, hrCoordinator,
                jobPosting.getInterviewManager(), round);
        this.addInterview(interview);
        interviewer.addInterview(interview);
    }


}
