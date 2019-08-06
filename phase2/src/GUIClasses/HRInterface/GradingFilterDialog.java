package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;
import GUIClasses.CommonUserGUI.GUIElementsCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

class GradingFilterDialog extends JDialog {

    private static final int MAX_TO_SELECT = 1000;

    private JFrame parent;
    HRBackend hrBackend;
    private ArrayList<JobApplication> applications;

    private JButton returnButton;

    private JScrollPane keywordInput;
    private JSpinner numberToSelect;

    GradingFilterDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton) {
        super(parent,"Sort by keywords");

        this.parent = parent;
        this.hrBackend = hrBackend;
        this.applications = applications;
        this.returnButton = returnButton;

        this.setDialogProperties();

        this.setKeywordInput();
        this.setSelectNumber();
        this.setButtons();
        this.setVisible(true);
    }

    private void setDialogProperties() {
        this.setSize(500, 350);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setAlwaysOnTop(true);
        this.setModal(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    private void setKeywordInput() {
        JPanel keywordPanel = new JPanel();
        keywordPanel.setLayout(new BoxLayout(keywordPanel, BoxLayout.Y_AXIS));
        JLabel keywordPrompt = new JLabel("Keywords to search for:");
        this.keywordInput = new GUIElementsCreator().createTextAreaWithScrollBar("", true);
        this.keywordInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.keywordInput.revalidate();
        JLabel separateLabel = new JLabel("Separate keywords by ';' (no space).");
        keywordPanel.add(keywordPrompt);
        keywordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        keywordPanel.add(separateLabel);
        keywordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        keywordPanel.add(this.keywordInput);
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
        JPanel confirmButtonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String textInput = ((JTextArea) keywordInput.getViewport().getView()).getText();
                String[] keywords = textInput.split(";");
                ArrayList<JobApplication> sortedApplications = hrBackend.getJobApplicationInNonDecreasingOrder(applications.get(0).getJobPosting(), keywords);
                JDialog reviewDialog = new InterviewSelectionDialog(parent, hrBackend, sortedApplications,
                        returnButton, ((SpinnerNumberModel)numberToSelect.getModel()).getNumber().intValue());
                reviewDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        dispose();
                    }
                });

            }
        });
        confirmButtonPanel.add(confirmButton);
        this.add(confirmButtonPanel, BorderLayout.SOUTH);
    }
}
