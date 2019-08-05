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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

class HRViewPosting extends HRPanel {

    private static final String OVERVIEW = "Overview";
    private static final String REJECT_LIST = "Rejected list";
    public static int HIGH_PRIORITY = 0;
    public static int ALL = 1;
    public static int UPDATABLE = 2;

    private HashMap<String, BranchJobPosting> unreviewedJP;
    private HashMap<String, BranchJobPosting> scheduleJP;
    private HashMap<String, BranchJobPosting> hiringJP;
    private HashMap<String, BranchJobPosting> archivedJP;
    private HashMap<String, BranchJobPosting> importantJP = new HashMap<>();
    private HashMap<String, BranchJobPosting> updatableJPs;
    private HashMap<String, BranchJobPosting> allJP;

    private HashMap<String, BranchJobPosting> currJPs;

    private HRViewPosting containerPane = this;
    private JPanel parent;
    private int jpType;
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JButton scheduleButton;
    private JButton updateButton;
    private JList<String> jobPostingList = new JList<>();
    private JPanel rejectedPanel = new JPanel();
    private BranchJobPosting selectedJP;


    HRViewPosting(HRBackend hrBackend, JPanel parent, int jpType) {
        super(hrBackend);
        this.parent = parent;
        this.jpType = jpType;
        this.setJPLists();
        if (jpType == HIGH_PRIORITY) {
            this.currJPs = importantJP;
        } else if (jpType == ALL) {
            this.currJPs = allJP;
        } else {
            this.currJPs = updatableJPs;
        }

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);
        this.setJobPostingList(splitDisplay);
        this.setInfoPane(splitDisplay);

        JPanel buttons = new JPanel(new FlowLayout());
        this.createScheduleButton();
        buttons.add(this.scheduleButton);
        this.createUpdateButton();
        buttons.add(this.updateButton);
        buttons.add(this.createViewAppButton());

        this.add(splitDisplay, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);

        this.setListSelectionListener();
    }

    void reload() {
        this.jobPostingList.removeAll();
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
        revalidate();
        repaint();
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
                if (jobPostingList.isSelectionEmpty()) {
                    overview.setText("Select a job posting to view information.");
                    setRejectListPanel();
                    scheduleButton.setVisible(false);
                } else {
                    String selectedTitle = jobPostingList.getSelectedValue();
                    selectedJP = currJPs.get(selectedTitle);
                    overview.setText(getStatus() + selectedJP.toString());
                    setRejectListPanel();
                    scheduleButton.setVisible(scheduleJP.containsKey(selectedTitle));
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
                JDialog groupDialog = new GroupInterviewDialog(frame, hrBackend, selectedJP, containerPane);
                groupDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        reload();
                    }
                });
            }
        });

        return scheduleButton;
    }

    private void createUpdateButton() {
        this.updateButton = new JButton("Update job posting");
        this.updateButton.setVisible(jpType == UPDATABLE);
        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = updatableJPs.get(selectedTitle);
                HRAddOrUpdatePostingForm updateForm = new HRAddOrUpdatePostingForm(hrBackend, false, selectedJP);
                if (parent.getComponents().length > HRMain.NUM_CARDS) {
                    parent.remove(HRMain.NUM_CARDS);
                }
                parent.add(updateForm, HRPanel.UPDATE_POSTING_FORM, 7);
                ((CardLayout) parent.getLayout()).show(parent, HRPanel.UPDATE_POSTING_FORM);
            }
        });
    }

    private void setJobPostingList (JSplitPane splitDisplay) {
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
        this.jobPostingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobPostingList.setLayoutOrientation(JList.VERTICAL);
        splitDisplay.setLeftComponent(new JScrollPane(this.jobPostingList));
    }

    private JScrollPane createOverviewScroll() {
        JScrollPane overviewScroll = new GUIElementsCreator().createTextAreaWithScrollBar("Select a job posting to view information.", false);
        this.overview = (JTextArea) overviewScroll.getViewport().getView();
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return overviewScroll;
    }

    private void setRejectListPanel() {
        rejectedPanel = new JPanel(new BorderLayout());
        ArrayList<Applicant> rejectList = this.hrBackend.getRejectedApplicantsForJobPosting(selectedJP);
        Object[][] data = new Object[rejectList.size()][];

        for (int i = 0; i < rejectList.size(); i++) {
            data[i] = rejectList.get(i).getCategoryValuesForRejectList();
        }
        JPanel panelTitle = new GUIElementsCreator().createLabelPanel("Rejection List", 20, true);
        rejectedPanel.add(panelTitle, BorderLayout.NORTH);
        JPanel rejectListPanel = new GUIElementsCreator().createTablePanel(Applicant.REJECT_LIST_CATEGORIES, data);
        rejectedPanel.add(rejectListPanel, BorderLayout.CENTER);
    }

    private void setInfoPane(JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(OVERVIEW, this.createOverviewScroll());
        this.infoPane.addTab(REJECT_LIST, rejectedPanel);
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
        //TODO: new Group interview doesn't show up until switch panel.
        // if (hrBackend.needsGroupInterview(selectedJP)
        reload();
    }

    private String getPreviousPanelKey() {
        if (jpType == HIGH_PRIORITY) {
            return HRPanel.HIGH_PRIORITY_POSTINGS;
        } else if (jpType == ALL) {
            return HRPanel.BROWSE_POSTINGS;
        } else {
            return HRPanel.UPDATE_POSTING;
        }
    }

    private void setJPLists() {
        this.unreviewedJP = this.getTitleToJPMap(hrBackend.getJPToReview());
        this.scheduleJP = this.getTitleToJPMap(hrBackend.getJPToSchedule());
        this.hiringJP = this.getTitleToJPMap(hrBackend.getJPToHire());
        this.archivedJP = this.getTitleToJPMap(hrBackend.getAllFilledJP());
        this.updatableJPs = this.getTitleToJPMap(hrBackend.getJPThatCanBeUpdated());
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
        } else if (archivedJP.containsValue(selectedJP)) {
            status = "Archived.\n\n";
        } else {
            status = "Low priority.\n\n";
        }
        return status;
    }
}
