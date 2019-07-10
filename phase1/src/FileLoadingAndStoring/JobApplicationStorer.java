package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.util.ArrayList;
import java.util.HashMap;

public class JobApplicationStorer extends GenericStorer<JobApplication> {

    /**
     * Stores the job application.
     * @param jobApplication    The job application to be stored.
     */
    void storeOne(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication) {
        LoaderManager.mapPut(JobApplication.FILENAME, String.valueOf(jobApplication.getId()), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(jobApplication, data);
        this.storeApplicant(jobApplicationSystem, jobApplication, data);
        this.storeJobPosting(jobApplicationSystem, jobApplication, data);
        this.storeInterviews(jobApplicationSystem, jobApplication, data);
        FileSystem.write(JobApplication.FILENAME, String.valueOf(jobApplication.getId()), data);
    }

    private void storePrelimInfo(JobApplication jobApplication, HashMap<String, Object> data) {
        if(!(jobApplication.getCV() == null)){
            data.put("CV", jobApplication.getCV().getId());
            data.put("CoverLetter", jobApplication.getCoverLetter().getId());
        }
        data.put("Status", jobApplication.getStatus().getValue());
        data.put("ApplicationDate", jobApplication.getApplicationDate());
    }

    private void storeApplicant(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap<String, Object> data) {
        data.put("Applicant", new ArrayList() {{
            add(Applicant.FILENAME);
            add(jobApplication.getApplicant().getUsername());
            StorerManager.subStore(jobApplicationSystem, jobApplication.getApplicant());
        }});
    }

    private void storeJobPosting(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap<String, Object> data) {
        data.put("JobPosting", new ArrayList() {{
            add(JobPosting.FILENAME);
            add(String.valueOf(jobApplication.getJobPosting().getId()));
            StorerManager.subStore(jobApplicationSystem, jobApplication.getJobPosting());
        }});
    }

    private void storeInterviews(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap<String, Object> data) {
        ArrayList interviews = new ArrayList();
        for (Interview x : jobApplication.getInterviews()) {
            interviews.add(new ArrayList() {{
                add(Interview.FILENAME);
                add(String.valueOf(x.getId()));
                StorerManager.subStore(jobApplicationSystem, x);
            }});
        }
        data.put("interviews", interviews);
    }
}
