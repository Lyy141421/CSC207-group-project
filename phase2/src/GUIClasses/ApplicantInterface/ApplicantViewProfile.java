package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which displays previous information about the account in question
 */
class ApplicantViewProfile extends JPanel {
    ApplicantViewProfile(Applicant applicant) {
        this.setLayout(null);
        ApplicantBackend backend = new ApplicantBackend(applicant);

        JLabel titleText = new JLabel("Account Information", SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        JLabel infoBox = new JLabel("Account Created: " + applicant.getDateCreated());
        infoBox.setBounds(100, 85, 400, 30);
        this.add(infoBox);

        JLabel daysSince = new JLabel(backend.daysSince());
        daysSince.setBounds(100, 115, 400, 30);
        this.add(daysSince);

        if(!daysSince.getText().equals("You have not yet submitted any job applications.")) {
            JLabel appHistory = new JLabel(backend.oldApps());
            appHistory.setVerticalAlignment(SwingConstants.TOP);
            appHistory.setBounds(100, 150, 400, 330);
            this.add(appHistory);
        }
    }
}
