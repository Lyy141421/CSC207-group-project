package GUIClasses.InterviewerInterface;

import GUIClasses.ActionListeners.CardLayoutPanelGetter;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import Main.JobApplicationSystem;
import GUIClasses.CommonUserGUI.SideBarMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class InterviewerSideBarMenuPanel extends JPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 6;

    // === Instance variables ===
    private LogoutActionListener logoutActionListener;

    // === Constructor ===
    InterviewerSideBarMenuPanel(LogoutActionListener logoutActionListener) {
        this.logoutActionListener = logoutActionListener;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        this.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Home", new ReturnHomeActionListener());
        fullMenu.put("2. Profile", new ProfileActionListener());
        fullMenu.put("3. Schedule Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, InterviewerMain.SCHEDULE);
            }
        });
        fullMenu.put("4. View Interviewees", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, InterviewerMain.INCOMPLETE);
            }
        });
        fullMenu.put("5. Add Interview Notes", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, InterviewerMain.ADD_NOTES);
            }
        });
        fullMenu.put("6. Complete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel cards = new CardLayoutPanelGetter().fromMenuItemDirectlyOnMenuBar(e);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, InterviewerMain.COORDINATOR);
            }
        });
        fullMenu.put("7. Logout", logoutActionListener);
        return fullMenu;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JobApplicationSystem jobAppSystem = new JobApplicationSystem();
                LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(),
                        new CardLayout(), jobAppSystem);

                JFrame frame = new JFrame("Interviewer Home Page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE); // contrasting bg
                menuPanel.add(new InterviewerSideBarMenuPanel(logoutActionListener));

                Container contentPane = frame.getContentPane();
                contentPane.setBackground(Color.WHITE); //contrasting bg

                frame.add(menuPanel, BorderLayout.LINE_START);

                //Display the window.
                frame.setSize(500, 250);
                frame.setVisible(true);
            }
        });
    }

}
