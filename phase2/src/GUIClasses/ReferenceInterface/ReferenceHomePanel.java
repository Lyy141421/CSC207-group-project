package GUIClasses.ReferenceInterface;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReferenceHomePanel extends JPanel {
    /**
     * The reference home panel.
     */

    // === Instance variables ===
    private ReferenceBackEnd referenceBackEnd;    // The reference who logged in

    // === Constructor ===
    ReferenceHomePanel(ReferenceBackEnd referenceBackEnd) {
        this.referenceBackEnd = referenceBackEnd;

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.createWelcomePanel(), c);
        c.gridy++;
        c.gridheight = 100;
        JPanel tablePanel = this.createReminderPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        this.add(tablePanel, c);
    }

    /**
     * Create the welcome panel to be displayed.
     *
     * @return the panel created.
     */
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome " + this.referenceBackEnd.getEmail());
        welcomeMessage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        welcomePanel.add(welcomeMessage);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.revalidate();
        return welcomePanel;
    }

    /**
     * Create the reminder panel to be displayed.
     *
     * @return the panel created.
     */
    private JPanel createReminderPanel() {
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.add(new GUIElementsCreator().createLabelPanel(
                "Reference letters that still need to be submitted: ", 20, true), BorderLayout.BEFORE_FIRST_LINE);
        reminderPanel.add(this.createJobAppTablePanel(), BorderLayout.CENTER);
        return reminderPanel;
    }

    /**
     * Create a panel that displays a table of job applications that still need reference letters submitted.
     *
     */
    private JPanel createJobAppTablePanel() {
        ArrayList<JobApplication> jobApps = this.referenceBackEnd.getJobAppsForReference();
        Object[][] data = new Object[jobApps.size()][];

        for (int i = 0; i < jobApps.size(); i++) {
            data[i] = jobApps.get(i).getCategoryValuesForReference();
        }

        return new GUIElementsCreator().createTablePanel(JobApplication.CATEGORY_NAMES_FOR_REFERENCE, data);
    }

}