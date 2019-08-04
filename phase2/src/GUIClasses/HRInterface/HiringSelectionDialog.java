package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

class HiringSelectionDialog extends SelectionDialog implements ItemListener {

    private int availablePositions;
    private BranchJobPosting jobPosting;

    HiringSelectionDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(parent, hrBackend, applications, returnButton, 0);
        this.jobPosting = applications.get(0).getJobPosting();
        this.availablePositions = this.jobPosting.getNumPositions();
        this.addCheckboxListener();
    }

    void addConfirmListener() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.selectApplicantsForHire(jobPosting, getApplicantsSelected());
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
