package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class HiringSelectionFrame extends SelectionFrame implements ItemListener {

    private int availablePositions;

    HiringSelectionFrame(MethodsTheGUICallsInHR HRInterface, ArrayList<JobApplication> applications) {
        super(HRInterface, applications);

        this.availablePositions = applications.get(0).getJobPosting().getNumPositions();
    }

    void addConfirmListener() {

    }

    private void addCheckboxListener() {
        for (JCheckBox checkBox : this.checkBoxToAppMap.keySet()) {
            checkBox.addItemListener(this);
        }
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
