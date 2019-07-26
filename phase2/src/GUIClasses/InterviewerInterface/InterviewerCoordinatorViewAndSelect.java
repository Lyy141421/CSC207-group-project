package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import java.util.HashMap;

public class InterviewerCoordinatorViewAndSelect extends InterviewerViewAndWriteNotes {

    InterviewerCoordinatorViewAndSelect(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);
        this.add(super.splitDisplay);
    }

    @Override
    HashMap<String, Interview> getInterviewMap() {
        return getTitleToInterviewMap(interviewerInterface.getIncompleteInterviewsAlreadyOccurredAsCoordinator());
    }

    //TODO: pass/fail. Refer to HRInterface.SelectionFrame(in progress)
}
