package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;

import javax.swing.*;
import java.awt.*;

public class ApplicantPanel extends JPanel {
    ApplicantPanel(Applicant applicant) {
        this.setLayout(new CardLayout());
        this.add(new ApplicantMain(applicant, this), "Main");
        this.add(new ApplicantTextDocSubmission(), "TextApplication");
    }
}
