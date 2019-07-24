package GUIClasses.StartInterface;

import javax.swing.*;

public class NewReferenceForm extends JPanel {
    NewReferenceForm() {
        this.setLayout(null);

        JLabel refEmailText = new JLabel("Email: ", SwingConstants.CENTER);
        refEmailText.setBounds(267, 60, 150, 30);
        this.add(refEmailText);

        JTextField refEmailEntry = new JTextField();
        refEmailEntry.setName("email");
        refEmailEntry.setBounds(437, 60, 150, 30);
        this.add(refEmailEntry);
    }
}
