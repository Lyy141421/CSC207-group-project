package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import GUIClasses.ActionListeners.LogoutActionListener;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which allows changing of visuals INCLUDING the sidebar
 */
public class ApplicantPanel extends JPanel {
    public ApplicantPanel(Applicant applicant, JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        this.setLayout(new CardLayout());
        this.add(new ApplicantMain(applicant, this, jobAppSystem, logout), "Main");
        this.add(new ApplicantTextDocSubmission(), "TextApplication");
        this.add(new ApplicantViewApps(), "SearchResults");
    }
}
