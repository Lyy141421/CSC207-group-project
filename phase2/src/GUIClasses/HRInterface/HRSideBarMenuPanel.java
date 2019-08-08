package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.InterviewManager;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;
import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

class HRSideBarMenuPanel extends JPanel {

    // === Instance variable ===
    private JPanel cards;
    private HRBackend hrBackend;
    private LogoutActionListener logoutActionListener;

    // === Constructor ===
    HRSideBarMenuPanel(JPanel cards, HRBackend hrBackend, LogoutActionListener logoutActionListener) {
        this.cards = cards;
        this.hrBackend = hrBackend;
        this.logoutActionListener = logoutActionListener;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenuCreator(fullMenu).createMenuBar());
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Home", new ReturnHomeActionListener(cards));
        fullMenu.put("2. Profile", new ProfileActionListener(cards));
        fullMenu.put("3. High priority tasks", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.HIGH_PRIORITY_POSTINGS);
            }
        });
        fullMenu.put("4. Add Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.ADD_POSTING);
            }
        });
        fullMenu.put("5. Update Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.UPDATE_POSTING);
            }
        });
        fullMenu.put("6. Browse Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.BROWSE_POSTINGS);
            }
        });
        fullMenu.put("7. Search applicant", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.SEARCH_APPLICANT);
            }
        });
        fullMenu.put("8. Logout", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printAllData();
                logoutActionListener.actionPerformed(e);
            }
        });
        return fullMenu;
    }

    private void printAllData() {
        System.out.println("From job application system");
        for (Company company : this.hrBackend.getJobAppSystem().getCompanies()) {
            for (Branch branch : company.getBranches()) {
                BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
                for (BranchJobPosting branchJobPosting : branchJobPostingManager.getBranchJobPostings()) {
                    InterviewManager interviewManager = branchJobPosting.getInterviewManager();
                    System.out.println("Interview manager ID: " + interviewManager);
                    if (interviewManager != null) {
                        for (JobApplication jobApp : interviewManager.getApplicationsInConsideration()) {
                            System.out.println(jobApp);
                        }
                    }
                }
            }
        }
        System.out.println("From hr");
        Branch branch = this.hrBackend.getHR().getBranch();
        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
        for (BranchJobPosting branchJobPosting : branchJobPostingManager.getBranchJobPostings()) {
            InterviewManager interviewManager = branchJobPosting.getInterviewManager();
            System.out.println("Interview manager ID: " + interviewManager);
            if (interviewManager != null) {
                for (JobApplication jobApp : interviewManager.getApplicationsInConsideration()) {
                    System.out.println(jobApp);
                }
            }
        }
    }
}