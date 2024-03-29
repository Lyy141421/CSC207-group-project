package GUIClasses.InterviewerInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

class InterviewerSideBarMenuPanel extends JPanel {
    /**
     * The side bar menu that is constant throughout the Interviewer GUI.
     */

    // === Instance variables ===
    private JPanel cards;   // The cards in the interviewer GUI
    private LogoutActionListener logoutActionListener;  // The action listener for logging out

    // === Constructor ===
    InterviewerSideBarMenuPanel(JPanel cards, LogoutActionListener logoutActionListener) {
        this.cards = cards;
        this.logoutActionListener = logoutActionListener;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenuCreator(fullMenu).createMenuBar());
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Home", new ReturnHomeActionListener(cards));
        fullMenu.put("2. Profile", new ProfileActionListener(cards));
        fullMenu.put("3. Schedule Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.SCHEDULE);
            }
        });
        fullMenu.put("4. View Interviewees", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.INCOMPLETE);
            }
        });
        fullMenu.put("5. Complete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserMain) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, InterviewerMain.COMPLETE);
            }
        });
        fullMenu.put("6. Logout", logoutActionListener);
        return fullMenu;
    }
}