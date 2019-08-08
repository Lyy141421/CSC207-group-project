package GUIClasses.CommonUserGUI;

import Main.User;
import NotificationSystem.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NotificationsGUI {

    // === Instance variables ===
    private User user;
    private JPanel notificationsPanel;
    private JButton notificationsButton;

    // === Constructor ===
    public NotificationsGUI(User user) {
        this.user = user;
        this.buildNotifications();
        notificationsPanel.setBounds(420, 0, 250, 470);
        this.setNotificationsButton();
    }

    // === Getters ===
    public JPanel getNotificationsPanel() {
        return notificationsPanel;
    }

    public JButton getNotificationsButton() {
        return notificationsButton;
    }

    /**
     * Displays notificationsPanel, or proper message if none
     */
    private void buildNotifications() {
        notificationsPanel = new JPanel(null);
        notificationsPanel.setBackground(Color.white);
        notificationsPanel.setVisible(false);

        ArrayList<Notification> notifications = this.user.getAllNotifications();
        if (notifications.size() == 0) {
            this.noNotifications();
        } else {
            int yPos = 30;
            for (Notification n : notifications) {
                addNotification(n, yPos);
                yPos += 35;
            }
        }

        JButton closeNotifications = new JButton("Close");
        closeNotifications.setBounds(130, 10, 100, 20);
        closeNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationsPanel.setVisible(false);
                notificationsButton.setVisible(true);
            }
        });
        notificationsPanel.add(closeNotifications);
    }

    /**
     * Builds text shown if the applicant has no notificationsPanel
     */
    private void noNotifications() {
        JLabel noNotifications = new JLabel("<html>You have no new notifications.</html>", SwingConstants.CENTER);
        noNotifications.setBounds(0, 30, 200, 30);
        notificationsPanel.add(noNotifications);
    }

    /**
     * Builds the text and clear button for an individual notification
     */
    private void addNotification(Notification n, int yPos) {
        JLabel notificationText = new JLabel("<html>" + n.getMessage() + "</html>", SwingConstants.LEFT);
        notificationText.setBounds(0, yPos, 200, 30);
        notificationsPanel.add(notificationText);

        JButton notificationButton = new JButton("✓");
        notificationButton.setBounds(200, yPos + 5, 25, 25);
        notificationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getNotificationManager().remove(n);
                notificationButton.setVisible(false);
                notificationText.setVisible(false);
                notificationsButton.setText("Alerts: " + user.getAllNotifications().size());
            }
        });
        notificationsPanel.add(notificationButton);
    }

    /**
     * Sets the notifications button.
     */
    private void setNotificationsButton() {
        notificationsButton = new JButton("Alerts: " + user.getAllNotifications().size());
        notificationsButton.setBounds(550, 10, 100, 20);
        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationsPanel.setVisible(true);
                notificationsButton.setVisible(false);
            }
        });
    }
}
