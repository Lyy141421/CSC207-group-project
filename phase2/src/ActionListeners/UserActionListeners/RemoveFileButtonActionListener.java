package ActionListeners.UserActionListeners;

import NewGUI.FileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class RemoveFileButtonActionListener implements ActionListener {

    private FileChooser fileChooser;
    private JButton buttonToRemove;
    private File fileToRemove;

    public RemoveFileButtonActionListener(FileChooser fileChooser, JButton buttonToRemove, File fileToRemove) {
        this.fileChooser = fileChooser;
        this.buttonToRemove = buttonToRemove;
        this.fileToRemove = fileToRemove;
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList<File> filesToSubmit = this.fileChooser.getFilesToSubmit();
        filesToSubmit.remove(fileToRemove);
        JPanel buttonPanel = this.fileChooser.getRemoveFileButtonsPanel();
        buttonPanel.remove(buttonToRemove);
        buttonPanel.validate();
        buttonPanel.repaint();
        this.fileChooser.getUploadButton().setEnabled(true);
        if (filesToSubmit.size() == 0) {
            this.fileChooser.getSubmitButton().setEnabled(false);
        }
    }
}
