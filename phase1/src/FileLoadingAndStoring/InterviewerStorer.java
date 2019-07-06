package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;

import java.util.ArrayList;
import java.util.HashMap;

public class InterviewerStorer extends GenericStorer<Interviewer> {

    /**
     * Store the interviewer.
     * @param interviewer   The interviewer to be stored.
     */
    void storeOne(Interviewer interviewer){
        LoaderManager.mapPut(Interviewer.FILENAME, interviewer.getUsername(), interviewer);
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
        StorerManager.subStore(interviewer.getCompany());
    }

    private void storeInterviews(Interviewer interviewer, HashMap<String, Object> data) {
        ArrayList<ArrayList> interview_list = new ArrayList<>();
        for(Interview x : interviewer.getInterviews()){
            interview_list.add(new ArrayList<Object>() {{
                add(Interview.FILENAME);
                add(String.valueOf(x.getId()));
                StorerManager.subStore(x);
            }});
        }
        data.put("interviews", interview_list);
    }
}
