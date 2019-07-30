package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import NotificationSystem.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The Applicant's "Home" screen, which also displays all of their notifications
 */
class ApplicantHome extends JPanel {
    private Applicant applicant;

    ApplicantHome(Applicant applicant) {
        this.setLayout(null);
        this.applicant = applicant;

        JLabel titleText = new JLabel("Welcome!", SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        this.buildNotifications();
    }

    /**
     * Displays notifications, or proper message if none
     */
    private void buildNotifications() {
        ArrayList<Notification> notifications = this.applicant.getAllNotifications();
        if(notifications.size() == 0) {
            this.noNotifications();
        }
        else {
            int yPos = 100;
            for(Notification n : notifications) {
                addNotification(n, yPos);
                yPos += 30;
            }
        }
    }

    /**
     * Builds text shown if the applicant has no notifications
     */
    private void noNotifications() {
        JLabel noNotifications = new JLabel("You have no new notifications.", SwingConstants.CENTER);
        noNotifications.setBounds(45, 100, 550, 20);
        this.add(noNotifications);
    }

    /**
     * Builds the text and clear button for an individual notification
     */
    private void addNotification(Notification n, int yPos) {
        JLabel notificationText = new JLabel(n.getMessage(), SwingConstants.LEFT);
        notificationText.setBounds(140, yPos, 400, 20);
        this.add(notificationText);

        JButton notificationButton = new JButton("Clear");
        notificationButton.setBounds(40, yPos, 80, 20);
        notificationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applicant.getNotificationManager().remove(n);
                notificationButton.setVisible(false);
                notificationText.setVisible(false);
            }
        } );
        this.add(notificationButton);
    }
}
