/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Adapted from FileChooserDemo.java from https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
 */

package GUIClasses.CommonUserGUI;

import GUIClasses.ActionListeners.FileChooserRemoveFileButtonActionListener;
import GUIClasses.ActionListeners.SubmitDocumentsActionListener;
import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import Main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class FileChooser extends JPanel implements ActionListener {

    // === Class variables ===
    //    private static final int APPLICANT_MAX_NUM_FILES = // TODO do we have a limit?
    private static final int REFERENCE_MAX_NUM_FILES = 1;

    // === Instance variables ===
    private JButton uploadButton;   // The upload button
    private JButton submitButton;   // The submit button
    private RemoveFileButtonsPanel removeFileButtonsPanel;  // The panel for removing the files selected
    private JFileChooser fc;    // The file chooser
    private String uploadButtonIconPath = "./Open16.gif";   // The icon image for uploading a document
    private ArrayList<File> filesToSubmit = new ArrayList<>();  // The list of files to submit

    private User user;  // The user who logged in
    private JobApplication jobApp;  // The job application for this document submission

    // === Constructor ===
    public FileChooser(User user, JobApplication jobApp) {
        super(new BorderLayout());
        this.user = user;
        this.jobApp = jobApp;

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.setUploadButtonPanel();
        this.setRemoveFilesButtonPanel();
        this.setSubmitButtonPanel();
    }

    // === Getters ===
    public JPanel getRemoveFileButtonsPanel() {
        return this.removeFileButtonsPanel;
    }

    public ArrayList<File> getFilesToSubmit() {
        return this.filesToSubmit;
    }

    public JButton getUploadButton() {
        return this.uploadButton;
    }

    public JButton getSubmitButton() {
        return this.submitButton;
    }

    public void enableUploadButton() {
        this.uploadButton.setEnabled(true);
        this.revalidate();
    }

    // === Private methods ===

    /**
     * Set the upload button panel.
     */
    private void setUploadButtonPanel() {
        uploadButton = new JButton("Upload", new ImageIcon(uploadButtonIconPath));
        uploadButton.addActionListener(this);

        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.add(uploadButton);
        uploadButton.setEnabled(false);
        this.add(uploadButtonPanel, BorderLayout.BEFORE_FIRST_LINE);
    }

    /**
     * Set the remove files button panel.
     */
    private void setRemoveFilesButtonPanel() {
        removeFileButtonsPanel = new RemoveFileButtonsPanel();
        removeFileButtonsPanel.setBackground(Color.WHITE);
        this.add(removeFileButtonsPanel, BorderLayout.CENTER);
    }

    /**
     * Set the submit button panel.
     */
    private void setSubmitButtonPanel() {
        submitButton = new JButton("Submit");
        submitButton.setEnabled(false);
        submitButton.addActionListener(new SubmitDocumentsActionListener(this.user, this.jobApp, this.filesToSubmit));

        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        this.add(submitButtonPanel, BorderLayout.AFTER_LAST_LINE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
            int returnVal = fc.showOpenDialog(FileChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.addFileToSubmitList();
                if (user instanceof Reference && filesToSubmit.size() == REFERENCE_MAX_NUM_FILES) {
                    uploadButton.setEnabled(false);
                }
            }
        }
    }

    /**
     * Add the file selected by the user to the list to be submitted and add the optional remove button for the file.
     */
    private void addFileToSubmitList() {
        File file = fc.getSelectedFile();
        JButton removeFileButton = removeFileButtonsPanel.addButton(file);
        removeFileButton.addActionListener(
                new FileChooserRemoveFileButtonActionListener(this, removeFileButton, file));
        filesToSubmit.add(file);
        submitButton.setEnabled(true);
    }
}
