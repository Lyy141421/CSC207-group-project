package ActionListeners;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.CompanyDocumentManager;
import Main.User;
import NewGUI.ReferenceGUI.ReferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class SubmitDocumentActionListener implements ActionListener {

    private User documentSubmitter;
    private JobApplication jobApp;
    private ArrayList<File> filesToSubmit;

    // TODO this requires that after the applicant chooses the job to apply to, it automatically creates a job
    // application object so that choosing files will work
    public SubmitDocumentActionListener(User documentSubmitter, JobApplication jobApp, ArrayList<File> files) {
        this.documentSubmitter = documentSubmitter;
        this.jobApp = jobApp;
        this.filesToSubmit = files;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.documentSubmitter instanceof Applicant) {
            // TODO what if applicant submits files from their account.
            ApplicantDocumentManager applicantDocumentManager = new ApplicantDocumentManager(jobApp.getApplicant());
            ArrayList<JobApplicationDocument> jobAppDocs = applicantDocumentManager.addFilesToAccount(filesToSubmit);
            jobApp.addFiles(jobAppDocs);
        } else {
            // Remove job application from reference's list
            Reference reference = (Reference) this.documentSubmitter;
            reference.removeJobApplicationForReference(jobApp);
        }
        CompanyDocumentManager companyDocManager = jobApp.getJobPosting().getCompany().getDocumentManager();
        companyDocManager.addFilesForJobApplication(jobApp, filesToSubmit);

        // Show 'submission was successful page'
        JPanel cards = new CardLayoutPanelGetter().fromSubmitFilesButton(e);
        ReferencePanel referencePanel = (ReferencePanel) cards.getParent();
        referencePanel.update(jobApp); // Update the list/table where the job application was displayed.
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, ReferencePanel.SUCCESSFUL_SUBMISSION);
    }
}
