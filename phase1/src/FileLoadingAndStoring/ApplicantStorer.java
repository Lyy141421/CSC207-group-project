package FileLoadingAndStoring;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantStorer extends GenericStorer<Applicant> {

    /**
     * Store the applicant.
     * @param applicant The applicant to be stored.
     */
    void storeOne(Applicant applicant){
        LoaderManager.mapPut(Applicant.FILENAME, applicant.getUsername(), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(applicant, data);
        this.storeJobApplications(applicant, data);
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

    private void storeJobApplications(Applicant applicant, HashMap<String, Object> data) {
        ArrayList<ArrayList> jobapps = new ArrayList<>();
        for(JobApplication x : applicant.getJobApplicationManager().getJobApplications()){
            ArrayList<String> temp = new ArrayList<>();
            temp.add(JobApplication.FILENAME);
            temp.add(x.getIdString());
            jobapps.add(temp);
            StorerManager.subStore(x);

        }
        data.put("jobApplicationManager", jobapps);
    }

    // TODO
    private void storeDocuments(Applicant applicant, HashMap<String, Object> data) {
    }
}
