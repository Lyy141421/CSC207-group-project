package GUIClasses;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HRPanel extends JPanel implements ActionListener {

    // Create interface for HR
    HRPanel () {
        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(browsePosting(), "POSTING");
        this.add(viewApplication(), "APPLICATION");
        this.add(searchApplicant(), "APPLICANT");
        this.add(addPosting(), "ADDPOSTING");
    }

    private JPanel home () {
        JPanel HomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");
        toDo.addActionListener(this);

        c.gridx = 0;
        c.gridy = 0;
        HomePanel.add(toDo, c);


        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));
        manual.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        JButton browsePosting = new JButton("Browse all job postings");
        browsePosting.addActionListener(this);
        JButton searchApplicant = new JButton("Search applicant");
        searchApplicant.addActionListener(this);
        JButton addPosting = new JButton("Add job posting");
        addPosting.addActionListener(this);

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
    private JPanel browsePosting () {
        JPanel postingPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());

        JComboBox<String> jobPostings = new JComboBox<>();
        JLabel status = new JLabel("Job posting status here. Changes according to JobPosting selected in JList.");
        status.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        JButton viewApplications = new JButton("View applications");
        viewApplications.addActionListener(this);
        JButton home = new JButton("Home");
        home.addActionListener(this);

        jobPostings.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.add(viewApplications);
        buttons.add(home);

        postingPanel.add(jobPostings, BorderLayout.NORTH);
        postingPanel.add(status, BorderLayout.CENTER);
        postingPanel.add(buttons, BorderLayout.SOUTH);

        return postingPanel;
    }

    private JPanel viewApplication () {
        JPanel applicationPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());

        JComboBox<String> app = new JComboBox<>();
        JList<String> viewable = new JList<>(new String[]{"abc", "xa"});
        JLabel info = new JLabel();
        JButton scheduleInterview = new JButton("Schedule");
        JButton hiring = new JButton("Hiring decision");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        buttons.add(scheduleInterview);
        buttons.add(hiring);
        buttons.add(home);

        applicationPanel.add(app, BorderLayout.NORTH);
        applicationPanel.add(viewable, BorderLayout.WEST);
        applicationPanel.add(info, BorderLayout.CENTER);
        applicationPanel.add(buttons, BorderLayout.SOUTH);

        return applicationPanel;
    }

    private JPanel searchApplicant () {
        JPanel applicantPanel = new JPanel();

        JLabel temporaryLabel = new JLabel("This is the addPosting Panel");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        applicantPanel.add(temporaryLabel);
        applicantPanel.add(home);

        return applicantPanel;
    }

    private JPanel addPosting () {
        JPanel addPostingPanel = new JPanel();

        JLabel temporaryLabel = new JLabel("This is the addPosting Panel");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        addPostingPanel.add(temporaryLabel);
        addPostingPanel.add(home);

        return addPostingPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) this.getLayout();
        JButton button = (JButton) e.getSource();

        if (button.getText().equals("Home")) {
            c.show(this, "HOME");
        } else if (button.getText().equals("Browse all job postings") || button.getText().equals("To-Do")) {
            c.show(this, "POSTING");
        } else if (button.getText().equals("Search applicant")) {
            c.show(this, "APPLICANT");
        } else if (button.getText().equals("Add job posting")) {
            c.show(this, "ADDPOSTING");
        } else if (button.getText().equals("View applications")) {
            c.show(this, "APPLICATION");
        }
    }
}
