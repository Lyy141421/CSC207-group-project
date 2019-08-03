package GUIClasses.ActionListeners;

import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.MainFrame;
import Main.JobApplicationSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        masterLayout.show(parent, "LOGIN");
        masterLayout.removeLayoutComponent(new PanelGetter().getUserPanelFromMenuItemDirectlyOnMenuBar(e));
    }
}
