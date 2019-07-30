package GUIClasses.ApplicantInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class ApplicantSideBarMenuPanel extends JPanel {
    // === Class variables ===
    private static int CELL_WIDTH = 170;
    private static int CELL_HEIGHT = 30;
    private static int NUM_MAIN_MENU_OPTIONS = 7;

    // === Instance variable ===
    private CardLayout affectedLayout;
    private JPanel cards;

    // === Constructor ===
    ApplicantSideBarMenuPanel(JPanel cards, CardLayout layout) {
        this.affectedLayout = layout;
        this.cards = cards;
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
        fullMenu.put("1. Home", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "HOME");}});
        fullMenu.put("2. Browse Postings", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "POSTINGS");}});
        fullMenu.put("3. Profile", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "PROFILE");}});
        fullMenu.put("4. View Uploaded Documents", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "DOCUMENTS");}});
        fullMenu.put("5. View Schedule", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "SCHEDULE");}});
        fullMenu.put("6. Withdraw Application", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {affectedLayout.show(cards, "MANAGE");}});
        fullMenu.put("7. Logout", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }});
        return fullMenu;
    }

}
