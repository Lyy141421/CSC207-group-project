package NewGUI;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReferenceHomePanel extends JPanel {

    private Reference reference;

    ReferenceHomePanel(Reference reference) {
        super(new BorderLayout());
        this.reference = reference;
        this.add(this.createWelcomePanel(), BorderLayout.BEFORE_FIRST_LINE);
        this.revalidate();
        this.add(this.createReminderPanel(), BorderLayout.CENTER);
        this.revalidate();
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

    private JPanel createReminderPanel() {
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.add(this.createReminderMessagePanel(), BorderLayout.BEFORE_FIRST_LINE);
        reminderPanel.add(this.createJobAppTablePanel(), BorderLayout.CENTER);
        return reminderPanel;
    }

    /**
     * Create a panel that displays a table of job applications that still need reference letters submitted.
     *
     * @return a panel with the table
     * https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
     */
    private JPanel createJobAppTablePanel() {
        JPanel tablePanel = new JPanel(new GridLayout(1, 0));

        String[] columnNames = JobApplication.categoryNamesForReference();

        ArrayList<JobApplication> jobApps = this.reference.getJobAppsForReference();
        Object[][] data = new Object[jobApps.size()][];

        for (int i = 0; i < jobApps.size(); i++) {
            data[i] = jobApps.get(i).getCategoryValuesForReference();
        }

        JTable table = new JTable(data, columnNames);
        table.setCellSelectionEnabled(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        tablePanel.add(scrollPane);
        return tablePanel;
    }

}
