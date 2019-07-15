package NewGUI;

import Actions.*;
import CompanyStuff.HRCoordinator;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

public class HRSideBarMenuPanel extends JPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 4;

    // === Instance variable ===
    private HRCoordinator HRC;

    // === Constructor ===
    private HRSideBarMenuPanel(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    /**
     * Create the map for the full high priority tasks sub menu.
     *
     * @return the map for the full high priority tasks sub menu.
     */
    private TreeMap<String, Object> createHighPriorityTasksMenu() {
        TreeMap<String, Object> highPriorityTasksMenu = new TreeMap<>();
        highPriorityTasksMenu.put("1. Review Applications", new ReviewApplicationsActionListener(this.HRC));
        highPriorityTasksMenu.put("2. Hire Candidates", new HireCandidatesActionListener(this.HRC));
        return highPriorityTasksMenu;
    }

    /**
     * Create the map for the view job postings sub menu.
     *
     * @return the map for the view job postings sub menu.
     */
    private TreeMap<String, Object> createViewJobPostingsSubMenu() {
        TreeMap<String, Object> viewJobPostingsMenu = new TreeMap<>();
        viewJobPostingsMenu.put("1. Open Job Postings", new ViewJobPostingActionListener(this.HRC, "Open"));
        viewJobPostingsMenu.put("2. Closed Job Postings", new ViewJobPostingActionListener(this.HRC, "Closed"));
        viewJobPostingsMenu.put("3. Filled Job Postings", new ViewJobPostingActionListener(this.HRC, "Filled"));
        viewJobPostingsMenu.put("4. All Job Postings", new ViewJobPostingActionListener(this.HRC, "All"));
        return viewJobPostingsMenu;
    }

    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> createJobPostingsMenu() {
        TreeMap<String, Object> jobPostingsMenu = new TreeMap<>();
        jobPostingsMenu.put("1. Add Job Posting", new AddOrUpdateJobPostingActionListener("Add"));
        jobPostingsMenu.put("2. Update Job Posting", new AddOrUpdateJobPostingActionListener("Update"));
        jobPostingsMenu.put("3. Search Job Posting", new SearchActionListener(this.HRC, "Job posting"));
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
        fullMenu.put("1. Profile", new ProfileActionListener(this.HRC));
        fullMenu.put("2. High Priority Tasks", this.createHighPriorityTasksMenu());
        fullMenu.put("3. Job Postings", this.createJobPostingsMenu());
        fullMenu.put("4. Search applicant", new SearchActionListener(this.HRC, "Applicant"));
        return fullMenu;
    }


    /**
     * Create the panel with the menu bar.
     */
    private JPanel createPanel() {
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        panel.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
        return panel;

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
                menuPanel.add(new HRSideBarMenuPanel(HRC).createPanel());

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
