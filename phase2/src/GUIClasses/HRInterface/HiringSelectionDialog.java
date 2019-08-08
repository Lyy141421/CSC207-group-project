package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

class HiringSelectionDialog extends SelectionDialog {

    private int numOfPositions;

    HiringSelectionDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(parent, hrBackend, applications, returnButton, 0);
    }

    void addConfirmListener() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int applicantsSelected = getApplicantsSelected().size();
                if (applicantsSelected > applications.get(0).getJobPosting().getNumPositions()) {
                    JOptionPane.showMessageDialog(contentPane, "Selection exceeded number of positions available.");
                } else {
                    hrBackend.selectApplicantsForHire(applications.get(0).getJobPosting(), getApplicantsSelected());
                    setModalityType(ModalityType.MODELESS);
                    setVisible(false);
                    dispose();
                }
            }
        });
    }

    void addCheckBoxListener(JCheckBox checkBox) {
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    numOfPositions--;
                    if (numOfPositions < 0) {
                        ((JCheckBox) e.getSource()).setSelected(false);
                        numOfPositions++;
                        assert numOfPositions >= 0;
                    }
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    numOfPositions++;
                }
            }
        });
    }

    @Override
    void addHeader(JPanel promptPanel) {
        this.numOfPositions = applications.get(0).getJobPosting().getNumPositions();
        JPanel positionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel positionLabel = new JLabel("Number of positions: " + this.numOfPositions);
        positionsPanel.add(positionLabel);
        promptPanel.add(positionsPanel, BorderLayout.CENTER);
    }

    private ArrayList<JobApplication> getApplicantsSelected() {
        ArrayList<JobApplication> appsSelected = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxToAppMap.keySet()) {
            if (checkBox.isSelected()) {
                appsSelected.add(checkBoxToAppMap.get(checkBox));
            }
        }

        return appsSelected;
    }
}
