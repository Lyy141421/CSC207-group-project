package GUIClasses.StartInterface;

import javax.swing.*;
import java.awt.*;

/**
 * Text and JComboBox shown when selecting which type of user will be created
 */
class NewUserSelector extends JPanel {
    private JComboBox selector;

    NewUserSelector() {
        this.setLayout(null);

        JLabel titleText = new JLabel("Create New Account", SwingConstants.CENTER);
        titleText.setBounds(227, 20, 400, 70);
        titleText.setFont(new Font("Serif", Font.PLAIN, 25));
        this.add(titleText);

        JLabel typeSelector = new JLabel("What type of account is being created?", SwingConstants.CENTER);
        typeSelector.setBounds(247, 105, 250, 30);
        this.add(typeSelector);

        String[] userTypes = {"Applicant", "Interviewer", "HR Coordinator", "Reference"};
        JComboBox selectorBox = new JComboBox(userTypes);
        selectorBox.setBounds(497, 105, 120, 30);
        this.selector = selectorBox;
        this.add(selectorBox);
    }

    JComboBox getSelector() {
        return selector;
    }
}
