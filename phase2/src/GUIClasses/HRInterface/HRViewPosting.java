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
    static int HIGH_PRIORITY = 0;
    static int ALL = 1;
    static int UPDATABLE = 2;
    HRMain main;
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
        assert SwingUtilities.isEventDispatchThread();
        this.parent = parent;
        this.main = (HRMain) parent.getParent();
        this.jpType = jpType;
        if (jpType == HIGH_PRIORITY) {
            this.currJPs = main.getImportantJP();
        } else if (jpType == ALL) {
            this.currJPs = main.getAllJP();
        } else {
            this.currJPs = main.getUpdatableJPs();
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

    private void reload() {
        this.jobPostingList.removeAll();
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
        revalidate();
        repaint();
    }

    private void setListSelectionListener() {
        this.jobPostingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jobPostingList.isSelectionEmpty()) {
                    overview.setText("Select a job posting to view information.");
                    scheduleButton.setVisible(false);
                } else {
                    String selectedTitle = jobPostingList.getSelectedValue();
                    selectedJP = currJPs.get(selectedTitle);
                    overview.setText(getStatus() + selectedJP.toString());
                    scheduleButton.setVisible(main.getScheduleJP().containsKey(selectedTitle));
                }
                setRejectListPanel();
                rejectedPanel.revalidate();
                rejectedPanel.repaint();
            }
        });
    }

    private void createScheduleButton() {
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
    }

    private void createUpdateButton() {
        this.updateButton = new JButton("Update job posting");
        this.updateButton.setVisible(jpType == UPDATABLE);
        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTitle = jobPostingList.getSelectedValue();
                BranchJobPosting selectedJP = currJPs.get(selectedTitle);
                HRAddOrUpdatePostingForm updateForm = new HRAddOrUpdatePostingForm(hrBackend, false, selectedJP);
                if (parent.getComponents().length > HRMain.NUM_CARDS) {
                    parent.remove(HRMain.NUM_CARDS);
                }
                parent.add(updateForm, HRPanel.UPDATE_POSTING_FORM, 7);
                ((CardLayout) parent.getLayout()).show(parent, HRPanel.UPDATE_POSTING_FORM);
            }
        });
    }

    private void setJobPostingList(JSplitPane splitDisplay) {
        this.jobPostingList.setListData(currJPs.keySet().toArray(new String[currJPs.size()]));
        this.jobPostingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        System.out.println(rejectList);
        Object[][] data = new Object[rejectList.size()][];

        for (int i = 0; i < rejectList.size(); i++) {
            data[i] = rejectList.get(i).getCategoryValuesForRejectList();
        }
        JPanel panelTitle = new GUIElementsCreator().createLabelPanel("Rejection List", 20, true);
        rejectedPanel.add(panelTitle, BorderLayout.NORTH);
        JPanel rejectListPanel = new GUIElementsCreator().createTablePanel(Applicant.REJECT_LIST_CATEGORIES, data);
        rejectedPanel.add(rejectListPanel, BorderLayout.CENTER);
        rejectedPanel.repaint();
        rejectedPanel.revalidate();
        this.infoPane.removeTabAt(1);
        this.infoPane.addTab(REJECT_LIST, rejectedPanel);
    }

    private void setInfoPane(JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(OVERVIEW, this.createOverviewScroll());
        this.infoPane.addTab(REJECT_LIST, rejectedPanel);
        splitDisplay.setRightComponent(this.infoPane);
    }


    private JButton createViewAppButton() {
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
        int mode = HRViewApp.VIEW_ONLY;
        if (main.getUnreviewedJP().containsValue(selectedJP)) {
            mode = HRViewApp.REVIEW;
            main.removeFromJPLists(selectedJP);
        } else if (main.getHiringJP().containsValue(selectedJP)) {
            mode = HRViewApp.HIRING;
            main.removeFromJPLists(selectedJP);
        }
        HRViewApp appPanel = new HRViewApp(parent, hrBackend, getTitleToAppMap(appsUnderSelectedJP), this.getPreviousPanelKey(), mode);
        if (parent.getComponents().length > 7) {
            parent.remove(7);
        }
        parent.add(appPanel, HRPanel.APPLICATION, 7);
        ((CardLayout) parent.getLayout()).show(parent, HRPanel.APPLICATION);
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

    private String getStatus() {
        String status;
        if (main.getUnreviewedJP().containsValue(selectedJP)) {
            status = "Important: Select applicants for the first round of interviews.\n\n";
        } else if (main.getScheduleJP().containsValue(selectedJP)) {
            status = "Important: Schedule group interviews for the next round.\n\n";
        } else if (main.getHiringJP().containsValue(selectedJP)) {
            status = "Important: Make hiring decisions for final candidates.\n\n";
        } else if (main.getArchivedJP().containsValue(selectedJP)) {
            status = "Archived.\n\n";
        } else {
            status = "Low priority.\n\n";
        }
        return status;
    }
}
