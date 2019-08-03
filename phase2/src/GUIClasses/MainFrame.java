package GUIClasses;

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

    private LocalDate getToday; // TODO delete
    private JobApplicationSystem jobAppSystem;
    private CardLayout layoutManager = new CardLayout();
    private NewUserPanel newUserRef;    // TODO delete?

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

        // TODO delete
//        LocalDate actualToday = LocalDate.now();
//        UtilDateModel dateModel = new UtilDateModel();
//        dateModel.setDate(actualToday.getYear(), actualToday.getMonthValue()-1, actualToday.getDayOfMonth());
//        dateModel.setSelected(true);
//        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
//        JDatePickerImpl manualToday = new JDatePickerImpl(datePanel);
//
//        // When confirmed this is the date they want:
//        // This converts the Date object from JDatePicker to a LocalDate object
//        getToday = ((Date) manualToday.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Constructs each of the JPanels used within the JFrame
     */
    // Call methods that create each interface and add to main frame.
    private void addCards () {
        // We need to be careful with when these cards get constructed, in case it's missing arguments to run methods
        NewUserPanel newUserRef = new NewUserPanel(this.getContentPane(), this.layoutManager, this.jobAppSystem);
        this.add(newUserRef, MainFrame.NEW_USER);
        this.add(new LoginMain(this.newUserRef, this.getContentPane(), this.layoutManager, this.jobAppSystem), MainFrame.LOGIN);
    }

    public static void main(String[] args) {
        MainFrame test = new MainFrame();
    }
}
