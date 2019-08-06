package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

class InterviewSelectionDialog extends SelectionDialog {

    InterviewSelectionDialog(JFrame parent, HRBackend HRInterface, ArrayList<JobApplication> applications, JButton returnButton, int toSelect) {
        super(parent, HRInterface, applications, returnButton, toSelect);
    }

    @Override
    void addConfirmListener() {
        this.confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BranchJobPosting branchJobPosting = applications.get(0).getJobPosting();
                hrBackend.rejectApplicationForFirstRound(branchJobPosting, getApplicantsDeselected());
                JDialog interviewConfig = new InterviewConfigDialog(parent, hrBackend, branchJobPosting, returnButton);
                interviewConfig.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        dispose();
                    }
                });
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
