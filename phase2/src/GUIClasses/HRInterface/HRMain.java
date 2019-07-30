package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;

public class HRMain extends JPanel {

    Container contentPane;
    MethodsTheGUICallsInHR HRInterface;
    LocalDate today;

    Container mainPanel = this;
    CardLayout cardLayout = new CardLayout();

    HRHome homePanel;
    HRViewPosting viewPostingPanel;
    HRSearchApplicant searchPanel;
    HRAddPosting addPostingPanel;

    private HRMain (Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        this.setLayout(this.cardLayout);

        this.contentPane = contentPane;
        this.HRInterface = HRInterface;
        this.today = today;

        this.addPanels();

        this.setPanelSwitchActions();
    }

    //=====Add component methods=====

    private void addPanels() {
        this.add(this.homePanel = new HRHome(this, this.HRInterface, this.today), HRPanel.HOME, 0);
        this.add(this.viewPostingPanel = new HRViewPosting(this, this.HRInterface, this.today), HRPanel.POSTING, 1);
        this.add(this.searchPanel = new HRSearchApplicant(this, this.HRInterface, this.today), HRPanel.SEARCH, 2);
        this.add(this.addPostingPanel = new HRAddPosting(this, this.HRInterface, this.today), HRPanel.ADD_POSTING, 3);
        //this.add(new HRViewApp(this, this.HRInterface, this.today, new HashMap<>()), HRPanel.APPLICATION, 4);
    }

    private void setPanelSwitchActions() {
        this.setLogoutAction();
        this.setToDoAction();
        this.setBrowseAction();
    }

    //=====panel switch methods=====

    private void setLogoutAction () {
        this.homePanel.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: save all changes.
                //TODO: Replace "LOGIN" with static constant.
                ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
            }
        });
    }

    private void setToDoAction () {
        this.homePanel.getToDoButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPostingPanel.setCurrJPs(viewPostingPanel.getImportantJP());
                viewPostingPanel.reload();
                cardLayout.show(mainPanel, HRPanel.POSTING);
            }
        });
    }

    private void setBrowseAction () {
        this.homePanel.getBrowseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPostingPanel.setCurrJPs(viewPostingPanel.getAllJP());
                viewPostingPanel.reload();
                cardLayout.show(mainPanel, HRPanel.POSTING);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HR Main");
        JobApplicationSystem jobAppSystem = new JobApplicationSystem();
        jobAppSystem.setToday(LocalDate.now());
        Company company = jobAppSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "L4B4P8");
        HRCoordinator hrc = jobAppSystem.getUserManager().createHRCoordinator("hr", "password", "name", "email", branch, LocalDate.now());
        frame.add(new HRMain(frame.getContentPane(), new MethodsTheGUICallsInHR(jobAppSystem, hrc), jobAppSystem.getToday()));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
