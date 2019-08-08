package GUIClasses;

import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.StartInterface.LoginMain;
import Main.JobApplicationSystem;

import GUIClasses.StartInterface.NewUserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
//        this.addWindowListener();
    }

//    private void addWindowListener() {
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
//                new DataLoaderAndStorer(jobAppSystem).storeAllData();
//            }
//        });
//    }

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
    public void addCards() {
        // We need to be careful with when these cards get constructed, in case it's missing arguments to run methods
        newUserRef = new NewUserPanel(this.getContentPane(), this.layoutManager, this.jobAppSystem);
        this.add(newUserRef, MainFrame.NEW_USER);
        this.add(new LoginMain(newUserRef, this.getContentPane(), this.layoutManager, this.jobAppSystem), MainFrame.LOGIN);
        layoutManager.show(this.getContentPane(), MainFrame.LOGIN);
    }
}
