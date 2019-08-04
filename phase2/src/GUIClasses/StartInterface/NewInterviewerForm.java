package GUIClasses.StartInterface;

import javax.swing.*;

/**
 * Form shown when creating a new interviewer
 */
class NewInterviewerForm extends JPanel {

    static final String NAME = "InterviewerCard";

    NewInterviewerForm() {
        this.setLayout(null);
        this.setName(NAME);
        this.addFields();
        this.addText();
    }

    private void addFields() {
        JPasswordField intPassEntry = new JPasswordField();
        intPassEntry.setName("password");
        intPassEntry.setBounds(260, 8, 150, 30);
        this.add(intPassEntry);

        JTextField intNameEntry = new JTextField();
        intNameEntry.setName("name");
        intNameEntry.setBounds(260, 43, 150, 30);
        this.add(intNameEntry);

        JTextField intEmailEntry = new JTextField();
        intEmailEntry.setName("email");
        intEmailEntry.setBounds(260, 77, 150, 30);
        this.add(intEmailEntry);

        JTextField intFieldEntry = new JTextField();
        intFieldEntry.setName("field");
        intFieldEntry.setBounds(555, 8, 150, 30);
        this.add(intFieldEntry);

        JTextField intCompanyEntry = new JTextField();
        intCompanyEntry.setName("company");
        intCompanyEntry.setBounds(555, 43, 150, 30);
        this.add(intCompanyEntry);

        JTextField intBranchEntry = new JTextField();
        intBranchEntry.setName("branch");
        intBranchEntry.setBounds(555, 77, 150, 30);
        this.add(intBranchEntry);
    }

    private void addText() {
        JLabel intPassText = new JLabel("Password: ", SwingConstants.CENTER);
        intPassText.setBounds(90, 8, 150, 30);
        this.add(intPassText);

        JLabel intNameText = new JLabel("Legal Name: ", SwingConstants.CENTER);
        intNameText.setBounds(90, 43, 150, 30);
        this.add(intNameText);

        JLabel intEmailText = new JLabel("Email Address: ", SwingConstants.CENTER);
        intEmailText.setBounds(90, 77, 150, 30);
        this.add(intEmailText);

        JLabel intFieldText = new JLabel("Field: ", SwingConstants.CENTER);
        intFieldText.setBounds(415, 8, 150, 30);
        this.add(intFieldText);

        JLabel intCompanyText = new JLabel("Company: ", SwingConstants.CENTER);
        intCompanyText.setBounds(415, 43, 150, 30);
        this.add(intCompanyText);

        JLabel intBranchText = new JLabel("Branch: ", SwingConstants.CENTER);
        intBranchText.setBounds(415, 77, 150, 30);
        this.add(intBranchText);
    }
}
