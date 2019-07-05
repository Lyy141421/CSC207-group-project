package GUIClasses;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterviewerPanel extends JPanel implements ActionListener {

    InterviewerPanel () {
        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(viewInterviews(), "VIEW");
        this.add(reviewInterviews(), "REVIEW");
        this.add(scheduleInterviews(), "SCHEDULE");
    }

    private JPanel home () {
        JPanel homePanel = new JPanel();

        JButton view = new JButton("View scheduled interviews");
        JButton review = new JButton("Update interview result");
        JButton schedule = new JButton("Schedule interview");

        homePanel.add(view);
        homePanel.add(review);
        homePanel.add(schedule);

        return homePanel;
    }

    private JPanel viewInterviews () {
        JPanel viewPanel = new JPanel();

        JComboBox<String> interviews = new JComboBox<>();
        JList<String> viewable = new JList<>(new String[]{"abc", "xa"});
        JTextArea info = new JTextArea("Information");
        info.setEditable(false);
        // Phase 2?
        // JButton reschedule = new JButton("Re-schedule");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(viewable), new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        viewPanel.add(interviews, BorderLayout.NORTH);
        viewPanel.add(display, BorderLayout.CENTER);
        viewPanel.add(home, BorderLayout.SOUTH);

        return viewPanel;
    }

    private JPanel reviewInterviews () {
        JPanel reviewPanel = new JPanel();



        return reviewPanel;
    }

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

        setTime.add(timeSlot);
        setTime.add(closeDateInput);
        setTime.add(schedule);

        schedulePanel.add(interviews);
        schedulePanel.add(setTime);

        return schedulePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
