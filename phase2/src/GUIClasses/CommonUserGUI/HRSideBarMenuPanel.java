package GUIClasses.CommonUserGUI;

import GUIClasses.ActionListeners.ProfileActionListener;
import GUIClasses.ActionListeners.ReturnHomeActionListener;
import CompanyStuff.HRCoordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class HRSideBarMenuPanel extends JPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 5;

    // === Instance variable ===
    private HRCoordinator hrCoordinator;

    // === Constructor ===
    private HRSideBarMenuPanel(HRCoordinator hrCoordinator) {
        this.hrCoordinator = hrCoordinator;
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenuCreator(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        this.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
    }

    /**
     * Create the map for the full high priority tasks sub menu.
     *
     * @return the map for the full high priority tasks sub menu.
     */
    private TreeMap<String, Object> createHighPriorityTasksMenu() {
        TreeMap<String, Object> highPriorityTasksMenu = new TreeMap<>();
        highPriorityTasksMenu.put("1. Review Applications", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        highPriorityTasksMenu.put("2. Hire Candidates", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return highPriorityTasksMenu;
    }

    /**
     * Create the map for the view job postings sub menu.
     *
     * @return the map for the view job postings sub menu.
     */
    private TreeMap<String, Object> createViewJobPostingsSubMenu() {
        TreeMap<String, Object> viewJobPostingsMenu = new TreeMap<>();
        viewJobPostingsMenu.put("1. Open Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewJobPostingsMenu.put("2. Closed Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewJobPostingsMenu.put("3. Filled Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewJobPostingsMenu.put("4. All Job Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return viewJobPostingsMenu;
    }

    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> createJobPostingsMenu() {
        TreeMap<String, Object> jobPostingsMenu = new TreeMap<>();
        jobPostingsMenu.put("1. Add Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jobPostingsMenu.put("2. Update Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jobPostingsMenu.put("3. Search Job Posting", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jobPostingsMenu.put("4. View Job Postings", this.createViewJobPostingsSubMenu());
        return jobPostingsMenu;
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
        fullMenu.put("3. High Priority Tasks", this.createHighPriorityTasksMenu());
        fullMenu.put("4. Job Postings", this.createJobPostingsMenu());
        fullMenu.put("5. Search applicant", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return fullMenu;
    }
}
