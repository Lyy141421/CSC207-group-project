package GUIClasses;

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
        JPanel schedulePanel = new JPanel();

        return schedulePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
