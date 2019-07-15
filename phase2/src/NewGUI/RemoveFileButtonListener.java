package NewGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class RemoveFileButtonListener implements ActionListener {

    private JPanel panelWithButtons;
    private JButton buttonToRemove;
    private ArrayList<File> filesToSubmit;
    private File fileToRemove;

    RemoveFileButtonListener(JPanel panelWithButtons, JButton buttonToRemove, ArrayList<File> filesToSubmit, File fileToRemove) {
        this.panelWithButtons = panelWithButtons;
        this.buttonToRemove = buttonToRemove;
        this.filesToSubmit = filesToSubmit;
        this.fileToRemove = fileToRemove;
    }

    public void actionPerformed(ActionEvent e) {
        this.filesToSubmit.remove(fileToRemove);
        this.panelWithButtons.remove(buttonToRemove);
        this.panelWithButtons.validate();
        this.panelWithButtons.repaint();
    }
}
