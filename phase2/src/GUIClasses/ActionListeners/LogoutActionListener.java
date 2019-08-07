package GUIClasses.ActionListeners;

import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.MainFrame;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class LogoutActionListener implements ActionListener {

    // === Instance variables ===
    private Container parent;
    private CardLayout masterLayout;
    private JobApplicationSystem jobApplicationSystem;

    public LogoutActionListener(Container parent, CardLayout masterLayout, JobApplicationSystem jobApplicationSystem) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.jobApplicationSystem = jobApplicationSystem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
        parent.remove(this.getUserPanelFromMenuItemDirectlyOnMenuBar(e));
        masterLayout.show(parent, MainFrame.LOGIN);
    }

    private JPanel getUserPanelFromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        return (JPanel) sideBarMenuPanel.getParent();
    }
}
