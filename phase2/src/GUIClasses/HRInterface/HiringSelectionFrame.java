package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

class HiringSelectionFrame extends SelectionFrame implements ItemListener {

    private int availablePositions;

    HiringSelectionFrame(HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(hrBackend, applications, returnButton, 0);

        this.availablePositions = applications.get(0).getJobPosting().getNumPositions();
        this.addCheckboxListener();
    }

    void addConfirmListener() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.selectApplicantsForHire(getApplicantsSelected());
            }
        });
    }

    private void addCheckboxListener() {
        for (JCheckBox checkBox : this.checkBoxToAppMap.keySet()) {
            checkBox.addItemListener(this);
        }
    }

    ArrayList<JobApplication> getApplicantsSelected() {
        ArrayList<JobApplication> appsSelected = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxToAppMap.keySet()) {
            if (checkBox.isSelected()) {
                appsSelected.add(checkBoxToAppMap.get(checkBox));
            }
        }

        return appsSelected;
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
