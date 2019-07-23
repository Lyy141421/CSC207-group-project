package ActionListeners;

import GUIClasses.CommonUserGUI.FileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class FileChooserRemoveFileButtonActionListener extends GenericRemoveFileButtonActionListener<FileChooser> {

    public FileChooserRemoveFileButtonActionListener(FileChooser fileChooser, JButton buttonToRemove, File fileToRemove) {
        super(fileChooser, buttonToRemove, fileToRemove);
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList<File> filesToSubmit = this.getMainPanel().getFilesToSubmit();
        filesToSubmit.remove(this.getFileToRemove());
        JPanel buttonPanel = this.getMainPanel().getRemoveFileButtonsPanel();
        buttonPanel.remove(this.getButtonToRemove());
        buttonPanel.validate();
        buttonPanel.repaint();
        this.getMainPanel().getUploadButton().setEnabled(true);
        if (filesToSubmit.size() == 0) {
            this.getMainPanel().getSubmitButton().setEnabled(false);
        }
    }
}
