package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import Managers.JobPostingManager;
import UsersAndJobObjects.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyLoader extends GenericLoader<Company> {

    /**
     * Load this Company.
     * @param jobApplicationSystem The job application system being used.
     * @param company The company being loaded.
     */
    void loadOne(JobApplicationSystem jobApplicationSystem, Company company) {
        HashMap data = FileSystem.read(Company.FILENAME, company.getName());
        this.loadHRCoordinators(jobApplicationSystem, company, data);
        this.loadFieldMap(jobApplicationSystem, company, data);
        this.loadJobPostingManager(jobApplicationSystem, company, data);

    }

    /**
     * Loads the HRCoordinators from memory
     *
     * @param jobApplicationSystem  The job application system being used.
     * @param company The company being loaded.
     * @param data The Company's Data
     */
    private void loadHRCoordinators(JobApplicationSystem jobApplicationSystem, Company company, HashMap data) {
        ArrayList<HRCoordinator> hrcords = new ArrayList<>();
        for(Object x : (ArrayList)data.get("hrCoordinators")){
            hrcords.add((HRCoordinator) LoaderManager.subLoad(jobApplicationSystem, HRCoordinator.class,
                    (String) ((ArrayList) x).get(0), (String) ((ArrayList) x).get(1)));
        }
        company.setHrCoordinators(hrcords);
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param jobApplicationSystem The job application system being loaded
     * @param company   The company being loaded.
     * @param data The Company's Data
     */
    private void loadFieldMap(JobApplicationSystem jobApplicationSystem, Company company, HashMap data) {
        HashMap<String, ArrayList<Interviewer>> fieldmap = new HashMap<>();
        for(ArrayList fields : ((ArrayList<ArrayList>)data.get("fields"))){
            ArrayList<Interviewer> interviewers = new ArrayList<>();
            for(Object y : fields.subList(1, fields.size())){
                interviewers.add((Interviewer) LoaderManager.subLoad(jobApplicationSystem, Interviewer.class,
                        (String) ((ArrayList) y).get(0), (String) ((ArrayList) y).get(1)));
            }
            fieldmap.put((String)fields.get(0), interviewers);
        }
        company.setFieldToInterviewers(fieldmap);
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param jobApplicationSystem The job application system being loaded
     * @param company The company being loaded.
     * @param data The Company's Data
     */
    private void loadJobPostingManager(JobApplicationSystem jobApplicationSystem, Company company, HashMap data) {
        ArrayList<JobPosting> jobpostings = new ArrayList<>();
        for(Object x : (ArrayList)data.get("jobpostings")){
            jobpostings.add((JobPosting) LoaderManager.subLoad(jobApplicationSystem, JobPosting.class,
                    (String) ((ArrayList) x).get(0), (String) ((ArrayList) x).get(1)));
        }
        company.setJobPostingManager(new JobPostingManager(jobpostings, company));
    }
}
