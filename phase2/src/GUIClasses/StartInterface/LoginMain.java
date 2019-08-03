package GUIClasses.StartInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.Reference;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ApplicantInterface.ApplicantPanel;
import GUIClasses.HRInterface.HRMain;
import GUIClasses.InterviewerInterface.InterviewerMain;
import GUIClasses.MainFrame;
import GUIClasses.ReferenceInterface.ReferenceMain;
import Main.JobApplicationSystem;
import Main.User;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
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

    public LoginMain(NewUserPanel newUserRef, Container parent, CardLayout masterLayout,
                     JobApplicationSystem jobAppSystem) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.mainframe = (MainFrame) this.parent.getParent().getParent().getParent();
        this.newUserRef = newUserRef;
        this.setLayout(null);
        this.addTextItems();
        this.addEntryItems();
        this.backend = new LoginBackend(jobAppSystem);
        this.jobAppSystem = jobAppSystem;
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

        this.add(welcomeLabel); this.add(userNameText); this.add(passwordText);
        this.add(createNewText); this.add(wrongPass); this.add(blankEntry);
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
                getDate(userNameEntry, passwordEntry);
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
     *
     */
    private void getDate(JTextField userNameEntry, JPasswordField passwordEntry) {
        JDialog popup = new JDialog();
        popup.setLayout(new BorderLayout());

        JLabel instructions = new JLabel("Please enter today's date.");
        popup.add(instructions, SwingConstants.NORTH);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        popup.add(datePicker, SwingConstants.CENTER);

        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate today = ((Date) ((JDatePickerImpl) datePicker).getModel().getValue()).
                        toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                login(userNameEntry, passwordEntry, today);
            }
        });
        popup.add(submit, SwingConstants.SOUTH);

        this.add(popup);
    }

    /**
     * attempts login with the provided information
     * Cases: 0 - blank field, 1 - no user exists, 2 - successful login, 3 - wrong pass
     */
    private void login(JTextField userNameEntry, JPasswordField passwordEntry, LocalDate today) {
        int result = backend.login(userNameEntry.getText(), new String(passwordEntry.getPassword()));
        switch(result) {
            case 1: this.showCreateNew();
                    this.hidePassError();
                    this.hideBlankField();
                    break;
            case 0: this.showBlankField();
                    this.hideCreateNew();
                    this.hidePassError();
                    break;
            case 2: this.hideCreateNew();
                    this.hidePassError();
                    this.hideBlankField();
                    this.GUILogin(userNameEntry.getText(), today);
                    userNameEntry.setText("");
                    passwordEntry.setText("");
                    break;
            case 3: this.showPassError();
                    this.hideCreateNew();
                    this.hideBlankField();
                    break;
        }
    }

    /**
     * Attempts a login through the GUI
     */
    private void GUILogin(String username, LocalDate today) {
        User user = this.backend.findUserByUsername(username);
        if(user instanceof Applicant) {
            ApplicantPanel newAppPanel = new ApplicantPanel((Applicant)user, this.jobAppSystem,
                    this.createLogoutListener());
            this.parent.add(newAppPanel, "APPLICANT");
            this.masterLayout.show(parent, "APPLICANT");
        } else if(user instanceof HRCoordinator) {
            HRMain newHRPanel = new HRMain((HRCoordinator)user, this.jobAppSystem, this.createLogoutListener());
            this.parent.add(newHRPanel, "HRC");
            this.masterLayout.show(parent, "HRC");
        } else if(user instanceof Interviewer) {
            InterviewerMain newIntPanel = new InterviewerMain((Interviewer)user, jobAppSystem,
                    this.createLogoutListener());
            this.parent.add(newIntPanel, "INTERVIEWER");
            this.masterLayout.show(parent, "INTERVIEWER");
        } else { //Reference
            ReferenceMain newRefPanel = new ReferenceMain((Reference)user, jobAppSystem, this.createLogoutListener());
            this.parent.add(newRefPanel, "REFERENCE");
            this.masterLayout.show(parent, "REFERENCE");
        }
        this.newUserRef.setNewUsername(null);
    }

    /**
     * passes user to the existing create user class, and requests card change
     */
    private void createUser(String username) {
        this.hideCreateNew();
        this.hidePassError();
        this.newUserRef.setNewUsername(username);
        this.masterLayout.show(parent, "NEWUSER");
    }

    /**
     * creates the appropriate action listener to logout
     */
    private LogoutActionListener createLogoutListener() {
        return new LogoutActionListener(mainframe, masterLayout, jobAppSystem);
    }
}
