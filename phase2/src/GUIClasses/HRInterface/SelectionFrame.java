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

abstract class SelectionFrame extends JInternalFrame{

    MethodsTheGUICallsInHR HRInterface;

    HashMap<JCheckBox, JobApplication> checkBoxToAppMap = new HashMap<>();
    GridBagConstraints c = new GridBagConstraints();

    JButton confirmButton;

    SelectionFrame(MethodsTheGUICallsInHR HRInterface, ArrayList<JobApplication> applications) {
        super("Select");
        this.HRInterface = HRInterface;

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
            JCheckBox checkBox = new JCheckBox(app.getApplicant().getLegalName());
            checkBoxToAppMap.put(checkBox, app);
            c.gridy += (c.gridx+1)/3;
            c.gridx = (c.gridx+1)%3;
            this.add(checkBox, c);
        }
    }

    private void addConfirmButton() {
        c.gridy++;
        c.gridx = 1;
        this.confirmButton = new JButton("Confirm");
        this.addConfirmListener();
        this.add(confirmButton, c);
    }

    abstract void addConfirmListener();

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
}
