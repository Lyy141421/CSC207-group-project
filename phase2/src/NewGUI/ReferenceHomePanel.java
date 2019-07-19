package NewGUI;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReferenceHomePanel extends JPanel {

    private Reference reference;

    ReferenceHomePanel(Reference reference) {
        this.reference = reference;
    }

    JPanel createPanel() {
        JPanel homePanel = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome " + reference.getEmail());
        homePanel.add(welcomeMessage, BorderLayout.NORTH);
        homePanel.add(this.createReferenceLetterReminder(), BorderLayout.CENTER);
        return homePanel;
    }

    private JPanel createReferenceLetterReminder() {
        JPanel reminders = new JPanel();
        JLabel reminderMessage = new JLabel("Reference letters that still need to be submitted: ");
        reminders.add(reminderMessage);
        ArrayList<JobApplication> jobAppsThatNeedReferenceLetters = reference.getJobAppsForReference();
        for (JobApplication jobApp : jobAppsThatNeedReferenceLetters) {
            JLabel jobAppSmallMessage = new JLabel(jobApp.getMiniDescriptionForReference());
            reminders.add(jobAppSmallMessage);
        }
        return reminders;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reference Home Page");
        Reference reference = new Reference("bob@gmail.com", LocalDate.now());
        JPanel homePanel = new ReferenceHomePanel(reference);
        frame.add(homePanel);
        frame.setSize(200, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
