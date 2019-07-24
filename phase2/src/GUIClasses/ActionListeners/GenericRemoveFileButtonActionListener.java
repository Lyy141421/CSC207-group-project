package GUIClasses.ActionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class GenericRemoveFileButtonActionListener<T> implements ActionListener {

    private T mainPanel;
    private JButton buttonToRemove;
    private File fileToRemove;

    public GenericRemoveFileButtonActionListener(T mainPanel, JButton buttonToRemove, File fileToRemove) {
        this.mainPanel = mainPanel;
        this.buttonToRemove = buttonToRemove;
        this.fileToRemove = fileToRemove;
    }

    public abstract void actionPerformed(ActionEvent e);

    // === Getters ===
    T getMainPanel() {
        return this.mainPanel;
    }

    JButton getButtonToRemove() {
        return this.buttonToRemove;
    }

    public File getFileToRemove() {
        return this.fileToRemove;
    }
}
