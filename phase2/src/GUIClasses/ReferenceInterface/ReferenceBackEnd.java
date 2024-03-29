package GUIClasses.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.JobPostings.BranchJobPosting;
import Main.JobApplicationSystem;

import java.util.ArrayList;

class ReferenceBackEnd {
    /**
     * Back end of the reference GUI.
     */

    // === Instance variables ===
    private Reference reference;    // The reference who logged in
    private JobApplicationSystem jobAppSystem;  // The job application system being used

    // === Constructor ===
    ReferenceBackEnd(Reference reference, JobApplicationSystem jobAppSystem) {
        this.reference = reference;
        this.jobAppSystem = jobAppSystem;
    }

    // === Getters ===
    Reference getReference() {
        return this.reference;
    }

    String getEmail() {
        return this.reference.getEmail();
    }

    ArrayList<JobApplication> getJobAppsForReference() {
        return this.reference.getJobAppsForReference();
    }

    ArrayList<BranchJobPosting> getJobPostings() {
        return this.reference.getJobPostings();
    }

    String[] getJobPostingNameList() {
        return this.reference.getJobPostingNameList();
    }

}
