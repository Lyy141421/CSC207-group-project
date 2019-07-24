package GUIClasses.InterviewerInterface;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.setActions();
    }

    private void addPanels() {
        this.add(this.homePanel = new InterviewerHome(this, this.interviewerInterface, this.today), InterviewerPanel.HOME);
        this.add(this.viewIncompletePanel = new InterviewerView(this, this.interviewerInterface, this.today), InterviewerPanel.INCOMPLETE);
        this.add(this.viewCompletePanel = new InterviewerViewComplete(this, this.interviewerInterface, this.today), InterviewerPanel.COMPLETE);
        this.add(this.schedulePanel = new InterviewerSchedule(this, this.interviewerInterface, this.today), InterviewerPanel.SCHEDULE);
    }

    private void setActions() {
        this.setViewCompleteAction();
        this.setViewIncompleteAction();
        this.setScheduleAction();
        this.setLogoutAction();
    }

    private void setViewCompleteAction() {
        this.homePanel.getViewCompleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCompletePanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.COMPLETE);
            }
        });
    }

    private void setViewIncompleteAction() {
        this.homePanel.getViewIncompleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewIncompletePanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.INCOMPLETE);
            }
        });
    }

    private void setScheduleAction() {
        this.homePanel.getScheduleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schedulePanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.SCHEDULE);
            }
        });
    }

    private void setLogoutAction() {
        this.homePanel.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: save data
                ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
            }
        });
    }
}
