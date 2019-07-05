package UsersAndJobObjects;

import FileLoadingAndStoring.FileSystem;
import GUIClasses.ApplicantInterface;
import Main.JobApplicationSystem;
import Managers.JobApplicationManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Applicant extends User {
    /**
     * An account for a job applicant.
     */

    // === Class variables ===
    // The filename under which this will be saved in the FileLoadingAndStoring.FileSystem
    public static final String FILENAME = "Applicants";

    // === Instance variables ===
    // The applicant's job application manager
    private JobApplicationManager jobApplicationManager = new JobApplicationManager();
    // List of filenames uploaded to account
    private ArrayList<String> filesSubmitted = new ArrayList<>();

    // === Constructors ===

    public Applicant(String id){
        this.setUsername(id);
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        super.setUserInterface(new ApplicantInterface(this));
    }

    public Applicant(String username, String password, String legalName, String email, LocalDate dateCreated,
              JobApplicationManager jobApplicationManager) {
        super(username, password, legalName, email, dateCreated);
        this.jobApplicationManager = jobApplicationManager;
        super.setUserInterface(new ApplicantInterface(this));
    }

    // === Getters ===

    public JobApplicationManager getJobApplicationManager() {
        return this.jobApplicationManager;
    }

    public ArrayList<String> getFilesSubmitted() {
        return this.filesSubmitted;
    }

    // === Setters ===

    private void setJobApplicationManager(JobApplicationManager jobApplicationManager) {
        this.jobApplicationManager = jobApplicationManager;
    }

    // === Other methods ===

    /**
     * Add a file to one's account.
     *
     * @param file The file contents to be added.
     */
    private void addFile(String file) {
        this.filesSubmitted.add(file);
    }

    /**
     * Remove a file from one's account.
     * @param file The file to be removed.
     */
    private void removeFile(String file) {
        this.filesSubmitted.remove(file);
    }

    /**
     * Apply for a job.
     *
     * @param jobPosting The job posting that this applicant wants to apply for.
     * @param CV        The applicant's CV.
     * @param coverLetter   The applicant's cover letter.
     * @return true iff this application is successfully submitted (ie before closing date and has not already applied)
     */
    public boolean applyForJob(JobPosting jobPosting, String CV, String coverLetter) {
        if (LocalDate.now().isBefore(jobPosting.getCloseDate()) && !this.hasAppliedTo(jobPosting)) {
            this.jobApplicationManager.addJobApplication(this, jobPosting, CV, coverLetter,
                    LocalDate.now());
            this.addFile(CV);
            this.addFile(coverLetter);
        }
        return false;
    }

    /**
     * Remove this applicant's application for this job.
     *
     * @param jobPosting The job that this user wants to withdraw their application from.
     * @return true iff this applicant can successfully withdraw their application; else return false
     */
    public boolean withdrawApplication(JobPosting jobPosting) {
        if (this.hasAppliedTo(jobPosting) && !jobPosting.isFilled()) {
            jobPosting.removeJobApplication(jobPosting.findJobApplication(this));
            this.jobApplicationManager.removeJobApplication(jobPosting);
            return true;
        }
        return false;
    }

    /**
     * Report whether this applicant has already applied to this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return true iff this applicant has not applied to this job posting.
     */
    public boolean hasAppliedTo(JobPosting jobPosting) {
        for (JobApplication jobApp : jobPosting.getJobApplications()) {
            if (jobApp.getApplicant().equals(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of open job postings not yet applied to.
     *
     * @param today Today's date.
     * @return a list of open job postings not yet applied to.
     */
    public ArrayList<JobPosting> getOpenJobPostingsNotAppliedTo(LocalDate today) {
        ArrayList<JobPosting> jobPostingsNotAppliedTo = new ArrayList<>();
        for (Company company : JobApplicationSystem.getCompanies()) {
            for (JobPosting posting : company.getJobPostingManager().getJobPostings())
            if (today.isBefore(posting.getCloseDate()) && !this.hasAppliedTo(posting)) {
                jobPostingsNotAppliedTo.add(posting);
            }
        }
        return jobPostingsNotAppliedTo;
    }

    /**
     * Report whether the date that the last job posting this applicant applied to was 30 days ago from today.
     *
     * @param today Today's date.
     * @return true iff today's date is 30 days after the closing date for the last job this applicant applied to.
     */
    public boolean isInactive(LocalDate today) {
        return this.jobApplicationManager.getNumDaysSinceMostRecentCloseDate(today) >= 30;
    }

    /**
     * Remove the files submitted for an application to a job posting that has been closed for 30 days.
     *
     * @param today Today's date.
     */
    public void removeFilesFromAccount(LocalDate today) {
        if (this.isInactive(today)) {
            JobApplication lastClosedJobApp = this.jobApplicationManager.getLastClosedJobApp();
            ArrayList<String> files = this.jobApplicationManager.getFilesSubmittedForApplication(lastClosedJobApp);
            this.filesSubmitted.removeAll(files);
        }
    }

    /**
     * Getter for the ID
     *
     * @return the string of the id
     */
    public String getIdString() {
        return this.getUsername();
    }

    /**
     * Saves the Object
     */
    public void saveSelf(){
        FileSystem.mapPut(Applicant.FILENAME, getIdString(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", this.getPassword());
        data.put("legalName", this.getLegalName());
        data.put("email", this.getEmail());
        data.put("dateCreated", this.getDateCreated());
        ArrayList<ArrayList> jobapps = new ArrayList<>();
        for(JobApplication x : this.jobApplicationManager.getJobApplications()){
            ArrayList<String> temp = new ArrayList<>();
            temp.add(JobApplication.FILENAME);
            temp.add(x.getIdString());
            jobapps.add(temp);

        }
        data.put("jobApplicationManager", jobapps);
        data.put("filesSubmitted", this.filesSubmitted);
        FileSystem.write(Applicant.FILENAME, getIdString(), data);
    }

    /**
     * loads the Object
     */
    public void loadSelf(){
        FileSystem.mapPut(Applicant.FILENAME, getIdString(), this);
        HashMap data = FileSystem.read(Applicant.FILENAME, getIdString());
        this.loadPrelimData(data);
        this.loadJobAppManager(data);
    }

    /**
     * Load the preliminary data for this applicant.
     *
     * @param data This applicant's data.
     */
    private void loadPrelimData(HashMap data) {
        this.setPassword((String) data.get("password"));
        this.setLegalName((String) data.get("legalName"));
        this.setEmail((String) data.get("email"));
        this.filesSubmitted = (ArrayList<String>) (data.get("filesSubmitted"));
        this.setDateCreated(LocalDate.parse((String) data.get("dateCreated")));
    }

    /**
     * Load the job application manager for this applicant.
     *
     * @param data This applicant's data.
     */
    private void loadJobAppManager(HashMap data) {
        ArrayList<JobApplication> temp = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) (data.get("jobApplicationManager"))) {
            temp.add((JobApplication) FileSystem.subLoader(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        this.setJobApplicationManager(new JobApplicationManager(temp));
    }

}
