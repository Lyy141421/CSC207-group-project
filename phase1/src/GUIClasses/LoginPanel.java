package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * REMEMBER:
 * Along with NewUserPanel, LoginPanel is one of the mandatory cards that should be present in MainFrame.
 */

class LoginPanel extends JPanel {
    private UserInterfaceTest BackEnd;
    private CardLayout masterLayout;
    private JPanel parent;
    private MainFrame mainframe;

    LoginPanel(JPanel parent, CardLayout masterLayout) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.mainframe = (MainFrame)this.parent.getParent().
                getParent().getParent().getParent();
        this.setLayout(null);
        this.addTextItems();
        this.addEntryItems();
        this.BackEnd = new UserInterfaceTest();
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
        createNewText.setBounds(327, 335, 100, 20);
        createNewText.setVisible(false);

        JLabel wrongPass = new JLabel("Something's wrong - try again", SwingConstants.CENTER);
        wrongPass.setBounds(337, 185, 180, 30);
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
        loginButton.setBounds(367, 300, 120, 22);
        loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                login(userNameEntry, passwordEntry);
            }
        });

        JButton createNewButton = new JButton("Create?");
        createNewButton.setBounds(427, 335, 100, 20);
        createNewButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hideInputError();
                createUser(userNameEntry.getText());
            }
        });
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
    private void showInputError() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Something's wrong - try again")) {
                    c.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Hides "Incorrect Password" warning
     */
    private void hideInputError() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Something's wrong - try again")) {
                    c.setVisible(false);
                    break;
                }
            }
        }
    }

    /**
     * attempts login with the provided information
     */
    private void login(JTextField userNameEntry, JPasswordField passwordEntry) {
        int result = BackEnd.login(userNameEntry.getText(), passwordEntry.getPassword().toString());
        switch(result) {
            case 1: this.showCreateNew();
                    this.hideInputError();
                    break;
            case 2: this.hideCreateNew();
                    this.hideInputError();
                    this.mainframe.newUserRef.setNewUsername(null);
                    break; //TODO: handle login
            case 3: this.showInputError();
                    this.hideCreateNew();
                    break;
        }
    }

    /**
     * passes user to the existing create user class, and requests card change
     */
    private void createUser(String username) {
        this.hideCreateNew();
        this.hideInputError();
        this.mainframe.newUserRef.setNewUsername(username);
        this.masterLayout.show(parent, "NEWUSER");
    }
}
