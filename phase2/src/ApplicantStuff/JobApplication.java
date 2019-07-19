package ApplicantStuff;

import CompanyStuff.Interview;
import JobPostings.BranchJobPosting;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JobApplication implements Serializable {
    /**
     * A submitted job application.
     */

    // === Class variables ===
    // Total number of applications in the system
    private static int totalNumOfApplications;

    // === Instance variables ===
    // Unique identifier for a submitted job application
    private int id;
    // The applicant for a job
    private Applicant applicant;
    // The BranchJobPosting that was applied for
    private BranchJobPosting jobPosting;
    // The files submitted for this job application
    private ArrayList<JobApplicationDocument> filesSubmitted;
    // The status of this application
    private Status status;
    // The date this application was submitted
    private LocalDate applicationDate;
    // The interviews conducted for this application
    private ArrayList<Interview> interviews = new ArrayList<>();

    // === Representation invariants ===
    // - interviews are ordered in terms of round number
    // - id >= 1

    // === Public methods ===

    // === Constructors ===

    public JobApplication(Applicant applicant, BranchJobPosting jobPosting,
                          ArrayList<JobApplicationDocument> filesSubmitted, LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.id = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.filesSubmitted = filesSubmitted;
        this.status = new Status();
        this.applicationDate = applicationDate;
    }

    // === Getters ===
    public int getId() {
        return this.id;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public BranchJobPosting getJobPosting() {
        return this.jobPosting;
    }

    ArrayList<JobApplicationDocument> getFilesSubmitted() {
        return this.filesSubmitted;
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

    public void setJobPosting(BranchJobPosting branchJobPosting) {
        this.jobPosting = branchJobPosting;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    // === Other methods ===

    public void addFile(JobApplicationDocument file) {
        this.filesSubmitted.add(file);
    }


    /**
     * Checks whether this application has been archived.
     *
     * @return true iff this application is set to archived.
     */
    boolean isArchived() {
        return this.status.isArchived();
    }

    /**
     * Checks whether the applicant of this application has been hired.
     *
     * @return true iff this application is set to hired.
     */
    boolean isHired() {
        return this.status.isHired();
    }

    /**
     * Gets the last interview conducted/scheduled for this job application
     *
     * @return the last interview conducted/scheduled for this job application.
     */
    public Interview getLastInterview() {
        if (this.interviews.size() == 0) {
            return null;
        } else {
            return this.interviews.get(this.interviews.size() - 1);
        }
    }

    /**
     * Add an interview for this job application.
     *
     * @param interview The interview to be added.
     */
    public void addInterview(Interview interview) {
        this.interviews.add(interview);
    }


    public String getMiniDescriptionForReference() {
        String s = "Referee: " + this.getApplicant().getLegalName() + ": \n";
        s += "Job Posting: " + this.getJobPosting().getTitle() + "\n";
        s += "Close Date: " + this.getJobPosting().getCloseDate().toString();
        return s;
    }

    /**
     * Get a string of what an applicant should see when viewing their applications.
     *
     * @return a string of what an applicant should see when viewing their applications.
     */
    public String toStringForApplicant() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Job Posting: " + jobPosting.getTitle() + " -- ID: " + jobPosting.getId() + "\n";
        s += "Status: " + this.getStatus().getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);
        return s;
    }

    @Override
    public String toString() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + "(" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.jobPosting.getTitle() + " -- ID: " + this.jobPosting.getId();
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf);
        return s;
    }
}
