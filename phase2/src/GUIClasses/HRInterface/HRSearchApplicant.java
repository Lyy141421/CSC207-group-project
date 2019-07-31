package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class HRSearchApplicant extends HRPanel{

    private JPanel parent;
    private JTextField nameInput;
    private JPanel inputPanel;
    HashMap<String, JobApplication> currApps;

    HRSearchApplicant(HRBackend hrBackend, JPanel parent) {
        super(hrBackend);
        this.parent = parent;
        this.setLayout(new BorderLayout());

        inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel applicantName = new JLabel("Applicant username");
        this.nameInput = new JTextField(30);

        inputPanel.add(applicantName);
        inputPanel.add(this.nameInput);
        inputPanel.add(createSearchButton());
        this.add(inputPanel, BorderLayout.CENTER);
    }

    private JButton createSearchButton () {
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<JobApplication> apps = hrBackend.getAllJobApplicationsToCompany(nameInput.getText());
                if (apps.isEmpty()) {
                    JOptionPane.showMessageDialog(inputPanel, "The applicant cannot be found.");
                } else {
                    HRViewApp appPanel = new HRViewApp(parent, hrBackend, getTitleToAppMap(apps), HRPanel.SEARCH_APPLICANT, 0);
                    parent.remove(4);
                    parent.add(appPanel, APPLICATION);
                    ((CardLayout) parent.getLayout()).show(parent, APPLICATION);
                }
            }
        });

        return searchButton;
    }

}
