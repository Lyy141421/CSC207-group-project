package NewGUI;

import Actions.*;
import CompanyStuff.Interviewer;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

public class InterviewerSideBarMenuPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 5;

    // === Instance variable ===
    private Interviewer interviewer;

    // === Constructor ===
    private InterviewerSideBarMenuPanel(Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> viewIntervieweesMenu() {
        TreeMap<String, Object> viewIntervieweesMenu = new TreeMap<>();
        viewIntervieweesMenu.put("1. Search Interviewee", new SearchActionListener(this.interviewer, "Applicant"));
        viewIntervieweesMenu.put("2. View All Interviewees", new ViewAllApplicantsActionListener(this.interviewer));
        return viewIntervieweesMenu;
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Profile", new ProfileActionListener(this.interviewer));
        fullMenu.put("2. Schedule Interviews", new ScheduleInterviewsActionListener(this.interviewer));
        fullMenu.put("3. View schedule", new ViewScheduleActionListener(this.interviewer));
        fullMenu.put("4. Complete Interviews", new CompleteInterviewsActionListener(this.interviewer));
        fullMenu.put("5. View Interviewees", this.viewIntervieweesMenu());
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
                Interviewer interviewer = new Interviewer();
                JFrame frame = new JFrame("Interviewer Home Page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE); // contrasting bg
                menuPanel.add(new InterviewerSideBarMenuPanel(interviewer).createPanel());

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
