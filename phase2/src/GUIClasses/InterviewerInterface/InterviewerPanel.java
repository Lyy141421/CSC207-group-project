package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.MethodsTheGUICallsInInterviewer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;


abstract class InterviewerPanel extends JPanel {
    /**
     * Class that contains all shared instance variables/methods among the different cards.
     */

    // === Instance variables ===
    MethodsTheGUICallsInInterviewer interviewerInterface;   // Interviewer GUI backend
    HashMap<String, Interview> interviews;  // The map of titles to interviews to be displayed on the left-hand side
    JList<String> interviewList = new JList<>();    // The list of interviews to be dispalyed on the left-hand side
    JSplitPane splitDisplay;    // The split display for the panels.

    // === Constructor ===
    InterviewerPanel(MethodsTheGUICallsInInterviewer interviewerInterface) {
        this.interviewerInterface = interviewerInterface;
        splitDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitDisplay.setDividerLocation(150);
    }

    // === Getter ===
    HashMap<String, Interview> getTitleToInterviewMap(ArrayList<Interview> interviewList) {
        HashMap<String, Interview> titleToInterviewMap = new HashMap<>();
        for (Interview interview : interviewList) {
            titleToInterviewMap.put(this.toInterviewTitle(interview), interview);
        }
        return titleToInterviewMap;
    }

    // === Setters ===
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

    /**
     * Get the interview title to be displayed in the interview list.
     *
     * @param interview The interview being displayed.
     * @return the title that is to be displayed for this interview.
     */
    private String toInterviewTitle(Interview interview) {
        String interviewTimeString = "";
        if (interview.getTime() != null) {
            interviewTimeString = interview.getTime().toString();
        }
        return interview.getId() + ": " + interviewTimeString + " " + interview.getIntervieweeNames();
    }

    /**
     * Refreshes all the cards in InterviewerMain when any changes are made.
     */
    void refresh() {
        ((UserPanel) this.getParent().getParent()).refresh();
    }
}

