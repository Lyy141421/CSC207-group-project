package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;
import java.util.HashMap;

public class JobPostingStorer extends GenericStorer<JobPosting> {

    /**
     * Stores the job posting.
     * @param jobPosting    The job posting to be stored.
     */
    void storeOne(JobPosting jobPosting){
        LoaderManager.mapPut(JobPosting.FILENAME, String.valueOf(jobPosting.getId()), jobPosting);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimData(jobPosting, data);
        this.storeCompany(jobPosting, data);
        this.storeAllApplications(jobPosting, data);
        if (jobPosting.isClosed(JobApplicationSystem.getToday())) {
            this.storeApplicationsInConsideration(jobPosting, data);
            this.storeApplicationsRejected(jobPosting, data);
            this.storeCurrentRound(jobPosting, data);
        }
        FileSystem.write(JobPosting.FILENAME, String.valueOf(jobPosting.getId()), data);
    }

    private void storePrelimData(JobPosting jobPosting, HashMap<String, Object> data) {
        data.put("title", jobPosting.getTitle());
        data.put("field", jobPosting.getField());
        data.put("description", jobPosting.getDescription());
        data.put("requirements", jobPosting.getRequirements());
        data.put("numPositions", jobPosting.getNumPositions());
        data.put("postDate", jobPosting.getPostDate());
        data.put("closeDate", jobPosting.getCloseDate());
        data.put("filled", jobPosting.isFilled());
    }

    private void storeCompany(JobPosting jobPosting, HashMap<String, Object> data) {
        Company c = jobPosting.getCompany();
        data.put("Company", new ArrayList(){{add(Company.FILENAME); add(c.getName());}});
        StorerManager.subStore(c);
    }

    private void storeAllApplications(JobPosting jobPosting, HashMap<String, Object> data) {
        ArrayList jobapplications = new ArrayList();
        for(JobApplication x : jobPosting.getJobApplications()){
            jobapplications.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
                StorerManager.subStore(x);
            }});
        }
        data.put("jobapplications", jobapplications);
    }

    private void storeApplicationsInConsideration(JobPosting jobPosting, HashMap<String, Object> data) {
        ArrayList applicationsInConsideration = new ArrayList();
        for(JobApplication x : jobPosting.getInterviewManager().getApplicationsInConsideration()){
            applicationsInConsideration.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
                StorerManager.subStore(x);
            }});
        }
        data.put("applicationsInConsideration", applicationsInConsideration);
    }

    private void storeApplicationsRejected(JobPosting jobPosting, HashMap<String, Object> data) {
        ArrayList applicationsRejected = new ArrayList();
        for(JobApplication x : jobPosting.getInterviewManager().getApplicationsRejected()){
            applicationsRejected.add(new ArrayList() {{
                add(JobApplication.FILENAME);
                add(x.getIdString());
                StorerManager.subStore(x);
            }});
        }
        data.put("applicationsRejected", applicationsRejected);
    }

    private void storeCurrentRound(JobPosting jobPosting, HashMap<String, Object> data) {
        data.put("currentRound", jobPosting.getInterviewManager().getCurrentRound());
    }

}
