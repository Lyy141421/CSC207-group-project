package GUIClasses.ActionListeners;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.MainFrame;
import Main.JobApplicationSystem;
import Main.User;

import javax.swing.*;
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
//        new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
//        new DataLoaderAndStorer(jobApplicationSystem).refreshAllData();
        parent.remove(this.getUserPanelFromMenuItemDirectlyOnMenuBar(e));
        masterLayout.show(parent, MainFrame.LOGIN);
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
    }

    private JPanel getUserPanelFromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        return (JPanel) sideBarMenuPanel.getParent();
    }
}
