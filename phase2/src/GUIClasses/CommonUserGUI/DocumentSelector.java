package GUIClasses.CommonUserGUI;

import GUIClasses.ActionListeners.DocumentSelectorRemoveFileButtonActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class DocumentSelector extends AbstractDocumentUser {
    /**
     * Panel for selecting documents to submit for an application from one's account.
     */

    // === Instance variables ==
    // The files chosen for submission
    ArrayList<File> filesToSubmit = new ArrayList<>();
    // The panel for removing files selected
    RemoveFileButtonsPanel removeFileButtonsPanel;

    // === Constructor ===
    public DocumentSelector(File folder) {
        super(folder);
        this.setButton("Select");
        this.setPanelTitle("Select a file to submit");

        removeFileButtonsPanel = new RemoveFileButtonsPanel();
        removeFileButtonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 100));
        this.add(removeFileButtonsPanel, BorderLayout.EAST);
    }

    // === Getter ===
    public ArrayList<File> getFilesToSubmit() {
        return filesToSubmit;
    }

    public JPanel getRemoveFileButtonsPanel() {
        return this.removeFileButtonsPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        File file = this.getFiles()[this.getList().getSelectedIndex()];
        JButton removeFileButton = removeFileButtonsPanel.addButton(file);
        removeFileButton.addActionListener(
                new DocumentSelectorRemoveFileButtonActionListener(this, removeFileButton, file));
        this.getButton().setEnabled(true);
    }
}
