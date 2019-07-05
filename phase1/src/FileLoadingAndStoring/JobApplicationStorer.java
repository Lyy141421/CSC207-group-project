package FileLoadingAndStoring;

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
    void storeOne(JobApplication jobApplication) {
        LoaderManager.mapPut(JobApplication.FILENAME, String.valueOf(jobApplication.getId()), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(jobApplication, data);
        this.storeApplicant(jobApplication, data);
        this.storeJobPosting(jobApplication, data);
        this.storeInterviews(jobApplication, data);
        FileSystem.write(JobApplication.FILENAME, String.valueOf(jobApplication.getId()), data);
    }

    private void storePrelimInfo(JobApplication jobApplication, HashMap<String, Object> data) {
        data.put("CV", jobApplication.getCV());
        data.put("CoverLetter", jobApplication.getCoverLetter());
        data.put("Status", jobApplication.getStatus());
        data.put("ApplicationDate", jobApplication.getApplicationDate());
    }

    private void storeApplicant(JobApplication jobApplication, HashMap<String, Object> data) {
        data.put("UsersAndJobObjects.Applicant", new ArrayList() {{
            add(Applicant.FILENAME);
            add(jobApplication.getApplicant().getUsername());
        }});
    }

    private void storeJobPosting(JobApplication jobApplication, HashMap<String, Object> data) {
        data.put("JobPosting", new ArrayList() {{
            add(JobPosting.FILENAME);
            add(String.valueOf(jobApplication.getJobPosting().getId()));
        }});
    }

    private void storeInterviews(JobApplication jobApplication, HashMap<String, Object> data) {
        ArrayList interviews = new ArrayList();
        for (Interview x : jobApplication.getInterviews()) {
            interviews.add(new ArrayList() {{
                add(Interview.FILENAME);
                add(String.valueOf(x.getId()));
            }});
        }
        data.put("interviews", interviews);
    }
}
