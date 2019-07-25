package GUIClasses.ActionListeners;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.CompanyDocumentManager;
import Main.User;
import GUIClasses.ReferenceInterface.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class SubmitDocumentsActionListener implements ActionListener {

    private User documentSubmitter;
    private JobApplication jobApp;
    private ArrayList<File> filesToSubmit;

    // TODO this requires that after the applicant chooses the job to apply to, it automatically creates a job
    // application object so that choosing files will work
    public SubmitDocumentsActionListener(User documentSubmitter, JobApplication jobApp, ArrayList<File> files) {
        this.documentSubmitter = documentSubmitter;
        this.jobApp = jobApp;
        this.filesToSubmit = files;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.updateFileStorage();
        this.showSuccessfulSubmissionPanel(e);

    }

    /**
     * Update the applicant and company folders that store job application documents.
     */
    private void updateFileStorage() {
        if (this.documentSubmitter instanceof Applicant) {
            ApplicantDocumentManager applicantDocumentManager = new ApplicantDocumentManager(jobApp.getApplicant());
            ArrayList<File> filesNotFromAccount = new ArrayList<>();
            for (File file : filesToSubmit) {
                if (!applicantDocumentManager.containsFile(file)) {
                    filesNotFromAccount.add(file);
                }
            }
            ArrayList<JobApplicationDocument> jobAppDocs = applicantDocumentManager.addFilesToAccount(filesNotFromAccount);
            jobApp.addFiles(jobAppDocs);
        } else {
            // Remove job application from reference's list
            Reference reference = (Reference) this.documentSubmitter;
            reference.removeJobApplication(jobApp);
        }
        CompanyDocumentManager companyDocManager = jobApp.getJobPosting().getCompany().getDocumentManager();
        companyDocManager.addFilesForJobApplication(jobApp, filesToSubmit);
    }

    /**
     * Switch the card to the successful submission panel.
     *
     * @param e The click of the submit button.
     */
    private void showSuccessfulSubmissionPanel(ActionEvent e) {
        JPanel cards = new CardLayoutPanelGetter().fromSubmitFilesButton(e);
        ReferencePanel referencePanel = (ReferencePanel) cards.getParent();
        referencePanel.updateCards(); // Update all the cards
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, ReferencePanel.SUCCESSFUL_SUBMISSION);
    }
}