package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.*;


import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

abstract class HRPanel extends JPanel {

    // === Constants ===
    static final String HIGH_PRIORITY_POSTINGS = "HIGH_PRIORITY_POSTINGS";
    static final String BROWSE_POSTINGS = "BROWSE_POSTINGS";
    static final String APPLICATION = "APPLICATION";
    static final String SEARCH_APPLICANT = "SEARCH_APPLICANT";
    static final String ADD_POSTING = "ADDPOSTING";

    // === Instance variables ===
    HRBackend hrBackend;

    HRPanel(HRBackend hrBackend) {
        this.hrBackend = hrBackend;
    }

    /**
     * Gets a hash map of titles to branch job postings from a list of job postings.
     * @param JPList a list of job postings.
     * @return the hash map of titles to branch job postings.
     */
    HashMap<String, BranchJobPosting> getTitleToJPMap(ArrayList<BranchJobPosting> JPList) {
        HashMap<String, BranchJobPosting> titleToJPMap = new HashMap<>();
        for (BranchJobPosting JP : JPList) {
            titleToJPMap.put(this.toJPTitle(JP), JP);
        }

        return titleToJPMap;
    }

    /**
     * Gets a string representation of the title of this branch job posting.
     * @param branchJobPosting a branch job posting.
     * @return the title to be displayed of this branch job posting.
     */
    String toJPTitle(BranchJobPosting branchJobPosting) {
        return branchJobPosting.getId() + "-" + branchJobPosting.getTitle();
    }

    HashMap<String, JobApplication> getTitleToAppMap(ArrayList<JobApplication> appList) {
        HashMap<String, JobApplication> titleToAppMap = new HashMap<>();
        for (JobApplication app : appList) {
            titleToAppMap.put(this.toAppTitles(app), app);
        }

        return titleToAppMap;
    }

    String toAppTitles(JobApplication jobApplication) {
        return jobApplication.getId() + "-" + jobApplication.getApplicant().getLegalName();
    }
}
