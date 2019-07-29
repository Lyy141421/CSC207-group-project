package GUIClasses.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import Main.JobApplicationSystem;

import java.util.ArrayList;

public class ReferenceBackEnd {

    private Reference reference;
    private JobApplicationSystem jobAppSystem;

    ReferenceBackEnd(Reference reference, JobApplicationSystem jobAppSystem) {
        this.reference = reference;
        this.jobAppSystem = jobAppSystem;
    }

    Reference getReference() {
        return this.reference;
    }

    boolean isTodayAfterApplicationCloseDate(JobApplication jobApplication) {
        return jobApplication.getJobPosting().getApplicantCloseDate().isAfter(jobAppSystem.getToday());
    }

    ArrayList<JobApplication> getJobAppsForReference() {
        return this.reference.getJobAppsForReference();
    }

}
