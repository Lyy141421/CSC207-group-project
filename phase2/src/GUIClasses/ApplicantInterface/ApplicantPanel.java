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

    public ApplicantPanel(Applicant applicant, JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        this.setLayout(new CardLayout());
        this.applicant = applicant;
        this.jobAppSystem = jobAppSystem;
        this.logout = logout;
        this.setCards();
        this.add(cards);
    }

    public void setCards() {
        cards.add(new ApplicantMain(this.applicant, this, this.jobAppSystem, this.logout), "Main");
        //this.add(new ApplicantTextDocSubmission(this.applicant, this,jobAppSystem.getToday()), "TextApplication");
        cards.add(new ApplicantViewApps(), "SearchResults");
    }

    // Abstract method in UserMain
    public void refresh() {
        cards.removeAll();
        this.setCards();
    }
}
