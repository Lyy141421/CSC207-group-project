package GUIClasses;

import UsersAndJobObjects.Interview;
import UsersAndJobObjects.JobApplication;
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

        ArrayList<Interview> allInterviews = deepClone(this.futureInterviews);
        allInterviews.addAll(deepClone(this.pastInterviews));
        JComboBox<String> interviews = new JComboBox<>((String[]) getInterviewTitles(allInterviews).toArray());
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

        interviews.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    info.setText(getInfo(allInterviews.get(interviews.getSelectedIndex()), viewable.getSelectedIndex()));
                }
            }
        });

        viewable.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                info.setText(getInfo(allInterviews.get(interviews.getSelectedIndex()), e.getFirstIndex()));
            }
        });

        viewPanel.add(interviews, BorderLayout.NORTH);
        viewPanel.add(display, BorderLayout.CENTER);
        viewPanel.add(home, BorderLayout.SOUTH);

        return viewPanel;
    }

    private ArrayList<Interview> deepClone (ArrayList<Interview> interviews) {
        ArrayList<Interview> interviewsClone = new ArrayList<>();
        for (Interview interview : interviews) {
            interviewsClone.add(interview);
        }

        return interviewsClone;
    }

    private ArrayList<String> getInterviewTitles(ArrayList<Interview> interviews) {
        ArrayList<String> titles = new ArrayList<>();
        for (Interview interview: interviews) {
            titles.add(interview.getId()+"-"+interview.getTime().toString()+" "+
                    interview.getApplicant().getLegalName());
        }

        return titles;
    }

    private String getInfo (Interview interview, int attributeIndex) {
        String info;

        switch (attributeIndex) {
            case 0:
                info = interview.getOverview();
                break;
            case 1:
                info = interview.getInterviewNotes();
                break;
            case 2:
                info = interview.getJobApplication().getCV().getContents();
                break;
            case 3:
                info = interview.getJobApplication().getCoverLetter().getContents();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeIndex);
        }

        return info;
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

