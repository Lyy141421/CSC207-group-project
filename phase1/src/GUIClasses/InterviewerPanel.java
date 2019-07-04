package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterviewerPanel extends JPanel implements ActionListener {

    InterviewerPanel () {
        this.setLayout(new CardLayout());
        this.add(home(), "HOME");
        this.add(viewInterviews(), "VIEW");
        this.add(reviewInterviews(), "REVIEW");
    }

    private JPanel home () {
        JPanel homePanel = new JPanel();

        return homePanel;
    }

    private JPanel viewInterviews () {
        JPanel viewPanel = new JPanel();

        return viewPanel;
    }

    private JPanel reviewInterviews() {
        JPanel reviewPanel = new JPanel();

        return reviewPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
