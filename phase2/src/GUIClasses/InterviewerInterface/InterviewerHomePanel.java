package GUIClasses.InterviewerInterface;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Main.JobApplicationSystem;
import GUIClasses.CommonUserGUI.TitleCreator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewerHomePanel extends InterviewerPanel {

    InterviewerHomePanel(MethodsTheGUICallsInInterviewer interviewerInterface) {
        super(interviewerInterface);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.createWelcomePanel(), c);
        c.gridy++;
        c.gridheight = 100;
        this.add(this.createInterviewsToSchedulePanel(), c);
        c.gridy = 101;
        c.gridheight = 100;
        this.add(this.createUpcomingInterviewsPanel(), c);
        c.gridy = 201;
        c.gridheight = 100;
        this.add(this.createIncompleteInterviewsPanel(), c);
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

    private JPanel createTablePanel(String[] categoryNames, Object[][] data) {
        JPanel tablePanel = new JPanel(new GridLayout(1, 0));

        JTable table = new JTable(data, categoryNames);
        table.setCellSelectionEnabled(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(500, 70));
        tablePanel.add(scrollPane);
        return tablePanel;
    }

    private JPanel createInterviewsToSchedulePanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new TitleCreator().createTitlePanel(
                "One-on-One interviews to schedule", 17), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createInterviewsToScheduleTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    private JPanel createInterviewsToScheduleTablePanel() {
        ArrayList<Interview> unscheduledInterviews = this.interviewerInterface.getInterviewsThatNeedScheduling();
        Object[][] data = new Object[unscheduledInterviews.size()][];

        for (int i = 0; i < unscheduledInterviews.size(); i++) {
            data[i] = unscheduledInterviews.get(i).getCategoryValuesForInterviewerUnscheduled();
        }

        return this.createTablePanel(Interview.getCategoryNamesForInterviewerUnscheduled(), data);
    }

    private JPanel createUpcomingInterviewsPanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new TitleCreator().createTitlePanel(
                "Upcoming interviews:", 17), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createUpcomingInterviewsTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    private JPanel createUpcomingInterviewsTablePanel() {
        ArrayList<Interview> scheduledInterviews = this.interviewerInterface.getScheduledUpcomingInterviews();
        Object[][] data = new Object[scheduledInterviews.size()][];

        for (int i = 0; i < scheduledInterviews.size(); i++) {
            data[i] = scheduledInterviews.get(i).getCategoryValuesForInterviewerScheduled();
        }

        return this.createTablePanel(Interview.getCategoryNamesForInterviewerScheduled(), data);
    }

    private JPanel createIncompleteInterviewsPanel() {
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.add(new TitleCreator().createTitlePanel(
                "Incomplete interviews:", 17), BorderLayout.BEFORE_FIRST_LINE);
        schedulePanel.add(this.createIncompleteInterviewsTablePanel(), BorderLayout.CENTER);
        return schedulePanel;
    }

    private JPanel createIncompleteInterviewsTablePanel() {
        ArrayList<Interview> incompleteInterviews = this.interviewerInterface.getIncompleteInterviewsAlreadyOccurredAsCoordinator();
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
        Branch branch = new Branch("Branch", "Toronto", company);
        branch.setCompany(company);
        company.addBranch(branch);
        Interviewer interviewer = jobApplicationSystem.getUserManager().createInterviewer("username", "password", "Legal Name", "email@gmail.com", branch, "Field", LocalDate.now());
        MethodsTheGUICallsInInterviewer interviewerInterface = new MethodsTheGUICallsInInterviewer(jobApplicationSystem, interviewer);
        JPanel panel = new InterviewerHomePanel(interviewerInterface);
        frame.add(panel);
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
