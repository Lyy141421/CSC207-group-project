package FileLoadingAndStoring;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreApplicants {

    /**
     * Save all the applicants in this list.
     * @param applicants    The list of applicants to be saved.
     */
    public void storeAll(ArrayList<Applicant> applicants) {
        for (Applicant applicant : applicants) {
            this.storeOne(applicant);
        }
    }

    /**
     * Save the applicant.
     */
    private void storeOne(Applicant applicant){
        FileSystem.mapPut(Applicant.FILENAME, applicant.getUsername(), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(applicant, data);
        this.storeJobApplications(applicant, data);
        this.storeFilesSubmitted(applicant, data);
        FileSystem.write(Applicant.FILENAME, applicant.getUsername(), data);
    }

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

        }
        data.put("jobApplicationManager", jobapps);
    }

    private void storeFilesSubmitted(Applicant applicant, HashMap<String, Object> data) {
        data.put("filesSubmitted", applicant.getFilesSubmitted());
    }
}
