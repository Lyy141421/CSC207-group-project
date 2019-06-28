package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {

    public MainFrame () {
        super("GET A JOB");
        initUI();
    }

    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(854, 480);
        setLayout(new CardLayout());
        addCards();
        setVisible(true);
        setResizable(false);
    }

    // Call methods that create each interface and add to main frame.
    private void addCards () {
        add(new HRPanel());
        add(new LoginPanel());
        add(new NewUserPanel());
    }
}
