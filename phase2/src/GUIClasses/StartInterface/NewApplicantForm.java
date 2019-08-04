package GUIClasses.StartInterface;

import javax.swing.*;

/**
 * Form shown when creating a new applicant
 */
class NewApplicantForm extends JPanel {

    static final String NAME = "ApplicantCard";

    NewApplicantForm() {
        this.setLayout(null);
        this.setName(NAME);
        this.addFields();
        this.addText();
    }

    private void addFields() {
        JPasswordField appPassEntry = new JPasswordField();
        appPassEntry.setName("password");
        appPassEntry.setBounds(437, 8, 150, 30);
        this.add(appPassEntry);

        JTextField appNameEntry = new JTextField();
        appNameEntry.setName("name");
        appNameEntry.setBounds(437, 43, 150, 30);
        this.add(appNameEntry);

        JTextField appEmailEntry = new JTextField();
        appEmailEntry.setName("email");
        appEmailEntry.setBounds(437, 77, 150, 30);
        this.add(appEmailEntry);

        JTextField postalCodeEntry = new JTextField();
        postalCodeEntry.setName("postalCode");
        postalCodeEntry.setBounds(437, 112, 150, 30);
        this.add(postalCodeEntry);
    }

    private void addText(){
        JLabel appPassText = new JLabel("Password: ", SwingConstants.CENTER);
        appPassText.setBounds(267, 8, 150, 30);
        this.add(appPassText);

        JLabel appNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        appNameText.setBounds(267, 43, 150, 30);
        this.add(appNameText);

        JLabel appEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        appEmailText.setBounds(267, 77, 150, 30);
        this.add(appEmailText);

        JLabel postalCodeText = new JLabel("Postal Code: ", SwingConstants.CENTER);
        postalCodeText.setBounds(267, 112, 150, 30);
        this.add(postalCodeText);
    }
}
