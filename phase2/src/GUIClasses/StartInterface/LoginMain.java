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
import java.util.ArrayList;
import java.util.Date;

/**
 * REMEMBER:
 * Along with NewUserPanel, LoginMain is one of the mandatory cards that should be present in NewGUI.MainFrame.
 */

public class LoginMain extends JPanel {

    // === Error messages ===
    static final String WRONG_PASSWORD = "Wrong password!";
    private static final String BLANK_FIELD_ERROR = "Please fill in all fields";
    static final String INVALID_USERNAME = "Invalid username";
    static final String USER_NOT_FOUND = "User not found";
    private static final String CREATE_NEW = "Create?";

    // === GUI elements ===
    private LoginBackend backend;
    private CardLayout masterLayout;
    private Container parent;
    private NewUserPanel newUserRef;
    private boolean dateInputted = false;
    private ArrayList<JLabel> errors = new ArrayList<>();
    private JButton createNewButton;
    private JDatePickerImpl datePicker;
    private JTextField userNameEntry;
    private JPasswordField passwordEntry;

    // === Connection to backend ===
    private JobApplicationSystem jobAppSystem;
    private LogoutActionListener logout;

    // === Constructor ===
    public LoginMain(NewUserPanel newUserRef, Container parent, CardLayout masterLayout, JobApplicationSystem jobAppSystem) {
        this.backend = new LoginBackend(jobAppSystem);
        this.jobAppSystem = jobAppSystem;
        this.logout = new LogoutActionListener(parent, masterLayout, jobAppSystem);
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.newUserRef = newUserRef;
        this.setLayout(null);
        this.addTextItems();
        this.addErrorMessages();
        this.addEntryItems();
    }

    /**
     * Update the job application system with the new date inputted
     *
     * @param inputtedDate The date inputted by the user.
     */
    private void updateSystem(LocalDate inputtedDate) {
        this.jobAppSystem.setPreviousLoginDate(inputtedDate);
        this.jobAppSystem.setToday(inputtedDate);
        jobAppSystem.applicant30Day();
        jobAppSystem.getUserManager().deleteAllEmptyReferenceAccounts();
        jobAppSystem.updateAllJobPostings();
    }

