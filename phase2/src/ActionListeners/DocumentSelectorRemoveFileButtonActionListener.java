package ActionListeners;

import GUIClasses.CommonUserGUI.DocumentSelector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class DocumentSelectorRemoveFileButtonActionListener extends GenericRemoveFileButtonActionListener<DocumentSelector> {

    public DocumentSelectorRemoveFileButtonActionListener(DocumentSelector documentSelector, JButton buttonToRemove,
                                                          File fileToRemove) {
        super(documentSelector, buttonToRemove, fileToRemove);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<File> filesToSubmit = this.getMainPanel().getFilesToSubmit();
        filesToSubmit.remove(this.getFileToRemove());
        JPanel buttonPanel = this.getMainPanel().getRemoveFileButtonsPanel();
        buttonPanel.remove(this.getButtonToRemove());
        buttonPanel.validate();
        buttonPanel.repaint();
        this.getMainPanel().getButton().setEnabled(true);
    }
}
