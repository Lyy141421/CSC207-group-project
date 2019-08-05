package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;
import GUIClasses.CommonUserGUI.DocumentSelector;
import GUIClasses.CommonUserGUI.RemoveFileButtonsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ApplicantFileSubmissionFromAccount extends JPanel {

    private JPanel thisPanel = this;
    private JPanel masterPanel;
    private ApplicantBackend applicantBackend;
    private JobApplication jobApp;
    private DocumentSelector documentSelector;

    ApplicantFileSubmissionFromAccount(JPanel masterPanel, ApplicantBackend applicantBackend, BranchJobPosting jobPosting) {
        this.masterPanel = masterPanel;
        this.applicantBackend = applicantBackend;
        this.jobApp = applicantBackend.createJobApplication(jobPosting);
        this.setLayout(null);
        this.add(this.createTitle());
        this.setFullDocumentSelectorPanel();

        this.createSubmitButton();
        this.add(this.createReturnButtonPanel(masterPanel));
    }

    private JLabel createTitle() {
        JLabel titleText = new JLabel("Document Submission");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setBounds(327, 20, 200, 40);
        return titleText;
    }

    private void setFullDocumentSelectorPanel() {
        documentSelector = new DocumentSelector(applicantBackend.getApplicantFolder());
        documentSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        documentSelector.setBounds(100, 100, 250, 300);
        this.add(documentSelector);
        RemoveFileButtonsPanel removeFileButtons = documentSelector.getRemoveFileButtonsPanel();
        removeFileButtons.setAlignmentY(Component.CENTER_ALIGNMENT);
        removeFileButtons.setBounds(504, 100, 250, 300);
        this.add(removeFileButtons);
    }

    private void createSubmitButton() {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (documentSelector.getFilesToSubmit().isEmpty()) {
                    JOptionPane.showMessageDialog(thisPanel, "You must select a file to submit");
                } else {
                    new SubmitDocumentsActionListener(masterPanel, applicantBackend.getApplicant(), jobApp,
                            documentSelector.getFilesToSubmit()).actionPerformed(e);
                }
            }
        });
        submitButton.setBounds(380, 370, 100, 30);
        this.add(submitButton);
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
        returnPanel.setBounds(20, 20, 80, 40);
        return returnPanel;
    }
}
