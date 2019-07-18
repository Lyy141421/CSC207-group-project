package ApplicantStuff;

import Main.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class Reference extends User {

    // === Instance variables ===
    // The job applications that need references from this referee
    private ArrayList<JobApplication> jobAppsForReference;

    // === Constructor ===
    public Reference(String email, LocalDate dateCreated) {
        super(email, dateCreated);
        this.jobAppsForReference = new ArrayList<>();
    }

    // === Getter ===

    public ArrayList<JobApplication> getJobAppsForReference() {
        return this.jobAppsForReference;
    }

    // === Other methods ===

    /**
     * Add a job application to this referee's list of job applications that they need to submit a reference for.
     *
     * @param jobApp The job application to be added.
     */
    public void addJobApplicationForReference(JobApplication jobApp) {
        this.jobAppsForReference.add(jobApp);
    }

    /**
     * Remove a job application from this referee's list of job applications that they need to submit a reference for.
     *
     * @param jobApp The job application to be removed.
     */
    public void removeJobApplicationForReference(JobApplication jobApp) {
        this.jobAppsForReference.remove(jobApp);
    }
}
