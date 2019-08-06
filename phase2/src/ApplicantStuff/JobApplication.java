package ApplicantStuff;

import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import DocumentManagers.DocumentManager;
import DocumentManagers.DocumentManagerFactory;
import DocumentManagers.ReferenceLetterDocumentManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class JobApplication implements Serializable {
    /**
     * A submitted job application.
     */

    // === Class variables ===
    static final long serialVersionUID = 1L;
    // Total number of applications in the system
    private static int totalNumOfApplications;
    // The category names for Reference
    public static final String[] CATEGORY_NAMES_FOR_REFERENCE = new String[]{"Referee", "Job Posting", "Submission Deadline"};

    // === Instance variables ===
    // Unique identifier for a submitted job application
    private int id;
    // The applicant for a job
    private Applicant applicant;
    // The BranchJobPosting that was applied for
    private BranchJobPosting jobPosting;
    // The files submitted for this job application
    private ArrayList<JobApplicationDocument> filesSubmitted = new ArrayList<>();
    // The status of this application
    private Status status;
    // The date this application was submitted
    private LocalDate applicationDate;
    // The referencesToLetters that this applicant has chosen
    private HashMap<Reference, ArrayList<JobApplicationDocument>> referencesToLetters = new HashMap<>();
    // The reference letter document manager
    private DocumentManager referenceLetterDocManager;
    // The interviews conducted for this application
    private ArrayList<Interview> interviews = new ArrayList<>();

    // === Representation invariants ===
    // - interviews are ordered in terms of round number
    // - id >= 1

    // === Public methods ===

    // === Constructors ===

    public JobApplication(Applicant applicant, BranchJobPosting jobPosting, LocalDate applicationDate) {
        JobApplication.totalNumOfApplications++;
        this.id = JobApplication.totalNumOfApplications;
        this.applicant = applicant;
        this.jobPosting = jobPosting;
        this.applicationDate = applicationDate;
        this.jobPosting.addJobApplication(this);
        this.referenceLetterDocManager = new DocumentManagerFactory().createDocumentManager(this);
        this.status = new Status(this.getApplicant(), this);
    }

    // === Getters ===
    public static int getTotalNumOfApplications() {
        return totalNumOfApplications;
    }

    public int getId() {
        return this.id;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public BranchJobPosting getJobPosting() {
        return this.jobPosting;
    }

    public ArrayList<JobApplicationDocument> getAllFilesSubmitted() {
        ArrayList<JobApplicationDocument> allFiles = (ArrayList<JobApplicationDocument>) this.filesSubmitted.clone();
        for (Reference reference : this.referencesToLetters.keySet()) {
            ArrayList<JobApplicationDocument> letters = this.referencesToLetters.get(reference);
            allFiles.addAll(letters);
        }
        return allFiles;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public Set<Reference> getReferences() {
        return this.referencesToLetters.keySet();
    }

    public ReferenceLetterDocumentManager getReferenceLetterDocManager() {
        return (ReferenceLetterDocumentManager) referenceLetterDocManager;
    }

    public ArrayList<Interview> getInterviews() {
        return this.interviews;
    }

    public static void setTotalNumOfApplications(int totalNum) {
        totalNumOfApplications = totalNum;
    }

    // === Other methods ===

    /**
     * Add files to this job application.
     *
     * @param files The files to be submitted to the company.
     */
    public void addFiles(ArrayList<JobApplicationDocument> files) {
        this.filesSubmitted.addAll(files);
    }

    /**
     * Add referencesToLetters for this job application.
     *
     * @param referencesToAdd The referencesToLetters chosen by the applicant.
     */
    public void addReferences(ArrayList<Reference> referencesToAdd) {
        for (Reference reference : referencesToAdd) {
            this.referencesToLetters.put(reference, new ArrayList<>());
        }
    }

    public void addReferenceLetters(Reference reference, ArrayList<JobApplicationDocument> letters) {
        this.referencesToLetters.get(reference).addAll(letters);
    }

    /**
     * Remove this job application from all the referencesToLetters' lists.
     * This is called when an application is withdrawn before the reference close date.
     */
    void removeAppFromAllReferences() {
        for (Reference reference : this.referencesToLetters.keySet()) {
            if (reference.getJobAppsForReference().contains(this)) {
                reference.removeJobApplication(this);
                if (this.referencesToLetters.get(reference) != null) {
                    for (JobApplicationDocument letter : this.referencesToLetters.get(reference)) {
                        letter.getFile().delete();
                    }
                }
            }
        }
        this.referenceLetterDocManager.getFolder().delete();
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

    public HashMap<String, HashMap<Interviewer, String>> getAllInterviewNotesForApplication() {
        HashMap<String, HashMap<Interviewer, String>> interviewNotes = new HashMap<>();
        for (Interview interview : this.getInterviews()) {
            String round = interview.getMiniDescriptionForHR();
            interviewNotes.put(round, interview.getAllInterviewersToNotes());
        }
        return interviewNotes;
    }


    public String getMiniDescriptionForReference() {
        String s = "Referee: " + this.getApplicant().getLegalName() + "   ";
        s += "Job Posting: " + this.getJobPosting().getTitle() + "   ";
        s += "Submission Deadline: " + this.getJobPosting().getCloseDate().toString();
        return s;
    }

    /**
     * Get the job application values corresponding to the categories to be displayed in the reference GUI.
     *
     * @return a list of values corresponding to the categories to be displayed in the reference GUI.
     */
    public String[] getCategoryValuesForReference() {
        return new String[]{this.getApplicant().getLegalName(), this.getJobPosting().getTitle(),
                this.getJobPosting().getCloseDate().toString()};
    }


    @Override
    public String toString() {
        String s = "Application ID: " + this.getId() + "\n";
        s += "Applicant: " + this.getApplicant().getLegalName() + " (" + this.getApplicant().getUsername() + ")" + "\n";
        s += "Job Posting: " + this.jobPosting.getTitle() + " -- ID: " + this.jobPosting.getId() + "\n";
        s += "Status: " + this.status.getDescription() + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        s += "Application date: " + this.getApplicationDate().format(dtf) + "\n";
        return s;
    }
}
