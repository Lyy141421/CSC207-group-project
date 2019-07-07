package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * REMEMBER:
 * Along with LoginPanel, NewUserPanel is one of the mandatory cards that should be present in MainFrame.
 */

class NewUserPanel extends JPanel {
    private UserInterfaceTest BackEnd;
    private CardLayout masterLayout;
    private JPanel parent;
    private JComboBox mainSelector;
    private String newUsername;

    NewUserPanel(JPanel parent, CardLayout masterLayout) {
        this.parent = parent;
        this.masterLayout = masterLayout;
        this.setLayout(new GridLayout(3, 1));

        JPanel selector = this.buildSelector();
        JPanel forms = this.buildForms();

        this.mainSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox actor = (JComboBox)e.getSource();
                CardLayout cl = (CardLayout)forms.getLayout();
                cl.show(forms, actor.getSelectedItem().toString());
            }
        });

        JPanel buttons = this.buildButtons();

        this.add(selector); this.add(forms); this.add(buttons);
        this.BackEnd = new UserInterfaceTest();
    }

    /**
     * Adds the drop down menu which allows a user to pick the account type being created
     */
    private JPanel buildSelector() {
        JPanel selectorPanel = new JPanel(null); selectorPanel.setName("selectorPanel");

        JLabel titleText = new JLabel("Create New Account", SwingConstants.CENTER);
        titleText.setBounds(227, 20, 400, 70);
        titleText.setFont(new Font("Serif", Font.PLAIN, 25));
        selectorPanel.add(titleText);

        JLabel typeSelector = new JLabel("What type of account is being created?", SwingConstants.CENTER);
        typeSelector.setBounds(247, 105, 250, 30);
        selectorPanel.add(typeSelector);

        String[] userTypes = {"Applicant", "Interviewer", "HR Coordinator"};
        JComboBox selectorBox = new JComboBox(userTypes);
        selectorBox.setBounds(497, 105, 120, 30);
        this.mainSelector = selectorBox;
        selectorPanel.add(selectorBox);

        return selectorPanel;
    }

    /**
     * Adds the different registration forms in card form to the master panel
     */
    private JPanel buildForms() {
        JPanel formPanel = new JPanel(new CardLayout()); formPanel.setName("formPanel");

        this.addAppForm(formPanel);
        this.addIntForm(formPanel);
        this.addHRCForm(formPanel);

        return formPanel;
    }

    /**
     * Applicant form
     */
    private void addAppForm(JPanel formPanel) {
        JPanel applicantCard = new JPanel(null); applicantCard.setName("applicantCard");

        JLabel appPassText = new JLabel("Password: ", SwingConstants.CENTER);
        appPassText.setBounds(267, 10, 150, 30);
        applicantCard.add(appPassText);

        JPasswordField appPassEntry = new JPasswordField();
        appPassEntry.setName("password");
        appPassEntry.setBounds(437, 10, 150, 30);
        applicantCard.add(appPassEntry);

        JLabel appNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        appNameText.setBounds(267, 60, 150, 30);
        applicantCard.add(appNameText);

        JTextField appNameEntry = new JTextField();
        appNameEntry.setName("name");
        appNameEntry.setBounds(437, 60, 150, 30);
        applicantCard.add(appNameEntry);

        JLabel appEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        appEmailText.setBounds(267, 110, 150, 30);
        applicantCard.add(appEmailText);

        JTextField appEmailEntry = new JTextField();
        appEmailEntry.setName("email");
        appEmailEntry.setBounds(437, 110, 150, 30);
        applicantCard.add(appEmailEntry);

        formPanel.add(applicantCard, "Applicant");
    }

    /**
     * Interviewer form
     */
    private void addIntForm(JPanel formPanel) {
        JPanel interviewerCard = new JPanel(null); interviewerCard.setName("interviewerCard");

        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(267, 10, 150, 30);
        interviewerCard.add(intPassText);

        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setName("password");
        intPassEntry.setBounds(437, 10, 150, 30);
        interviewerCard.add(intPassEntry);

        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(267, 60, 150, 30);
        interviewerCard.add(intEmailText);

        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setName("email");
        intEmailEntry.setBounds(437, 60, 150, 30);
        interviewerCard.add(intEmailEntry);

        JLabel intCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        intCompanyText.setBounds(267, 110, 150, 30);
        interviewerCard.add(intCompanyText);

        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setName("company");
        intCompanyEntry.setBounds(437, 110, 150, 30);
        interviewerCard.add(intCompanyEntry);

        formPanel.add(interviewerCard, "Interviewer");
    }

    /**
     * HR Coordinator Form
     */
    private void addHRCForm(JPanel formPanel) {
        JPanel HRCCard = new JPanel(null); HRCCard.setName("HRCCard");

        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(267, 8, 150, 30);
        HRCCard.add(hrcPassText);

        JPasswordField hrcPassEntry = new JPasswordField();
        hrcPassEntry.setName("password");
        hrcPassEntry.setBounds(437, 8, 150, 30);
        HRCCard.add(hrcPassEntry);

        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(267, 43, 150, 30);
        HRCCard.add(hrcNameText);

        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setName("name");
        hrcNameEntry.setBounds(437, 43, 150, 30);
        HRCCard.add(hrcNameEntry);

        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(267, 77, 150, 30);
        HRCCard.add(hrcEmailText);

        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setName("email");
        hrcEmailEntry.setBounds(437, 77, 150, 30);
        HRCCard.add(hrcEmailEntry);

        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(267, 112, 150, 30);
        HRCCard.add(hrcCompanyText);

        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setName("company");
        hrcCompanyEntry.setBounds(437, 112, 150, 30);
        HRCCard.add(hrcCompanyEntry);

        formPanel.add(HRCCard, "HR Coordinator");
    }

    /**
     * Adds buttons to create account as well as return to login
     */
    private JPanel buildButtons() {
        JPanel buttonPanel = new JPanel(null); buttonPanel.setName("buttonPanel");

        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(352, 15, 150, 30);
        submitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int status = createUser();
                postCreation(status);
            }
        });
        buttonPanel.add(submitButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(352, 80, 150, 30);
        backButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                masterLayout.show(parent, "LOGIN");
            }
        });
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    /**
     * Attempts to create the selected user type with the given inputs
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private int createUser() {
        switch(this.mainSelector.getSelectedItem().toString()) {
            case "Applicant":
                return this.createApplicant();
            case "Interviewer":
                return this.createInterviewer();
            case "HR Coordinator":
                return this.createHRC();
        }
        return 0;
    }

    /**
     * The below functions simply attempt to instantiate their corresponding user types
     * @return 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private int createApplicant() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "applicantCard").getComponents();
        return BackEnd.createNewApplicant(this.getInputs(items));
    }

    private int createInterviewer() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "interviewerCard").getComponents();
        return BackEnd.createNewInterviewer(this.getInputs(items));
    }

    private int createHRC() {
        JPanel forms = this.getPanelByName(this, "formPanel");
        Component[] items = this.getPanelByName(forms, "HRCCard").getComponents();
        return BackEnd.createNewHRC(this.getInputs(items));
    }

    /**
     * Finds a panel within another panel by its name as set by .setName()
     */
    private JPanel getPanelByName(JPanel panel, String name) {
        JPanel ret = new JPanel();
        for(Component c : panel.getComponents()) {
            if(c instanceof JPanel && c.getName().equals(name)) {
                ret = (JPanel)c;
            }
        }
        return ret;
    }

    /**
     * Collects user input data from the different forms, so that a new account may be created
     */
    private HashMap<String, String> getInputs(Component[] items) {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("username", this.newUsername);
        for(Component c : items) {
            if(c.getName() != null) {
                switch (c.getName()) {
                    case "password":
                        ret.put("password", new String(((JPasswordField)c).getPassword()));
                        break;
                    case "name":
                        ret.put("name", ((JTextField)c).getText());
                        break;
                    case "email":
                        ret.put("email", ((JTextField)c).getText());
                        break;
                    case "company":
                        ret.put("company", ((JTextField)c).getText());
                        break;
                    case "field":
                        ret.put("field", ((JTextField)c).getText());
                        break;
                }
            }
        }
        return ret;
    }

    /**
     * Updates the GUI's visuals based on the result of an account creation
     * @param status 0 - blank entry, 1 - bad email, 2 - bad company, 3 - success
     */
    private void postCreation(int status) { //TODO: Finish this function
        switch(status) {
            case 0:
                System.out.println("Blank entry");
                break;
            case 1:
                System.out.println("Bad email");
                break;
            case 2:
                System.out.println("Bad company");
                break;
            case 3:
                System.out.println("Successful");
                this.setNewUsername(null);
                this.masterLayout.show(this.parent, "LOGIN");
                break;
        }
    }

    void setNewUsername(String username) {
        this.newUsername = username;
    }
}
