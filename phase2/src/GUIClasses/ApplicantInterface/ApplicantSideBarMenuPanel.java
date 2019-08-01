package GUIClasses.ApplicantInterface;

import GUIClasses.CommonUserGUI.SideBarMenuCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class ApplicantSideBarMenuPanel extends JPanel {

    // === Instance variable ===
    private CardLayout affectedLayout;
    private JPanel cards;

    // === Constructor ===
    ApplicantSideBarMenuPanel(JPanel cards, CardLayout layout) {
        this.affectedLayout = layout;
        this.cards = cards;
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