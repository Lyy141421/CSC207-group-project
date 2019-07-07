package GUIClasses;

import UsersAndJobObjects.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    LocalDate today;
    private CardLayout layoutManager;
    private JPanel homePanel, login, newUser, HR, interviewer, applicant; //TODO: consider removing

    public MainFrame (LocalDate today) {
        super("GET A JOB");
        today = today;
        initUI();
    }

    public MainFrame () {
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
        this.homePanel.setLayout(new CardLayout());
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
        // We need to be careful with when these cards get constructed, in case it's missing arguments to run methods
        this.login = new LoginPanel(this.homePanel, this.layoutManager); this.homePanel.add(this.login, "LOGIN");
        this.newUser = new NewUserPanel(this.homePanel, this.layoutManager); this.homePanel.add(this.newUser, "NEWUSER");
        // got User object from login/signup
        //interface = new InterfaceFactory.create(User)
        //if User instanceOf ...
        //(cast to xxxInterface) interface
//        add(new HRPanel(interface, today), "HR");
//        add(new InterviewerPanel(), "INTERVIEWER");
//        add(new ApplicantPanel(), "APPLICANT");
        this.add(this.homePanel);
    }

    CardLayout getCardLayout() {
        return this.layoutManager;
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
