package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {

    public MainFrame () {
        super("GET A JOB");
        initUI();
    }

    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(854, 480);
        setLayout(new CardLayout());
        addCards();
        setVisible(true);
        setResizable(false);
    }

    // Call methods that create each interface and add to main frame.
    private void addCards () {
        add(LoginInterface());
        add(new HRPanel());
    }

    // Create interface for login screen
    private JPanel LoginInterface() {
        JPanel loginScreen = new JPanel(null);
        JLabel welcomeLabel = new JLabel("CSC207 Summer 2019 Job Application System", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
            welcomeLabel.setBounds(320, 140, 640, 90);
            //welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        JLabel userNameText = new JLabel("Username: ", SwingConstants.CENTER);
            userNameText.setBounds(540, 340, 100, 30);
        JTextField userNameEntry = new JTextField();
            userNameEntry.setBounds(640, 340, 100, 30);
        JLabel passwordText = new JLabel("Password: ", SwingConstants.CENTER);
            passwordText.setBounds(540, 375, 100, 30);
        JPasswordField passwordEntry = new JPasswordField();
            passwordEntry.setBounds(640, 375, 100, 30);
        JButton loginButton = new JButton("Login/Register");
            loginButton.setBounds(580, 420, 120, 20);

        JLabel createNewText = new JLabel("User not found.", SwingConstants.CENTER);
            createNewText.setBounds(540, 420, 100, 20);
        JButton createNewButton = new JButton("Create?");
            createNewButton.setBounds(640, 420, 100, 20);

        JLabel wrongPass = new JLabel("Incorrect password!", SwingConstants.CENTER);
            wrongPass.setBounds(580, 305, 120, 30);

        loginScreen.add(welcomeLabel); loginScreen.add(userNameText); loginScreen.add(passwordText);
        loginScreen.add(userNameEntry); loginScreen.add(passwordEntry); loginScreen.add(loginButton);
        //loginScreen.add(createNewText); loginScreen.add(createNewButton); loginButton.setVisible(false);
        //loginScreen.add(wrongPass);

        return loginScreen;
    }

    // Create interface for new User account
    private JPanel NewUserInterface() { //TODO: Split into helper functions
        JPanel createUser = new JPanel();
        createUser.setLayout(new GridLayout(3, 1));

        JPanel selectorPanel = new JPanel(); selectorPanel.setLayout(null);
        JPanel formPanel = new JPanel(); formPanel.setLayout(new CardLayout());
        JPanel buttonPanel = new JPanel(); buttonPanel.setLayout(null);

        //Selector panel
        JLabel titleText = new JLabel("Create New Account", SwingConstants.CENTER);
        titleText.setBounds(440, 75, 400, 90);
        titleText.setFont(new Font("Serif", Font.PLAIN, 30));
        selectorPanel.add(titleText);
        JLabel typeSelector = new JLabel("What type of account is being created?", SwingConstants.CENTER);
        typeSelector.setBounds(465, 200, 250, 30);
        selectorPanel.add(typeSelector);
        String[] userTypes = {"Applicant", "Interviewer", "HR Coordinator"};
        JComboBox selectorBox = new JComboBox(userTypes);
        selectorBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout c1 = (CardLayout)(formPanel.getLayout()); c1.show(formPanel, (String)selectorBox.getSelectedItem());
            }
        });
        selectorBox.setBounds(715, 200, 100, 30);
        selectorPanel.add(selectorBox);

        //Form panel - Applicant Card
        JPanel applicantCard = new JPanel(); applicantCard.setLayout(null);
        JLabel appPassText = new JLabel("Password: ", SwingConstants.CENTER);
        appPassText.setBounds(480, 45, 150, 30);
        applicantCard.add(appPassText);
        JPasswordField appPassEntry = new JPasswordField();
        appPassEntry.setBounds(650, 45, 150, 30);
        applicantCard.add(appPassEntry);
        JLabel appNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        appNameText.setBounds(480, 105, 150, 30);
        applicantCard.add(appNameText);
        JTextField appNameEntry = new JTextField();
        appNameEntry.setBounds(650, 105, 150, 30);
        applicantCard.add(appNameEntry);
        JLabel appEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        appEmailText.setBounds(480, 165, 150, 30);
        applicantCard.add(appEmailText);
        JTextField appEmailEntry = new JTextField();
        appEmailEntry.setBounds(650, 165, 150, 30);
        applicantCard.add(appEmailEntry);
        //Form panel - Interviewer card
        JPanel interviewerCard = new JPanel(); interviewerCard.setLayout(null);
        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(480, 45, 150, 30);
        interviewerCard.add(intPassText);
        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setBounds(650, 45, 150, 30);
        interviewerCard.add(intPassEntry);
        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(480, 105, 150, 30);
        interviewerCard.add(intEmailText);
        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setBounds(650, 105, 150, 30);
        interviewerCard.add(intEmailEntry);
        JLabel intCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        intCompanyText.setBounds(480, 165, 150, 30);
        interviewerCard.add(intCompanyText);
        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setBounds(650, 165, 150, 30);
        interviewerCard.add(intCompanyEntry);
        //Form panel - HRC card
        JPanel HRCCard = new JPanel(); HRCCard.setLayout(null);
        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(480, 33, 150, 30);
        HRCCard.add(hrcPassText);
        JPasswordField hrcPassEntry = new JPasswordField();
        hrcPassEntry.setBounds(650, 33, 150, 30);
        HRCCard.add(hrcPassEntry);
        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(480, 81, 150, 30);
        HRCCard.add(hrcNameText);
        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setBounds(650, 81, 150, 30);
        HRCCard.add(hrcNameEntry);
        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(480, 129, 150, 30);
        HRCCard.add(hrcEmailText);
        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setBounds(650, 129, 150, 30);
        HRCCard.add(hrcEmailEntry);
        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(480, 177, 150, 30);
        HRCCard.add(hrcCompanyText);
        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setBounds(650, 177, 150, 30);
        HRCCard.add(hrcCompanyEntry);
        //Form panel - add all
        formPanel.add(applicantCard, "Applicant");
        formPanel.add(interviewerCard, "Interviewer");
        formPanel.add(HRCCard, "HR Coordinator");
        //Button panel
        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(565, 15, 150, 30);
        buttonPanel.add(submitButton);
        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(565, 170, 150, 30);
        buttonPanel.add(backButton);

        createUser.add(selectorPanel); createUser.add(formPanel); createUser.add(buttonPanel);
        
        return createUser;
    }
}
