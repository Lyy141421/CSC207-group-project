package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.DocumentSelector;
import GUIClasses.CommonUserGUI.RemoveFileButtonsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class DocumentSelectorRemoveFileButtonActionListener extends GenericRemoveFileButtonActionListener<DocumentSelector> {

    public DocumentSelectorRemoveFileButtonActionListener(DocumentSelector documentSelector, RemoveFileButtonsPanel buttonsPanel,
                                                          File fileToRemove) {
        super(documentSelector, buttonsPanel, fileToRemove);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<File> filesToSubmit = this.getMainPanel().getFilesToSubmit();
        filesToSubmit.remove(this.getFileToRemove());
        this.getButtonsPanel().removeButton(getFileToRemove());
        this.getMainPanel().getButton().setEnabled(true);
    }
}
