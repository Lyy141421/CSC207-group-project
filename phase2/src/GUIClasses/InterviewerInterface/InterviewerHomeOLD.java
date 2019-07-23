package GUIClasses.InterviewerInterface;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class InterviewerHomeOLD extends InterviewerPanel {

    InterviewerHomeOLD(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        JButton viewComplete = new JButton("View completed interviews");
        JButton viewIncomplete = new JButton("View future interviews");
        JButton schedule = new JButton("Schedule interview");
        JButton logout = new JButton("Logout");
        //TODO: actionListeners

        this.add(viewComplete);
        this.add(viewIncomplete);
        this.add(schedule);
        this.add(logout);
    }
}
