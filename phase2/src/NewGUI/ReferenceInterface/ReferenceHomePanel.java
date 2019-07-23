package NewGUI.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import NewGUI.FrequentlyUsedMethods;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReferenceHomePanel extends JPanel {

    private Reference reference;

    ReferenceHomePanel(Reference reference) {
        this.reference = reference;

        this.setLayout(new BorderLayout());
        this.add(this.createWelcomePanel(), BorderLayout.BEFORE_FIRST_LINE);
        this.add(this.createReminderPanel(), BorderLayout.CENTER);
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

    private JPanel createReminderPanel() {
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.add(new FrequentlyUsedMethods().createTitlePanel(
                "Reference letters that still need to be submitted: ", 20), BorderLayout.BEFORE_FIRST_LINE);
        reminderPanel.add(this.createJobAppTablePanel(), BorderLayout.CENTER);
        return reminderPanel;
    }

    /**
     * Create a panel that displays a table of job applications that still need reference letters submitted.
     *
     * https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
     */
    private JPanel createJobAppTablePanel() {
        JPanel jobAppTablePanel = new JPanel(new GridLayout(1, 0));

        ArrayList<JobApplication> jobApps = this.reference.getJobAppsForReference();
        Object[][] data = new Object[jobApps.size()][];

        for (int i = 0; i < jobApps.size(); i++) {
            data[i] = jobApps.get(i).getCategoryValuesForReference();
        }

        JTable jobAppTable = new JTable(data, JobApplication.categoryNamesForReference());
        jobAppTable.setCellSelectionEnabled(false);
        jobAppTable.setEnabled(false);
        jobAppTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        jobAppTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(jobAppTable);
        jobAppTablePanel.add(scrollPane);
        return jobAppTablePanel;
    }

}
