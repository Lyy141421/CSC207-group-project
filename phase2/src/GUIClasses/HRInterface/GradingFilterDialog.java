package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

class GradingFilterDialog extends JDialog {

    private static int MAX_TO_SELECT = 1000;

    JFrame parent;
    HRBackend hrBackend;
    ArrayList<JobApplication> applications;

    JButton returnButton;

    JTextField keywordInput;
    JSpinner numberToSelect;

    GradingFilterDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(parent,"Sort by keywords");

        this.parent = parent;
        this.hrBackend = hrBackend;
        this.applications = applications;
        this.returnButton = returnButton;

        setSize(300, 200);

        this.setKeywordInput();
        this.setSelectNumber();
        this.setButtons();

        this.setVisible(true);
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
                ArrayList<JobApplication> sortedApplications = hrBackend.getJobApplicationInNonDecreasingOrder(applications.get(0).getJobPosting(), keywords);
                JDialog interviewSelectionFrame = new InterviewSelectionDialog(parent, hrBackend, sortedApplications,
                        returnButton, ((SpinnerNumberModel)numberToSelect.getModel()).getNumber().intValue());
                dispose();
            }
        });

        this.add(confirmButton, BorderLayout.SOUTH);
    }
}
