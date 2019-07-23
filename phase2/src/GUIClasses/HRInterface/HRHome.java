package GUIClasses.HRInterface;

import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class HRHome extends HRPanel {

    GridBagConstraints c;

    private JButton logoutButton;
    private JButton searchButton;
    private JButton browseButton;
    private JButton addButton;
    private JButton toDoButton;

    HRHome(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new GridBagLayout());
        this.c = new GridBagConstraints();

        this.toDoButton = new JButton("To-Do");
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.add(toDoButton, this.c);

        this.createBrowseButton();
        this.createAddButton();
        this.createSearchButton();
        this.addMenu();

        this.logoutButton = new JButton("Logout");
        this.c.gridx = 2;
        this.c.gridy = 2;
        this.c.gridwidth = 1;
        this.c.insets = new Insets(20, 100, 0, 0);
        this.add(logoutButton, this.c);
    }

    void reload() {
        //TODO: update to-do button
    }

    JButton getLogoutButton() {
        return this.logoutButton;
    }

    JButton getBrowseButton() {
        return this.browseButton;
    }

    JButton getToDoButton() {
        return this.toDoButton;
    }

    private void addMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        menu.add(this.browseButton);
        menu.add(this.addButton);
        menu.add(this.searchButton);

        this.c.gridx = 1;
        this.c.gridy = 1;
        this.c.gridwidth = 2;
        this.add(menu, this.c);
    }

    private void createBrowseButton() {
        this.browseButton = new JButton("Browse all job postings");
        this.browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: insert allJP to currJP and change jobPostingList content
                ((CardLayout) parent.getLayout()).show(parent, POSTING);
            }
        });
    }

    private void createSearchButton() {
        this.searchButton = new JButton("Search applicant");
        this.searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, SEARCH);
            }
        });
    }

    private void createAddButton() {
        this.addButton = new JButton("Add job posting");
        this.addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, ADD_POSTING);
            }
        });
    }
}
