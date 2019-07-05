package FileLoadingAndStoring;

import Managers.InterviewManager;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadJobPostings extends LoadObjects<JobPosting> {
    /**
     * A class that loads all job postings.
     */


    /**
     * Loads the job posting.
     * @param jobPosting The job posting to load.
     */
    void loadOne(JobPosting jobPosting){
        FileSystem.mapPut(JobPosting.FILENAME, String.valueOf(jobPosting.getId()), jobPosting);
        HashMap data = FileSystem.read(JobPosting.FILENAME,  String.valueOf(jobPosting.getId()));
        this.loadPrelimData(data, jobPosting);
        this.loadJobApps(data, jobPosting);
        ArrayList<JobApplication> appsInConsideration = this.loadAppsInConsideration(data);
        ArrayList<JobApplication> appsRejected = this.loadAppsRejected(data);
        jobPosting.setInterviewManager(new InterviewManager(jobPosting, appsInConsideration, appsRejected,
                (int) data.get("currentRound")));
        jobPosting.setCompany((Company) FileSystem.subLoader(Company.class, (String)((ArrayList)data.
                        get("UsersAndJobObjects.Company")).get(0),
                (String)((ArrayList)data.get("UsersAndJobObjects.Company")).get(1)));
    }

    /**
     * Load the preliminary data for this job posting.
     *
     * @param data The data for this job posting.
     * @param jobPosting The job posting to load.
     */
    private void loadPrelimData(HashMap data, JobPosting jobPosting) {
        jobPosting.setTitle((String) data.get("title"));
        jobPosting.setField((String) data.get("field"));
        jobPosting.setDescription((String) data.get("description"));
        jobPosting.setRequirements((String) data.get("requirements"));
        jobPosting.setNumPositions((int) data.get("numPositions"));
        jobPosting.setPostDate(LocalDate.parse((String) data.get("postDate")));
        jobPosting.setCloseDate(LocalDate.parse((String) data.get("closeDate")));
        if ((boolean) data.get("filled")) {
            jobPosting.setFilled();
        }
    }

    /**
     * Load the job applications for this job posting.
     *
     * @param data The data for this job posting.
     * @param jobPosting The job posting to load.
     */
    private void loadJobApps(HashMap data, JobPosting jobPosting) {
        ArrayList<JobApplication> jobApplications = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("jobapplications")) {
            jobApplications.add((JobApplication) FileSystem.subLoader(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        jobPosting.setJobApplications(jobApplications);
    }

    /**
     * Load the applications in consideration for this job posting.
     *
     * @param data The data for this job posting.
     * @return a list of job applications in consideration.
     */
    private ArrayList<JobApplication> loadAppsInConsideration(HashMap data) {
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("applicationsInConsideration")) {
            applicationsInConsideration.add((JobApplication) FileSystem.subLoader(JobApplication.class,
                    (String) x.get(0), (String) x.get(1)));
        }
        return applicationsInConsideration;
    }

    /**
     * Load the applications rejected for this job posting.
     *
     * @param data The data for this job posting.
     * @return a list of job applications rejected for this job posting.
     */
    private ArrayList<JobApplication> loadAppsRejected(HashMap data) {
        ArrayList<JobApplication> applicationsRejected = new ArrayList<>();
        for (ArrayList x : (ArrayList<ArrayList>) data.get("applicationsRejected")) {
            applicationsRejected.add((JobApplication) FileSystem.subLoader(JobApplication.class, (String) x.get(0),
                    (String) x.get(1)));
        }
        return applicationsRejected;
    }
}
