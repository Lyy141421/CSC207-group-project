package GUIClasses.StartInterface;

import javax.swing.*;

class NewReferenceForm extends JPanel {

    static final String NAME = "ReferenceCard";

    NewReferenceForm() {
        this.setLayout(null);
        this.setName(NAME);

        JLabel refEmailText = new JLabel("Email: ", SwingConstants.CENTER);
        refEmailText.setBounds(267, 60, 150, 30);
        this.add(refEmailText);

        JTextField refEmailEntry = new JTextField();
        refEmailEntry.setName("email");
        refEmailEntry.setBounds(437, 60, 150, 30);
        this.add(refEmailEntry);
    }
}
