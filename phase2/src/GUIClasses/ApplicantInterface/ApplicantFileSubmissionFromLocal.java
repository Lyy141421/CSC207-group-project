package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ApplicantFileSubmissionFromLocal extends JPanel {

    ApplicantFileSubmissionFromLocal(JPanel masterPanel, ApplicantBackend applicantBackend, BranchJobPosting jobPosting) {
        JobApplication jobApp = applicantBackend.createJobApplication(jobPosting);
        this.setLayout(null);
        this.add(this.createTitle());
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        FileChooser fileChooser = new FileChooser(masterPanel, applicantBackend.getApplicant(), jobApp);
        fileChooser.enableUploadButton();
        fileChooser.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
        fileChooser.setBounds(0, 120, 854, 300);
        this.add(fileChooser);
        this.add(this.createReturnButtonPanel(masterPanel));
        this.add(this.createAddReferencesButton(masterPanel, applicantBackend, jobApp));
    }

    private JLabel createTitle() {
        JLabel titleText = new JLabel("Document Submission");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleText.setBounds(327, 20, 200, 35);
        return titleText;
    }

    private JPanel createReturnButtonPanel(JPanel masterPanel) {
        JPanel returnPanel = new JPanel();
        JButton returnButton = new JButton("Back");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) masterPanel.getLayout()).previous(masterPanel);
            }
        });
        returnPanel.add(returnButton);
        returnPanel.setBounds(20, 20, 80, 50);
        return returnPanel;
    }

    private JButton createAddReferencesButton(JPanel masterPanel, ApplicantBackend applicantBackend, JobApplication jobApp) {
        JButton referencesButton = new JButton("Add References");
        referencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(masterPanel);
                new AddReferencesDialog(frame, applicantBackend, jobApp);
            }
        });
        referencesButton.setBounds(350, 80, 150, 30);
        return referencesButton;
    }
}
