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
        //TODO: might want to change the arrayList retrieved here to specifically interviews where this
        // interviewer is the coordinator.
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviews(today));
    }

    //TODO: pass/fail. Refer to HRInterface.SelectionFrame(in progress)
}
