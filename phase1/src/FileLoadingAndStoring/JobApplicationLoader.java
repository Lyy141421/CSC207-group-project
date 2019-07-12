package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class JobApplicationLoader extends GenericLoader<JobApplication> {

    /**
     * Load this job application.
     * @param jobApplicationSystem The job application system being used.
     * @param jobApplication The job application being loaded.
     */
    void loadOne(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication) {
        HashMap data = FileSystem.read(JobApplication.FILENAME, String.valueOf(jobApplication.getId()));
        this.loadPrelimData(jobApplication, data);
        this.loadApplicant(jobApplicationSystem, jobApplication, data);
        this.loadPosting(jobApplicationSystem, jobApplication, data);
        this.loadInterviews(jobApplicationSystem, jobApplication, data);
        jobApplication.setCv(jobApplication.getApplicant().getDocumentManager().getDocumentById((String) data.get("CV")));
        jobApplication.setCoverLetter(jobApplication.getApplicant().getDocumentManager().getDocumentById((String) data.get("CoverLetter")));
    }

    /**
     * Load the preliminary data for this job application.
     *
     * @param jobApplication The job application being loaded.
     * @param data The data for this job application.
     */
    private void loadPrelimData(JobApplication jobApplication, HashMap data) {
        jobApplication.setStatus(new Status((int)data.get("Status")));
        jobApplication.setApplicationDate((LocalDate.parse((String) data.get("ApplicationDate"))));
    }

    /**
     * Load the application for this job application.
     *
     * @param jobApplicationSystem The job application system being used.
     * @param jobApplication The job application being loaded.
     * @param data The data for this job application.
     */
    private void loadApplicant(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap data) {
        jobApplication.setApplicant((Applicant) LoaderManager.subLoad(jobApplicationSystem, Applicant.class, (String)
                ((ArrayList) data.get("Applicant")).get(0), (String)
                ((ArrayList) data.get("Applicant")).get(1)));
    }

    /**
     * Load the application for this job application.
     *
     * @param jobApplicationSystem The job application system being used.
     * @param jobApplication The job application being loaded.
     * @param data The data for this job application.
     */
    private void loadPosting(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap data) {
        jobApplication.setJobPosting(((JobPosting) LoaderManager.subLoad(jobApplicationSystem, JobPosting.class, (String) ((ArrayList)
                data.get("JobPosting")).get(0), (String) ((ArrayList) data.get("JobPosting")).get(1))));
    }

    /**
     * Load the interviews for this job application.
     *
     * @param data The data for this job application.
     */
    private void loadInterviews(JobApplicationSystem jobApplicationSystem, JobApplication jobApplication, HashMap data) {
        ArrayList<Interview> temp = new ArrayList();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("interviews")) {
            temp.add((Interview) LoaderManager.subLoad(jobApplicationSystem, Interview.class, (String) x.get(0), (String) x.get(1)));
        }
        jobApplication.setInterviews(temp);
    }
}
