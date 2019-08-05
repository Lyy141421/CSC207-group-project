package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.FileChooser;
import GUIClasses.CommonUserGUI.RemoveFileButtonsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class FileChooserRemoveFileButtonActionListener extends GenericRemoveFileButtonActionListener<FileChooser> {

    public FileChooserRemoveFileButtonActionListener(FileChooser fileChooser, RemoveFileButtonsPanel buttonsPanel, File fileToRemove) {
        super(fileChooser, buttonsPanel, fileToRemove);
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList<File> filesToSubmit = this.getMainPanel().getFilesToSubmit();
        filesToSubmit.remove(this.getFileToRemove());
        this.getButtonsPanel().removeButton(getFileToRemove());
        this.getMainPanel().getUploadButton().setEnabled(true);
        if (filesToSubmit.size() == 0) {
            this.getMainPanel().getSubmitButton().setEnabled(false);
        }
    }
}
