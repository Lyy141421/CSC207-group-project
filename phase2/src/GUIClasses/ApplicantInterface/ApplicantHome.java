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

        JPanel notifications = this.buildNotifications();
        notifications.setBounds(420, 0, 250, 470);
        this.add(notifications, 0);

        this.addBasicComponents(notifications);
    }

    /**
     * Displays notifications, or proper message if none
     */
    private JPanel buildNotifications() {
        JPanel ret = new JPanel(null);
        ret.setBackground(Color.white);
        ret.setVisible(false);

        ArrayList<Notification> notifications = this.applicant.getAllNotifications();
        if(notifications.size() == 0) {
            this.noNotifications(ret);
        }
        else {
            int yPos = 30;
            for(Notification n : notifications) {
                addNotification(ret, n, yPos);
                yPos += 35;
            }
        }

        JButton closeNotifications = new JButton("Close");
        closeNotifications.setBounds(130, 10, 100, 20);
        closeNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ret.setVisible(false);
                for(Component c: getComponents()) {
                    if(c instanceof JButton) {
                        c.setVisible(true);
                    }
                }
            }
        });
        ret.add(closeNotifications);

        return ret;
    }

    /**
     * Builds text shown if the applicant has no notifications
     */
    private void noNotifications(JPanel ret) {
        JLabel noNotifications = new JLabel("<html>You have no new notifications.</html>", SwingConstants.CENTER);
        noNotifications.setBounds(0, 30, 200, 30);
        ret.add(noNotifications);
    }

    /**
     * Builds the text and clear button for an individual notification
     */
    private void addNotification(JPanel ret, Notification n, int yPos) {
        JLabel notificationText = new JLabel(n.getMessage(), SwingConstants.LEFT);
        notificationText.setBounds(0, yPos, 200, 30);
        ret.add(notificationText);

        JButton notificationButton = new JButton("✓");
        notificationButton.setBounds(150, yPos + 2, 20, 25);
        notificationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applicant.getNotificationManager().remove(n);
                notificationButton.setVisible(false);
                notificationText.setVisible(false);
            }
        } );
        ret.add(notificationButton);
    }

    /**
     * Adds basic text and buttons
     */
    private void addBasicComponents(JPanel notifications) {
        JLabel titleText = new JLabel("Welcome, " + applicant.getLegalName(), SwingConstants.CENTER);
        titleText.setBounds(170, 35, 300, 40);
        titleText.setFont(new Font("Serif", Font.PLAIN, 27));
        this.add(titleText);

        JButton viewNotifications = new JButton("Alerts: " + applicant.getAllNotifications().size());
        viewNotifications.setBounds(550, 10, 100, 20);
        viewNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifications.setVisible(true);
                viewNotifications.setVisible(false);
            }
        });
        this.add(viewNotifications);
    }
}
