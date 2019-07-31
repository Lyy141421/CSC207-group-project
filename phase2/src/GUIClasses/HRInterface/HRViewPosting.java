package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

class HRViewPosting extends HRPanel {

    private HashMap<String, BranchJobPosting> unreviewedJP;
    private HashMap<String, BranchJobPosting> scheduleJP;
    private HashMap<String, BranchJobPosting> hiringJP;
    private HashMap<String, BranchJobPosting> importantJP = new HashMap<>();
    private HashMap<String, BranchJobPosting> allJP;

    private HashMap<String, BranchJobPosting> currJPs;

    private HRViewPosting containerPane = this;
    private JPanel parent;
    private boolean isTodo;
    private JTextArea info;
    private JButton scheduleButton;
    private JList<String> jobPostingList = new JList<>();


    HRViewPosting(HRBackend hrBackend, JPanel parent, boolean isTodo) {
        super(hrBackend);
        this.parent = parent;
        this.isTodo = isTodo;
        this.setJPLists();
        if (isTodo) {
            this.currJPs = importantJP;
        } else {
            this.currJPs = allJP;
        }

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setJobPostingList(splitDisplay);
        this.setInfoBox(splitDisplay);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(this.createScheduleButton());
        buttons.add(this.createViewAppButton());

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

    public HashMap<String, BranchJobPosting> getAllJP() {
        return this.allJP;
    }

    public void setCurrJPs(HashMap<String, BranchJobPosting> currJPs) {
        this.currJPs = currJPs;
    }

    private void setListSelectionListener() {
        this.jobPostingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = currJPs.get(selectedTitle);
                info.setText(getStatus(selectedTitle) + selectedJP.toString());
                if (scheduleJP.containsKey(selectedTitle)) {
                    scheduleButton.setEnabled(true);
                } else {
                    scheduleButton.setEnabled(false);
                }
            }
        });
    }

    private JButton createScheduleButton () {
        this.scheduleButton = new JButton("Schedule group interview");
        this.scheduleButton.setVisible(false);
        this.scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = currJPs.get(selectedTitle);
                JInternalFrame popUp = new GroupInterviewFrame(hrBackend, selectedJP, containerPane);
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
                if (jobPostingList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(containerPane, "Please select a job posting!");
                } else {
                    setApplicationPanel();
                }
            }
        });

        return viewAppsButton;
    }

    private void setApplicationPanel() {
        String selectedTitle = jobPostingList.getSelectedValue();
        BranchJobPosting selectedJP = currJPs.get(selectedTitle);
        ArrayList<JobApplication> appsUnderSelectedJP = selectedJP.getJobApplications();
        int mode = 0;
        if (unreviewedJP.containsKey(selectedTitle)) {
            mode = 1;
            removeFromJPLists(selectedTitle);
        } else if (hiringJP.containsKey(selectedTitle)) {
            mode = 2;
            removeFromJPLists(selectedTitle);
        }
        HRViewApp appPanel = new HRViewApp(parent, hrBackend, getTitleToAppMap(appsUnderSelectedJP), this.getPreviousPanelKey(), mode);
        parent.remove(4);
        parent.add(appPanel, APPLICATION);
        ((CardLayout) parent.getLayout()).show(parent, APPLICATION);
    }

    private String getPreviousPanelKey() {
        if (isTodo) {
            return HRPanel.TODO_POSTINGS;
        } else {
            return HRPanel.BROWSE_POSTINGS;
        }
    }

    private void setJPLists() {
        this.unreviewedJP = this.getTitleToJPMap(hrBackend.getJPToReview());
        this.scheduleJP = this.getTitleToJPMap(hrBackend.getJPToSchedule());
        this.hiringJP = this.getTitleToJPMap(hrBackend.getJPToHire());
        this.allJP = this.getTitleToJPMap(hrBackend.getAllJP());

        this.importantJP.putAll(this.unreviewedJP);
        this.importantJP.putAll(this.scheduleJP);
        this.importantJP.putAll(this.hiringJP);
    }

    void removeFromJPLists(String title) {
        this.unreviewedJP.remove(title);
        this.scheduleJP.remove(title);
        this.hiringJP.remove(title);
        this.importantJP.remove(title);
    }

    private String getStatus(String selectedJPTitle) {
        String status;
        if (unreviewedJP.containsKey(selectedJPTitle)) {
            status = "Important: Select applicants for the first round of interviews.\n\n";
        } else if (scheduleJP.containsKey(selectedJPTitle)) {
            status = "Important: Schedule group interviews for the next round.\n\n";
        } else if (hiringJP.containsKey(selectedJPTitle)) {
            status = "Important: Make hiring decisions for final candidates.\n\n";
        } else {
            status = "Low priority.\n\n";
        }

        return status;
    }
}
