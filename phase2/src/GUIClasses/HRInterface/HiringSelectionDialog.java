package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

class HiringSelectionDialog extends SelectionDialog {


    HiringSelectionDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(parent, hrBackend, applications, returnButton, 0);
    }

    void addConfirmListener() {
        Component component = this;
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int applicantsSelected = getApplicantsSelected().size();
                if (applicantsSelected > applications.get(0).getJobPosting().getNumPositions()) {
                    JOptionPane.showMessageDialog(component, "Selection exceeded number of positions available.");
                }
                else {
                    hrBackend.selectApplicantsForHire(applications.get(0).getJobPosting(), getApplicantsSelected());
                    dispose();
                }
            }
        });
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
}
