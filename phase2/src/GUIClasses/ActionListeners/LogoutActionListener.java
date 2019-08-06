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
        Thread newThread = new Thread() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
                            masterLayout.show(parent, MainFrame.LOGIN);
                            parent.remove(new PanelGetter().getUserPanelFromMenuItemDirectlyOnMenuBar(e));
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    System.out.println("Something when wrong");
                }
            }
        };
        newThread.start();
    }
}
