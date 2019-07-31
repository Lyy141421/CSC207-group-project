package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

abstract class SelectionFrame extends JInternalFrame{

    HRBackEnd HRInterface;

    HashMap<JCheckBox, JobApplication> checkBoxToAppMap = new HashMap<>();
    GridBagConstraints c = new GridBagConstraints();

    JButton confirmButton;
    JButton returnButton;

    SelectionFrame(HRBackEnd HRInterface, ArrayList<JobApplication> applications, JButton returnButton, int toSelect) {
        super("Select");
        this.HRInterface = HRInterface;
        this.returnButton = returnButton;

        this.setLayout(new GridBagLayout());

        this.addApplicants(applications, toSelect);
        this.addConfirmButton();
        this.addCancelButton();
        this.addPrompt();

        this.setVisible(true);
    }

    private void addPrompt() {
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel prompt = new JLabel("Please select from the following applicants");
        this.add(prompt, c);
    }

    private void addApplicants(ArrayList<JobApplication> applications, int toSelect) {
        c.gridx = -1;
        c.gridy = 0;
        for (int i=0; i<applications.size(); i++) {
            JCheckBox checkBox = new JCheckBox(applications.get(i).getApplicant().getLegalName());
            checkBoxToAppMap.put(checkBox, applications.get(i));
            if (i<toSelect) {
                checkBox.setSelected(true);
            }
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
                returnButton.setVisible(false);
                dispose();
            }
        });
        this.add(cancelButton, c);
    }
}
