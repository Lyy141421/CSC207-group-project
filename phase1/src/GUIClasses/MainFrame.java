package GUIClasses;

import UsersAndJobObjects.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    LocalDate today;
    private CardLayout layoutManager;
    private JPanel login, newUser, HR, interviewer, applicant;
    String currentUser;

    public MainFrame (LocalDate today) {
        super("GET A JOB");
        today = today;
        initUI();
    }

    /**
     * Sets up the parameters used in the main JFrame
     */
    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(854, 480);
        setLayout(new CardLayout()); this.layoutManager = (CardLayout)this.getLayout();
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
        this.login = new LoginPanel(); add(this.login, "LOGIN");
        this.newUser = new NewUserPanel(); add(this.newUser, "NEWUSER");
        // got User object from login/signup
        //interface = new InterfaceFactory.create(User)
        //if User instanceOf ...
        //(cast to xxxInterface) interface
//        add(new HRPanel(interface, today), "HR");
//        add(new InterviewerPanel(), "INTERVIEWER");
//        add(new ApplicantPanel(), "APPLICANT");
    }

    CardLayout getCardLayout() {
        return this.layoutManager;
    }

    void setUser(String username) {
        this.currentUser = username;
    }
}
