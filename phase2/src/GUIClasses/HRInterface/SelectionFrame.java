package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectionFrame extends JInternalFrame {
    private int selectionCounter = 0;
    HashMap<JCheckBox, JobApplication> checkboxToAppMap = new HashMap<>();

    SelectionFrame(ArrayList<JobApplication> applications) {
        super("Select");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        c.gridx = -1;
        c.gridy = 0;
        for (JobApplication app : applications) {
            JCheckBox checkbox = new JCheckBox(app.getApplicant().getLegalName());
            checkboxToAppMap.put(checkbox, app);
            c.gridx = (c.gridx+1)%3;
            if (c.gridx == 0) {
                c.gridy++;
            }
            this.add(checkbox, c);
        }

        c.gridy++;
        c.gridx = 1;
        //TODO: ActionListeners
        JButton confirmButton = new JButton("Confirm");
        this.add(confirmButton, c);
        JButton cancelButton = new JButton("Cancel");
        this.add(cancelButton, c);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel prompt = new JLabel("Please select from the following applicants");
        this.add(prompt, c);
    }


}
