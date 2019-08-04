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
        hrcPassEntry.setBounds(260, 8, 150, 30);
        this.add(hrcPassEntry);

        JTextField hrcNameEntry = new JTextField();
        hrcNameEntry.setName("name");
        hrcNameEntry.setBounds(260, 43, 150, 30);
        this.add(hrcNameEntry);

        JTextField hrcEmailEntry = new JTextField();
        hrcEmailEntry.setName("email");
        hrcEmailEntry.setBounds(260, 77, 150, 30);
        this.add(hrcEmailEntry);

        JTextField hrcCompanyEntry = new JTextField();
        hrcCompanyEntry.setName("company");
        hrcCompanyEntry.setBounds(555, 8, 150, 30);
        this.add(hrcCompanyEntry);

        JTextField hrcBranchEntry = new JTextField();
        hrcBranchEntry.setName("branch");
        hrcBranchEntry.setBounds(555, 43, 150, 30);
        this.add(hrcBranchEntry);

        JTextField hrcPostalCodeEntry = new JTextField();
        hrcPostalCodeEntry.setName("postalCode");
        hrcPostalCodeEntry.setBounds(555, 77, 150, 30);
        this.add(hrcPostalCodeEntry);
    }

    private void addText() {
        JLabel hrcPassText = new JLabel("Password: ", SwingConstants.CENTER);
        hrcPassText.setBounds(90, 8, 150, 30);
        this.add(hrcPassText);

        JLabel hrcNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        hrcNameText.setBounds(90, 43, 150, 30);
        this.add(hrcNameText);

        JLabel hrcEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        hrcEmailText.setBounds(90, 77, 150, 30);
        this.add(hrcEmailText);

        JLabel hrcCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        hrcCompanyText.setBounds(415, 8, 150, 30);
        this.add(hrcCompanyText);

        JLabel hrcBranchText = new JLabel("Branch: ", SwingConstants.CENTER);
        hrcBranchText.setBounds(415, 43, 150, 30);
        this.add(hrcBranchText);

        JLabel postalCodeText = new JLabel("Branch postal code: ", SwingConstants.CENTER);
        postalCodeText.setBounds(415, 77, 150, 30);
        this.add(postalCodeText);
    }
}
