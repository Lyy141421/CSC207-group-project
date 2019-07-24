package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import java.time.LocalDate;
import java.util.HashMap;

public class InterviewerCoordinatorView extends InterviewerViewComplete{

    InterviewerCoordinatorView(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAsCoordinator());
    }

    //TODO: pass/fail. Refer to HRInterface.SelectionFrame(in progress)
}
