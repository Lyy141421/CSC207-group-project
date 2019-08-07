package GUIClasses.ReferenceInterface;

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

class ReferenceSideBarMenuPanel extends JPanel {

    // === Instance variable ===
    private JPanel cards;   // The cards that are being switched
    private LogoutActionListener logoutActionListener;  // The action listener for logging out


    // === Constructor ===
    ReferenceSideBarMenuPanel(JPanel cards, LogoutActionListener logoutActionListener) {
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
        fullMenu.put("3. Submit Reference Letter", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(ReferenceMain.SUBMIT_REFERENCE_LETTER).start();
            }
        });
        fullMenu.put("4. View Referee Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewThread(ReferenceMain.VIEW_REFEREE_JOB_POSTINGS).start();
            }
        });
        fullMenu.put("5. Logout", logoutActionListener);
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