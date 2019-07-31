package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.CommonUserGUI.DocumentViewer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class HRViewApp extends HRPanel {

    private String OVERVIEW = "Overview";
    private String FILE = "View Files";

    HashMap<String, JobApplication> currApps;

    JPanel parent;
    private JList<String> applicationList = new JList<>();
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JPanel documentViewer;
    private JButton hireButton;
    private JButton selectButton;

    JButton returnButton;
    String previousPanel;


    //mode: 0 view only
    //      1 review
    //      2 hiring
    HRViewApp(JPanel parent, HRBackEnd hrBackEnd, HashMap<String, JobApplication> currApps, String previousPanel, int mode) {
        super(hrBackEnd);
        this.parent = parent;
        this.currApps = currApps;
        this.previousPanel = previousPanel;

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);

        this.setApplicationList(splitDisplay);
        this.setInfoPane(splitDisplay);
        this.setButtons(mode);

        this.setListSelectionListener();
    }

    void reload () {
        this.applicationList.removeAll();
        this.applicationList.setListData(this.currApps.keySet().toArray(new String[this.currApps.size()]));
    }

    private void setButtons(int mode) {
        JPanel buttons = new JPanel(new FlowLayout());
        this.createHireButton();
        buttons.add(this.hireButton);
        this.createSelectButton();
        buttons.add(this.selectButton);
        this.createReturnButton();
        buttons.add(this.returnButton);

        switch (mode) {
            case 0:
                this.setViewOnlyMode();
                break;
            case 1:
                this.setSelectMode();
                break;
            case 2:
                this.setHireMode();
                break;
        }
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
                String selectedTitle = applicationList.getSelectedValue();
                JobApplication selectedApp = currApps.get(selectedTitle);
                overview.setText(selectedApp.toString());
                documentViewer.removeAll();
                documentViewer.add(createDocumentViewer(selectedApp));
            }
        });
    }

    private void setInfoPane (JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(this.OVERVIEW, makeOverviewTab("Select an application to view overview."));
        this.documentViewer = new JPanel();
        this.infoPane.addTab(this.FILE, this.documentViewer);

        splitDisplay.setRightComponent(this.infoPane);
    }

    private JPanel createDocumentViewer(JobApplication selectedApp) {
        File folderForApp = this.hrBackEnd.getFolderForJobApplication(selectedApp);
        DocumentViewer documentViewer = new DocumentViewer(folderForApp);

        return documentViewer;
    }

    private JComponent makeOverviewTab (String text) {
        this.overview = new JTextArea(text);
        this.overview.setEditable(false);
        this.overview.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return new JScrollPane(this.overview);
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

    private void setViewOnlyMode() {
        this.returnButton.setVisible(true);
    }

    private void createHireButton() {
        this.hireButton = new JButton("Select candidates to hire");
        this.hireButton.setVisible(false);
        this.hireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame popUp = new HiringSelectionFrame(hrBackEnd, new ArrayList<>(currApps.values()), returnButton);
            }
        });
    }

    void setHireMode() {
        this.hireButton.setVisible(true);
    }

    private void createSelectButton() {
        this.selectButton = new JButton("Select candidates to interview");
        this.selectButton.setVisible(false);
        this.selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame popUp = new GradingFilterFrame(hrBackEnd, new ArrayList<>(currApps.values()), returnButton);
            }
        });
    }

    void setSelectMode() {
        this.selectButton.setVisible(true);
    }
}
