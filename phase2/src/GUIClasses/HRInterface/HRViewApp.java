package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.DocumentViewer;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

class HRViewApp extends HRPanel {

    private static final String OVERVIEW = "Overview";
    private static final String FILE = "View Files";
    private static final String INTERVIEW_NOTES = "Interview Notes";

    HashMap<String, JobApplication> currApps;

    private JPanel parent;
    private JList<String> applicationList = new JList<>();
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JPanel documentViewer;
    private JPanel interviewNotesPanel;
    private JButton hireButton;
    private JButton selectButton;
    private JobApplication jobAppSelected;

    private JButton returnButton;
    private String previousPanel;


    //mode: 0 view only
    //      1 review
    //      2 hiring
    HRViewApp(JPanel parent, HRBackend hrBackend, HashMap<String, JobApplication> currApps, String previousPanel, int mode) {
        super(hrBackend);
        assert SwingUtilities.isEventDispatchThread();
        this.parent = parent;
        this.currApps = currApps;
        this.previousPanel = previousPanel;
        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);

        this.setApplicationList(splitDisplay);
        this.setInfoPane(splitDisplay);
        this.setListSelectionListener();
        this.add(splitDisplay);
        this.setButtons();
        this.setMode(mode);
    }

    void reload () {
        //TODO: check if applications are from list still in consideration.
        this.currApps = this.getAppsInConsideration();
        this.applicationList.removeAll();
        this.applicationList.setListData(this.currApps.keySet().toArray(new String[this.currApps.size()]));
        revalidate();
        repaint();
    }

    private HashMap<String, JobApplication> getAppsInConsideration() {
        BranchJobPosting branchJP = ((JobApplication)this.currApps.values().toArray()[0]).getJobPosting();
        ArrayList<JobApplication> applications = branchJP.getInterviewManager().getApplicationsInConsideration();
        return getTitleToAppMap(applications);
    }

    private void setButtons() {
        JPanel buttons = new JPanel(new FlowLayout());
        this.createHireButton();
        buttons.add(this.hireButton);
        this.createSelectButton();
        buttons.add(this.selectButton);
        this.createReturnButton();
        buttons.add(this.returnButton);


        this.add(buttons, BorderLayout.SOUTH);
    }

    private void setApplicationList (JSplitPane splitDisplay) {
        this.applicationList.setListData(this.currApps.keySet().toArray(new String[this.currApps.size()]));
        this.applicationList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.applicationList.setLayoutOrientation(JList.VERTICAL);

        splitDisplay.setLeftComponent(new JScrollPane(this.applicationList));
    }

    private void setListSelectionListener() {
        this.applicationList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (applicationList.isSelectionEmpty()) {
                    overview.setText("Select an application to view overview.");
                } else {
                    String selectedTitle = applicationList.getSelectedValue();
                    jobAppSelected = currApps.get(selectedTitle);
                    overview.setText(jobAppSelected.toString());
                    documentViewer.removeAll();
                    documentViewer.add(createDocumentViewer(jobAppSelected), new GridBagConstraints());
                    interviewNotesPanel.removeAll();
                    setInterviewNotesPanel();
                }
            }
        });
    }

    private void setInfoPane (JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(OVERVIEW, makeOverviewTab("Select an application to view overview."));
        this.documentViewer = new JPanel();
        this.documentViewer.setLayout(new GridBagLayout());
        this.infoPane.addTab(FILE, this.documentViewer);
        this.interviewNotesPanel = new JPanel();
        this.infoPane.addTab(INTERVIEW_NOTES, this.interviewNotesPanel);

        splitDisplay.setRightComponent(this.infoPane);
    }

    private JPanel createDocumentViewer(JobApplication selectedApp) {
        File folderForApp = this.hrBackend.getFolderForJobApplication(selectedApp);
        DocumentViewer documentViewer = new DocumentViewer(folderForApp);

        return documentViewer;
    }

    private JComponent makeOverviewTab (String text) {
        this.overview = new JTextArea(text);
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return new JScrollPane(this.overview);
    }

    private void setInterviewNotesPanel() {
        HashMap<String, HashMap<Interviewer, String>> roundToInterviewerToNotes = this.hrBackend.
                getAllInterviewNotesForApplication(jobAppSelected);
        this.interviewNotesPanel.setLayout(new BoxLayout(this.interviewNotesPanel, BoxLayout.Y_AXIS));
        for (String round : roundToInterviewerToNotes.keySet()) {
            JLabel interviewRoundTitle = new JLabel(round);
            interviewRoundTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            this.interviewNotesPanel.add(interviewRoundTitle);
            for (Interviewer interviewer : roundToInterviewerToNotes.get(round).keySet()) {
                if (roundToInterviewerToNotes.get(round).get(interviewer) != null) {
                    this.setInterviewNotesForOneInterview(interviewer, roundToInterviewerToNotes.get(round).get(interviewer));
                }
            }
            this.interviewNotesPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        }
        interviewNotesPanel.revalidate();
    }

    private void setInterviewNotesForOneInterview(Interviewer interviewer, String notes) {
        JLabel interviewerName = new JLabel(interviewer.getLegalName());
        JScrollPane notesTextArea = new GUIElementsCreator().createTextAreaWithScrollBar(notes, false);
        notesTextArea.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        this.interviewNotesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        this.interviewNotesPanel.add(interviewerName);
        this.interviewNotesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.interviewNotesPanel.add(notesTextArea);
        this.interviewNotesPanel.add(Box.createRigidArea(new Dimension(0, 25)));
    }

    private void setMode(int mode) {
        switch (mode) {
            case 0:
                this.returnButton.setVisible(true);
                break;
            case 1:
                this.selectButton.setVisible(true);;
                break;
            case 2:
                this.hireButton.setVisible(true);
                break;
        }
    }

    private void createReturnButton() {
        this.returnButton = new JButton("Return");
        this.returnButton.setVisible(false);
        this.returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, previousPanel);
            }
        });
    }

    private void createHireButton() {
        this.hireButton = new JButton("Select candidates to hire");
        this.hireButton.setVisible(false);
        this.hireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(parent);
                JDialog hiringDialog = new HiringSelectionDialog(frame, hrBackend, new ArrayList<>(currApps.values()), returnButton);
                hiringDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        hireButton.setVisible(false);
                        Thread newThread = new Thread() {
                            public void run() {
                                try {
                                    SwingUtilities.invokeAndWait(new Runnable() {
                                        @Override
                                        public void run() {
                                            reload();
                                        }
                                    });
                                } catch (InterruptedException | InvocationTargetException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };
                        newThread.start();
                    }
                });
            }
        });
    }

    private void createSelectButton() {
        this.selectButton = new JButton("Select candidates to interview");
        this.selectButton.setVisible(false);
        this.selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(parent);
                JDialog filterDialog = new GradingFilterDialog(frame, hrBackend, new ArrayList<>(currApps.values()), returnButton);
                filterDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        selectButton.setVisible(false);
                        Thread newThread = new Thread() {
                            public void run() {
                                try {
                                    SwingUtilities.invokeAndWait(new Runnable() {
                                        @Override
                                        public void run() {
                                            reload();
                                        }
                                    });
                                } catch (InterruptedException | InvocationTargetException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };
                        newThread.start();
                    }
                });
            }
        });
    }
}
