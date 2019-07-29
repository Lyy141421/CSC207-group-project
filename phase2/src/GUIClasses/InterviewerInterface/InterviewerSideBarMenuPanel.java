package GUIClasses.InterviewerInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

class InterviewerSideBarMenuPanel extends JPanel {
    /**
     * The side bar menu that is constant throughout the Interviewer GUI.
     */

    // === Static variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 6;

    // === Instance variables ===
    private JPanel cards;   // The cards in the interviewer GUI
    private LogoutActionListener logoutActionListener;  // The action listener for logging out

    // === Constructor ===
    InterviewerSideBarMenuPanel(JPanel cards, LogoutActionListener logoutActionListener) {
        this.cards = cards;
        this.logoutActionListener = logoutActionListener;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenuCreator(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
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
                ((CardLayout) cards.getLayout()).show(cards, InterviewerPanel.SCHEDULE);
            }
        });
        fullMenu.put("4. View Interviewees", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerPanel.INCOMPLETE);
            }
        });
        fullMenu.put("5. Complete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerPanel.COMPLETE);
            }
        });
        fullMenu.put("7. Logout", logoutActionListener);
        return fullMenu;
    }
}