package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class InterviewSelectionFrame extends SelectionFrame {

    InterviewSelectionFrame(MethodsTheGUICallsInHR HRInterface, ArrayList<JobApplication> applications, JButton returnButton, int toSelect) {
        super(HRInterface, applications, returnButton, toSelect);
    }

    @Override
    void addConfirmListener() {
        this.confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HRInterface.rejectApplicationForFirstRound(getApplicantsDeselected());
                returnButton.setVisible(true);
                dispose();
            }
        });
    }

    ArrayList<JobApplication> getApplicantsDeselected() {
        ArrayList<JobApplication> appsDeselected = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxToAppMap.keySet()) {
            if (!checkBox.isSelected()) {
                appsDeselected.add(checkBoxToAppMap.get(checkBox));
            }
        }

        return appsDeselected;
    }
}
