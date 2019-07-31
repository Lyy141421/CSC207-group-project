package GUIClasses.HRInterface;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class HRMain extends UserPanel {

    HRBackend hrBackend;
    JPanel cards = new JPanel(new CardLayout());

    private HRMain(HRCoordinator hrCoordinator, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        this.hrBackend = new HRBackend(jobAppSystem, hrCoordinator);
        this.setLayout(new GridBagLayout());
        this.setCards();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        this.add(new HRSideBarMenuPanel(cards, logoutActionListener), c);

        c.gridx++;
        this.add(cards, c);
    }

    //=====Add component methods=====

    public void setCards() {
        cards.add(new HRHome(this.hrBackend), UserPanel.HOME);
        cards.add(new UserProfilePanel(this.hrBackend.getHR()), UserPanel.PROFILE);
        cards.add(new HRViewPosting(this.hrBackend, cards, true), HRPanel.TODO_POSTINGS);
        cards.add(new HRViewPosting(this.hrBackend, cards, false), HRPanel.BROWSE_POSTINGS);
        cards.add(new HRSearchApplicant(this.hrBackend, cards), HRPanel.SEARCH_APPLICANT);
//        cards.add(new HRAddPosting(this.hrBackend
//       ), HRPanel.ADD_POSTING);
        cards.add(new HRAddPostingForm(this.hrBackend), HRPanel.ADD_POSTING);
        //cards.add(new HRViewApp(this, this.hrBackend
        //, this.today, new HashMap<>()), HRPanel.APPLICATION);
    }

    public void refresh() {
        cards.removeAll();
        this.setCards();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HR Main");
        JobApplicationSystem jobAppSystem = new JobApplicationSystem();
        jobAppSystem.setToday(LocalDate.now());
        Company company = jobAppSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "L4B4P8");
        HRCoordinator hrc = jobAppSystem.getUserManager().createHRCoordinator("hr", "password", "name", "email", branch, LocalDate.now());
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobAppSystem);
        frame.add(new HRMain(hrc, jobAppSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
