package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import Managers.DocumentManager;
import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobApplicationDocument;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantStorer extends GenericStorer<Applicant> {

    /**
     * Store the applicant.
     * @param jobApplicationSystem The job application system being used.
     * @param applicant The applicant to be stored.
     */
    void storeOne(JobApplicationSystem jobApplicationSystem, Applicant applicant) {
        LoaderManager.mapPut(Applicant.FILENAME, applicant.getUsername(), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(applicant, data);
        this.storeJobApplications(jobApplicationSystem, applicant, data);
        this.storeDocuments(applicant, data);
        FileSystem.write(Applicant.FILENAME, applicant.getUsername(), data);
    }

    /**
     * Store the preliminary information for this applicant.
     * @param applicant The applicant to be stored.
     * @param data  The hashmap that is to be written to a file.
     */
    private void storePrelimInfo(Applicant applicant, HashMap<String, Object> data) {
        data.put("password", applicant.getPassword());
        data.put("legalName", applicant.getLegalName());
        data.put("email", applicant.getEmail());
        data.put("dateCreated", applicant.getDateCreated());
    }

    /**
     * Store the job applications for this applicant.
     *
     * @param jobApplicationSystem The job application system being used.
     * @param applicant            The applicant being loaded.
     * @param data                 The applicant's data.
     */
    private void storeJobApplications(JobApplicationSystem jobApplicationSystem, Applicant applicant, HashMap<String, Object> data) {
        ArrayList<ArrayList> jobapps = new ArrayList<>();
        for(JobApplication x : applicant.getJobApplicationManager().getJobApplications()){
            ArrayList<String> temp = new ArrayList<>();
            temp.add(JobApplication.FILENAME);
            temp.add(x.getIdString());
            jobapps.add(temp);
            StorerManager.subStore(jobApplicationSystem, x);

        }
        data.put("jobApplicationManager", jobapps);
    }

    /**
     * Store the documents for this applicant.
     * @param applicant The applicant being stored.
     * @param data  The applicant's data.
     */
    private void storeDocuments(Applicant applicant, HashMap<String, Object> data) {
        DocumentManager docman = applicant.getDocumentManager();
        ArrayList documents = new ArrayList();
        for(JobApplicationDocument x : docman.getDocuments()){
            documents.add(new ArrayList<String>() {{add(x.getId()); add(x.getContents());}});
        }
        data.put("Documents", documents);
    }
}
