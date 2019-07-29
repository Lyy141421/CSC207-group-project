package GUIClasses.InterviewerInterface;

import CompanyStuff.Interview;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class InterviewerHomePanel extends AbstractInterviewerPanel {
    /**
     * The Home panel of the Interviewer GUI.
     */

    // === Constructor ===
    InterviewerHomePanel(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.createWelcomePanel(), c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridy++;
        c.gridheight = 100;
        this.add(this.createInterviewsToSchedulePanel(), c);
        c.gridy = 101;
        c.gridheight = 100;
        this.add(this.createUpcomingInterviewsPanel(), c);
        c.gridy = 201;
        c.gridheight = 100;
        c.insets = new Insets(10, 10, 10, 10);
        this.add(this.createIncompleteInterviewsPanel(), c);
    }

    /**
     * Create a welcome panel to be displayed.
     *
     * @return the welcome panel created.
     */
    private JPanel createWelcomePanel() {
        JPanel welcomeMessage = new GUIElementsCreator().createLabelPanel("Welcome " +
                this.interviewerInterface.getInterviewer().getLegalName(), 20, true);
        return welcomeMessage;
    }

    /**
     * Create the full panel that contains information for interviews to schedule.
     * @return the panel created.
     */
    private JPanel createInterviewsToSchedulePanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new GUIElementsCreator().createLabelPanel(
                "One-on-One interviews to schedule", 17, true), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createInterviewsToScheduleTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    /**
     * Create a panel with a table that contains information for interviews to schedule.
     * @return the panel created.
     */
    private JPanel createInterviewsToScheduleTablePanel() {
        ArrayList<Interview> unscheduledInterviews = this.interviewerInterface.getInterviewsThatNeedScheduling();
        Object[][] data = new Object[unscheduledInterviews.size()][];

        for (int i = 0; i < unscheduledInterviews.size(); i++) {
            data[i] = unscheduledInterviews.get(i).getCategoryValuesForInterviewerUnscheduledOrIncomplete();
        }

        return new GUIElementsCreator().createTablePanel(Interview.getCategoryNamesForInterviewerUnscheduledOrIncomplete(), data);
    }

    /**
     * Create the full panel that contains information for upcoming interviews.
     * @return the panel created.
     */
    private JPanel createUpcomingInterviewsPanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new GUIElementsCreator().createLabelPanel(
                "Upcoming interviews:", 17, true), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createUpcomingInterviewsTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    /**
     * Create a panel with a table that contains information for upcoming interviews.
     * @return the panel created.
     */
    private JPanel createUpcomingInterviewsTablePanel() {
        ArrayList<Interview> scheduledInterviews = this.interviewerInterface.getScheduledUpcomingInterviews();
        Object[][] data = new Object[scheduledInterviews.size()][];

        for (int i = 0; i < scheduledInterviews.size(); i++) {
            data[i] = scheduledInterviews.get(i).getCategoryValuesForInterviewerScheduled();
        }

        return new GUIElementsCreator().createTablePanel(Interview.getCategoryNamesForInterviewerScheduled(), data);
    }

    /**
     * Create the full panel that contains information for incomplete interviews.
     * @return the panel created.
     */
    private JPanel createIncompleteInterviewsPanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new GUIElementsCreator().createLabelPanel(
                "Incomplete Interviews:", 17, true), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createIncompleteInterviewsTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    /**
     * Create a panel with a table that contains information for incomplete interviews.
     * @return the panel created.
     */
    private JPanel createIncompleteInterviewsTablePanel() {
        ArrayList<Interview> incompleteInterviews = this.interviewerInterface.getIncompleteInterviewsAlreadyOccurred();
        Object[][] data = new Object[incompleteInterviews.size()][];

        for (int i = 0; i < incompleteInterviews.size(); i++) {
            data[i] = incompleteInterviews.get(i).getCategoryValuesForInterviewerUnscheduledOrIncomplete();
        }

        return new GUIElementsCreator().createTablePanel(Interview.getCategoryNamesForInterviewerUnscheduledOrIncomplete(), data);
    }
}