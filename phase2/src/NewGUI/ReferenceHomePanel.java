package NewGUI;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ReferenceHomePanel extends JPanel {

    private Reference reference;

    ReferenceHomePanel(Reference reference) {
        this.reference = reference;
    }

    JPanel createPanel() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        JPanel welcomePanel = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome " + this.reference.getEmail());
        welcomeMessage.setFont(new Font("Century Gothic", Font.BOLD, 20));
        welcomePanel.add(welcomeMessage, BorderLayout.NORTH);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.revalidate();
        homePanel.add(welcomeMessage);
        JPanel reminderMessagePanel = new JPanel();
        reminderMessagePanel.setLayout(new BoxLayout(reminderMessagePanel, BoxLayout.Y_AXIS));
        reminderMessagePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        JLabel reminderMessage = new JLabel("Reference letters that still need to be submitted: ");
        reminderMessagePanel.add(reminderMessage);
        homePanel.add(reminderMessagePanel);
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.Y_AXIS));
        reminderMessage.setHorizontalAlignment(JLabel.CENTER);
        reminderMessage.revalidate();
        reminderPanel.setBackground(Color.WHITE);
        reminderPanel.setOpaque(true);
        ArrayList<JobApplication> jobAppsThatNeedReferenceLetters = reference.getJobAppsForReference();
        for (JobApplication jobApp : jobAppsThatNeedReferenceLetters) {
            JLabel jobAppSmallMessage = new JLabel(jobApp.getMiniDescriptionForReference());
            jobAppSmallMessage.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            reminderPanel.add(jobAppSmallMessage);
            jobAppSmallMessage.setHorizontalAlignment(JLabel.CENTER);
            jobAppSmallMessage.revalidate();
        }
        homePanel.add(reminderPanel);
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return homePanel;
    }

}
