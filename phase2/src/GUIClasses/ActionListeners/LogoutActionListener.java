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
        System.out.println("From logout action listener");
        this.printAllData();
//        new DataLoaderAndStorer(jobApplicationSystem).storeAllData();
//        System.out.println("From logout action listener after storing");
//        this.printAllData();
        new DataLoaderAndStorer(jobApplicationSystem).refreshAllData();
        parent.remove(this.getUserPanelFromMenuItemDirectlyOnMenuBar(e));
        masterLayout.show(parent, MainFrame.LOGIN);
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        System.out.println("From logout action listener after re-loading");
//        this.printAllData();
    }

    public void printAllData() {
        System.out.println("Today: " + jobApplicationSystem.getToday());
        System.out.println("Previous login: " + jobApplicationSystem.getPreviousLoginDate());
        for (Company company : jobApplicationSystem.getCompanies()) {
            System.out.println("Company: " + company.getName());
            for (Branch branch : company.getBranches()) {
                System.out.println("Branch: " + branch.getName());
                System.out.println("HR: " + branch.getHrCoordinators().toString());
                System.out.println("Interviewers: " + branch.getFieldToInterviewers().toString());
                BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
                for (BranchJobPosting jobPosting : branchJobPostingManager.getBranchJobPostings()) {
                    System.out.println(jobPosting);
                }
            }
        }
        for (User user : jobApplicationSystem.getUserManager().getAllUsers()) {
            System.out.println(user.getUsername());
        }
    }


    private JPanel getUserPanelFromMenuItemDirectlyOnMenuBar(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JMenuBar sideBarMenu = (JMenuBar) menuItem.getParent();
        JPanel sideBarMenuPanel = (JPanel) sideBarMenu.getParent();
        return (JPanel) sideBarMenuPanel.getParent();
    }
}
