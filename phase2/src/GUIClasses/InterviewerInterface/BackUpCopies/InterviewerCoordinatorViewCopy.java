package GUIClasses.InterviewerInterface.BackUpCopies;

import CompanyStuff.Interview;
import GUIClasses.InterviewerInterface.InterviewerViewComplete;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;

public class InterviewerCoordinatorViewCopy extends InterviewerViewComplete {

    InterviewerCoordinatorViewCopy(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAsCoordinator(today));
    }

    //TODO: pass/fail. Refer to HRInterface.SelectionFrame(in progress)
}
