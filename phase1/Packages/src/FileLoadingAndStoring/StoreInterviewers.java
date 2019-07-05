package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreInterviewers {

    /**
     * Store all the interviewers in this list.
     * @param interviewers  The list of interviewers to be stored.
     */
    public void storeAll(ArrayList<Interviewer> interviewers) {
        for (Interviewer interviewer : interviewers) {
            this.storeOne(interviewer);
        }
    }

    /**
     * Store the interviewer.
     */
    private void storeOne(Interviewer interviewer){
        FileSystem.mapPut(Interviewer.FILENAME, interviewer.getUsername(), interviewer);
        HashMap<String, Object> data = new HashMap<>();
        this.storePrelimInfo(interviewer, data);
        this.storeInterviews(interviewer, data);
        FileSystem.write(Interviewer.FILENAME, interviewer.getUsername(), data);
    }

    private void storePrelimInfo(Interviewer interviewer, HashMap<String, Object> data) {
        data.put("password", interviewer.getPassword());
        data.put("legalName", interviewer.getLegalName());
        data.put("email", interviewer.getEmail());
        data.put("dateCreated", interviewer.getDateCreated());
        data.put("field", interviewer.getField());
        data.put("company", new String[]{Company.FILENAME,
                interviewer.getCompany().getName()});
    }

    private void storeInterviews(Interviewer interviewer, HashMap<String, Object> data) {
        ArrayList<ArrayList> interview_list = new ArrayList<>();
        for(Interview x : interviewer.getInterviews()){
            interview_list.add(new ArrayList<Object>() {{
                add(Interview.FILENAME);
                add(String.valueOf(x.getId()));
            }});
        }
        data.put("interviews", interview_list);
    }
}