    /**
     * Adds the text items required for the login screen.
     */
    private void addTextItems() {
        JLabel welcomeLabel = new JLabel("CSC207 Summer 2019 Job Application System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        welcomeLabel.setBounds(107, 50, 640, 90);

        JLabel userNameText = new JLabel("Username: ", SwingConstants.CENTER);
        userNameText.setBounds(327, 220, 100, 30);

        JLabel passwordText = new JLabel("Password: ", SwingConstants.CENTER);
        passwordText.setBounds(327, 255, 100, 30);

        this.add(welcomeLabel); this.add(userNameText); this.add(passwordText);
    }

    /**
     * Adds the error meessages required for the login screen.
     */
    private void addErrorMessages() {
        String[] errorMessages = new String[]{USER_NOT_FOUND, WRONG_PASSWORD, BLANK_FIELD_ERROR, INVALID_USERNAME};
        for (String error : errorMessages) {
            JLabel errorLabel = new JLabel(error, SwingConstants.CENTER);
            if (error.equals(USER_NOT_FOUND)) {
                errorLabel.setBounds(337, 330, 100, 30);
            } else {
                errorLabel.setBounds(337, 185, 180, 30);
            }
            errorLabel.setVisible(false);
            this.add(errorLabel);
            errors.add(errorLabel);
        }
    }

    /**
     * Adds the interactive items necessary for the login screen.
     */
    private void addEntryItems() {
        userNameEntry = new JTextField();
        userNameEntry.setBounds(427, 220, 100, 30);

        passwordEntry = new JPasswordField();
        passwordEntry.setBounds(427, 255, 100, 30);

        JButton loginButton = new JButton("Login/Register");
        loginButton.setBounds(367, 300, 120, 22);
        loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (!dateInputted) {
                    getDate();
                } else {
                    login();
                }
            }
        });
        this.add(userNameEntry);
        this.add(passwordEntry);
        this.setCreateNewButton();
        this.add(createNewButton);
        this.add(loginButton);
    }

    /**
     * Sets the create new user button.
     */
    private void setCreateNewButton() {
        createNewButton = new JButton(CREATE_NEW);
        createNewButton.setBounds(460, 335, 100, 20);
        createNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hideError(WRONG_PASSWORD);
                createUser(userNameEntry.getText());
            }
        });
        createNewButton.setVisible(false);
    }

    /**
     * Shows the specified error to the screen.
     * @param error The error to be shown
     */
    private void showError(String error) {
        for (JLabel errorLabel : errors) {
            if (errorLabel.getText().equals(error)) {
                errorLabel.setVisible(true);
                if (error.equals(USER_NOT_FOUND)) {
                    createNewButton.setVisible(true);
                }
            } else {
                errorLabel.setVisible(false);
                if (errorLabel.getText().equals(USER_NOT_FOUND)) {
                    createNewButton.setVisible(false);
                }
            }
        }
    }

    /**
     * Hides all the errors from the screen.
     */
    private void hideAllErrors() {
        for (JLabel errorLabel : errors) {
            errorLabel.setVisible(false);
        }
        createNewButton.setVisible(false);
    }

    /**
     * Hides the specified error
     * @param error The error to be hidden
     */
    private void hideError(String error) {
        for (JLabel errorLabel : errors) {
            if (errorLabel.getText().equals(error)) {
                errorLabel.setVisible(false);
            }
        }
    }

    /**
     *  Get the date inputted by the user.
     */
    private void getDate() {
        JDialog popup = new JDialog();
        popup.setLayout(new BorderLayout());

        JPanel instructions = new GUIElementsCreator().createLabelPanel("Please enter today's date", 18, true);
        instructions.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        popup.add(instructions, BorderLayout.NORTH);
        popup.add(this.createDatePickerPanel(), BorderLayout.CENTER);
        JPanel submitButtonPanel = this.createSubmitButtonPanel(popup);
        submitButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        popup.add(submitButtonPanel, BorderLayout.SOUTH);
        popup.setSize(new Dimension(500, 300));
        popup.setResizable(false);
        popup.setVisible(true);
    }

    /**
     * Create the date picker panel.
     *
     * @return the panel created.
     */
    private JPanel createDatePickerPanel() {
        JPanel fullDatePickerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        fullDatePickerPanel.add(datePicker);
        return fullDatePickerPanel;
    }

    /**
     * Create the submit button panel.
     *
     * @param popup The dialog popup for the date.
     * @return the panel created.
     */
    private JPanel createSubmitButtonPanel(JDialog popup) {
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
                    login();
                } else {
                    JOptionPane.showMessageDialog(popup, "Invalid date. Please pick again");
                }
            }
        });
        buttonPanel.add(submit);
        return buttonPanel;
    }


    /**
     * Attempts login with the provided information
     */
    private void login() {
        String result = backend.login(userNameEntry.getText(), new String(passwordEntry.getPassword()));
        if (result.equals(NewUserPanel.SUCCESS)) {
            this.hideAllErrors();
            this.GUILogin(userNameEntry.getText());
            this.clearEntries(userNameEntry, passwordEntry);
            this.dateInputted = false;
        } else {
            this.showError(result);
        }
    }

    /**
     * Clears the username and password text fields.
     * @param userNameEntry The username text field.
     * @param passwordEntry The password text field.
     */
    private void clearEntries(JTextField userNameEntry, JPasswordField passwordEntry) {
        userNameEntry.setText("");
        passwordEntry.setText("");
    }

    /**
     * Attempts a login through the GUI
     * @param username The username inputted.
     */
    private void GUILogin(String username) {
        User user = this.backend.findUserByUsername(username);
        UserMain userMain = new UserMainFactory(user, jobAppSystem, logout).createPanel();
        this.parent.add(userMain, MainFrame.USER_PANEL);
        this.masterLayout.show(parent, MainFrame.USER_PANEL);
        this.newUserRef.setNewUsername(null);
    }

    /**
     * Passes user to the existing create user class, and requests card change
     * @param username  The username inputted
     */
    private void createUser(String username) {
        this.hideAllErrors();
        this.newUserRef.setNewUsername(username);
        this.masterLayout.show(parent, MainFrame.NEW_USER);
    }
}
