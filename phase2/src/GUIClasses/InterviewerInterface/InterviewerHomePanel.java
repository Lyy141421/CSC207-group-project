package GUIClasses.InterviewerInterface;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Main.JobApplicationSystem;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewerHomePanel extends InterviewerPanel {

    // === Instance variables ===

    InterviewerHomePanel(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        super(contentPane, interviewerInterface, today);

        this.setLayout(new BorderLayout());
        this.add(this.createWelcomePanel(), BorderLayout.BEFORE_FIRST_LINE);
        this.createSchedulingReminderPanel();
        this.add(this.createSchedulePanel(), BorderLayout.CENTER);
        JPanel remindersPanel = new JPanel();
        remindersPanel.setLayout(new BoxLayout(remindersPanel, BoxLayout.X_AXIS));
        remindersPanel.add(this.createSchedulingReminderPanel());
        remindersPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        remindersPanel.add(this.createIncompleteInterviewsPanel());
        this.add(remindersPanel, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome " + this.interviewerInterface.getInterviewer().getLegalName());
        welcomeMessage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        welcomePanel.add(welcomeMessage);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.revalidate();
        return welcomePanel;
    }

    private JPanel createSchedulingReminderPanel() {
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.add(new FrequentlyUsedMethods().createTitlePanel(
                "One-on-One interviews that need scheduling:", 17), BorderLayout.BEFORE_FIRST_LINE);
        reminderPanel.add(this.createInterviewsToScheduleTablePanel(), BorderLayout.CENTER);
        return reminderPanel;
    }

    private JPanel createTablePanel(String[] categoryNames, Object[][] data) {
        JPanel tablePanel = new JPanel(new GridLayout(1, 0));

        JTable table = new JTable(data, categoryNames);
        table.setCellSelectionEnabled(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        return tablePanel;
    }

    private JPanel createInterviewsToScheduleTablePanel() {
        ArrayList<Interview> unscheduledInterviews = this.interviewerInterface.getInterviewsThatNeedScheduling();
        Object[][] data = new Object[unscheduledInterviews.size()][];

        for (int i = 0; i < unscheduledInterviews.size(); i++) {
            data[i] = unscheduledInterviews.get(i).getCategoryValuesForInterviewerUnscheduled();
        }

        return this.createTablePanel(Interview.getCategoryNamesForInterviewerUnscheduled(), data);
    }

    private JPanel createSchedulePanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new FrequentlyUsedMethods().createTitlePanel(
                "Upcoming interviews:", 17), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createScheduleTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    private JPanel createScheduleTablePanel() {
        ArrayList<Interview> scheduledInterviews = this.interviewerInterface.getScheduledUpcomingInterviews(this.today);
        Object[][] data = new Object[scheduledInterviews.size()][];

        for (int i = 0; i < scheduledInterviews.size(); i++) {
            data[i] = scheduledInterviews.get(i).getCategoryValuesForInterviewerScheduled();
        }

        return this.createTablePanel(Interview.getCategoryNamesForInterviewerScheduled(), data);
    }

    private JPanel createIncompleteInterviewsPanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new FrequentlyUsedMethods().createTitlePanel(
                "Incomplete interviews:", 17), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createIncompleteInterviewsTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    private JPanel createIncompleteInterviewsTablePanel() {
        ArrayList<Interview> incompleteInterviews = this.interviewerInterface.getIncompleteInterviewsAsCoordinator(this.today);
        Object[][] data = new Object[incompleteInterviews.size()][];

        for (int i = 0; i < incompleteInterviews.size(); i++) {
            data[i] = incompleteInterviews.get(i).getCategoryValuesForInterviewerScheduled();
        }

        return this.createTablePanel(Interview.getCategoryNamesForInterviewerScheduled(), data);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Interviewer Home Page");
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        jobApplicationSystem.setToday(LocalDate.now());
        jobApplicationSystem.setPreviousLoginDate(LocalDate.now());
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        branch.setCompany(company);
        company.addBranch(branch);
        Interviewer interviewer = jobApplicationSystem.getUserManager().createInterviewer("username", "password", "Legal Name", "email@gmail.com", branch, "Field", LocalDate.now());
        MethodsTheGUICallsInInterviewer interviewerInterface = new MethodsTheGUICallsInInterviewer(interviewer);
        JPanel panel = new InterviewerHomePanel(frame.getContentPane(), interviewerInterface, jobApplicationSystem.getToday());
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
