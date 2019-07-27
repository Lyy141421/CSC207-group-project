package GUIClasses.InterviewerInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
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
    private JPanel cards;
    private LogoutActionListener logoutActionListener;

    // === Constructor ===
    InterviewerSideBarMenuPanel(JPanel cards, LogoutActionListener logoutActionListener) {
        this.cards = cards;
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
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.SCHEDULE);
            }
        });
        fullMenu.put("4. View Interviewees", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.INCOMPLETE);
            }
        });
        fullMenu.put("5. Add Interview Notes", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.ADD_NOTES);
            }
        });
        fullMenu.put("6. Complete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.COORDINATOR);
            }
        });
        fullMenu.put("7. Logout", logoutActionListener);
        return fullMenu;
    }
}
