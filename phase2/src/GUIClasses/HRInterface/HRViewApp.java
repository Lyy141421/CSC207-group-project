package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class HRViewApp extends HRPanel {

    private String OVERVIEW = "Overview";
    private String CV = "CV";
    private String COVER_LETTER = "Cover letter";

    private JList<String> applicationList = new JList<>();
    private JTabbedPane infoPane;
    private JTextArea overview;
    private JTextArea cv;
    private JTextArea coverLetter;


    HRViewApp(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new BorderLayout());

        JSplitPane splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(250);

        this.setApplicationList(splitDisplay);
        this.setInfoPane(splitDisplay);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(this.createHireButton());
        buttons.add(this.createSelectButton());
        buttons.add(this.homeButton);

        this.setListSelectionListener();
    }

    void reload () {
        this.applicationList.removeAll();
        this.applicationList.setListData(currApps.keySet().toArray(new String[currApps.size()]));
        //TODO: change buttons based on job posting selected
    }

    private void setApplicationList (JSplitPane splitDisplay) {
        this.applicationList.setListData(currApps.keySet().toArray(new String[currApps.size()]));
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
                overview.setText(getInfo(selectedApp, OVERVIEW));
                cv.setText(getInfo(selectedApp, CV));
                coverLetter.setText(getInfo(selectedApp, COVER_LETTER));
            }
        });
    }

    private void setInfoPane (JSplitPane splitDisplay) {
        this.infoPane = new JTabbedPane();
        this.infoPane.addTab(this.OVERVIEW, makeInfoTab("Select an application to view Overview.", this.overview));
        this.infoPane.addTab(this.CV, makeInfoTab("Select an application to view CV.", this.cv));
        this.infoPane.addTab(this.COVER_LETTER, makeInfoTab("Select an application to view cover letter", this.coverLetter));

        splitDisplay.setRightComponent(this.infoPane);
    }

    private JComponent makeInfoTab (String text, JTextArea info) {
        info = new JTextArea(text);
        info.setEditable(false);
        info.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return new JScrollPane(info);
    }

    private String getInfo(JobApplication app, String attributeName) {
        String info;

        switch (attributeName) {
            case OVERVIEW:
                info = app.toString();
                break;
                //TODO: replace with documentViewer panel.
            case CV:
                info = app.getCV().getContents();
                break;
            case COVER_LETTER:
                info = app.getCoverLetter().getContents();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeName);
        }

        return info;
    }

    private JButton createHireButton() {
        JButton hire = new JButton("Select candidates to hire");
        hire.setVisible(false);
        //TODO: actionlistener.
        return hire;
    }

    private JButton createSelectButton() {
        JButton select = new JButton("Select candidates to consider");
        select.setVisible(false);
        //TODO: actionlistener.
        return select;
    }

    /*
    private boolean isPhoneSetup() {
        boolean isSetup = true;
        for (JobApplication jobApps : currApps) {
            if (!(jobApps.isOnPhoneInterview() || HRInterface.isRejected(jobApps))) {
                isSetup = false;
            }
        }
        return isSetup;
    }*/
}
