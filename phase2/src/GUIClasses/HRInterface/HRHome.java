package GUIClasses.HRInterface;

import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class HRHome extends HRPanel {

    HRHome(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton toDo = new JButton("To-Do");
        c.gridx = 0;
        c.gridy = 0;
        this.add(toDo, c);

        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));
        manual.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        manual.add(this.createBrowseButton());
        manual.add(this.createAddButton());
        manual.add(this.createSearchButton());

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        this.add(manual, c);

        JButton logout = new JButton("Logout");

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(20, 100, 0, 0);
        this.add(logout, c);
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
