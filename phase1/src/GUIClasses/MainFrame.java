package GUIClasses;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    LocalDate today;
    private CardLayout layoutManager;
    private JPanel homePanel; NewUserPanel newUserRef;

    public MainFrame (LocalDate today) {
        super("GET A JOB");
        today = today;
        initUI();
    }

    private MainFrame () {
        super("TESTING INSTANTIATOR DONT USE");
        this.homePanel = new JPanel (new CardLayout());
        initUI();
        addCards();
    }

    /**
     * Sets up the parameters used in the main JFrame
     */
    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(854, 480);
        this.layoutManager = (CardLayout)this.homePanel.getLayout();
        addCards();
        setVisible(true);
        setResizable(false);
    }

    /**
     * Constructs each of the JPanels used within the JFrame
     */
    // Call methods that create each interface and add to main frame.
    private void addCards () {
        this.add(this.homePanel);
        // We need to be careful with when these cards get constructed, in case it's missing arguments to run methods
        this.homePanel.add(new LoginPanel(this.homePanel, this.layoutManager), "LOGIN");
        this.newUserRef = new NewUserPanel(this.homePanel, this.layoutManager);
        this.homePanel.add(this.newUserRef, "NEWUSER");
        // got User object from login/signup
        //interface = new InterfaceFactory.create(User)
        //if User instanceOf ...
        //(cast to xxxInterface) interface
//        add(new HRPanel(interface, today), "HR");
//        add(new InterviewerPanel(), "INTERVIEWER");
//        add(new ApplicantPanel(), "APPLICANT");
    }

    public static void main(String[] args) {
        MainFrame test = new MainFrame();
    }
}
