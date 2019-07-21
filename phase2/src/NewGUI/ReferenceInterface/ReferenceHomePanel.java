package NewGUI.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReferenceHomePanel extends JPanel {

    private Reference reference;
    private JPanel reminderPanel;
    private JPanel jobAppTablePanel;
    private String[] columnNames = JobApplication.categoryNamesForReference();

    ReferenceHomePanel(Reference reference) {
        super(new BorderLayout());
        this.reference = reference;
        this.add(this.createWelcomePanel(), BorderLayout.BEFORE_FIRST_LINE);
        this.createReminderPanel();
        this.add(reminderPanel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome " + this.reference.getEmail());
        welcomeMessage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        welcomePanel.add(welcomeMessage);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.revalidate();
        return welcomePanel;
    }

    private JPanel createReminderMessagePanel() {
        JPanel reminderMessagePanel = new JPanel();
        reminderMessagePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel reminderMessage = new JLabel("Reference letters that still need to be submitted: ");
        reminderMessagePanel.add(reminderMessage);
        reminderMessage.setHorizontalAlignment(JLabel.CENTER);
        reminderMessage.revalidate();
        return reminderMessagePanel;
    }

    private void createReminderPanel() {
        reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.add(this.createReminderMessagePanel(), BorderLayout.BEFORE_FIRST_LINE);
        this.createJobAppTablePanel();
        reminderPanel.add(jobAppTablePanel, BorderLayout.CENTER);
    }

    /**
     * Update the table contents.
     */
    void updateTable() {
        reminderPanel.remove(jobAppTablePanel);
        this.createJobAppTablePanel();
        reminderPanel.add(jobAppTablePanel, BorderLayout.CENTER);
        reminderPanel.revalidate();
    }

    /**
     * Create a panel that displays a table of job applications that still need reference letters submitted.
     *
     * https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
     */
    private void createJobAppTablePanel() {
        jobAppTablePanel = new JPanel(new GridLayout(1, 0));

        ArrayList<JobApplication> jobApps = this.reference.getJobAppsForReference();
        Object[][] data = new Object[jobApps.size()][];

        for (int i = 0; i < jobApps.size(); i++) {
            data[i] = jobApps.get(i).getCategoryValuesForReference();
        }

        JTable jobAppTable = new JTable(data, columnNames);
        jobAppTable.setCellSelectionEnabled(false);
        jobAppTable.setEnabled(false);
        jobAppTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        jobAppTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(jobAppTable);
        jobAppTablePanel.add(scrollPane);
    }

}
