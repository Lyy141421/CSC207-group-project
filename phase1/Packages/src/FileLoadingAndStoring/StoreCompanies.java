package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreCompanies extends StoreObjects<Company> {

    /**
     * Stores the company.
     */
    void storeOne(Company company){
        FileSystem.mapPut(Company.FILENAME, company.getName(), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storeHRCoordinators(company, data);
        this.storeFieldToInterviewers(company, data);
        this.storeJobPostings(company, data);
        FileSystem.write(Company.FILENAME, company.getName(), data);
    }

    private void storeHRCoordinators(Company company, HashMap<String, Object> data) {
        ArrayList<ArrayList<String>> hrcoords = new ArrayList<>();
        for(HRCoordinator x : company.getHrCoordinators()){
            hrcoords.add(new ArrayList<String>() {{
                add(HRCoordinator.FILENAME);
                add(x.getUsername());
            }});
        }
        data.put("hrCoordinators", hrcoords);
    }

    private void storeFieldToInterviewers(Company company, HashMap<String, Object> data) {
        ArrayList fields = new ArrayList();
        for(String field : company.getFieldToInterviewers().keySet()){
            ArrayList temp = new ArrayList();
            temp.add(field);
            for(Interviewer interviewer : company.getFieldToInterviewers().get(field)){
                temp.add(new ArrayList<String>() {{
                    add(Interviewer.FILENAME);
                    add(interviewer.getUsername());
                }});
            }
        }
        data.put("fields", fields);
    }

    private void storeJobPostings(Company company, HashMap<String, Object> data) {
        ArrayList<ArrayList<String>> jobpostings = new ArrayList<>();
        for(JobPosting x : company.getJobPostingManager().getJobPostings()){
            jobpostings.add(new ArrayList<String>() {{
                add(JobPosting.FILENAME);
                add(String.valueOf(x.getId()));
            }});
        }
        data.put("jobpostings", jobpostings);
    }
}
