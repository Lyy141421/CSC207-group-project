package GUIClasses.StartInterface;

import javax.swing.*;

/**
 * Form shown when creating a new HR Coordinator
 */
class NewHRCForm extends JPanel {

    static final String NAME = "HRCard";

    NewHRCForm() {
        this.setLayout(null);
        this.setName(NAME);
        this.addFields();
        this.addText();
    }

    private void addFields() {
        JPasswordField hrcPassEntry = new JPasswordField();
        hrcPassEntry.setName("password");
        hrcPassEntry.setBounds(437, 8, 150, 30);
        this.add(hrcPassEntry);

        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setName("name");
        hrcNameEntry.setBounds(437, 43, 150, 30);
        this.add(hrcNameEntry);

        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setName("email");
        hrcEmailEntry.setBounds(437, 77, 150, 30);
        this.add(hrcEmailEntry);

        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setName("company");
        hrcCompanyEntry.setBounds(437, 112, 150, 30);
        this.add(hrcCompanyEntry);

        // TODO how to handle a branch?
//        JTextField hrcBranchEntry = new JTextField();
//        hrcBranchEntry.setName("branch");
//        hrcBranchEntry.setBounds(437, 146, 150, 30);
//        this.add(hrcBranchEntry);
    }

    private void addText() {
        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(267, 8, 150, 30);
        this.add(hrcPassText);

        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(267, 43, 150, 30);
        this.add(hrcNameText);

        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(267, 77, 150, 30);
        this.add(hrcEmailText);

        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(267, 112, 150, 30);
        this.add(hrcCompanyText);

        // TODO how to handle branch?
//        JLabel hrcBranchText = new JLabel("Branch: ", SwingConstants.CENTER);
//        hrcBranchText.setBounds(267, 146, 150, 30);
//        this.add(hrcBranchText);
    }
}
