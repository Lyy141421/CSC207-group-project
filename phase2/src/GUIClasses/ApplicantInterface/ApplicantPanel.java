package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserMain;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which allows changing of visuals INCLUDING the sidebar
 */
public class ApplicantPanel extends UserMain {

    private Applicant applicant;
    private JobApplicationSystem jobAppSystem;
    private LogoutActionListener logout;
    private JPanel cards = new JPanel(new CardLayout());
    private ApplicantMain applicantMain;

    public ApplicantPanel(Applicant applicant, JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        this.setLayout(new CardLayout());
        this.applicant = applicant;
        this.jobAppSystem = jobAppSystem;
        this.logout = logout;

        this.setCards();
        this.add(cards);
    }

    public void setCards() {
        applicantMain = new ApplicantMain(this.applicant, this, this.jobAppSystem, this.logout);
        cards.add(applicantMain, "Main");
        cards.add(new ApplicantViewApps(), "SearchResults");
    }

    @Override
    public void refresh() {
        ((CardLayout) cards.getLayout()).show(cards, "Main");
        applicantMain.refresh();
//        cards.removeAll();
//        this.setCards();
    }
}
