package GUIClasses.HRInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

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

    private static final String OVERVIEW = "Overview";
    private static final String REJECT_LIST = "Rejected list";

    private HashMap<String, BranchJobPosting> unreviewedJP;
    private HashMap<String, BranchJobPosting> scheduleJP;
    private HashMap<String, BranchJobPosting> hiringJP;
    private HashMap<String, BranchJobPosting> filledJP;
    private HashMap<String, BranchJobPosting> importantJP = new HashMap<>();
    private HashMap<String, BranchJobPosting> allJP;

    private HashMap<String, BranchJobPosting> currJPs;

    private HRViewPosting containerPane = this;
    private JPanel parent;
    private boolean isHighPriority;
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JButton scheduleButton;
    private JList<String> jobPostingList = new JList<>();
    private BranchJobPosting selectedJP;


    HRViewPosting(HRBackend hrBackend, JPanel parent, boolean isHighPriority) {
        super(hrBackend);
        this.parent = parent;
        this.isHighPriority = isHighPriority;
        this.setJPLists();
        if (isHighPriority) {
            this.currJPs = importantJP;
        } else {
            this.currJPs = allJP;
        }

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setJobPostingList(splitDisplay);
        this.setInfoPane(splitDisplay);

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
                selectedJP = currJPs.get(selectedTitle);
                overview.setText(getStatus() + selectedJP.toString());
                if (scheduleJP.containsKey(selectedTitle)) {
                    scheduleButton.setEnabled(true);
                    scheduleButton.setVisible(true);
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
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(containerPane);
                new GroupInterviewDialog(frame, hrBackend, selectedJP, containerPane);
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

    private void setOverview() {
        this.overview = new JTextArea("Select a job posting to view information.");
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    }

    private JPanel createRejectListPanel() {
        JPanel rejectedPanel = new JPanel(new BorderLayout());
        ArrayList<Applicant> rejectList = this.hrBackend.getRejectedApplicantsForJobPosting(selectedJP);
        Object[][] data = new Object[rejectList.size()][];

        for (int i = 0; i < rejectList.size(); i++) {
            data[i] = rejectList.get(i).getCategoryValuesForRejectList();
        }
        JPanel panelTitle = new GUIElementsCreator().createLabelPanel("Rejection List", 20, true);
        rejectedPanel.add(panelTitle, BorderLayout.NORTH);
        JPanel rejectListPanel = new GUIElementsCreator().createTablePanel(Applicant.REJECT_LIST_CATEGORIES, data);
        rejectedPanel.add(rejectListPanel, BorderLayout.CENTER);
        return rejectedPanel;
    }

    private void setInfoPane(JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.setOverview();
        this.infoPane.addTab(OVERVIEW, this.overview);
        this.infoPane.addTab(REJECT_LIST, this.createRejectListPanel());
        splitDisplay.setRightComponent(this.infoPane);
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
        ArrayList<JobApplication> appsUnderSelectedJP = this.hrBackend.getApplicationsInConsiderationForJobPosting(selectedJP);
        int mode = 0;
        if (unreviewedJP.containsValue(selectedJP)) {
            mode = 1;
            removeFromJPLists(this.toJPTitle(selectedJP));
        } else if (hiringJP.containsValue(selectedJP)) {
            mode = 2;
            removeFromJPLists(this.toJPTitle(selectedJP));
        }
        HRViewApp appPanel = new HRViewApp(parent, hrBackend, getTitleToAppMap(appsUnderSelectedJP), this.getPreviousPanelKey(), mode);
        if (parent.getComponents().length > 7) {
            parent.remove(7);
        }
        parent.add(appPanel, HRPanel.APPLICATION, 7);
        ((CardLayout) parent.getLayout()).show(parent, HRPanel.APPLICATION);
    }

    private String getPreviousPanelKey() {
        if (isHighPriority) {
            return HRPanel.HIGH_PRIORITY_POSTINGS;
        } else {
            return HRPanel.BROWSE_POSTINGS;
        }
    }

    private void setJPLists() {
        this.unreviewedJP = this.getTitleToJPMap(hrBackend.getJPToReview());
        this.scheduleJP = this.getTitleToJPMap(hrBackend.getJPToSchedule());
        this.hiringJP = this.getTitleToJPMap(hrBackend.getJPToHire());
        this.filledJP = this.getTitleToJPMap(hrBackend.getAllFilledJP());
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

    private String getStatus() {
        String status;
        if (unreviewedJP.containsValue(selectedJP)) {
            status = "Important: Select applicants for the first round of interviews.\n\n";
        } else if (scheduleJP.containsValue(selectedJP)) {
            status = "Important: Schedule group interviews for the next round.\n\n";
        } else if (hiringJP.containsValue(selectedJP)) {
            status = "Important: Make hiring decisions for final candidates.\n\n";
        } else if (filledJP.containsValue(selectedJP)) {
            status = "Archived.";
        } else {
            status = "Low priority.\n\n";
        }
        return status;
    }
}
