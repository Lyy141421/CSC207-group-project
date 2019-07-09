package GUIClasses;


import Main.JobApplicationSystem;
import UsersAndJobObjects.JobApplication;
import UsersAndJobObjects.JobPosting;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


public class HRPanel extends JPanel implements ActionListener {

    private Container contentPane;
    private HRCoordinatorInterface HRInterface;
    private LocalDate today;

    private ArrayList<JobPosting> prePhoneJP;
    private ArrayList<JobPosting> scheduleJP;
    private ArrayList<JobPosting> hiringJP;
    private ArrayList<JobPosting> importantJP = new ArrayList<>();
    private ArrayList<JobPosting> allJP;

    private JComboBox<String> jobPostings = new JComboBox<>();
    private DefaultComboBoxModel<String> allTitles;
    private DefaultComboBoxModel<String> importantTitles;
    private ArrayList<JobPosting> currJPs;

    private JComboBox<String> applications = new JComboBox<>();
    private DefaultComboBoxModel<String> appTitles = new DefaultComboBoxModel<>();
    private ArrayList<JobApplication> currApps;
    private JPanel hireOrRejectButtons = new JPanel();
    private JPanel phoneOrNotButtons = new JPanel();

    // Create interface for HR
    HRPanel (Container contentPane, HRCoordinatorInterface HRInterface, LocalDate today) {
        this.contentPane = contentPane;
        this.HRInterface = HRInterface;
        this.today = today;
        ArrayList<ArrayList<JobPosting>> HRInfoList = HRInterface.getHighPriorityAndAllJobPostings(today);
        this.prePhoneJP = HRInfoList.get(0);
        this.scheduleJP = HRInfoList.get(1);
        this.hiringJP = HRInfoList.get(2);
        this.importantJP.addAll(this.prePhoneJP);
        this.importantJP.addAll(this.scheduleJP);
        this.importantJP.addAll(this.hiringJP);
        this.allJP = HRInfoList.get(3);

        this.allTitles = new DefaultComboBoxModel<>(getJPTitles(allJP));
        this.importantTitles = new DefaultComboBoxModel<>(getJPTitles(importantJP));

        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(browsePosting(), "POSTING");
        this.add(viewApplication(), "APPLICATION");
        this.add(searchApplicant(), "APPLICANT");
        this.add(addPosting(), "ADDPOSTING");
    }

    // Construct HomePanel. Static besides number of things in To-Do
    private JPanel home () {
        JPanel homePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");
        toDo.addActionListener(this);

        c.gridx = 0;
        c.gridy = 0;
        homePanel.add(toDo, c);


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
        homePanel.add(manual, c);

        JButton logout = new JButton("Logout");
        logout.addActionListener(this);

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(20, 100, 0, 0);
        homePanel.add(logout, c);

        return homePanel;
    }


    // ====browsePosting methods====

    // Need to pass in list of job postings (all for browse all or particular for to-do)
    private JPanel browsePosting () {
        JPanel postingPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());

        // could change

