package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectionFrame extends JInternalFrame implements ItemListener {

    MethodsTheGUICallsInHR HRInterface;

    private int availablePositions;
    HashMap<JCheckBox, JobApplication> checkboxToAppMap = new HashMap<>();
    GridBagConstraints c = new GridBagConstraints();

    SelectionFrame(MethodsTheGUICallsInHR HRInterface, ArrayList<JobApplication> applications) {
        super("Select");
        this.HRInterface = HRInterface;
        this.availablePositions = applications.get(0).getJobPosting().getNumPositions();

        this.setLayout(new GridBagLayout());

        this.addApplicants(applications);
        this.addConfirmButton();
        this.addCancelButton();
        this.addPrompt();
    }

    private void addPrompt() {
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel prompt = new JLabel("Please select from the following applicants");
        this.add(prompt, c);
    }

    private void addApplicants(ArrayList<JobApplication> applications) {
        c.gridx = -1;
        c.gridy = 0;
        for (JobApplication app : applications) {
            JCheckBox checkbox = new JCheckBox(app.getApplicant().getLegalName());
            checkbox.addItemListener(this);
            checkboxToAppMap.put(checkbox, app);
            c.gridx = (c.gridx+1)%3;
            if (c.gridx == 0) {
                c.gridy++;
            }
            this.add(checkbox, c);
        }
    }

    private void addConfirmButton() {
        c.gridy++;
        c.gridx = 1;
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Send warning if not enough applicants selected
                //TODO: hire the selected applicants
            }
        });
        this.add(confirmButton, c);
    }

    private void addCancelButton() {
        c.gridx = 2;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.add(cancelButton, c);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox changedSelection = (JCheckBox) e.getItemSelectable();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            availablePositions--;
            if (availablePositions <0) {
                changedSelection.setSelected(false);
                availablePositions++;
                JOptionPane.showMessageDialog(this, "Selection exceeded number of positions available.");
            }
        } else {
            availablePositions++;
        }
    }
}
