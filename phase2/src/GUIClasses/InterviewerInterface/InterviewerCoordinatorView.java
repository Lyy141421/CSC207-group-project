package GUIClasses.InterviewerInterface;

import GUIClasses.MethodsTheGUICallsInInterviewer;

import java.awt.*;
import java.time.LocalDate;

public class InterviewerCoordinatorView extends InterviewerPanel{

    InterviewerCoordinatorView(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);
    }
}
