package FileLoadingAndStoring;

import Managers.JobPostingManager;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadCompanies {

    /**
     * Load all companies in this list.
     * @param companies The list of companies to be loaded.
     */
    public void loadAll(ArrayList<Company> companies) {
        for (Company company : companies) {
            this.loadOne(company);
        }
    }

    /**
     * Load this UsersAndJobObjects.Company.
     */
    private void loadOne(Company company){
        FileSystem.mapPut(Company.FILENAME, company.getName(), this);
        HashMap data = FileSystem.read(Company.FILENAME, company.getName());
        this.loadHRCoordinators(company, data);
        this.loadFieldMap(company, data);
        this.loadJobPostingManager(company, data);

    }

    /**
     * Loads the HRCoordinators from memory
     *
     * @param data The UsersAndJobObjects.Company's Data
     */
    private void loadHRCoordinators(Company company, HashMap data){
        ArrayList<HRCoordinator> hrcords = new ArrayList<>();
        for(Object x : (ArrayList)data.get("hrCoordinators")){
            hrcords.add((HRCoordinator) FileSystem.subLoader(HRCoordinator.class, (String)((ArrayList)x).get(0),
                    (String)((ArrayList)x).get(1)));
        }
        company.setHrCoordinators(hrcords);
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param data The UsersAndJobObjects.Company's Data
     */
    private void loadFieldMap(Company company, HashMap data){
        HashMap<String, ArrayList<Interviewer>> fieldmap = new HashMap<>();
        for(ArrayList fields : ((ArrayList<ArrayList>)data.get("fields"))){
            ArrayList<Interviewer> interviewers = new ArrayList<>();
            for(Object y : fields.subList(1, fields.size())){
                interviewers.add((Interviewer) FileSystem.subLoader(Interviewer.class, (String)((ArrayList)y).get(0),
                        (String)((ArrayList)y).get(1)));
            }
            fieldmap.put((String)fields.get(0), interviewers);
        }
        company.setFieldToInterviewers(fieldmap);
    }

    /**
     * Loads the FieldMap from memory
     *
     * @param data The UsersAndJobObjects.Company's Data
     */
    private void loadJobPostingManager(Company company, HashMap data){
        ArrayList<JobPosting> jobpostings = new ArrayList<>();
        for(Object x : (ArrayList)data.get("jobpostings")){
            jobpostings.add((JobPosting) FileSystem.subLoader(JobPosting.class, (String)((ArrayList)x).get(0),
                    (String)((ArrayList)x).get(1)));
        }
        company.setJobPostingManager(new JobPostingManager(jobpostings, company));
    }
}
