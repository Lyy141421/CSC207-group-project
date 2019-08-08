package GUIClasses.ApplicantInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;
import GUIClasses.CommonUserGUI.DocumentSelector;
import GUIClasses.CommonUserGUI.RemoveFileButtonsPanel;
import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

class ApplicantFileSubmissionFromAccount extends JPanel {

    private JPanel thisPanel = this;
    private JPanel masterPanel;
    private ApplicantBackend applicantBackend;
    private JobApplication jobApp;
    private BranchJobPosting jobPosting;
    private DocumentSelector documentSelector;
    private JButton returnButton;

    ApplicantFileSubmissionFromAccount(JPanel masterPanel, ApplicantBackend applicantBackend, BranchJobPosting jobPosting) {
        this.masterPanel = masterPanel;
        this.applicantBackend = applicantBackend;
        this.jobPosting = jobPosting;
        this.setLayout(null);
        this.add(this.createTitle());
        this.setFullDocumentSelectorPanel();

        this.createAddReferencesButton();
        this.createSubmitButton();
        this.add(this.createReturnButtonPanel(masterPanel));
    }

    private JLabel createTitle() {
        JLabel titleText = new JLabel("Document Submission", SwingConstants.CENTER);
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setBounds(227, 20, 400, 40);
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
                    if (jobApp == null) {
                        jobApp = applicantBackend.createJobApplication(jobPosting);
                    }
                    new SubmitDocumentsActionListener(masterPanel, applicantBackend.getApplicant(), jobApp,
                            documentSelector.getFilesToSubmit()).actionPerformed(e);
                    returnButton.setEnabled(true);
                    ((UserMain) masterPanel).refresh();
                }
            }
        });
        submitButton.setBounds(470, 400, 100, 30);
        this.add(submitButton);
    }

    private JPanel createReturnButtonPanel(JPanel masterPanel) {
        JPanel returnPanel = new JPanel();
        returnButton = new JButton("Back");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) masterPanel.getLayout()).show(masterPanel, "SearchResults");
            }
        });
        returnPanel.add(returnButton);
        returnPanel.setBounds(20, 20, 80, 40);
        return returnPanel;
    }

    private void createAddReferencesButton() {
        JButton referencesButton = new JButton("Add References");
        referencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.windowForComponent(masterPanel);
                if (jobApp == null) {
                    jobApp = applicantBackend.createJobApplication(jobPosting);
                }
                new AddReferencesDialog(frame, applicantBackend, jobApp);
            }
        });
        referencesButton.setBounds(300, 400, 150, 30);
        this.add(referencesButton);
    }
}
