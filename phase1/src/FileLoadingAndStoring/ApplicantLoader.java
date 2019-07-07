package FileLoadingAndStoring;

import Managers.DocumentManager;
import Managers.JobApplicationManager;
import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobApplicationDocument;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantLoader extends GenericLoader<Applicant> {

    /**
     * Loads the applicant
     */
    void loadOne(Applicant applicant){
        HashMap data = FileSystem.read(Applicant.FILENAME, applicant.getUsername());
        this.loadPrelimData(applicant, data);
        this.loadJobAppManager(applicant, data);
    }

    /**
     * Load the preliminary data for this applicant.
     *
     * @param data This applicant's data.
     */
    private void loadPrelimData(Applicant applicant, HashMap data) {
        applicant.setPassword((String) data.get("password"));
        applicant.setLegalName((String) data.get("legalName"));
        applicant.setEmail((String) data.get("email"));
        applicant.setDateCreated(LocalDate.parse((String) data.get("dateCreated")));
    }

    /**
     * Load the job application manager for this applicant.
     *
     * @param data This applicant's data.
     */
    private void loadJobAppManager(Applicant applicant, HashMap data) {
        ArrayList<JobApplication> temp = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) (data.get("jobApplicationManager"))) {
            temp.add((JobApplication) LoaderManager.subLoad(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        applicant.setJobApplicationManager(new JobApplicationManager(temp));
    }

    // TODO
    private void loadDocumentManager(Applicant applicant, HashMap data) {

    }

}
