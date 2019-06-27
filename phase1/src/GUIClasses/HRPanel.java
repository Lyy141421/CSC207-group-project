package GUIClasses;

import javax.swing.*;
import java.awt.*;

public class HRPanel extends JPanel {
    // Create interface for HR
    HRPanel () {
        setLayout(new CardLayout());
        add(HRHome());
        add(HRBrowsePosting());
    }

    private JPanel HRHome () {
        JPanel HomePanel = new JPanel();
        HomePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");

        c.gridx = 0;
        c.gridy = 0;
        HomePanel.add(toDo, c);


        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));
        manual.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        JButton browsePosting = new JButton("Browse all job postings");
        JButton searchApplicant = new JButton("Search applicant");
        JButton addPosting = new JButton("Add job posting");

        browsePosting.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchApplicant.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPosting.setAlignmentX(Component.CENTER_ALIGNMENT);

        manual.add(addPosting);
        manual.add(browsePosting);
        manual.add(searchApplicant);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        HomePanel.add(manual, c);

        JButton logout = new JButton("Logout");

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(20, 100, 0, 0);
        HomePanel.add(logout, c);

        return HomePanel;
    }

    // Need to pass in list of job postings (all for browse all or particular for to-do)
    private JPanel HRBrowsePosting () {
        JPanel postingPanel = new JPanel();
        postingPanel.setLayout(new BoxLayout(postingPanel, BoxLayout.PAGE_AXIS));
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JList<String> jobPostings = new JList();
        JLabel status = new JLabel("Job posting status here. Changes according to JobPosting selected in JList.");
        JButton scheduleInterview = new JButton("Schedule");
        JButton hiring = new JButton("Hiring decision");
        JButton home = new JButton("Home");

        jobPostings.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        scheduleInterview.setAlignmentX(Component.CENTER_ALIGNMENT);
        hiring.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.add(scheduleInterview);
        buttons.add(hiring);
        buttons.add(home);

        postingPanel.add(jobPostings);
        postingPanel.add(status);
        postingPanel.add(buttons);

        return postingPanel;
    }
}
