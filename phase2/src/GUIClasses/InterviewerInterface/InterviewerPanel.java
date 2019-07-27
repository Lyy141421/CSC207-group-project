package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;


abstract class InterviewerPanel extends JPanel {

    MethodsTheGUICallsInInterviewer interviewerInterface;
    HashMap<String, Interview> interviews;
    JList<String> interviewList = new JList<>();
    JSplitPane splitDisplay;

    InterviewerPanel(MethodsTheGUICallsInInterviewer interviewerInterface) {
        this.interviewerInterface = interviewerInterface;
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(150);
    }

    void setInterviews(HashMap<String, Interview> interviews) {
        this.interviews = interviews;
    }

    void setInterviewList() {
        this.interviewList.setListData(interviews.keySet().toArray(new String[interviews.size()]));
        this.interviewList.setSelectedIndex(-1);
        this.interviewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.interviewList.setLayoutOrientation(JList.VERTICAL);
        splitDisplay.setLeftComponent(this.interviewList);
    }

    HashMap<String, Interview> getTitleToInterviewMap(ArrayList<Interview> interviewList) {
        HashMap<String, Interview> titleToInterviewMap = new HashMap<>();
        for (Interview interview: interviewList) {
            titleToInterviewMap.put(this.toInterviewTitle(interview), interview);
        }
        return titleToInterviewMap;
    }

    String toInterviewTitle(Interview interview) {
        String interviewTimeString = "";
        if (interview.getTime() != null) {
            interviewTimeString = interview.getTime().toString();
        }
        return interview.getId() + ": " + interviewTimeString + " " + interview.getIntervieweeNames();
    }

    void refresh() {
        ((UserPanel) this.getParent().getParent()).refresh();
    }
}

