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

    private JPanel hireOrRejectButtons = new JPanel();
    private JPanel phoneOrNotButtons = new JPanel();

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

    private void setApplicationList (JSplitPane splitDisplay) {
        JList<String> applicationList = new JList<>();
        //Todo: add content to JList
        applicationList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        applicationList.setLayoutOrientation(JList.VERTICAL);

        splitDisplay.setLeftComponent(new JScrollPane(applicationList));
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
                info = app.getOverview();
                break;
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


    /*JRadioButton hire = new JRadioButton("Hire");
    hire.setSelected(true);
    JRadioButton reject = new JRadioButton("Reject");
    ButtonGroup hireOrReject = new ButtonGroup();
    hireOrReject.add(hire);
    hireOrReject.add(reject);
    hireOrRejectButtons.add(hire);
    hireOrRejectButtons.add(reject);
    JButton confirm1 = new JButton("Confirm");
    hireOrRejectButtons.add(confirm1);
    confirm1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = applications.getSelectedIndex();
            JobApplication selectedApp = currApps.get(selectedIndex);
            boolean filled = HRInterface.hireOrRejectApplication(selectedApp, hire.isSelected());
            if (filled) {
                appTitles.removeAllElements();
                hiringJP.remove(selectedApp.getJobPosting());
                importantJP.remove(selectedApp.getJobPosting());
                for (JobApplication jobApp : currApps) {
                    if (!(HRInterface.isRejected(jobApp) || jobApp.isHired())) {
                        HRInterface.hireOrRejectApplication(jobApp, false);
                    }
                }
                JOptionPane.showMessageDialog(applicationPanel, "All positions for this job has been filled");
            }
        }
    });

        phoneOrNotButtons.setLayout(new BoxLayout(phoneOrNotButtons, BoxLayout.Y_AXIS));
    JRadioButton phone = new JRadioButton("Select for phone Interview");
        phone.setSelected(true);
    JRadioButton noPhone = new JRadioButton("Reject");
    ButtonGroup phoneOrNot = new ButtonGroup();
        phoneOrNot.add(phone);
        phoneOrNot.add(noPhone);
        phoneOrNotButtons.add(phone);
        phoneOrNotButtons.add(noPhone);
    JButton confirm2 = new JButton("Confirm");
        phoneOrNotButtons.add(confirm2);
        confirm2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = applications.getSelectedIndex();
            JobApplication selectedApp = currApps.get(selectedIndex);
            HRInterface.selectApplicationForPhoneInterview(selectedApp, phone.isSelected());
            if (isPhoneSetup()) {
                appTitles.removeAllElements();
                prePhoneJP.remove(selectedApp.getJobPosting());
                scheduleJP.add(selectedApp.getJobPosting());
                JOptionPane.showMessageDialog(applicationPanel, "All applications have been processed. Please proceed to schedule interviews.");
            }
        }
    });

    JButton home = new JButton("Home");
        home.addActionListener(this);

    JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(viewable), new JScrollPane(info));
        display.setDividerLocation(250);



        applications.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JobApplication selectedApp = currApps.get(applications.getSelectedIndex());
                info.setText(getInfo(selectedApp, viewable.getSelectedIndex()));
                if (selectedApp.isInPerson3() && !HRInterface.isRejected(selectedApp)) {
                    hireOrRejectButtons.setVisible(true);
                    phoneOrNotButtons.setVisible(false);
                } else if (selectedApp.isUnderReview() && !HRInterface.isRejected(selectedApp)) {
                    hireOrRejectButtons.setVisible(false);
                    phoneOrNotButtons.setVisible(true);
                } else {
                    hireOrRejectButtons.setVisible(false);
                    phoneOrNotButtons.setVisible(false);
                }
            }
        }
    });

        buttons.add(hireOrRejectButtons);
        buttons.add(phoneOrNotButtons);
        buttons.add(home);

        applicationPanel.add(applications, BorderLayout.NORTH);
        applicationPanel.add(display, BorderLayout.CENTER);
        applicationPanel.add(buttons, BorderLayout.SOUTH);

        return applicationPanel;
}

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
