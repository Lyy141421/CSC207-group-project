import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame () {
        super("GET A JOB");
        initUI();
    }

    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLayout(new CardLayout());
        addCards();
        setVisible(true);
    }

    // Call methods that create each interface and add to main frame.
    private void addCards () {
        add(HRInterface());
    }

    // Create interface for HR
    private JPanel HRInterface () {
        JPanel HRPanel = new JPanel();
        HRPanel.setLayout(new CardLayout());
        return HRPanel;
    }
}
