package GUIClasses.StartInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.MainFrame;
import Main.JobApplicationSystem;
import Main.User;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * REMEMBER:
 * Along with NewUserPanel, LoginMain is one of the mandatory cards that should be present in NewGUI.MainFrame.
 */

public class LoginMain extends JPanel {
    private LoginBackend backend;
    private CardLayout masterLayout;
    private Container parent;
    private MainFrame mainframe;
    private NewUserPanel newUserRef;
    private JobApplicationSystem jobAppSystem;
    private LogoutActionListener logout;
    private boolean dateInputted = false;

    public LoginMain(NewUserPanel newUserRef, Container parent, CardLayout masterLayout, JobApplicationSystem jobAppSystem) {
        this.backend = new LoginBackend(jobAppSystem);
        this.jobAppSystem = jobAppSystem;
        this.logout = new LogoutActionListener(parent, masterLayout, jobAppSystem);
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.mainframe = (MainFrame) this.parent.getParent().getParent().getParent();
        this.newUserRef = newUserRef;
        this.setLayout(null);
        this.addTextItems();
        this.addEntryItems();
    }

    private void updateSystem(LocalDate inputtedDate) {
        this.jobAppSystem.setPreviousLoginDate(inputtedDate);
        this.jobAppSystem.setToday(inputtedDate);
        jobAppSystem.applicant30Day();
        jobAppSystem.getUserManager().deleteAllEmptyReferenceAccounts();
        jobAppSystem.updateAllJobPostings();
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

        JLabel wrongPass = new JLabel("Wrong password!", SwingConstants.CENTER);
        wrongPass.setBounds(337, 185, 180, 30);
        wrongPass.setVisible(false);

        JLabel blankEntry = new JLabel("Please fill in both fields", SwingConstants.CENTER);
        blankEntry.setBounds(337, 185, 180, 30);
        blankEntry.setVisible(false);

        JLabel invalidUsername = new JLabel("Invalid username", SwingConstants.CENTER);
        invalidUsername.setBounds(337, 185, 180, 30);
        invalidUsername.setVisible(false);

        this.add(welcomeLabel); this.add(userNameText); this.add(passwordText);
        this.add(createNewText);
        this.add(wrongPass);
        this.add(blankEntry);
        this.add(invalidUsername);
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
                if (!dateInputted) {
                    getDate();
                }
                login(userNameEntry, passwordEntry);
            }
        });

        JButton createNewButton = new JButton("Create?");
        createNewButton.setBounds(427, 335, 100, 20);
        createNewButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hidePassError();
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
     * Shows wrong pass warning
     */
    private void showPassError() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Wrong password!")) {
                    c.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Hides wrong pass warning
     */
    private void hidePassError() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Wrong password!")) {
                    c.setVisible(false);
                    break;
                }
            }
        }
    }

    /**
     * Shows blank field warning
     */
    private void showBlankField() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Please fill in both fields")) {
                    c.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Hides blank field warning
     */
    private void hideBlankField() {
        Component[] components = this.getComponents();
        for(Component c : components) {
            if(c instanceof JLabel) {
                if(((JLabel) c).getText().equals("Please fill in both fields")) {
                    c.setVisible(false);
                    break;
                }
            }
        }
    }

    /**
     * Shows invalid username warning
     */
    private void showInvalidUsername() {
        Component[] components = this.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel) {
                if (((JLabel) c).getText().equals("Invalid username")) {
                    c.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Hides blank field warning
     */
    private void hideInvalidUsername() {
        Component[] components = this.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel) {
                if (((JLabel) c).getText().equals("Invalid username")) {
                    c.setVisible(false);
                    break;
                }
            }
        }
    }

    /**
     *
     */
    private void getDate() {
        JDialog popup = new JDialog();
        popup.setLayout(new BorderLayout());

        JPanel instructions = new GUIElementsCreator().createLabelPanel("Please enter today's date", 15, true);
        instructions.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        popup.add(instructions, BorderLayout.NORTH);

        JPanel fullDatePickerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        fullDatePickerPanel.add(datePicker);
        popup.add(fullDatePickerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate today = ((Date) ((JDatePickerImpl) datePicker).getModel().getValue()).
                        toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (backend.isValidDate(today)) {
                    updateSystem(today);
                    popup.dispose();
                    dateInputted = true;
                } else {
                    JOptionPane.showMessageDialog(popup, "Invalid date. Please pick again");
                }
            }
        });
        buttonPanel.add(submit);
        popup.add(buttonPanel, BorderLayout.SOUTH);
        popup.setSize(new Dimension(500, 300));
        popup.setResizable(false);
        popup.setVisible(true);
    }


    /**
     * attempts login with the provided information
     */
    private void login(JTextField userNameEntry, JPasswordField passwordEntry) {
        int result = backend.login(userNameEntry.getText(), new String(passwordEntry.getPassword()));
        switch(result) {
            case LoginBackend.USER_NONEXISTENT:
                this.showCreateNew();
                    this.hidePassError();
                    this.hideBlankField();
                this.hideInvalidUsername();
                    break;
            case LoginBackend.BLANK_ENTRY:
                this.showBlankField();
                    this.hideCreateNew();
                    this.hidePassError();
                this.hideInvalidUsername();
                    break;
            case LoginBackend.SUCCESS:
                this.hideCreateNew();
                    this.hidePassError();
                    this.hideBlankField();
                this.hideInvalidUsername();
                this.GUILogin(userNameEntry.getText());
                this.clearEntries(userNameEntry, passwordEntry);
                    break;
            case LoginBackend.WRONG_PASSWORD:
                this.showPassError();
                    this.hideCreateNew();
                    this.hideBlankField();
                this.hideInvalidUsername();
                break;
            case LoginBackend.INVALID_USERNAME:
                this.showInvalidUsername();
                this.hideCreateNew();
                this.hideBlankField();
                this.hidePassError();
                    break;
        }
    }

    private void clearEntries(JTextField userNameEntry, JPasswordField passwordEntry) {
        userNameEntry.setText("");
        passwordEntry.setText("");
    }

    /**
     * Attempts a login through the GUI
     */
    private void GUILogin(String username) {
        User user = this.backend.findUserByUsername(username);
        UserMain userMain = new UserMainFactory(user, jobAppSystem, logout).createPanel();
        this.parent.add(userMain, MainFrame.USER_PANEL);
        this.masterLayout.show(parent, MainFrame.USER_PANEL);
        this.newUserRef.setNewUsername(null);
    }

    /**
     * passes user to the existing create user class, and requests card change
     */
    private void createUser(String username) {
        this.hideCreateNew();
        this.hidePassError();
        this.hideInvalidUsername();
        this.newUserRef.setNewUsername(username);
        this.masterLayout.show(parent, MainFrame.NEW_USER);
    }
}
