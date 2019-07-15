package NewGUI;

import Actions.*;
import CompanyStuff.HRCoordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class HRHomePanel extends JPanel {

    // === Class variables ===
    private static int WINDOW_WIDTH = 500;
    private static int WINDOW_HEIGHT = 250;

    // === Instance variable ===
    private HRCoordinator HRC;

    // === Constructor ===
    HRHomePanel(HRCoordinator HRC) {
        this.HRC = HRC;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        TreeMap<String, ActionListener> highPriorityTasksMenu = new TreeMap<>();
        highPriorityTasksMenu.put("1. Review Applications", new ReviewApplicationsActionListener(this.HRC));
        highPriorityTasksMenu.put("2. Hire Candidates", new HireCandidatesActionListener(this.HRC));

        TreeMap<String, ActionListener> viewJobPostingsMenu = new TreeMap<>();
        viewJobPostingsMenu.put("1. Open Job Postings", new ViewJobPostingActionListener(this.HRC, "Open"));
        viewJobPostingsMenu.put("2. Closed Job Postings", new ViewJobPostingActionListener(this.HRC, "Closed"));
        viewJobPostingsMenu.put("3. Filled Job Postings", new ViewJobPostingActionListener(this.HRC, "Filled"));
        viewJobPostingsMenu.put("4. All Job Postings", new ViewJobPostingActionListener(this.HRC, "All"));

        TreeMap<String, Object> jobPostingsMenu = new TreeMap<>();
        jobPostingsMenu.put("1. Add Job Posting", new AddOrUpdateJobPostingActionListener("Add"));
        jobPostingsMenu.put("2. Update Job Posting", new AddOrUpdateJobPostingActionListener("Update"));
        jobPostingsMenu.put("3. Search Job Posting", new HRCSearchActionListener(this.HRC, "Job posting"));
        jobPostingsMenu.put("4. View Job Postings", viewJobPostingsMenu);


        TreeMap<String, Object> menuTitles = new TreeMap<>();
        menuTitles.put("1. Profile", new ProfileActionListener(this.HRC));
        menuTitles.put("2. High Priority Tasks", highPriorityTasksMenu);
        menuTitles.put("3. Job Postings", jobPostingsMenu);
        menuTitles.put("4. Search applicant", new HRCSearchActionListener(this.HRC, "Applicant"));


        JLabel title = new JLabel("HR Home Page");
        title.setVerticalTextPosition(SwingConstants.TOP);
        title.setHorizontalTextPosition(SwingConstants.CENTER);
        title.invalidate();

        JFrame frame = new JFrame("HR Home Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(title);

        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.WHITE); //contrasting bg
        contentPane.add(new SideBarMenu(menuTitles).createMenuBar(), BorderLayout.LINE_START);

        //Display the window.
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HRCoordinator HRC = new HRCoordinator();
                new HRHomePanel(HRC).createAndShowGUI();
            }
        });
    }
}
