import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

class JobApplication {
    /**
     * A submitted job application.
     */

    // === Instance variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // Unique identifier for a submitted job application
    private int ID;
    // The applicant for a job
    private Applicant applicant;
    // The JobPosting that was applied for
    private JobPosting jobPosting;
    // The CV submitted for this application
    private File CV;
    // The cover letter submitted for this application
    private File coverLetter;
    // The list of reference letters submitted for this application
//    private ArrayList<ReferenceLetter> referenceLetters;
    // The status of this application
    private int status = -2;
    // The date this application was submitted
    private LocalDate applicationDate;

    // === Representation invariants ===
    // status:
    //      -3 : Archived
    //      -2 : Submitted
    //      -1 : Under review
    //       0 : Phone interview
    //       1 : In-person interview 1
    //       2 : In-person interview 2
    //       3 : In-person interview 3
    //       4 : Hired

    // === Constructors ===

    /**
     * Create a new job application.
     */
    JobApplication() {
        this.ID = JobApplication.totalNumOfApplications;
        // this.status = ...;
        JobApplication.totalNumOfApplications++;
        this.applicationDate = LocalDate.now();
    }

    ;

    /**
     * Create a new job application.
     *
     * @param applicant   The Applicant associated with this application.
     * @param jobPosting  The JobPosting associated with this application.
     * @param CV          The CV of the applicant.
     * @param coverletter The cover letter of the applicant.
     */
    JobApplication(Applicant applicant, JobPosting jobPosting, File CV, File coverletter) {
        this.ID = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
        JobApplication.totalNumOfApplications++;
        this.applicationDate = LocalDate.now();
    }

    /**
     * Create a new job application.
     *
     * @param ID          The application ID.
     * @param applicant   The applicant associated with this application.
     * @param jobPosting  The JobPosting associated with this application.
     * @param CV          The CV of the applicant.
     * @param coverletter The cover letter of the applicant.
     *                    //     * @param referenceLetters The reference letters for the applicant.
     * @param status      The status of the application.
     */
    JobApplication(int ID, Applicant applicant, JobPosting jobPosting, File CV, File coverletter,
                   ArrayList<ReferenceLetter> referenceLetters, int status) {
        this.ID = ID;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.CV = CV;
        this.coverLetter = coverletter;
//        this.referenceLetters = referenceLetters;
        this.applicationDate = LocalDate.now();
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
     * Get the CV of the applicant.
     *
     * @return the CV of the applicant.
     */
    File getCV() {
        return this.CV;
    }

    /**
     * Get the applicant's cover letter.
     *
     * @return the applicant's cover letter.
     */
    File getCoverLetter() {
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

    // === Setters ===

    /**
     * Change the CV for this application.
     *
     * @param CV The applicant's CV.
     */
    void setCV(File CV) {
        this.CV = CV;
    }

    /**
     * Change the cover letter for this application.
     *
     * @param coverLetter The applicant's cover letter.
     */
    void setCoverLetter(File coverLetter) {
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

}