        JTextArea info = new JTextArea("Job posting status here. Changes according to JobPosting selected in JComboBox");
        info.setEditable(false);
        info.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        JButton viewApplications = new JButton("View applications");
        JButton scheduleInterview = new JButton("Schedule");
        scheduleInterview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jobPostings.getSelectedIndex();
                JobPosting selectedJP = currJPs.get(selectedIndex);
                boolean success = HRInterface.setUpInterviews(selectedJP);
                if (success) {
                    scheduleJP.remove(selectedJP);
                    importantJP.remove(selectedJP);
                    importantTitles.removeElementAt(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(postingPanel, "Interviews cannot be scheduled at this time.");
                }
            }
        });
        viewApplications.addActionListener(this);
        JButton home = new JButton("Home");
        home.addActionListener(this);

        jobPostings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = jobPostings.getSelectedIndex();
                    JobPosting selectedJP = currJPs.get(selectedIndex);
                    info.setText(getStatus(selectedJP) + selectedJP.toString());
                    if (scheduleJP.contains(selectedJP)) {
                        scheduleInterview.setEnabled(true);
                    } else {
                        scheduleInterview.setEnabled(false);
                    }
                }
            }
        });

        jobPostings.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.add(viewApplications);
        buttons.add(scheduleInterview);
        buttons.add(home);

        postingPanel.add(jobPostings, BorderLayout.NORTH);
        postingPanel.add(info, BorderLayout.CENTER);
        postingPanel.add(buttons, BorderLayout.SOUTH);

        return postingPanel;
    }

    private String[] getJPTitles (ArrayList<JobPosting> JPToShow) {
        ArrayList<String> titles = new ArrayList<>();
        for (JobPosting JP : JPToShow) {
            titles.add(JP.getId() + "-" + JP.getTitle());
        }
        return titles.toArray(new String[titles.size()]);
    }

    private String getStatus (JobPosting selectedJP) {
        String status;
        if (prePhoneJP.contains(selectedJP)) {
            status = "Important: Select applicants for phone interview.\n\n";
        } else if (scheduleJP.contains(selectedJP)) {
            status = "Important: Schedule interviews for the next round.\n\n";
        } else if (hiringJP.contains(selectedJP)) {
            status = "Important: Make hiring decisions for final candidates.\n\n";
        } else {
            status = "Low priority.\n\n";
        }

        return status;
    }


    // ====Application panel methods====

    private JPanel viewApplication () {
        JPanel applicationPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());
        hireOrRejectButtons.setLayout(new BoxLayout(hireOrRejectButtons, BoxLayout.Y_AXIS));

        applications.setModel(this.appTitles);
        String[] attributes = new String[]{"Overview", "CV", "Cover letter"};
        JList<String> viewable = new JList<>(attributes);
        JTextArea info = new JTextArea();
        info.setEditable(false);

        JRadioButton hire = new JRadioButton("Hire");
        hire.setSelected(true);
        JRadioButton reject = new JRadioButton("Reject");
        ButtonGroup hireOrReject = new ButtonGroup();
        hireOrReject.add(hire);
        hireOrReject.add(reject);
        hireOrRejectButtons.add(hire);
        hireOrRejectButtons.add(reject);
        JButton confirm1 = new JButton("Confirm");
        hireOrRejectButtons.add(confirm1);
        confirm1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = applications.getSelectedIndex();
                JobApplication selectedApp = currApps.get(selectedIndex);
                boolean filled = HRInterface.hireOrRejectApplication(selectedApp, hire.isSelected());
                if (filled) {
                    appTitles.removeAllElements();
                    hiringJP.remove(selectedApp.getJobPosting());
                    importantJP.remove(selectedApp.getJobPosting());
                    for (JobApplication jobApp : currApps) {
                        if (!(HRInterface.isRejected(jobApp)||jobApp.isHired())) {
                            HRInterface.hireOrRejectApplication(jobApp, false);
                        }
                    }
                    JOptionPane.showMessageDialog(applicationPanel, "All positions for this job has been filled");
                }
            }
        });

        phoneOrNotButtons.setLayout(new BoxLayout(phoneOrNotButtons, BoxLayout.Y_AXIS));
        JRadioButton phone = new JRadioButton("Select for phone Interview");
        phone.setSelected(true);
        JRadioButton noPhone = new JRadioButton("Reject");
        ButtonGroup phoneOrNot = new ButtonGroup();
        phoneOrNot.add(phone);
        phoneOrNot.add(noPhone);
        phoneOrNotButtons.add(phone);
        phoneOrNotButtons.add(noPhone);
        JButton confirm2 = new JButton("Confirm");
        phoneOrNotButtons.add(confirm2);
        confirm2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = applications.getSelectedIndex();
                JobApplication selectedApp = currApps.get(selectedIndex);
                HRInterface.selectApplicationForPhoneInterview(selectedApp, phone.isSelected());
                if (isPhoneSetup()) {
                    appTitles.removeAllElements();
                    prePhoneJP.remove(selectedApp.getJobPosting());
                    scheduleJP.add(selectedApp.getJobPosting());
                    JOptionPane.showMessageDialog(applicationPanel, "All applications have been processed. Please proceed to schedule interviews.");
                }
            }
        });

        JButton home = new JButton("Home");
        home.addActionListener(this);

        JSplitPane display = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(viewable), new JScrollPane(info));
        display.setDividerLocation(250);

        viewable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        viewable.setLayoutOrientation(JList.VERTICAL);

        applications.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JobApplication selectedApp = currApps.get(applications.getSelectedIndex());
                    info.setText(getInfo(selectedApp, viewable.getSelectedIndex()));
                    if (selectedApp.isInPerson3() && !HRInterface.isRejected(selectedApp)) {
                        hireOrRejectButtons.setVisible(true);
                        phoneOrNotButtons.setVisible(false);
                    } else if (selectedApp.isUnderReview() && !HRInterface.isRejected(selectedApp)) {
                        hireOrRejectButtons.setVisible(false);
                        phoneOrNotButtons.setVisible(true);
                    } else {
                        hireOrRejectButtons.setVisible(false);
                        phoneOrNotButtons.setVisible(false);
                    }
                }
            }
        });

        viewable.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JobApplication selectedApp = currApps.get(applications.getSelectedIndex());
                info.setText(getInfo(selectedApp, e.getFirstIndex()));
            }
        });

        buttons.add(hireOrRejectButtons);
        buttons.add(phoneOrNotButtons);
        buttons.add(home);

        applicationPanel.add(applications, BorderLayout.NORTH);
        applicationPanel.add(display, BorderLayout.CENTER);
        applicationPanel.add(buttons, BorderLayout.SOUTH);

        return applicationPanel;
    }

    private boolean isPhoneSetup() {
        boolean isSetup = true;
        for (JobApplication jobApps : currApps) {
            if (!(jobApps.isOnPhoneInterview()||HRInterface.isRejected(jobApps))) {
                isSetup = false;
            }
        }
        return isSetup;
    }

    private ArrayList<String> getAppTitles (ArrayList<JobApplication> appsToShow) {
        ArrayList<String> titles = new ArrayList<>();
        for (JobApplication app : appsToShow) {
            titles.add(app.getId() + "-" + app.getApplicant().getLegalName());
        }
        return titles;
    }

    private String getInfo (JobApplication app, int attributeIndex) {
        String info;

        switch (attributeIndex) {
            case 0:
                info = app.getOverview();
                break;
            case 1:
                info = app.getCV().getContents();
                break;
            case 2:
                info = app.getCoverLetter().getContents();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeIndex);
        }

        return info;
    }


    // ====Search panel====

    private JPanel searchApplicant () {
        JPanel applicantPanel = new JPanel(new BorderLayout());
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel applicantName = new JLabel("Applicant username");
        JTextField nameInput = new JTextField(30);
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JobApplication> apps = HRInterface.getAllJobApplicationsToCompany(nameInput.getText());
                if (apps.isEmpty()) {
                    JOptionPane.showMessageDialog(applicantPanel, "The applicant cannot be found.");
                } else {
                    currApps = apps;
                    appTitles.removeAllElements();
                    addListToModel(appTitles, getAppTitles(apps));
                    //todo: might cause issue
                    hireOrRejectButtons.setVisible(false);
                    ((CardLayout) getLayout()).show(getParent(), "APPLICATION");
                }
            }
        });
        row.add(applicantName);
        row.add(nameInput);
        row.add(search);
        applicantPanel.add(row, BorderLayout.CENTER);

        JButton home = new JButton("Home");
        home.addActionListener(this);
        applicantPanel.add(home, BorderLayout.SOUTH);

        return applicantPanel;
    }

    private void addListToModel(DefaultComboBoxModel<String> model, ArrayList<String> appTitles) {
        for (String title : appTitles) {
            model.addElement(title);
        }
    }


    // ====Add posting panel methods====

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
        dateModel.setDate(today.getYear(), today.getMonthValue()-1, today.getDayOfMonth());
        dateModel.setSelected(true);
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
        numPositionsInput.setValue(1);
        // Not changable in Phase1. Will remove this in Phase2.
        numPositionsInput.setEditable(false);
        numPositionsInput.setColumns(30);
        c.gridy++;
        addPostingPanel.add(numPositionsInput, c);
        JDatePickerImpl closeDateInput = new JDatePickerImpl(datePanel);
        c.gridy++;
        addPostingPanel.add(closeDateInput, c);

        JButton submit = new JButton("Submit");
        JButton home = new JButton("Home");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] postingFields = new Object[] {jobTitleInput.getText(), jobFieldInput.getText(),
                        jobDescriptionInput.getText(), requirementsInput.getText(), numPositionsInput.getValue(),
                        ((Date) closeDateInput.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()};
                if (isValidInput(postingFields)) {
                    HRInterface.addJobPosting(today, postingFields);
                } else {
                    JOptionPane.showMessageDialog(addPostingPanel, "One or more fields have illegal input.");
                }
            }
        });
        home.addActionListener(this);
        buttons.add(submit);
        buttons.add(home);
        c.gridy++;
        addPostingPanel.add(buttons, c);

        return addPostingPanel;
    }


    // ====Switch panel button ActionListener methods====

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) this.getLayout();
        JButton button = (JButton) e.getSource();

        switch (button.getText()) {
            case "Home":
                c.show(this, "HOME");
                break;
            case "Browse all job postings":
                this.currJPs = this.allJP;
                this.jobPostings.setModel(this.allTitles);
                c.show(this, "POSTING");
                break;
            case "To-Do":
                this.currJPs = this.importantJP;
                this.jobPostings.setModel(this.importantTitles);
                c.show(this, "POSTING");
                break;
            case "Search applicant":
                c.show(this, "APPLICANT");
                break;
            case "Add job posting":
                c.show(this, "ADDPOSTING");
                break;
            case "View applications":
                int selectedIndex = this.jobPostings.getSelectedIndex();
                JobPosting selectedJP = currJPs.get(selectedIndex);
                this.currApps = selectedJP.getJobApplications();
                this.appTitles.removeAllElements();
                addListToModel(appTitles, getAppTitles(currApps));
                c.show(this, "APPLICATION");
                break;
            case "Logout":
                ((CardLayout) this.contentPane.getLayout()).show(contentPane, "LOGIN");
                JobApplicationSystem.mainEnd();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + button.getText());
        }
    }

    // ==== Back end methods ====
    private boolean isValidInput (Object[] fields) {
        boolean valid = true;

        int i = 0;
        while (valid && i<4) {
            if (fields[i].equals("")) {
                valid = false;
            }
            i++;
        }

        if (((LocalDate) fields[5]).isBefore(today)) {
            valid = false;
        }

        return valid;
    }
}
