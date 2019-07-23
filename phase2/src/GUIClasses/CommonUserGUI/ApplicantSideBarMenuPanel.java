package GUIClasses.CommonUserGUI;

import ActionListeners.ViewUploadedDocumentsActionListener;
import ActionListeners.ProfileActionListener;
import ActionListeners.ReturnHomeActionListener;
import ApplicantStuff.Applicant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class ApplicantSideBarMenuPanel extends JPanel {
    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 5;

    // === Instance variable ===
    private Applicant applicant;

    // === Constructor ===
    private ApplicantSideBarMenuPanel(Applicant applicant) {
        this.applicant = applicant;
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
        fullMenu.put("3. View Uploaded Documents", new ViewUploadedDocumentsActionListener(this.applicant));
        fullMenu.put("3. View Schedule", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fullMenu.put("4. Browse Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fullMenu.put("5. Withdraw Application", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return fullMenu;
    }

}
