package FileLoadingAndStoring;

import Miscellaneous.InterviewTime;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;
import UsersAndJobObjects.JobApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadInterviews {

    /**
     * Load all interviews in this list.
     * @param interviews    The interviews to be loaded.
     */
    public void loadAll(ArrayList<Interview> interviews) {
        for (Interview interview : interviews) {
            this.loadOne(interview);
        }
    }

    /**
     * Loads this interview
     */
    private void loadOne(Interview interview) {
        FileSystem.mapPut(Interview.FILENAME, String.valueOf(interview.getId()), this);
        HashMap data = FileSystem.read(Interview.FILENAME, String.valueOf(interview.getId()));
        this.loadPrelimData(interview, data);
        this.loadJobApplication(interview, data);
        this.loadInterviewer(interview, data);
        this.loadHRCoordinator(interview, data);
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
        interview.setJobApplication((JobApplication) FileSystem.subLoader(JobApplication.class, (String) ((ArrayList)
                data.get("UsersAndJobObjects.JobApplication")).get(0), (String)
                ((ArrayList) data.get("UsersAndJobObjects.JobApplication")).get(1)));
    }

    private void loadInterviewer(Interview interview, HashMap data) {
        interview.setHrCoordinator((HRCoordinator) FileSystem.subLoader(HRCoordinator.class, (String)
                ((ArrayList) data.get("UsersAndJobObjects.HRCoordinator")).get(0), (String) ((ArrayList)
                data.get("UsersAndJobObjects.HRCoordinator")).get(1)));
    }

    private void loadHRCoordinator(Interview interview, HashMap data) {
        interview.setInterviewer((Interviewer) FileSystem.subLoader(Interviewer.class, (String) ((ArrayList)
                data.get("interviewer")).get(0), (String) ((ArrayList) data.get("interviewer")).get(1)));
    }
}
