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
        JPanel jobAppPanel = new JPanel();
        jobAppPanel.setLayout(new BoxLayout(jobAppPanel, BoxLayout.Y_AXIS));
        jobAppPanel.setBackground(Color.WHITE);
        ArrayList<JobApplication> jobAppsThatNeedReferenceLetters = reference.getJobAppsForReference();
        for (JobApplication jobApp : jobAppsThatNeedReferenceLetters) {
            JLabel jobAppLabel = new JLabel(jobApp.getMiniDescriptionForReference());
            jobAppLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            jobAppPanel.add(jobAppLabel);
            jobAppLabel.setHorizontalTextPosition(JLabel.CENTER);
            jobAppLabel.revalidate();
        }
//        reminderPanel.add(jobAppPanel);
        return reminderPanel;
    }

}
