package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class InterviewerLoader extends GenericLoader<Interviewer> {

    /**
     *  Loads the interviewer.
     * @param jobApplicationSystem The job application system being used.
     * @param interviewer   The interviewer to be loaded.
     */
    void loadOne(JobApplicationSystem jobApplicationSystem, Interviewer interviewer) {
        HashMap data = FileSystem.read(Interviewer.FILENAME, interviewer.getUsername());
        this.loadPrelimData(interviewer, data);
        this.loadCompany(jobApplicationSystem, interviewer, data);
        this.loadInterviews(jobApplicationSystem, interviewer, data);
    }

    /**
     * Load the preliminary data for this interviewer.
     *
     * @param interviewer   The interviewer being loaded
     * @param data The data for this interviewer.
     */
    private void loadPrelimData(Interviewer interviewer, HashMap data) {
        interviewer.setPassword((String) data.get("password"));
        interviewer.setLegalName((String) data.get("legalName"));
        interviewer.setEmail((String) data.get("email"));
        interviewer.setPassword((String) data.get("password"));
        interviewer.setDateCreated(LocalDate.parse((String) data.get("dateCreated")));
        interviewer.setField((String) data.get("field"));
    }

    /**
     * Load the company for this interviewer.
     *
     * @param jobApplicationSystem  The job application system being used.
     * @param interviewer The interviewer being loaded.
     * @param data The data for this interviewer.
     */
    private void loadCompany(JobApplicationSystem jobApplicationSystem, Interviewer interviewer, HashMap data) {
        interviewer.setCompany((Company) LoaderManager.subLoad(jobApplicationSystem, Company.class, (String)
                        ((ArrayList) data.get("company")).get(0), (String) ((ArrayList) data.get("company")).get(1)));
    }

    /**
     * Load the interviews for this interviewer.
     *
     * @param jobApplicationSystem  The job application system being used.
     * @param interviewer The interviewer being loaded.
     * @param data The data for this interviewer.
     */
    private void loadInterviews(JobApplicationSystem jobApplicationSystem, Interviewer interviewer, HashMap data) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Object x : (ArrayList) (data.get("interviews"))) {
            interviews.add((Interview) LoaderManager.subLoad(jobApplicationSystem, Interview.class, (String) ((ArrayList) x).get(0), (String)
                    ((ArrayList) x).get(1)));
        }
        interviewer.setInterviews(interviews);
    }
}
