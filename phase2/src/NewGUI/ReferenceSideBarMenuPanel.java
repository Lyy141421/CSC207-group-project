package NewGUI;

import ActionListeners.ReferenceActionListeners.SubmitReferenceLetterActionListener;
import ActionListeners.UserActionListeners.*;
import ApplicantStuff.Reference;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

public class ReferenceSideBarMenuPanel {

    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 20;
    private static int NUM_MAIN_MENU_OPTIONS = 4;

    // === Instance variable ===
    private Reference reference;


    // === Constructor ===
    ReferenceSideBarMenuPanel(Reference reference) {
        this.reference = reference;
    }

    /**
     * Create the map for the full job postings sub menu.
     *
     * @return the map for the full job postings sub menu.
     */
    private TreeMap<String, Object> viewJobPostingsOfRefereesMenu() {
        TreeMap<String, Object> viewIntervieweesMenu = new TreeMap<>();
        viewIntervieweesMenu.put("1. Search Referee", new SearchActionListener(this.reference, "Applicant"));
        viewIntervieweesMenu.put("2. View All Referees", new ViewAllApplicantsActionListener(this.reference));
        return viewIntervieweesMenu;
    }

    /**
     * Create the map for the full menu.
     *
     * @return the map for the full menu.
     */
    private TreeMap<String, Object> createFullMenu() {
        TreeMap<String, Object> fullMenu = new TreeMap<>();
        fullMenu.put("1. Home", new ReturnHomeActionListener(this.reference));
        fullMenu.put("2. Profile", new ProfileActionListener(this.reference));
        fullMenu.put("3. Submit Reference Letter", new SubmitReferenceLetterActionListener(this.reference));
        fullMenu.put("4. View Referees", this.viewJobPostingsOfRefereesMenu());
        return fullMenu;
    }


    /**
     * Create the panel with the menu bar.
     */
    JPanel createPanel() {
        TreeMap<String, Object> fullMenu = this.createFullMenu();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new SideBarMenu(fullMenu, CELL_WIDTH, CELL_HEIGHT).createMenuBar());
        panel.setSize(CELL_WIDTH, CELL_HEIGHT * NUM_MAIN_MENU_OPTIONS);
        return panel;
    }
}
