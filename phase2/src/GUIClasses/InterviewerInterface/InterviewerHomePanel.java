package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.CommonUserGUI.NotificationsGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class InterviewerHomePanel extends JLayeredPane {
    /**
     * The Home panel of the Interviewer GUI.
     */

    private InterviewerBackEnd interviewerBackEnd;

    // === Constructor ===
    InterviewerHomePanel(InterviewerBackEnd interviewerBackEnd) {
        this.interviewerBackEnd = interviewerBackEnd;
        this.setLayout(null);
        JPanel welcomePanel = this.createWelcomePanel();
        welcomePanel.setBounds(170, 10, 300, 30);
        this.add(welcomePanel, new Integer(0));

        int y = 40;
        for (Object[] titleAndArray : this.createTitleToInterviewsArray()) {
            JPanel tablePanel = this.createInterviewsTablePanel((String) titleAndArray[0], (ArrayList<Interview>) titleAndArray[1], (Boolean) titleAndArray[2]);
            tablePanel.setBounds(30, y, 610, 125);
            this.add(tablePanel, new Integer(0));
            y += 135;
        }

        NotificationsGUI notifications = new NotificationsGUI(interviewerBackEnd.getInterviewer());
        this.add(notifications.getNotificationsButton(), new Integer(1));
        this.add(notifications.getNotificationsPanel(), new Integer(1));
    }

    /**
     * Create a welcome panel to be displayed.
     *
     * @return the welcome panel created.
     */
    private JPanel createWelcomePanel() {
        return new GUIElementsCreator().createLabelPanel("Welcome " +
                this.interviewerBackEnd.getInterviewer().getLegalName(), 20, true);
    }

    private ArrayList<Object[]> createTitleToInterviewsArray() {
        ArrayList<Object[]> titleToJobPostings = new ArrayList<>();
        titleToJobPostings.add(new Object[]{"One-on-One Interviews to Schedule", this.interviewerBackEnd.getInterviewsThatNeedScheduling(), false});
        titleToJobPostings.add(new Object[]{"Upcoming Interviews", this.interviewerBackEnd.getScheduledUpcomingInterviews(), true});
        titleToJobPostings.add(new Object[]{"Incomplete Interviews", this.interviewerBackEnd.getIncompleteInterviewsAlreadyOccurred(), false});
        return titleToJobPostings;
    }

    /**
     * Create the full panel that contains information for jobPostings to schedule.
     *
     * @return the panel created.
     */
    private JPanel createInterviewsTablePanel(String tableTitle, ArrayList<Interview> interviews, boolean upcoming) {
        JPanel applicantSelection = new JPanel();
        applicantSelection.setLayout(new BorderLayout());
        applicantSelection.add(new GUIElementsCreator().createLabelPanel(
                tableTitle, 15, true), BorderLayout.BEFORE_FIRST_LINE);
        applicantSelection.add(this.createJobPostingsTable(interviews, upcoming), BorderLayout.CENTER);
        return applicantSelection;
    }

    private JPanel createJobPostingsTable(ArrayList<Interview> interviews, boolean upcoming) {
        Object[][] data = new Object[interviews.size()][];
        if (upcoming) {
            for (int i = 0; i < interviews.size(); i++) {
                data[i] = interviews.get(i).getCategoryValuesForInterviewerScheduled();
            }
            return new GUIElementsCreator().createTablePanel(Interview.CATEGORY_NAMES_FOR_INTERVIEWER_SCHEDULED, data);
        } else {
            for (int i = 0; i < interviews.size(); i++) {
                data[i] = interviews.get(i).getCategoryValuesForInterviewerUnscheduledOrIncomplete();
            }
            return new GUIElementsCreator().createTablePanel(Interview.CATEGORY_NAMES_FOR_INTERVIEWER_UNSCHEDULED_OR_INCOMPLETE, data);
        }
    }
}