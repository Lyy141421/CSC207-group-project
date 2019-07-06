package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    LocalDate today;

    public MainFrame (LocalDate today) {
        super("GET A JOB");
        today = today;
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
        // We need to be careful with when these cards get constructed, in case it's missing arguments to run methods
        add(new LoginPanel(), "LOGIN");
        add(new NewUserPanel(), "NEWUSER");
        // got User object from login/signup
        //interface = new InterfaceFactory.create(User)
        //if User instanceOf ...
        //(cast to xxxInterface) interface
//        add(new HRPanel(interface, today), "HR");
//        add(new InterviewerPanel(), "INTERVIEWER");
//        add(new ApplicantPanel(), "APPLICANT");
    }
}
