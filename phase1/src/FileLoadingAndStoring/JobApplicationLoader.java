package FileLoadingAndStoring;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class JobApplicationLoader extends GenericLoader<JobApplication> {

    /**
     * Load this job application.
     */
    void loadOne(JobApplication jobApplication) {
        HashMap data = FileSystem.read(JobApplication.FILENAME, String.valueOf(jobApplication.getId()));
        this.loadPrelimData(jobApplication, data);
        this.loadApplicant(jobApplication, data);
        this.loadPosting(jobApplication, data);
        this.loadInterviews(jobApplication, data);
    }

    /**
     * Load the preliminary data for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadPrelimData(JobApplication jobApplication, HashMap data) {
        jobApplication.setCV((String) data.get("CV"));
        jobApplication.setCoverLetter((String) data.get("CoverLetter"));
        jobApplication.setStatus((int) data.get("Status"));
        jobApplication.setApplicationDate((LocalDate.parse((String) data.get("ApplicationDate"))));
    }

    /**
     * Load the application for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadApplicant(JobApplication jobApplication, HashMap data) {
        jobApplication.setApplicant((Applicant) LoaderManager.subLoad(Applicant.class, (String)
                ((ArrayList) data.get("Applicant")).get(0), (String)
                ((ArrayList) data.get("Applicant")).get(1)));
    }

    /**
     * Load the application for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadPosting(JobApplication jobApplication, HashMap data) {
        jobApplication.setJobPosting(((JobPosting) LoaderManager.subLoad(JobPosting.class, (String) ((ArrayList)
                data.get("JobPosting")).get(0), (String) ((ArrayList) data.get("JobPosting")).get(1))));
    }

    /**
     * Load the interviews for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadInterviews(JobApplication jobApplication, HashMap data) {
        ArrayList<Interview> temp = new ArrayList();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("interviews")) {
            temp.add((Interview) LoaderManager.subLoad(Interview.class, (String)x.get(0), (String)x.get(1)));
        }
        jobApplication.setInterviews(temp);
    }
}
