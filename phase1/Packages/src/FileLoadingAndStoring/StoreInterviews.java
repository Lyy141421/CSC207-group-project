package FileLoadingAndStoring;

import UsersAndJobObjects.Applicant;
import UsersAndJobObjects.HRCoordinator;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreInterviews extends StoreObjects<Interview> {

    /**
     * Stores the interview.
     */
    void storeOne(Interview interview){
        FileSystem.mapPut(Interview.FILENAME, String.valueOf(interview.getId()), this);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(interview, data);
        this.storeApplicant(interview, data);
        this.storeInterviewer(interview, data);
        this.storeHRCoordinator(interview, data);
        this.storeInterviewTime(interview, data);
    }

    private void storePrelimInfo(Interview interview, HashMap<String, Object> data) {
        data.put("interviewNotes", interview.getInterviewNotes());
        data.put("pass", interview.isPassed());
        data.put("roundNumber", interview.getRoundNumber());
    }

    private void storeApplicant(Interview interview, HashMap<String, Object> data) {
        data.put("UsersAndJobObjects.JobApplication", new ArrayList() {{
            add(Applicant.FILENAME);
            add(interview.getApplicant().getUsername());
        }});
    }

    private void storeInterviewer(Interview interview, HashMap<String, Object> data) {
        data.put("interviewer", new ArrayList() {{
            add(Interviewer.FILENAME);
            add(interview.getInterviewer().getUsername());
        }});
    }

    private void storeHRCoordinator(Interview interview, HashMap<String, Object> data) {
        data.put("UsersAndJobObjects.HRCoordinator", new ArrayList() {{
            add(HRCoordinator.FILENAME);
            add(interview.getHRCoordinator().getUsername());
        }});
    }

    private void storeInterviewTime(Interview interview, HashMap<String, Object> data) {
        data.put("InterviewTimeDate", interview.getTime().getDate());
        data.put("InterviewTimeTimeslot", interview.getTime().getTimeSlot());
    }
}
