package GUIClasses;

import Main.JobApplicationSystem;
import UsersAndJobObjects.Interview;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class InterviewerPanel extends JPanel implements ActionListener {

    private Container contentPane;
    private InterviewerInterface interviewerInterface;
    private LocalDate today;

    private ArrayList<Interview> pastInterviews;
    private ArrayList<Interview> futureInterviews;
    private ArrayList<Interview> interviewsToBeScheduled;
    private String temporaryNotes = "";
    private ArrayList<Interview> currList;

    private JComboBox<String> interviews;
    private DefaultComboBoxModel<String> incompleteTitles;
    private DefaultComboBoxModel<String> completeTitles;
    private JList<String> scheduleInterviews;

    InterviewerPanel(Container contentPane, InterviewerInterface interviewerInterface, LocalDate today) {
        this.contentPane = contentPane;
        this.interviewerInterface = interviewerInterface;
        this.today = today;
        ArrayList<ArrayList<Interview>> interviews = interviewerInterface.getInterviewsBeforeOnAndAfterToday(today);
        this.pastInterviews = interviews.get(0);
        this.futureInterviews = interviews.get(1);
        this.futureInterviews.addAll(interviews.get(2));
        this.interviewsToBeScheduled = interviewerInterface.getUnscheduledInterviews();
        this.currList = this.futureInterviews;

        this.incompleteTitles = new DefaultComboBoxModel<>((String[]) getInterviewTitles(this.futureInterviews).toArray());
        this.completeTitles = new DefaultComboBoxModel<>((String[]) getInterviewTitles(this.pastInterviews).toArray());

        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(viewInterviews(), "VIEW");
        this.add(scheduleInterviews(), "SCHEDULE");
    }

    private JPanel home() {
        JPanel homePanel = new JPanel();

        JButton view = new JButton("View interviews");
        view.addActionListener(this);
        JButton schedule = new JButton("Schedule interview");
        schedule.addActionListener(this);
        JButton logout = new JButton("Logout");
        logout.addActionListener(this);

        homePanel.add(view);
        homePanel.add(schedule);
        homePanel.add(logout);

        return homePanel;
    }


    // ====View Interviews panel methods====

    private JPanel viewInterviews() {
        JPanel viewPanel = new JPanel(new BorderLayout());
        JPanel select = new JPanel();
        select.setLayout(new BoxLayout(select, BoxLayout.Y_AXIS));
        JPanel passOrFailButtons = new JPanel();
        passOrFailButtons.setLayout(new BoxLayout(passOrFailButtons, BoxLayout.Y_AXIS));
        JPanel buttons = new JPanel(new FlowLayout());

        this.interviews = new JComboBox<>(this.incompleteTitles);
        JRadioButton incomplete = new JRadioButton("Incomplete", true);
        JRadioButton complete = new JRadioButton("Complete", false);
        ButtonGroup showContent = new ButtonGroup();
        showContent.add(incomplete);
        showContent.add(complete);
        JList<String> viewable = new JList<>(new String[]{"Overview", "Notes", "CV", "Cover letter"});

        select.add(incomplete);
        select.add(complete);
        select.add(new JScrollPane(viewable));

        JTextArea info = new JTextArea("Information");
        info.setEditable(false);
        // Phase 2?
        // JButton reschedule = new JButton("Re-schedule");
        ButtonGroup passOrFail = new ButtonGroup();
        JRadioButton advance = new JRadioButton("Advance");
        advance.setEnabled(false);
        advance.setSelected(true);
        JRadioButton fail = new JRadioButton("Fail");
        fail.setEnabled(false);
        passOrFail.add(advance);
        passOrFail.add(fail);
        passOrFailButtons.add(advance);
        passOrFailButtons.add(fail);
        JButton submit = new JButton("Submit results");
        submit.setEnabled(false);
        JButton home = new JButton("Home");
        home.addActionListener(this);
        buttons.add(passOrFailButtons);
        buttons.add(submit);
        buttons.add(home);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, select, new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        incomplete.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    interviews.setModel(incompleteTitles);
                    currList = futureInterviews;
                    advance.setEnabled(false);
                    fail.setEnabled(false);
                    submit.setEnabled(false);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    interviews.setModel(completeTitles);
                    currList = pastInterviews;
                    advance.setEnabled(true);
                    fail.setEnabled(true);
                    submit.setEnabled(true);
                }
            }
        });

        interviews.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setInfo(currList.get(interviews.getSelectedIndex()), viewable.getSelectedIndex(), info);
                    // Phase2: "you have unsaved changes. Would you like to proceed?"
                    temporaryNotes = "";
                }
            }
        });

        viewable.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (info.isEditable()) {
                    temporaryNotes = info.getText();
                }
                setInfo(currList.get(interviews.getSelectedIndex()), e.getFirstIndex(), info);
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = interviews.getSelectedIndex();
                Interview interview = currList.get(selectedIndex);
                if (viewable.getSelectedIndex() == 1) {
                    interviewerInterface.storeInterviewNotes(interview, info.getText());
                } else {
                    interviewerInterface.storeInterviewNotes(interview, temporaryNotes);
                }
                interviewerInterface.passOrFailInterview(interview, advance.isSelected());

                // Clear temporarily stored notes
                temporaryNotes = "";
                // Remove interview from list
                currList.remove(interview);
                ((DefaultComboBoxModel<String>) interviews.getModel()).removeElementAt(selectedIndex);
            }
        });

        viewPanel.add(interviews, BorderLayout.NORTH);
        viewPanel.add(display, BorderLayout.CENTER);
        viewPanel.add(buttons, BorderLayout.SOUTH);

        return viewPanel;
    }

    private ArrayList<String> getInterviewTitles(ArrayList<Interview> interviews) {
        ArrayList<String> titles = new ArrayList<>();
        for (Interview interview : interviews) {
            titles.add(interview.getId() + "-" + interview.getTime().toString() + " " +
                    interview.getApplicant().getLegalName());
        }

        return titles;
    }

    private void setInfo(Interview interview, int attributeIndex, JTextArea info) {
        switch (attributeIndex) {
            case 0:
                info.setText(interview.getOverview());
                break;
            case 1:
                info.setText(interview.getInterviewNotes());
                if (interview.isComplete()) {
                    info.setEditable(true);
                } else {
                    info.setEditable(false);
                }
                break;
            case 2:
                info.setText(interview.getJobApplication().getCV().getContents());
                break;
            case 3:
                info.setText(interview.getJobApplication().getCoverLetter().getContents());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeIndex);
        }
    }


    // ====Schedule interview panel methods====

    private JPanel scheduleInterviews() {
        JPanel schedulePanel = new JPanel(new BorderLayout());
        JPanel setTime = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.scheduleInterviews = new JList<>(getIdAndApplicants(interviewsToBeScheduled));
        scheduleInterviews.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        scheduleInterviews.setLayoutOrientation(JList.VERTICAL);
        JComboBox<String> timeSlot = new JComboBox<>(new String[]{"9-10 am", "10-11 am", "1-2 pm", "2-3 pm", "3-4 pm",
                "4-5 pm"});
        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl interviewDate = new JDatePickerImpl(datePanel);
        JButton schedule = new JButton("Confirm");
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = ((Date) interviewDate.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int selectedIndex = interviews.getSelectedIndex();
                Interview interview = interviewsToBeScheduled.get(selectedIndex);
                if (date.isAfter(today)) {
                    boolean canSchedule = interviewerInterface.scheduleInterview(interview, date, timeSlot.getSelectedIndex());
                    if (canSchedule) {
                        interviewsToBeScheduled.remove(selectedIndex);
                        scheduleInterviews.remove(selectedIndex);
                        futureInterviews.add(interview);
                        incompleteTitles.addElement(getInterviewTitles(new ArrayList<Interview>(Arrays.asList(interview))).get(0));
                    } else {
                        JOptionPane.showMessageDialog(schedulePanel, "Unable to book the interview at the selected date and time");
                    }
                } else {
                    JOptionPane.showMessageDialog(schedulePanel, "Please choose a date after today.");
                }
            }
        });
        JButton home = new JButton("Home");
        home.addActionListener(this);

        setTime.add(interviewDate);
        setTime.add(timeSlot);
        setTime.add(schedule);

        schedulePanel.add(scheduleInterviews, BorderLayout.WEST);
        schedulePanel.add(setTime, BorderLayout.CENTER);
        schedulePanel.add(home, BorderLayout.SOUTH);

        return schedulePanel;
    }

    private String[] getIdAndApplicants(ArrayList<Interview> interviews) {
        ArrayList<String> titles = new ArrayList<>();
        for (Interview interview : interviews) {
            titles.add(interview.getId() + "-" + interview.getApplicant().getLegalName());
        }

        return titles.toArray(new String[titles.size()]);
    }

    // For switching between cards
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) this.getLayout();
        JButton source = (JButton) e.getSource();

        switch (source.getText()) {
            case "Home":
                c.show(this, "HOME");
                break;
            case "View interviews":
                c.show(this, "VIEW");
                break;
            case "Schedule interview":
                c.show(this, "SCHEDULE");
                break;
            case "Logout":
                JobApplicationSystem.mainEnd();
                ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + source.getText());
        }
    }
}

