package GUIClasses.StartInterface;

import javax.swing.*;

/**
 * Form shown when creating a new interviewer
 */
class NewInterviewerForm extends JPanel {
    NewInterviewerForm() {
        this.setLayout(null);
        this.addFields();
        this.addText();
    }

    private void addFields() {
        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setName("password");
        intPassEntry.setBounds(437, 8, 150, 30);
        this.add(intPassEntry);

        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setName("email");
        intEmailEntry.setBounds(437, 77, 150, 30);
        this.add(intEmailEntry);

        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setName("company");
        intCompanyEntry.setBounds(437, 112, 150, 30);
        this.add(intCompanyEntry);

        JTextField intNameEntry = new JTextField();
        intNameEntry.setName("name");
        intNameEntry.setBounds(437, 43, 150, 30);
        this.add(intNameEntry);
    }

    private void addText() {
        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(267, 8, 150, 30);
        this.add(intPassText);

        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(267, 77, 150, 30);
        this.add(intEmailText);

        JLabel intCompanyText = new JLabel("Branch: ", SwingConstants.CENTER);
        intCompanyText.setBounds(267, 112, 150, 30);
        this.add(intCompanyText);

        JLabel intNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        intNameText.setBounds(267, 43, 150, 30);
        this.add(intNameText);
    }
}
