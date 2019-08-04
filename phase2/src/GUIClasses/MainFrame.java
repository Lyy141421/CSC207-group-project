package GUIClasses;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.StartInterface.LoginMain;
import Main.JobApplicationSystem;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import GUIClasses.StartInterface.NewUserPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MainFrame extends JFrame {

    public static final String LOGIN = "LOGIN";
    public static final String NEW_USER = "NEW_USER";
    public static final String USER_PANEL = "USER_PANEL";

    private JobApplicationSystem jobAppSystem;
    private CardLayout layoutManager = new CardLayout();
    private NewUserPanel newUserRef;

    public MainFrame(JobApplicationSystem jobAppSystem) {
        super("GET A JOB");
        this.jobAppSystem = jobAppSystem;
        initUI();
        addCards();
    }

    private MainFrame () {
        super("TESTING INSTANTIATOR DONT USE");
        //this.homePanel = new JPanel(this.layoutManager);
        //this.add(homePanel);
        initUI();
        addCards();
    }

    /**
     * Sets up the parameters used in the main JFrame
     */
    private void initUI () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(854, 480);
        this.getContentPane().setLayout(layoutManager);
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
        newUserRef = new NewUserPanel(this.getContentPane(), this.layoutManager, this.jobAppSystem);
        this.add(newUserRef, MainFrame.NEW_USER);
        this.add(new LoginMain(newUserRef, this.getContentPane(), this.layoutManager, this.jobAppSystem), MainFrame.LOGIN);
        layoutManager.show(this.getContentPane(), MainFrame.LOGIN);
    }

    public static void main(String[] args) {
        MainFrame test = new MainFrame();
    }
}
