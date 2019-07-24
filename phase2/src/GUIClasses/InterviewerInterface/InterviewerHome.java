package GUIClasses.InterviewerInterface;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class InterviewerHome extends InterviewerPanel {

    JButton viewCompleteButton;
    JButton viewIncompleteButton;
    JButton scheduleButton;
    JButton logoutButton;

    InterviewerHome(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //TODO: this needs a more descriptive name. Lets interviewer write notes and pass/fail
        this.viewCompleteButton = new JButton("View completed interviews");
        this.viewIncompleteButton = new JButton("View future interviews");
        this.scheduleButton = new JButton("Schedule interview");
        this.logoutButton = new JButton("Logout");
        //TODO: actionListeners

        this.add(viewCompleteButton);
        this.add(viewIncompleteButton);
        this.add(scheduleButton);
        this.add(logoutButton);
    }

    void reload() {

    }

    JButton getViewCompleteButton() {
        return this.viewCompleteButton;
    }

    JButton getViewIncompleteButton() {
        return this.viewIncompleteButton;
    }

    JButton getScheduleButton() {
        return this.scheduleButton;
    }

    JButton getLogoutButton() {
        return this.logoutButton;
    }
}
