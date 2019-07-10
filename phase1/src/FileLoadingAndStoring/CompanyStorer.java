package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyStorer extends GenericStorer<Company> {

    /**
     * Stores the company.
     * @param company   The company to be stored.
     */
    void storeOne(JobApplicationSystem jobApplicationSystem, Company company) {
        LoaderManager.mapPut(Company.FILENAME, company.getName(), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storeHRCoordinators(jobApplicationSystem, company, data);
        this.storeFieldToInterviewers(jobApplicationSystem, company, data);
        this.storeJobPostings(jobApplicationSystem, company, data);
        FileSystem.write(Company.FILENAME, company.getName(), data);
    }

    private void storeHRCoordinators(JobApplicationSystem jobApplicationSystem, Company company, HashMap<String, Object> data) {
        ArrayList<ArrayList<String>> hrcoords = new ArrayList<>();
        for(HRCoordinator x : company.getHrCoordinators()){
            hrcoords.add(new ArrayList<String>() {{
                add(HRCoordinator.FILENAME);
                add(x.getUsername());
                StorerManager.subStore(jobApplicationSystem, x);
            }});
        }
        data.put("hrCoordinators", hrcoords);
    }

    private void storeFieldToInterviewers(JobApplicationSystem jobApplicationSystem, Company company,
                                          HashMap<String, Object> data) {
        ArrayList fields = new ArrayList();
        for(String field : company.getFieldToInterviewers().keySet()){
            ArrayList temp = new ArrayList();
            temp.add(field);
            for(Interviewer interviewer : company.getFieldToInterviewers().get(field)){
                temp.add(new ArrayList<String>() {{
                    add(Interviewer.FILENAME);
                    add(interviewer.getUsername());
                    StorerManager.subStore(jobApplicationSystem, interviewer);
                }});
            }
        }
        data.put("fields", fields);
    }

    private void storeJobPostings(JobApplicationSystem jobApplicationSystem, Company company, HashMap<String, Object> data) {
        ArrayList<ArrayList<String>> jobpostings = new ArrayList<>();
        for(JobPosting x : company.getJobPostingManager().getJobPostings()){
            jobpostings.add(new ArrayList<String>() {{
                add(JobPosting.FILENAME);
                add(String.valueOf(x.getId()));
                StorerManager.subStore(jobApplicationSystem, x);
            }});
        }
        data.put("jobpostings", jobpostings);
    }
}
