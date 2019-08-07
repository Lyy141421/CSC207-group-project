package GUIClasses.HRInterface;

import ApplicantStuff.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

abstract class SelectionDialog extends JDialog{

    HRBackend hrBackend;
    JFrame parent;
    ArrayList<JobApplication> applications;

    HashMap<JCheckBox, JobApplication> checkBoxToAppMap = new HashMap<>();
    GridBagConstraints c = new GridBagConstraints();

    JButton confirmButton;
    JButton returnButton;

    SelectionDialog(JFrame parent, HRBackend hrBackend, ArrayList<JobApplication> applications, JButton returnButton, int toSelect) {
        super(parent, "Select");
        this.parent = parent;
        this.hrBackend = hrBackend;
        this.applications = applications;
        this.returnButton = returnButton;

        this.setLayout(new BorderLayout());

        this.addPrompt();
        this.addApplicants(applications, toSelect);
        this.addButtons();


        this.setDialogProperties();
        this.setVisible(true);
    }

    private void setDialogProperties() {
        this.setSize(500, 350);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    private void addPrompt() {
        JPanel promptPanel = new JPanel();
        JLabel prompt = new JLabel("Please select from the following applicants:");
        promptPanel.add(prompt);
        this.add(promptPanel, BorderLayout.PAGE_START);
    }

    private void addApplicants(ArrayList<JobApplication> applications, int toSelect) {
        JPanel applicantPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = -1;
        c.gridy = 0;
        for (int i=0; i<applications.size(); i++) {
            JCheckBox checkBox = new JCheckBox(applications.get(i).getApplicant().getLegalName());
            checkBoxToAppMap.put(checkBox, applications.get(i));
            if (i<toSelect) {
                checkBox.setSelected(true);
            }
            c.gridy += (c.gridx+1)/3;
            c.gridx = (c.gridx+1)%3;
            applicantPanel.add(checkBox, c);
        }
        this.add(applicantPanel, BorderLayout.CENTER);
    }

    private void addButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.confirmButton = new JButton("Confirm");
        this.addConfirmListener();
        buttonPanel.add(confirmButton);
        /*this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnButton.setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(cancelButton);*/
        this.add(buttonPanel, BorderLayout.PAGE_END);
    }

    abstract void addConfirmListener();
}
