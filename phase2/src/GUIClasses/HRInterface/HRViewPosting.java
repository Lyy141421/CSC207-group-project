package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class HRViewPosting extends HRPanel{
    JPanel containerPane = this;

    private HashMap<String, BranchJobPosting> prePhoneJP;
    private HashMap<String, BranchJobPosting> scheduleJP;
    private HashMap<String, BranchJobPosting> hiringJP;
    private HashMap<String, BranchJobPosting> importantJP = new HashMap<>();
    private HashMap<String, BranchJobPosting> allJP;

    HashMap<String, BranchJobPosting> currJPs;

    private JTextArea info;
    private JButton scheduleButton;
    private JList<String> jobPostingList = new JList<>();


    HRViewPosting(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);
        this.setJPLists();

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setJobPostingList(splitDisplay);
        this.setInfoBox(splitDisplay);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(this.createScheduleButton());
        buttons.add(this.createViewAppButton());
        buttons.add(this.homeButton);
        //TODO: when page first loaded, first item is automatically displayed, but not SELECTED for itemListener to pick up.

        this.add(splitDisplay, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);

        this.setListSelectionListener();
    }

    void reload() {
        this.jobPostingList.removeAll();
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
    }

    public HashMap<String, BranchJobPosting> getImportantJP() {
        return this.importantJP;
    }

    private void setListSelectionListener() {
        this.jobPostingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = currJPs.get(selectedTitle);
                info.setText(getStatus(selectedTitle) + selectedJP.toString());
                if (scheduleJP.containsKey(selectedTitle)) {
                    scheduleButton.setEnabled(false);
                } else {
                    scheduleButton.setEnabled(true);
                }
            }
        });
    }

    private JButton createScheduleButton () {
        this.scheduleButton = new JButton("Schedule");
        this.scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = currJPs.get(selectedTitle);
                boolean success = HRInterface.setUpInterviews(selectedJP);
                if (success) {
                    removeFromJPLists(selectedTitle);
                    JOptionPane.showMessageDialog(containerPane, "Interviews have been scheduled.");
                } else {
                    JOptionPane.showMessageDialog(containerPane, "Interviews cannot be scheduled at this time.");
                }
            }
        });

        return scheduleButton;
    }

    private void setJobPostingList (JSplitPane splitDisplay) {
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
        this.jobPostingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobPostingList.setLayoutOrientation(JList.VERTICAL);
        splitDisplay.setLeftComponent(new JScrollPane(this.jobPostingList));
    }

    private void setInfoBox (JSplitPane splitDisplay) {
        this.info = new JTextArea("Select a job posting to view information.");
        this.info.setEditable(false);
        this.info.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        splitDisplay.setRightComponent(new JScrollPane(this.info));
    }

    private JButton createViewAppButton () {
        JButton viewAppsButton = new JButton("View applications");
        viewAppsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BranchJobPosting selectedJP = currJPs.get(jobPostingList.getSelectedValue());
                ArrayList<JobApplication> appsUnderSelectedJP = selectedJP.getJobApplications();
                HRViewApp appPanel = new HRViewApp(parent, HRInterface, today, getTitleToAppMap(appsUnderSelectedJP));
                parent.remove(4);
                parent.add(appPanel, APPLICATION);
                ((CardLayout) parent.getLayout()).show(parent, APPLICATION);
            }
        });

        return viewAppsButton;
    }

    private void setJPLists() {
        this.prePhoneJP = this.getTitleToJPMap(HRInterface.getJPToReview(today));
        this.scheduleJP = this.getTitleToJPMap(HRInterface.getJPToSchedule(today));
        this.hiringJP = this.getTitleToJPMap(HRInterface.getJPToHire(today));
        this.allJP = this.getTitleToJPMap(HRInterface.getAllJP());

        this.importantJP.putAll(this.prePhoneJP);
        this.importantJP.putAll(this.scheduleJP);
        this.importantJP.putAll(this.hiringJP);
    }

    private void removeFromJPLists(String title) {
        this.prePhoneJP.remove(title);
        this.scheduleJP.remove(title);
        this.hiringJP.remove(title);
        this.importantJP.remove(title);
    }

    private String getStatus(String selectedJPTitle) {
        String status;
        if (prePhoneJP.containsKey(selectedJPTitle)) {
            status = "Important: Select applicants for phone interview.\n\n";
        } else if (scheduleJP.containsKey(selectedJPTitle)) {
            status = "Important: Schedule interviews for the next round.\n\n";
        } else if (hiringJP.containsKey(selectedJPTitle)) {
            status = "Important: Make hiring decisions for final candidates.\n\n";
        } else {
            status = "Low priority.\n\n";
        }

        return status;
    }
}
