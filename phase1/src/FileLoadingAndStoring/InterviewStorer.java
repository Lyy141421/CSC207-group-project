package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InterviewStorer extends GenericStorer<Interview> {

    /**
     * Stores the interview.
     * @param interview The interview to be stored.
     */
    void storeOne(JobApplicationSystem jobApplicationSystem, Interview interview) {
        LoaderManager.mapPut(Interview.FILENAME, String.valueOf(interview.getId()), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(interview, data);
        this.storeApplicant(jobApplicationSystem, interview, data);
        this.storeInterviewer(jobApplicationSystem, interview, data);
        this.storeHRCoordinator(jobApplicationSystem, interview, data);
        this.storeInterviewTime(interview, data);
        FileSystem.write(Interview.FILENAME, String.valueOf(interview.getId()), data);
    }

    private void storePrelimInfo(Interview interview, HashMap<String, Object> data) {
        data.put("interviewNotes", interview.getInterviewNotes());
        data.put("pass", interview.isPassed());
        data.put("roundNumber", interview.getRoundNumber());
    }

    private void storeApplicant(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("UsersAndJobObjects.JobApplication", new ArrayList() {{
            add(Applicant.FILENAME);
            add(interview.getApplicant().getUsername());
            StorerManager.subStore(jobApplicationSystem, interview.getApplicant());
        }});
    }

    private void storeInterviewer(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("interviewer", new ArrayList() {{
            add(Interviewer.FILENAME);
            add(interview.getInterviewer().getUsername());
            StorerManager.subStore(jobApplicationSystem, interview.getInterviewer());
        }});
    }

    private void storeHRCoordinator(JobApplicationSystem jobApplicationSystem, Interview interview, HashMap<String, Object> data) {
        data.put("UsersAndJobObjects.HRCoordinator", new ArrayList() {{
            add(HRCoordinator.FILENAME);
            add(interview.getHRCoordinator().getUsername());
            StorerManager.subStore(jobApplicationSystem, interview.getHRCoordinator());
        }});
    }

    private void storeInterviewTime(Interview interview, HashMap<String, Object> data) {
        data.put("InterviewTimeDate", interview.getTime().getDate());
        data.put("InterviewTimeTimeslot", interview.getTime().getTimeSlot());
    }
}
