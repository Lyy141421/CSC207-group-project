package GUIClasses.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import GUIClasses.CommonUserGUI.TitleCreator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReferenceHomePanel extends JPanel {

    private Reference reference;

    ReferenceHomePanel(Reference reference) {
        this.reference = reference;

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.createWelcomePanel(), c);
        c.gridy++;
        c.gridheight = 100;
        this.add(this.createReminderPanel(), c);
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
        reminderPanel.add(new TitleCreator().createTitlePanel(
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
        JPanel jobAppTablePanel = new JPanel(new GridLayout());

        ArrayList<JobApplication> jobApps = this.reference.getJobAppsForReference();
        Object[][] data = new Object[jobApps.size()][];

        for (int i = 0; i < jobApps.size(); i++) {
            data[i] = jobApps.get(i).getCategoryValuesForReference();
        }

        JTable jobAppTable = new JTable(data, JobApplication.categoryNamesForReference());
        jobAppTable.setCellSelectionEnabled(false);
        jobAppTable.setEnabled(false);
        jobAppTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(jobAppTable);
        jobAppTablePanel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        return jobAppTablePanel;
    }

}
