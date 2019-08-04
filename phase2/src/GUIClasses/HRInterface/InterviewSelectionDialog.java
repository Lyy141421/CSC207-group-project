package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class InterviewSelectionDialog extends SelectionDialog {

    private JPanel cardPanel;
    private BranchJobPosting branchJobPosting;

    InterviewSelectionDialog(JFrame parent, JPanel cardPanel, HRBackend HRInterface, ArrayList<JobApplication> applications, JButton returnButton, int toSelect) {
        super(parent, HRInterface, applications, returnButton, toSelect);
        this.cardPanel = cardPanel;
        this.branchJobPosting = applications.get(0).getJobPosting();
    }

    @Override
    void addConfirmListener() {
        this.confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hrBackend.rejectApplicationForFirstRound(branchJobPosting, getApplicantsDeselected());
                new InterviewConfigDialog(parent, cardPanel, hrBackend, branchJobPosting, returnButton);
                dispose();
            }
        });
    }

    private ArrayList<JobApplication> getApplicantsDeselected() {
        ArrayList<JobApplication> appsDeselected = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxToAppMap.keySet()) {
            if (!checkBox.isSelected()) {
                appsDeselected.add(checkBoxToAppMap.get(checkBox));
            }
        }

        return appsDeselected;
    }
}
