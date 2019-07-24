package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;

public class InterviewerCoordinatorView extends InterviewerViewComplete{

    InterviewerCoordinatorView(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAsCoordinator(today));
    }

    //TODO: pass/fail. Refer to HRInterface.SelectionFrame(in progress)
}
