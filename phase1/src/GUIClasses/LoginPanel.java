package GUIClasses;

import javax.swing.*;
import java.awt.*;

/**
 * REMEMBER:
 * Along with NewUserPanel, LoginPanel is one of the mandatory cards that should be present in MainFrame.
 */

class LoginPanel extends JPanel {
    LoginPanel() {
        this.setLayout(null);
        this.addTextItems();
        this.addEntryItems();
    }

    /**
     * Adds the text items required for the login screen.
     * Note "Incorrect password" warning and "User not found" are hidden by default.
     */
    private void addTextItems() {
        JLabel welcomeLabel = new JLabel("CSC207 Summer 2019 Job Application System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        welcomeLabel.setBounds(107, 50, 640, 90);

        JLabel userNameText = new JLabel("Username: ", SwingConstants.CENTER);
        userNameText.setBounds(327, 220, 100, 30);

        JLabel passwordText = new JLabel("Password: ", SwingConstants.CENTER);
        passwordText.setBounds(327, 255, 100, 30);

        JLabel createNewText = new JLabel("User not found.", SwingConstants.CENTER);
        createNewText.setBounds(327, 330, 100, 20);
        createNewText.setVisible(false);

        JLabel wrongPass = new JLabel("Incorrect password!", SwingConstants.CENTER);
        wrongPass.setBounds(367, 185, 120, 30);
        wrongPass.setVisible(false);

        this.add(welcomeLabel); this.add(userNameText); this.add(passwordText);
        this.add(createNewText); this.add(wrongPass);
    }

    /**
     * Adds the interactive items necessary for the login screen.
     * Note "Create New User" button is hidden by default.
     */
    private void addEntryItems() {
        JTextField userNameEntry = new JTextField();
        userNameEntry.setBounds(427, 220, 100, 30);

        JPasswordField passwordEntry = new JPasswordField();
        passwordEntry.setBounds(427, 255, 100, 30);

        JButton loginButton = new JButton("Login/Register");
        loginButton.setBounds(367, 300, 120, 20);

        JButton createNewButton = new JButton("Create?");
        createNewButton.setBounds(427, 330, 100, 20);
        createNewButton.setVisible(false);

        this.add(userNameEntry); this.add(passwordEntry); this.add(loginButton);
        this.add(createNewButton);
    }

    /**
     * Shows prompt to create new account
     */
    private void showCreateNew() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JButton){
                if(((JButton) c).getText().equals("Create?")) {
                    c.setVisible(true);
                }
            } else if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("User not found.")) {
                    c.setVisible(true);
                }
            }
        }
    }

    /**
     * Hides prompt to create a new user
     */
    private void hideCreateNew() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JButton){
                if(((JButton) c).getText().equals("Create?")) {
                    c.setVisible(false);
                }
            } else if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("User not found.")) {
                    c.setVisible(false);
                }
            }
        }
    }

    /**
     * Shows "Incorrect Password" warning
     */
    private void showWrongPass() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Incorrect password!")) {
                    c.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Hides "Incorrect Password" warning
     */
    private void hideWrongPass() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Incorrect password!")) {
                    c.setVisible(false);
                    break;
                }
            }
        }
    }
}