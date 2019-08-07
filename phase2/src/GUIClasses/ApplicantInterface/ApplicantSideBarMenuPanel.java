package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;
import Main.JobApplicationSystem;
import Main.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

class ApplicantSideBarMenuPanel extends JPanel {

    // === Instance variable ===
    private CardLayout affectedLayout;
    private JPanel cards;
    private LogoutActionListener logout;
    private ApplicantBackend applicantBackend;
    private JobApplicationSystem jobApplicationSystem;

    // === Constructor ===
    ApplicantSideBarMenuPanel(JPanel cards, ApplicantBackend applicantBackend, JobApplicationSystem jobApplicationSystem,
                              CardLayout layout, LogoutActionListener logout) {
        this.applicantBackend = applicantBackend;
        this.jobApplicationSystem = jobApplicationSystem;
        this.affectedLayout = layout;
        this.cards = cards;
        this.logout = logout;
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
        fullMenu.put("2. Browse Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "POSTINGS");}});
        fullMenu.put("3. Profile", new ProfileActionListener(cards));
        fullMenu.put("4. View Uploaded Documents", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "DOCUMENTS");}});
        fullMenu.put("5. View Schedule", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "SCHEDULE");}});
        fullMenu.put("6. Manage Applications", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "MANAGE");}});
        fullMenu.put("7. Logout", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printAllData(jobApplicationSystem);
                resetJobApplicationManager();
                printAllData(jobApplicationSystem);
                logout.actionPerformed(e);
            }
        });
        return fullMenu;
    }

    private void resetJobApplicationManager() {
        Applicant applicantFromJobAppSystem = (Applicant) jobApplicationSystem.getUserManager().findUserByUsername(applicantBackend.getApplicant().getUsername());
        applicantFromJobAppSystem.setJobApplicationManager(applicantBackend.getApplicant().getJobApplicationManager());
    }

    public void printAllData(JobApplicationSystem jobApplicationSystem) {
        System.out.println("From job application system");
        Applicant applicantFromJobAppSystem = (Applicant) jobApplicationSystem.getUserManager().findUserByUsername(applicantBackend.getApplicant().getUsername());
        System.out.println(applicantFromJobAppSystem.getJobApplicationManager());
        for (JobApplication jobApp : applicantFromJobAppSystem.getJobApplicationManager().getJobApplications()) {
            System.out.println(jobApp);
        }

        System.out.println("From applicant");
        System.out.println(applicantBackend.getApplicant().getJobApplicationManager());
        for (JobApplication jobApp : applicantBackend.getApplicant().getJobApplicationManager().getJobApplications()) {
            System.out.println(jobApp);
        }
        System.out.println("From branch job posting");
        for (Company company : this.jobApplicationSystem.getCompanies()) {
            for (Branch branch : company.getBranches()) {
                BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
                for (BranchJobPosting branchJobPosting : branchJobPostingManager.getBranchJobPostings()) {
                    for (JobApplication jobApp : branchJobPosting.getJobApplications()) {
                        System.out.println(jobApp);
                    }
                }
            }
        }
    }
}
