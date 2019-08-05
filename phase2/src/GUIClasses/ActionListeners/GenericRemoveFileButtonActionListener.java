package GUIClasses.ActionListeners;

import GUIClasses.CommonUserGUI.RemoveFileButtonsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class GenericRemoveFileButtonActionListener<T> implements ActionListener {

    private T mainPanel;
    private RemoveFileButtonsPanel buttonsPanel;
    private File fileToRemove;

    public GenericRemoveFileButtonActionListener(T mainPanel, RemoveFileButtonsPanel buttonsPanel, File fileToRemove) {
        this.mainPanel = mainPanel;
        this.buttonsPanel = buttonsPanel;
        this.fileToRemove = fileToRemove;
    }

    public abstract void actionPerformed(ActionEvent e);

    // === Getters ===
    T getMainPanel() {
        return this.mainPanel;
    }

    File getFileToRemove() {
        return this.fileToRemove;
    }

    RemoveFileButtonsPanel getButtonsPanel() {
        return this.buttonsPanel;
    }
}
