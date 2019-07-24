package NewGUI.InterviewerInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;
import Miscellaneous.InterviewTime;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewerScheduleInterviewsPanel extends JPanel {

    // === Instance variables ===
    // The interviewer who logged in
    private Interviewer interviewer;
    private LocalDate today;    // Today's date
    private Interview interviewSelected;    // The interview selected
    private LocalDate dateSelected; // The date selected
    private String timeSlotSelected;    // The time selected

    private JButton selectInterview = new JButton("Select Interview");  // The button for selecting a job application
    private JButton selectDateButton = new JButton("Select Date");  // The button for selecting an interview date
    private JButton selectTimeButton = new JButton("Select Time");  // The button for selecting an interview time
    private JList jobAppList;   // The list that displays the job applications that need interviews scheduled
    private JList timeList; // The list that displays the times available for the date selected
    private JPanel dateTimePanel = new JPanel();    // The dateTimePanel
    private JPanel datePanel = new JPanel();    // The date panel
    private JPanel timePanel = new JPanel();    // The time panel
    private JFormattedTextField dateField;

        // === Constructor ===
    InterviewerScheduleInterviewsPanel(Interviewer interviewer, LocalDate today) {
        this.interviewer = interviewer;
        this.today = today;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(new FrequentlyUsedMethods().createTitlePanel("Schedule One-on-One Interviews", 20));

        JPanel selectionPanels = new JPanel();
        selectionPanels.setLayout(new BoxLayout(selectionPanels, BoxLayout.X_AXIS));
        selectionPanels.add(this.createJobApplicationListPanel());
        selectionPanels.add(Box.createRigidArea(new Dimension(30, 0)));
        this.setInterviewDateTimePanel();
        selectionPanels.add(dateTimePanel);
        this.add(selectionPanels, BorderLayout.CENTER);
    }

    /**
     * Create a list panel with all the job applications that this reference needs to write a reference for.
     *
     * @return the panel with this list.
     */
    private JPanel createJobApplicationListPanel() {
        JPanel jobAppListPanel = new JPanel();
        jobAppListPanel.setLayout(new BorderLayout());
        jobAppListPanel.add(new FrequentlyUsedMethods().createTitlePanel("Select an Interview", 15),
                BorderLayout.PAGE_START);
        DefaultListModel listModel = new DefaultListModel();
        for (Interview interview : this.interviewer.getUnscheduledInterviews()) {
            listModel.addElement(interview.getMiniDescriptionForInterviewer());
        }
        jobAppList = new JList(listModel);
        jobAppList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobAppList.setSelectedIndex(-1);
        jobAppList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (jobAppList.getSelectedIndex() == -1) {
                        selectInterview.setEnabled(false);   //No selection, disable select button.
                    } else {
                        selectInterview.setEnabled(true);    //Selection, enable the select button.
                    }
                }
            }
        });
        jobAppList.setVisibleRowCount(5);
        jobAppList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane listScrollPane = new JScrollPane(jobAppList);
        jobAppListPanel.add(listScrollPane, BorderLayout.CENTER);
        jobAppListPanel.add(this.createInterviewButtonPane(), BorderLayout.PAGE_END);
        return jobAppListPanel;
    }

    private void setInterviewDateTimePanel() {
        dateTimePanel = new JPanel();
        dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.Y_AXIS));
        this.setDatePanel();
        dateTimePanel.add(datePanel);
    }

    private void setDatePanel() {
        datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.add(new FrequentlyUsedMethods().createTitlePanel("Enter a date", 15));
        dateField = new JFormattedTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateField.setColumns(10);
        dateField.setSize(50, 20);
        dateField.setValue("yyyy-MM-dd");
        dateField.setHorizontalAlignment(JTextField.CENTER);
        dateField.revalidate();
        datePanel.add(dateField);
        datePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        datePanel.add(this.createDateButtonPane());
    }

    private void setTimePanel() {
        timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        DefaultListModel listModel = new DefaultListModel();
        for (String timeSlotDescription : interviewer.getTimeSlotsAvailableOnDate(dateSelected)) {
            listModel.addElement(timeSlotDescription);
        }
        timePanel.add(new FrequentlyUsedMethods().createTitlePanel("Select a time", 15),
                BorderLayout.PAGE_START);
        timeList = new JList(listModel);
        timeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        timeList.setSelectedIndex(-1);
        timeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (timeList.getSelectedIndex() == -1) {
                        selectTimeButton.setEnabled(false);   //No selection, disable select button.
                    } else {
                        selectTimeButton.setEnabled(true);    //Selection, enable the select button.
                    }
                }
            }
        });
        timeList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane listScrollPane = new JScrollPane(timeList);
        timePanel.add(listScrollPane, BorderLayout.CENTER);
        timePanel.add(this.createTimeSlotButtonPane(), BorderLayout.PAGE_END);
    }

    /**
     * Create the button pane for the list.
     *
     * @return the button pane for the job application list.
     */
    private JPanel createInterviewButtonPane() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        selectInterview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jobAppList.getSelectedIndex();
                interviewSelected = interviewer.getUnscheduledInterviews().get(selectedIndex);
                dateTimePanel.remove(datePanel);
                selectDateButton.setEnabled(true);
                dateTimePanel.add(datePanel);
                revalidate();
            }
        });
        selectInterview.setEnabled(false);
        buttonPane.add(selectInterview);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonPane;
    }

    /**
     * Create the button pane for the list.
     *
     * @return the button pane for the job application list.
     */
    private JPanel createDateButtonPane() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        selectDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateString = (String) dateField.getValue();
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (!date.isAfter(today)) {
                    dateField.setValue("Invalid date.");
                } else if (!interviewer.isAvailable(dateSelected)) {
                    dateField.setValue("You are unavailable on this date.");
                } else {
                    dateSelected = date;
                    setTimePanel();
                    selectDateButton.setEnabled(true);
                    dateTimePanel.add(Box.createRigidArea(new Dimension(30, 0)));
                    dateTimePanel.add(timePanel);
                    revalidate();
                }
            }
        });
        selectDateButton.setEnabled(false);
        buttonPane.add(selectDateButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonPane;
    }

    /**
     * Create the button pane for the time slot list.
     *
     * @return the button pane for the time slot list.
     */
    private JPanel createTimeSlotButtonPane() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        selectTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeSlotSelected = interviewer.getTimeSlotsAvailableOnDate(dateSelected).get(timeList.getSelectedIndex());
                InterviewTime interviewTime = new InterviewTime(dateSelected, InterviewTime.timeSlots.indexOf(timeSlotSelected));
                interviewSelected.setTime(interviewTime);
            }
        });
        selectTimeButton.setEnabled(false);
        buttonPane.add(selectTimeButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonPane;
    }

    public static void main(String[] args) {

        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "L4B3Z9");
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer);
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();

        JFrame frame = new JFrame();
        frame.add(new InterviewerScheduleInterviewsPanel(interviewer, LocalDate.now()));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
