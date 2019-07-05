package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.Interview;
import UsersAndJobObjects.Interviewer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class InterviewerLoader extends GenericLoader<Interviewer> {

    /**
     *  Loads the interviewer.
     * @param interviewer   The interviewer to be loaded.
     */
    void loadOne(Interviewer interviewer){
        FileSystem.mapPut(Interviewer.FILENAME, interviewer.getUsername(), this);
        HashMap data = FileSystem.read(Interviewer.FILENAME, interviewer.getUsername());
        this.loadPrelimData(interviewer, data);
        this.loadCompany(interviewer, data);
        this.loadInterviews(interviewer, data);
    }

    /**
     * Load the preliminary data for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadPrelimData(Interviewer interivewer, HashMap data) {
        interivewer.setPassword((String) data.get("password"));
        interivewer.setLegalName((String) data.get("legalName"));
        interivewer.setEmail((String) data.get("email"));
        interivewer.setPassword((String)data.get("password"));
        interivewer.setDateCreated(LocalDate.parse((String) data.get("dateCreated")));
        interivewer.setField((String) data.get("field"));
    }

    /**
     * Load the company for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadCompany(Interviewer interviewer, HashMap data) {
        interviewer.setCompany((Company) FileSystem.subLoader(Company.class, (String)
                        ((ArrayList) data.get("company")).get(0), (String) ((ArrayList) data.get("company")).get(1)));
    }

    /**
     * Load the interviews for this interviewer.
     *
     * @param data The data for this interviewer.
     */
    private void loadInterviews(Interviewer interviewer, HashMap data) {
        ArrayList<Interview> interviews = new ArrayList<>();
        for (Object x : (ArrayList) (data.get("interviews"))) {
            interviews.add((Interview) FileSystem.subLoader(Interview.class, (String) ((ArrayList) x).get(0), (String)
                    ((ArrayList) x).get(1)));
        }
        interviewer.setInterviews(interviews);
    }
}
