package GUIClasses;


import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


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
        JTextArea status = new JTextArea("Job posting status here. Changes according to JobPosting selected in JList.");
        status.setEditable(false);
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
        JTextArea info = new JTextArea("Information");
        info.setEditable(false);
        JButton scheduleInterview = new JButton("Schedule");
        JButton hiring = new JButton("Hiring decision");
        JButton home = new JButton("Home");
        home.addActionListener(this);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(viewable), new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        buttons.add(scheduleInterview);
        buttons.add(hiring);
        buttons.add(home);

        applicationPanel.add(app, BorderLayout.NORTH);
        applicationPanel.add(display, BorderLayout.CENTER);
        applicationPanel.add(buttons, BorderLayout.SOUTH);

        return applicationPanel;
    }

    private JPanel searchApplicant () {
        JPanel applicantPanel = new JPanel(new BorderLayout());
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel applicantName = new JLabel("Applicant name");
        JTextField nameInput = new JTextField(30);
        JButton search = new JButton("Search");
        search.addActionListener(this);
        row.add(applicantName);
        row.add(nameInput);
        row.add(search);
        applicantPanel.add(row, BorderLayout.CENTER);

        JButton home = new JButton("Home");
        home.addActionListener(this);
        applicantPanel.add(home, BorderLayout.SOUTH);

        return applicantPanel;
    }

    private JPanel addPosting () {
        JPanel addPostingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel buttons = new JPanel(new FlowLayout());

        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1, 0, 1, 0);
        JLabel jobTitle = new JLabel("Job title");
        addPostingPanel.add(jobTitle, c);
        JLabel jobField = new JLabel("Job field");
        c.gridy++;
        addPostingPanel.add(jobField, c);
        JLabel jobDescription = new JLabel("Job description");
        c.gridy++;
        addPostingPanel.add(jobDescription, c);
        JLabel requirements = new JLabel("Requirements");
        c.gridy++;
        addPostingPanel.add(requirements, c);
        JLabel numPositions = new JLabel("Number of positions");
        c.gridy++;
        addPostingPanel.add(numPositions, c);
        // JLabel postDate = new JLabel("Date");
        JLabel closeDate = new JLabel("Close date");
        c.gridy++;
        addPostingPanel.add(closeDate, c);

        c.gridx = 1;
        c.gridy = 0;
        JTextField jobTitleInput = new JTextField(30);
        addPostingPanel.add(jobTitleInput, c);
        JTextField jobFieldInput = new JTextField(30);
        c.gridy++;
        addPostingPanel.add(jobFieldInput, c);
        JTextArea jobDescriptionInput = new JTextArea(4, 30);
        c.gridy++;
        addPostingPanel.add(jobDescriptionInput, c);
        JTextArea requirementsInput = new JTextArea(4, 30);
        c.gridy++;
        addPostingPanel.add(requirementsInput, c);
        JFormattedTextField numPositionsInput = new JFormattedTextField(formatter);
        numPositionsInput.setColumns(30);
        c.gridy++;
        addPostingPanel.add(numPositionsInput, c);
        // JTextField postDateInput = new JTextField();
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        c.gridy++;
        addPostingPanel.add(closeDateInput, c);

        JButton submit = new JButton("Submit");
        JButton home = new JButton("Home");
        home.addActionListener(this);
        buttons.add(submit);
        buttons.add(home);
        c.gridy++;
        addPostingPanel.add(buttons, c);

        return addPostingPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) this.getLayout();
        JButton button = (JButton) e.getSource();

        switch (button.getText()) {
            case "Home":
                c.show(this, "HOME");
                break;
            case "Browse all job postings":
            case "To-Do":
                c.show(this, "POSTING");
                break;
            case "Search applicant":
                c.show(this, "APPLICANT");
                break;
            case "Add job posting":
                c.show(this, "ADDPOSTING");
                break;
            case "View applications":
            case "Search":
                c.show(this, "APPLICATION");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + button.getText());
        }
    }

    // ==== Back end methods ====
    private void createJobPosting() {

    }
}
