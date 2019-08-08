package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

abstract class HRPanel extends JPanel {

    // === Constants ===
    static final String HIGH_PRIORITY_POSTINGS = "HIGH_PRIORITY_POSTINGS";
    static final String BROWSE_POSTINGS = "BROWSE_POSTINGS";
    static final String APPLICATION = "APPLICATION";
    static final String SEARCH_APPLICANT = "SEARCH_APPLICANT";
    static final String ADD_POSTING = "ADD_POSTING";
    static final String UPDATE_POSTING = "UPDATE_POSTING";
    static final String UPDATE_POSTING_FORM = "UPDATE_POSTING_FORM";

    // === Instance variables ===
    HRBackend hrBackend;

    HRPanel(HRBackend hrBackend) {
        this.hrBackend = hrBackend;
    }

    HashMap<String, JobApplication> getTitleToAppMap(ArrayList<JobApplication> appList) {
        HashMap<String, JobApplication> titleToAppMap = new HashMap<>();
        for (JobApplication app : appList) {
            titleToAppMap.put(this.toAppTitles(app), app);
        }

        return titleToAppMap;
    }

    private String toAppTitles(JobApplication jobApplication) {
        return jobApplication.getId() + "-" + jobApplication.getApplicant().getLegalName();
    }
}
