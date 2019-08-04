package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import GUIClasses.CommonUserGUI.NotificationsGUI;

import javax.swing.*;
import java.awt.*;

/**
 * The Applicant's "Home" screen, which also displays all of their notifications
 */
class ApplicantHome extends JPanel {
    private Applicant applicant;

    ApplicantHome(Applicant applicant) {
        this.setLayout(null);
        this.applicant = applicant;

        this.addBasicComponents();
    }

    /**
     * Adds basic text and buttons
     */
    private void addBasicComponents() {
        JLabel titleText = new JLabel("Welcome, " + applicant.getLegalName(), SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        NotificationsGUI notifications = new NotificationsGUI(applicant);
        this.add(notifications.getNotificationsButton());
        this.add(notifications.getNotificationsPanel());
    }
}
