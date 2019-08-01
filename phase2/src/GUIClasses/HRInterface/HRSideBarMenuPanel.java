package GUIClasses.HRInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;
import GUIClasses.CommonUserGUI.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

class HRSideBarMenuPanel extends JPanel {

    // === Instance variable ===
    private JPanel cards;
    private LogoutActionListener logoutActionListener;

    // === Constructor ===
    HRSideBarMenuPanel(JPanel cards, LogoutActionListener logoutActionListener) {
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
        fullMenu.put("1. Home", new ReturnHomeActionListener());
        fullMenu.put("2. Profile", new ProfileActionListener());
        fullMenu.put("3. High priority tasks", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.HIGH_PRIORITY_POSTINGS);
            }
        });
        fullMenu.put("4. Add Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.ADD_POSTING);
            }
        });
        fullMenu.put("5. Browse Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.BROWSE_POSTINGS);
            }
        });
        fullMenu.put("6. Search applicant", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((UserPanel) cards.getParent()).refresh();
                ((CardLayout) cards.getLayout()).show(cards, HRPanel.SEARCH_APPLICANT);
            }
        });
        fullMenu.put("7. Logout", logoutActionListener);
        return fullMenu;
    }
}