package FileLoadingAndStoring;

import Main.JobApplicationSystem;
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
     * @param jobApplicationSystem The job application system being used.
     * @param applicant The applicant being loaded
     */
    void loadOne(JobApplicationSystem jobApplicationSystem, Applicant applicant) {
        HashMap data = FileSystem.read(Applicant.FILENAME, applicant.getUsername());
        this.loadPrelimData(applicant, data);
        this.loadJobAppManager(jobApplicationSystem, applicant, data);
        this.loadDocumentManager(applicant, data);
    }

    /**
     * Load the preliminary data for this applicant.
     *
     * @param applicant The applicant being loaded.
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
     * @param jobApplicationSystem The job application system being used.
     * @param applicant The applicant being loaded.
     * @param data This applicant's data.
     */
    private void loadJobAppManager(JobApplicationSystem jobApplicationSystem, Applicant applicant, HashMap data) {
        ArrayList<JobApplication> temp = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) (data.get("jobApplicationManager"))) {
            temp.add((JobApplication) LoaderManager.subLoad(jobApplicationSystem, JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        applicant.setJobApplicationManager(new JobApplicationManager(temp));
    }

    /**
     * Load the document manager for this applicant.
     *
     * @param applicant The applicant being loarded.
     * @param data      This applicant's data.
     */
    private void loadDocumentManager(Applicant applicant, HashMap data) {
        ArrayList documents = (ArrayList) data.get("Documents");
        ArrayList jad = new ArrayList();
        for(Object document : documents){
            JobApplicationDocument doc = new JobApplicationDocument((String)((ArrayList)document).get(0),
                    (String)((ArrayList)document).get(1));
            jad.add(doc);
        }
        applicant.setDocumentManager(new DocumentManager(jad));
    }

}
