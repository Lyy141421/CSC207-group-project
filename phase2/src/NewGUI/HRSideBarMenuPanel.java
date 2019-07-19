package NewGUI;

import ActionListeners.HRActionListeners.AddOrUpdateJobPostingActionListener;
import ActionListeners.HRActionListeners.HireCandidatesActionListener;
import ActionListeners.HRActionListeners.ReviewApplicationsActionListener;
import ActionListeners.UserActionListeners.ProfileActionListener;
import ActionListeners.UserActionListeners.ReturnHomeActionListener;
import ActionListeners.UserActionListeners.SearchActionListener;
import ActionListeners.HRActionListeners.ViewJobPostingsActionListener;
import CompanyStuff.HRCoordinator;

import javax.swing.*;
import java.awt.*;
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
        this.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        this.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
    }

    /**
     * Create the map for the full high priority tasks sub menu.
     *
     * @return the map for the full high priority tasks sub menu.
     */
    private TreeMap<String, Object> createHighPriorityTasksMenu() {
        TreeMap<String, Object> highPriorityTasksMenu = new TreeMap<>();
        highPriorityTasksMenu.put("1. Review Applications", new ReviewApplicationsActionListener(this.hrCoordinator));
        highPriorityTasksMenu.put("2. Hire Candidates", new HireCandidatesActionListener(this.hrCoordinator));
        return highPriorityTasksMenu;
    }

    /**
     * Create the map for the view job postings sub menu.
     *
     * @return the map for the view job postings sub menu.
     */
    private TreeMap<String, Object> createViewJobPostingsSubMenu() {
        TreeMap<String, Object> viewJobPostingsMenu = new TreeMap<>();
        viewJobPostingsMenu.put("1. Open Job Postings", new ViewJobPostingsActionListener(this.hrCoordinator, "Open"));
        viewJobPostingsMenu.put("2. Closed Job Postings", new ViewJobPostingsActionListener(this.hrCoordinator, "Closed"));
        viewJobPostingsMenu.put("3. Filled Job Postings", new ViewJobPostingsActionListener(this.hrCoordinator, "Filled"));
        viewJobPostingsMenu.put("4. All Job Postings", new ViewJobPostingsActionListener(this.hrCoordinator, "All"));
        return viewJobPostingsMenu;
    }

    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> createJobPostingsMenu() {
        TreeMap<String, Object> jobPostingsMenu = new TreeMap<>();
        jobPostingsMenu.put("1. Add Job Posting", new AddOrUpdateJobPostingActionListener(this.hrCoordinator, "Add"));
        jobPostingsMenu.put("2. Update Job Posting", new AddOrUpdateJobPostingActionListener(this.hrCoordinator, "Update"));
        jobPostingsMenu.put("3. Search Job Posting", new SearchActionListener(this.hrCoordinator, "Job posting"));
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
        fullMenu.put("1. Home", new ReturnHomeActionListener(this.hrCoordinator));
        fullMenu.put("2. Profile", new ProfileActionListener(this.hrCoordinator));
        fullMenu.put("3. High Priority Tasks", this.createHighPriorityTasksMenu());
        fullMenu.put("4. Job Postings", this.createJobPostingsMenu());
        fullMenu.put("5. Search applicant", new SearchActionListener(this.hrCoordinator, "Applicant"));
        return fullMenu;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HRCoordinator HRC = new HRCoordinator();
                JFrame frame = new JFrame("HR Home Page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE); // contrasting bg
                menuPanel.add(new HRSideBarMenuPanel(HRC));

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
