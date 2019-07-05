package FileLoadingAndStoring;

import Miscellaneous.InterviewTime;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class InterviewLoader extends GenericLoader<Interview> {

    /**
     * Loads this interview
     * @param interview The interview to be loaded.
     */
    void loadOne(Interview interview) {
        System.out.println("Here");
        HashMap data = FileSystem.read(Interview.FILENAME, String.valueOf(interview.getId()));
        this.loadJobApplication(interview, data);
        this.loadInterviewer(interview, data);
        this.loadHRCoordinator(interview, data);
        this.loadPrelimData(interview, data);
    }

    /**
     * Load the preliminary data for this UsersAndJobObjects.Interviewer.
     *
     * @param data The UsersAndJobObjects.Company's Data
     */
    private void loadPrelimData(Interview interview, HashMap data) {
        interview.setInterviewNotes((String)data.get("interviewNotes"));
        interview.setPass((boolean)data.get("pass"));
        interview.setRoundNumber((int)data.get("roundNumber"));
        interview.setInterviewManager((interview.getJobApplication().getJobPosting().getInterviewManager()));
        interview.setTime(new InterviewTime(LocalDate.parse((String) data.get("InterviewTimeDate")),
                (int) data.get("InterviewTimeTimeslot")));
    }

    private void loadJobApplication(Interview interview, HashMap data) {
        System.out.println("Check");
        interview.setJobApplication((JobApplication) LoaderManager.subLoad(JobApplication.class, (String) ((ArrayList)
                data.get("JobApplication")).get(0), (String)
                ((ArrayList) data.get("JobApplication")).get(1)));
    }

    private void loadInterviewer(Interview interview, HashMap data) {
        interview.setHrCoordinator((HRCoordinator) LoaderManager.subLoad(HRCoordinator.class, (String)
                ((ArrayList) data.get("HRCoordinator")).get(0), (String) ((ArrayList)
                data.get("HRCoordinator")).get(1)));
    }

    private void loadHRCoordinator(Interview interview, HashMap data) {
        interview.setInterviewer((Interviewer) LoaderManager.subLoad(Interviewer.class, (String) ((ArrayList)
                data.get("interviewer")).get(0), (String) ((ArrayList) data.get("interviewer")).get(1)));
    }
}
