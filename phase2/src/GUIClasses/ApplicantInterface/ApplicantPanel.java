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

    public ApplicantPanel(Applicant applicant, JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        super(jobAppSystem);
        this.setLayout(new CardLayout());
        this.applicant = applicant;
        this.jobAppSystem = jobAppSystem;
        this.logout = logout;

        this.setCards();
    }

    public void setCards() {
        this.add(new ApplicantMain(this.applicant, this, this.jobAppSystem, this.logout), "Main");
        this.add(new ApplicantViewApps(), "SearchResults");
    }

    @Override
    public void refresh() {
        this.removeAll();
        this.setCards();
        ((CardLayout)this.getLayout()).show(this, "Main");
    }
}
