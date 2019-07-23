package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class HRSearchApplicant extends HRPanel{

    private JTextField nameInput;

    HRSearchApplicant(Container contentPane, MethodsTheGUICallsInHR HRInterface, LocalDate today) {
        super(contentPane, HRInterface, today);

        this.setLayout(new BorderLayout());

        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel applicantName = new JLabel("Applicant username");
        this.nameInput = new JTextField(30);

        row.add(applicantName);
        row.add(this.nameInput);
        row.add(createSearchButton());
        this.add(row, BorderLayout.CENTER);

        this.add(this.homeButton, BorderLayout.SOUTH);
    }

    void reload() {

    }

    private JButton createSearchButton () {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JobApplication> apps = HRInterface.getAllJobApplicationsToCompany(nameInput.getText());
                if (apps.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "The applicant cannot be found.");
                } else {
                    currApps = getTitleToAppMap(apps);
                    //Todo: clear exiting JList and add new titles
                    ((CardLayout) parent.getLayout()).show(parent, APPLICATION);
                }
            }
        });

        return searchButton;
    }

}
