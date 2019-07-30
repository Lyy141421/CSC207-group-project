package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.MethodsTheGUICallsInHR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class GradingFilterFrame extends JInternalFrame {

    private static int MAX_TO_SELECT = 1000;

    MethodsTheGUICallsInHR HRInterface;
    ArrayList<JobApplication> applications;

    JButton homeButton;

    JTextField keywordInput;
    JSpinner numberToSelect;

    GradingFilterFrame(MethodsTheGUICallsInHR HRInterface, ArrayList<JobApplication> applications, JButton homeButton) {
        super("Sort by keywords");

        this.HRInterface = HRInterface;
        this.applications = applications;
        this.homeButton = homeButton;

        this.setKeywordInput();
        this.setSelectNumber();
        this.setButtons();

    }

    void setKeywordInput() {
        JPanel keywordPanel = new JPanel();
        JLabel keywordPrompt = new JLabel("Keywords to search for:");
        this.keywordInput = new JTextField();
        JLabel separateLabel = new JLabel("Separate keywords by ';' (no space).");
        keywordPanel.add(keywordPrompt, BorderLayout.NORTH);
        keywordPanel.add(this.keywordInput, BorderLayout.CENTER);
        keywordPanel.add(separateLabel, BorderLayout.SOUTH);
        this.add(keywordPanel, BorderLayout.CENTER);
    }

    void setSelectNumber() {
        JPanel numberPanel = new JPanel(new FlowLayout());
        JLabel numberPrompt = new JLabel("Select top");
        this.numberToSelect = new JSpinner(new SpinnerNumberModel(1, 1, MAX_TO_SELECT, 1));
        numberPanel.add(numberPrompt);
        numberPanel.add(this.numberToSelect);
        this.add(numberPanel, BorderLayout.EAST);
    }

    void setButtons() {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> keywords = new ArrayList<>(Arrays.asList(keywordInput.getText().split(";")));
                ArrayList<JobApplication> sortedApplications = HRInterface.getJobApplicationInNonDecreasingOrder(applications.get(0).getJobPosting(), keywords);
                JInternalFrame interviewSelectionFrame = new InterviewSelectionFrame(HRInterface, sortedApplications,
                        homeButton, ((SpinnerNumberModel)numberToSelect.getModel()).getNumber().intValue());
                dispose();
            }
        });

        this.add(confirmButton, BorderLayout.SOUTH);
    }
}
