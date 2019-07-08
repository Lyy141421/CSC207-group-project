package GUIClasses;

import UsersAndJobObjects.Interview;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;


public class InterviewerPanel extends JPanel implements ActionListener, ItemListener {

    InterviewerInterface interviewerInterface;
    LocalDate today;
    ArrayList<Interview> pastInterviews;
    ArrayList<Interview> futureInterviews;

    InterviewerPanel (InterviewerInterface interviewerInterface, LocalDate today) {
        this.interviewerInterface = interviewerInterface;
        this.today = today;
        ArrayList<ArrayList<Interview>> interviews = interviewerInterface.getInterviewsBeforeOnAndAfterToday(today);
        this.pastInterviews = interviews.get(0);
        this.futureInterviews = interviews.get(1);
        this.futureInterviews.addAll(interviews.get(2));

        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(viewInterviews(), "VIEW");
        this.add(scheduleInterviews(), "SCHEDULE");
    }

    private JPanel home () {
        JPanel homePanel = new JPanel();

        JButton view = new JButton("View interviews");
        view.addActionListener(this);
        JButton schedule = new JButton("Schedule interview");
        schedule.addActionListener(this);

        homePanel.add(view);
        homePanel.add(schedule);

        return homePanel;
    }

    private JPanel viewInterviews () {
        JPanel viewPanel = new JPanel(new BorderLayout());
        JPanel select = new JPanel();
        select.setLayout(new BoxLayout(select, BoxLayout.Y_AXIS));

        ArrayList<String> allInterviewTitles = getInterviewTitles(this.futureInterviews);
        allInterviewTitles.addAll(getInterviewTitles(this.pastInterviews));
        JComboBox<String> interviews = new JComboBox<>((String[]) allInterviewTitles.toArray());

        JCheckBox incomplete = new JCheckBox("Incomplete", true);
        incomplete.addItemListener(this);
        JCheckBox complete = new JCheckBox("Complete", true);
        complete.addItemListener(this);
        JList<String> viewable = new JList<>(new String[]{"Overview", "Notes", "CV", "Cover letter"});

        select.add(incomplete);
        select.add(complete);
        select.add(new JScrollPane(viewable));

        JTextArea info = new JTextArea("Information");
        info.setEditable(false);
        // Phase 2?
        // JButton reschedule = new JButton("Re-schedule");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, select, new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        viewPanel.add(interviews, BorderLayout.NORTH);
        viewPanel.add(display, BorderLayout.CENTER);
        viewPanel.add(home, BorderLayout.SOUTH);

        return viewPanel;
    }

    private ArrayList<String> getInterviewTitles(ArrayList<Interview> interviews) {
        ArrayList<String> titles = new ArrayList<>();
        for (Interview interview: interviews) {
            titles.add(interview.getId()+"-"+interview.getTime().toString()+" "+
                    interview.getApplicant().getLegalName());
        }

        return titles;
    }


    // =========

    private JPanel scheduleInterviews () {
        JPanel schedulePanel = new JPanel(new BorderLayout());
        JPanel setTime = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JList<String> interviews = new JList<>(new String[]{"Posting1-001 app1", "Posting3-003 app2"});
        interviews.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        interviews.setLayoutOrientation(JList.VERTICAL);
        JComboBox<String> timeSlot = new JComboBox<>(new String[]{"9-10 am", "10-11 am", "1-2 pm",
                "2-3 pm", "3-4 pm", "4-5 pm"});
        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        JButton schedule = new JButton("Confirm");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        setTime.add(closeDateInput);
        setTime.add(timeSlot);
        setTime.add(schedule);

        schedulePanel.add(interviews, BorderLayout.WEST);
        schedulePanel.add(setTime, BorderLayout.CENTER);
        schedulePanel.add(home, BorderLayout.SOUTH);

        return schedulePanel;
    }

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
            case "Confirm":

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + source.getText());
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox source = (JCheckBox) e.getItemSelectable();

        switch (source.getText()) {
            case "Incomplete":
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Show scheduled interviews
                }else {
                    // Hide scheduled interviews
                }
                break;
            case "Complete":
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Show conducted interviews
                }else {
                    // Hide conducted interviews
                }
                break;
        }
    }
}

