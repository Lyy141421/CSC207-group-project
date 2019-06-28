package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * REMEMBER:
 * Along with LoginPanel, NewUserPanel is one of the mandatory cards that should be present in MainFrame.
 */

class NewUserPanel extends JPanel {
    NewUserPanel() {
        this.setLayout(new GridLayout(3, 1));
        JPanel selector = this.buildSelector();
        JPanel forms = this.buildForms();

        for(Component c : selector.getComponents()) {
            if(c instanceof JComboBox) {
                ((JComboBox)c).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JComboBox actor = (JComboBox)e.getSource();
                        CardLayout cl = (CardLayout)forms.getLayout();
                        cl.show(forms, actor.getSelectedItem().toString());
                    }
                });
            }
        }

        JPanel buttons = this.buildButtons();

        this.add(selector); this.add(forms); this.add(buttons);
    }

    /**
     * Adds the drop down menu which allows a user to pick the account type being created
     */
    private JPanel buildSelector() {
        JPanel selectorPanel = new JPanel(); selectorPanel.setLayout(null);

        JLabel titleText = new JLabel("Create New Account", SwingConstants.CENTER);
        titleText.setBounds(227, 20, 400, 70);
        titleText.setFont(new Font("Serif", Font.PLAIN, 25));
        selectorPanel.add(titleText);
        JLabel typeSelector = new JLabel("What type of account is being created?", SwingConstants.CENTER);
        typeSelector.setBounds(252, 110, 250, 30);
        selectorPanel.add(typeSelector);
        String[] userTypes = {"Applicant", "Interviewer", "HR Coordinator"};
        JComboBox selectorBox = new JComboBox(userTypes);
        selectorBox.setBounds(502, 110, 100, 30);
        selectorPanel.add(selectorBox);

        return selectorPanel;
    }

    /**
     * Adds the different registration forms in card form to the master panel
     */
    private JPanel buildForms() {
        JPanel formPanel = new JPanel(); formPanel.setLayout(new CardLayout());

        this.addAppForm(formPanel);
        this.addIntForm(formPanel);
        this.addHRCForm(formPanel);

        return formPanel;
    }

    /**
     * Applicant form
     */
    private void addAppForm(JPanel formPanel) {
        JPanel applicantCard = new JPanel(); applicantCard.setLayout(null);

        JLabel appPassText = new JLabel("Password: ", SwingConstants.CENTER);
        appPassText.setBounds(267, 10, 150, 30);
        applicantCard.add(appPassText);

        JPasswordField appPassEntry = new JPasswordField();
        appPassEntry.setBounds(437, 10, 150, 30);
        applicantCard.add(appPassEntry);

        JLabel appNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        appNameText.setBounds(267, 60, 150, 30);
        applicantCard.add(appNameText);

        JTextField appNameEntry = new JTextField();
        appNameEntry.setBounds(437, 60, 150, 30);
        applicantCard.add(appNameEntry);

        JLabel appEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        appEmailText.setBounds(267, 110, 150, 30);
        applicantCard.add(appEmailText);

        JTextField appEmailEntry = new JTextField();
        appEmailEntry.setBounds(437, 110, 150, 30);
        applicantCard.add(appEmailEntry);

        formPanel.add(applicantCard, "Applicant");
    }

    /**
     * Interviewer form
     */
    private void addIntForm(JPanel formPanel) {
        JPanel interviewerCard = new JPanel(); interviewerCard.setLayout(null);

        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(267, 10, 150, 30);
        interviewerCard.add(intPassText);

        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setBounds(437, 10, 150, 30);
        interviewerCard.add(intPassEntry);

        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(267, 60, 150, 30);
        interviewerCard.add(intEmailText);

        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setBounds(437, 60, 150, 30);
        interviewerCard.add(intEmailEntry);

        JLabel intCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        intCompanyText.setBounds(267, 110, 150, 30);
        interviewerCard.add(intCompanyText);

        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setBounds(437, 110, 150, 30);
        interviewerCard.add(intCompanyEntry);

        formPanel.add(interviewerCard, "Interviewer");
    }

    /**
     * HR Coordinator Form
     */
    private void addHRCForm(JPanel formPanel) {
        JPanel HRCCard = new JPanel(); HRCCard.setLayout(null);

        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(267, 8, 150, 30);
        HRCCard.add(hrcPassText);

        JPasswordField hrcPassEntry = new JPasswordField();
        hrcPassEntry.setBounds(437, 8, 150, 30);
        HRCCard.add(hrcPassEntry);

        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(267, 43, 150, 30);
        HRCCard.add(hrcNameText);

        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setBounds(437, 43, 150, 30);
        HRCCard.add(hrcNameEntry);

        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(267, 77, 150, 30);
        HRCCard.add(hrcEmailText);

        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setBounds(437, 77, 150, 30);
        HRCCard.add(hrcEmailEntry);

        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(267, 112, 150, 30);
        HRCCard.add(hrcCompanyText);

        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setBounds(437, 112, 150, 30);
        HRCCard.add(hrcCompanyEntry);

        formPanel.add(HRCCard, "HR Coordinator");
    }

    /**
     * Adds buttons to create account as well as return to login
     */
    private JPanel buildButtons() {
        JPanel buttonPanel = new JPanel(); buttonPanel.setLayout(null);

        JButton submitButton = new JButton("Create Account");
        submitButton.setBounds(352, 10, 150, 30);
        buttonPanel.add(submitButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(352, 80, 150, 30);
        buttonPanel.add(backButton);

        return buttonPanel;
    }
}
