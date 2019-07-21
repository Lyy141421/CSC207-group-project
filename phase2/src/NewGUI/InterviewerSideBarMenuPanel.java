package NewGUI;

import ActionListeners.ProfileActionListener;
import ActionListeners.ReturnHomeActionListener;
import CompanyStuff.Interviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class InterviewerSideBarMenuPanel extends JPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 6;

    // === Constructor ===
    private InterviewerSideBarMenuPanel(Interviewer interviewer) {
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        this.setLayout(new BorderLayout());
        this.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        this.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
    }


    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> viewIntervieweesMenu() {
        TreeMap<String, Object> viewIntervieweesMenu = new TreeMap<>();
        viewIntervieweesMenu.put("1. Search Interview", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewIntervieweesMenu.put("2. View All Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return viewIntervieweesMenu;
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
        fullMenu.put("3. Schedule Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fullMenu.put("4. View schedule", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fullMenu.put("5. Complete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fullMenu.put("6. View Incomplete Interviews", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return fullMenu;
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
                menuPanel.add(new InterviewerSideBarMenuPanel(interviewer));

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
