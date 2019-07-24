package GUIClasses.InterviewerInterface;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class InterviewerMain extends JPanel {

    Container contentPane;
    MethodsTheGUICallsInInterviewer interviewerInterface;
    LocalDate today;

    Container mainPanel = this;
    CardLayout cardLayout = new CardLayout();

    InterviewerHome homePanel;
    InterviewerView viewIncompletePanel;
    InterviewerViewComplete viewCompletePanel;
    InterviewerSchedule schedulePanel;

    private InterviewerMain (Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        this.setLayout(this.cardLayout);

        this.contentPane = contentPane;
        this.interviewerInterface = interviewerInterface;
        this.today = today;

        this.addPanels();
    }

    private void addPanels() {
        this.add(this.homePanel = new InterviewerHome(this, this.interviewerInterface, this.today), InterviewerPanel.HOME);
        this.add(this.viewIncompletePanel = new InterviewerView(this, this.interviewerInterface, this.today), InterviewerPanel.INCOMPLETE);
        this.add(this.viewCompletePanel = new InterviewerViewComplete(this, this.interviewerInterface, this.today), InterviewerPanel.COMPLETE);
        this.add(this.schedulePanel = new InterviewerSchedule(this, this.interviewerInterface, this.today), InterviewerPanel.SCHEDULE);
    }
}
