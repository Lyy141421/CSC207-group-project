package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.MethodsTheGUICallsInHR;
import CompanyStuff.JobPostings.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

abstract class HRPanel extends JPanel {
    Container parent;
    MethodsTheGUICallsInHR HRInterface;
    LocalDate today;

    //=====Constants=====
    static String HOME = "HOME";
    static String POSTING = "POSTING";
    static String APPLICATION = "APPLICATION";
    static String SEARCH = "SEARCH";
    static String ADD_POSTING = "ADDPOSTING";

    JButton homeButton;


    HRPanel(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        this.parent = contentPane;
        this.HRInterface = HRInterface;
        this.today = today;

        this.createHomeButton();
    }

    abstract void reload();

    private void createHomeButton() {
        this.homeButton = new JButton("Home");
        this.homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, HOME);
                //TODO: update todo on home panel
            }
        });
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
