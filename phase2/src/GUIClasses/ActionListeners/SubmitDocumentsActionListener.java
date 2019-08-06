package GUIClasses.ActionListeners;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.ReferenceLetterDocumentManager;
import GUIClasses.CommonUserGUI.UserMain;
import Main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class SubmitDocumentsActionListener implements ActionListener {

    private JPanel cardPanel;
    private User documentSubmitter;
    private JobApplication jobApp;
    private HashMap<String, String> fileTypeToTextEntry;
    private ArrayList<File> filesToSubmit = new ArrayList<>();

    // === Constructor for applicant entries ===
    public SubmitDocumentsActionListener(JPanel cardPanel, User documentSubmitter, JobApplication jobApp, HashMap<String, JTextArea> fileTypeToContents) {
        this.cardPanel = cardPanel;
        this.documentSubmitter = documentSubmitter;
        this.jobApp = jobApp;
        this.fileTypeToTextEntry = new HashMap<>();
        for (String fileType : fileTypeToContents.keySet()) {
            this.fileTypeToTextEntry.put(fileType, fileTypeToContents.get(fileType).getText());
        }
    }

    // === Constructor for file submission ===
    public SubmitDocumentsActionListener(JPanel cardPanel, User documentSubmitter, JobApplication jobApp, ArrayList<File> files) {
        this.cardPanel = cardPanel;
        this.documentSubmitter = documentSubmitter;
        this.jobApp = jobApp;
        this.filesToSubmit = files;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.updateFileStorage();
        JPanel parent = (JPanel) ((JButton) e.getSource()).getParent();
        int value = filesToSubmit.size();
        if (filesToSubmit.isEmpty()) {
            value = this.fileTypeToTextEntry.size();
        }
        JOptionPane.showMessageDialog(parent, "You have successfully submitted " + value + " files.");
        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        if (cardPanel.getParent() instanceof UserMain) {
            Thread newThread = new Thread() {
                public void run() {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                ((UserMain) cardPanel.getParent()).refresh();
                            }
                        });
                    } catch (InterruptedException | InvocationTargetException ex) {
                        System.out.println("Something went wrong");
                    }
                }
            };
            newThread.start();
        } else {
            Thread newThread = new Thread() {
                public void run() {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                ((UserMain) cardPanel).refresh();
                            }
                        });
                    } catch (InterruptedException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            newThread.start();
        }
    }

    /**
     * Update the applicant and company folders that store job application documents.
     */
    private void updateFileStorage() {
        if (this.documentSubmitter instanceof Applicant) {
            if (this.filesToSubmit.isEmpty()) {
                this.uploadTextEntries();
            } else {
                this.uploadFiles();
            }
            ((Applicant) this.documentSubmitter).getJobApplicationManager().addJobApplication(jobApp);
        } else {
            // Reference
            this.uploadReferenceLetters();
        }
    }

    private void uploadReferenceLetters() {
        Reference reference = (Reference) this.documentSubmitter;
        reference.removeJobApplication(jobApp);
        ReferenceLetterDocumentManager referenceLetterDocManager = jobApp.getReferenceLetterDocManager();
        ArrayList<JobApplicationDocument> letters = referenceLetterDocManager.addFileToFolder(filesToSubmit);
        jobApp.addReferenceLetters(reference, letters);
    }

    /**
     * Upload the files chosen to this applicant's account.
     */
    private void uploadFiles() {
        ApplicantDocumentManager applicantDocumentManager = new ApplicantDocumentManager(jobApp.getApplicant());
        ArrayList<File> filesNotFromAccount = new ArrayList<>();
        for (File file : filesToSubmit) {
            if (!applicantDocumentManager.containsFile(file)) {
                filesNotFromAccount.add(file);
            }
        }
        ArrayList<JobApplicationDocument> jobAppDocs = applicantDocumentManager.addFilesToAccount(filesNotFromAccount);
        jobApp.addFiles(jobAppDocs);
    }

    /**
     * Upload the text entries to this applicant's account.
     */
    private void uploadTextEntries() {
        ApplicantDocumentManager applicantDocumentManager = new ApplicantDocumentManager(jobApp.getApplicant());
        ArrayList<JobApplicationDocument> jobAppDocs = applicantDocumentManager.addTextEntriesToAccount(fileTypeToTextEntry);
        jobApp.addFiles(jobAppDocs);
    }
}
