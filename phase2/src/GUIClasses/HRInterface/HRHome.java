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

    HRHome(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new GridBagLayout());
        this.c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.add(toDo, this.c);

        this.addMenu();

        this.logoutButton = new JButton("Logout");
        this.c.gridx = 2;
        this.c.gridy = 2;
        this.c.gridwidth = 1;
        this.c.insets = new Insets(20, 100, 0, 0);
        this.add(logoutButton, this.c);
    }

    JButton getLogoutButton() {
        return this.logoutButton;
    }

    private void addMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        menu.add(this.createBrowseButton());
        menu.add(this.createAddButton());
        menu.add(this.createSearchButton());

        this.c.gridx = 1;
        this.c.gridy = 1;
        this.c.gridwidth = 2;
        this.add(menu, this.c);
    }

    private JButton createBrowseButton() {
        JButton browsePosting = new JButton("Browse all job postings");
        browsePosting.setAlignmentX(Component.CENTER_ALIGNMENT);
        browsePosting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: insert allJP to currJP and change jobPostingList content
                ((CardLayout) parent.getLayout()).show(parent, POSTING);
            }
        });

        return browsePosting;
    }

    private JButton createSearchButton() {
        JButton searchApplicant = new JButton("Search applicant");
        searchApplicant.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchApplicant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, SEARCH);
            }
        });

        return searchApplicant;
    }

    private JButton createAddButton() {
        JButton addPosting = new JButton("Add job posting");
        addPosting.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPosting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) parent.getLayout()).show(parent, ADD_POSTING);
            }
        });

        return addPosting;
    }
}
