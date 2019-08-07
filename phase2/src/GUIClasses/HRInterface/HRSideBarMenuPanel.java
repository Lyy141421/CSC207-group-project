package GUIClasses.HRInterface;

import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import GUIClasses.CommonUserGUI.SideBarMenuCreator;
import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
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
        fullMenu.put("1. Home", new ReturnHomeActionListener(cards));
        fullMenu.put("2. Profile", new ProfileActionListener(cards));
        fullMenu.put("3. High priority tasks", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(HRPanel.HIGH_PRIORITY_POSTINGS).start();
            }
        });
        fullMenu.put("4. Add Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(HRPanel.ADD_POSTING).start();
            }
        });
        fullMenu.put("5. Update Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(HRPanel.UPDATE_POSTING).start();
            }
        });
        fullMenu.put("6. Browse Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(HRPanel.BROWSE_POSTINGS).start();
            }
        });
        fullMenu.put("7. Search applicant", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(HRPanel.SEARCH_APPLICANT).start();
            }
        });
        fullMenu.put("8. Logout", logoutActionListener);
        return fullMenu;
    }

    private Thread createNewThread(String key) {
        Thread newThread = new Thread() {
            public void run() {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            ((UserMain) cards.getParent()).refresh();
                            ((CardLayout) cards.getLayout()).show(cards, key);
                        }
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return newThread;
    }
}