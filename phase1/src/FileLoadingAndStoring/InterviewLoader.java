package FileLoadingAndStoring;

import Main.JobApplicationSystem;
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
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview to be loaded.
     */
    void loadOne(JobApplicationSystem jobApplicationSystem, Interview interview) {
        HashMap data = FileSystem.read(Interview.FILENAME, String.valueOf(interview.getId()));
        this.loadJobApplication(jobApplicationSystem, interview, data);
        this.loadInterviewer(jobApplicationSystem, interview, data);
        this.loadHRCoordinator(jobApplicationSystem, interview, data);
        this.loadPrelimData(interview, data);
    }

    /**
     * Load the preliminary data for this Interview.
     * @param interview The interview being loaded
     * @param data The Company's Data
     */
    private void loadPrelimData(Interview interview, HashMap data) {
        interview.setInterviewNotes((String)data.get("interviewNotes"));
        interview.setPass((boolean)data.get("pass"));
        interview.setRoundNumber((int)data.get("roundNumber"));
        interview.setInterviewManager((interview.getJobApplication().getJobPosting().getInterviewManager()));
        interview.setTime(new InterviewTime(LocalDate.parse((String) data.get("InterviewTimeDate")),
                (int) data.get("InterviewTimeTimeslot")));
    }

    /**
     * Loads the job applications for this interview.
     *
     * @param jobApplicationSystem The job application system being used.
     * @param interview            The interview being loaded
     * @param data                 The Company's Data
     */
    private void loadJobApplication(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap data) {
        interview.setJobApplication((JobApplication) LoaderManager.subLoad(jobApplicationSystem, JobApplication.class, (String) ((ArrayList)
                data.get("JobApplication")).get(0), (String)
                ((ArrayList) data.get("JobApplication")).get(1)));
    }

    /**
     * Loads the interviewer for this interview.
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview being loaded
     * @param data The Company's Data
     */
    private void loadInterviewer(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap data) {
        interview.setHrCoordinator((HRCoordinator) LoaderManager.subLoad(jobApplicationSystem, HRCoordinator.class, (String)
                ((ArrayList) data.get("HRCoordinator")).get(0), (String) ((ArrayList)
                data.get("HRCoordinator")).get(1)));
    }

    /**
     * Loads the HR Coordinator for this interview.
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview being loaded
     * @param data The Company's Data
     */
    private void loadHRCoordinator(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap data) {
        interview.setInterviewer((Interviewer) LoaderManager.subLoad(jobApplicationSystem, Interviewer.class, (String) ((ArrayList)
                data.get("interviewer")).get(0), (String) ((ArrayList) data.get("interviewer")).get(1)));
    }
}
