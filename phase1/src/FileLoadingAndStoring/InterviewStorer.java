package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InterviewStorer extends GenericStorer<Interview> {

    /**
     * Stores the interview.
     * @param jobApplicationSystem The job application being used.
     * @param interview The interview to be stored.
     */
    void storeOne(JobApplicationSystem jobApplicationSystem, Interview interview) {
        LoaderManager.mapPut(Interview.FILENAME, String.valueOf(interview.getId()), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(interview, data);
        this.storeJobApplicant(jobApplicationSystem, interview, data);
        this.storeInterviewer(jobApplicationSystem, interview, data);
        this.storeHRCoordinator(jobApplicationSystem, interview, data);
        this.storeInterviewTime(interview, data);
        FileSystem.write(Interview.FILENAME, String.valueOf(interview.getId()), data);
    }

    /**
     * Stores the preliminary information for the interview.
     *
     * @param interview The interview to be stored.
     * @param data      The company's data.
     */
    private void storePrelimInfo(Interview interview, HashMap<String, Object> data) {
        data.put("interviewNotes", interview.getInterviewNotes());
        data.put("pass", interview.isPassed());
        data.put("roundNumber", interview.getRoundNumber());
    }

    /**
     * Stores the preliminary information for the interview.
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview to be stored.
     * @param data  The company's data.
     */
    private void storeJobApplicant(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("JobApplication", new ArrayList() {{
            add(JobApplication.FILENAME);
            add(interview.getJobApplication().getId());
            StorerManager.subStore(jobApplicationSystem, interview.getApplicant());
        }});
    }

    /**
     * Stores the interviewer for the interview.
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview to be stored.
     * @param data  The company's data.
     */
    private void storeInterviewer(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("interviewer", new ArrayList() {{
            add(Interviewer.FILENAME);
            add(interview.getInterviewer().getUsername());
            StorerManager.subStore(jobApplicationSystem, interview.getInterviewer());
        }});
    }

    /**
     * Stores the HR Coordinator for the interview.
     * @param jobApplicationSystem The job application system being used.
     * @param interview The interview to be stored.
     * @param data  The company's data.
     */
    private void storeHRCoordinator(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("HRCoordinator", new ArrayList() {{
            add(HRCoordinator.FILENAME);
            add(interview.getHRCoordinator().getUsername());
            StorerManager.subStore(jobApplicationSystem, interview.getHRCoordinator());
        }});
    }

    /**
     * Stores the interview time for the interview.
     * @param interview The interview to be stored.
     * @param data  The company's data.
     */
    private void storeInterviewTime(Interview interview, HashMap<String, Object> data) {
        data.put("InterviewTimeDate", interview.getTime().getDate());
        data.put("InterviewTimeTimeslot", interview.getTime().getTimeSlot());
    }
}
