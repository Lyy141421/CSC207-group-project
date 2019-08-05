package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;
import GUIClasses.CommonUserGUI.DocumentSelector;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

class ApplicantFileSubmission extends JPanel {

    private ApplicantBackend applicantBackend;
    private JobApplication jobApp;
    private DocumentSelector documentSelector;

    ApplicantFileSubmission(ApplicantBackend applicantBackend, BranchJobPosting jobPosting) {
        this.applicantBackend = applicantBackend;
        this.jobApp = applicantBackend.createJobApplication(jobPosting);
        this.setLayout(null);
        this.add(this.createTitle());
        this.add(this.createFullDocumentSelectorPanel());

        JPanel submitButtonPanel = this.createSubmitButtonPanel();
        submitButtonPanel.setBounds(380, 370, 100, 50);
        this.add(submitButtonPanel);

    }

    private JLabel createTitle() {
        JLabel titleText = new JLabel("Document Submission");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setBounds(327, 20, 200, 40);
        return titleText;
    }

    private JPanel createFullDocumentSelectorPanel() {
        JPanel fullDocumentSelectorPanel = new JPanel();
        fullDocumentSelectorPanel.setLayout(new BoxLayout(fullDocumentSelectorPanel, BoxLayout.X_AXIS));
        documentSelector = new DocumentSelector(applicantBackend.getApplicantFolder());
        fullDocumentSelectorPanel.add(documentSelector);
        fullDocumentSelectorPanel.add(documentSelector.getRemoveFileButtonsPanel());
        fullDocumentSelectorPanel.setBounds(230, 100, 400, 250);
        return fullDocumentSelectorPanel;
    }

    private JPanel createSubmitButtonPanel() {
        JPanel submitPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (documentSelector.getFilesToSubmit().isEmpty()) {
                    JOptionPane.showMessageDialog(submitPanel, "You must select a file to submit");
                } else {
                    new SubmitDocumentsActionListener(applicantBackend.getApplicant(), jobApp,
                            documentSelector.getFilesToSubmit()).actionPerformed(e);
                }
            }
        });
        submitPanel.add(submitButton);
        return submitPanel;
    }
}
